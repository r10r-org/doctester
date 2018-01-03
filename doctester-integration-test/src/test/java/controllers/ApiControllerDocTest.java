/**
 * Copyright (C) 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controllers;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map;

import models.Article;
import models.ArticleDto;
import models.ArticlesDto;

import org.r10r.doctester.testbrowser.Request;
import org.r10r.doctester.testbrowser.Response;
import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import controllers.utils.NinjaApiDoctester;

public class ApiControllerDocTest extends NinjaApiDoctester {

    String GET_ARTICLES_URL = "/api/{username}/articles.json";
    String POST_ARTICLE_URL = "/api/{username}/article.json";
    String DELETE_ARTICLE_URL = "/api/article/{id}";
    String LOGIN_URL = "/login";

    String USER = "bob@gmail.com";
    String ADMIN = "tom@domain.com";


	@Test
	public void testGetMetaDataViaHeadRequest() throws Exception {

		sayNextSection("Fetching metadata");

		Response response = sayAndMakeRequest(Request.HEAD().url(testServerUrl()));

		sayAndAssertThat("We get some headers, but no metadata.", response.headers, notNullValue());
		sayAndAssertThat("We get no payload", response.payload, nullValue());
		sayAndAssertThat("Ninja does not yet support HEAD requests", response.httpStatus, is(404));
	}

	
    @Test
    public void testGetAndPostArticleViaJson() {

        // /////////////////////////////////////////////////////////////////////
        // Test initial data:
        // /////////////////////////////////////////////////////////////////////
        
        sayNextSection("Retrieving articles for a user (Json)");
        
        say("Retrieving all articles of a user is a GET request to " + GET_ARTICLES_URL);
        
        Response response = sayAndMakeRequest( 
               Request
                    .GET().url(testServerUrl().path((GET_ARTICLES_URL.replace("{username}", USER)))));
        
        ArticlesDto articlesDto = response.payloadAs(ArticlesDto.class);


        sayAndAssertThat("We get back all 3 articles of that user ", 3, equalTo(articlesDto.articles.size()));

        // /////////////////////////////////////////////////////////////////////
        // Post new article:
        // /////////////////////////////////////////////////////////////////////
        sayNextSection("Posting new article (Json)");
        
        say("Posting a new article is a post request to " + POST_ARTICLE_URL);
        say("Please note that you have to be authenticated in order to be allowed to post.");
        
        ArticleDto articleDto = new ArticleDto();
        articleDto.content = "contentcontent";
        articleDto.title = "new title new title";

        response = sayAndMakeRequest(
                Request
                    .POST()
                    .url(testServerUrl().path((POST_ARTICLE_URL.replace("{username}", USER))))
                    .contentTypeApplicationJson()
                    .payload(articleDto));
        
        sayAndAssertThat("You have to be authenticated in order to post articles", response.httpStatus, equalTo(403));
        
        doLogin();

        say("Now we are authenticated and expect the post to succeed...");
        
        
        response = sayAndMakeRequest(
                Request
                    .POST()
                    .url(testServerUrl().path((POST_ARTICLE_URL.replace("{username}", USER))))
                    .contentTypeApplicationJson()
                    .payload(articleDto));
        
        sayAndAssertThat("After successful login we are able to post articles", response.httpStatus, equalTo(200));

        // /////////////////////////////////////////////////////////////////////
        // Fetch articles again => assert we got a new one ...
        // /////////////////////////////////////////////////////////////////////
        
        say("If we now fetch the articles again we are getting a new article (the one we have posted successfully");
        response = sayAndMakeRequest(
                Request
                    .GET()
                    .url(testServerUrl().path((GET_ARTICLES_URL.replace("{username}", USER)))));
        

        articlesDto = getGsonWithLongToDateParsing().fromJson(response.payload, ArticlesDto.class);
        // one new result:
        sayAndAssertThat("We are now getting 4 articles.", 4, equalTo(articlesDto.articles.size()));

    }

    @Test
    public void testGetAndDeleteArticle() {

        // /////////////////////////////////////////////////////////////////////
        // Test initial data:
        // /////////////////////////////////////////////////////////////////////

        sayNextSection("Retrieving articles for a user (Json)");

        say("Retrieving all articles of a user is a GET request to "
                + GET_ARTICLES_URL);

        Response response = sayAndMakeRequest(Request.GET().url(
                testServerUrl().path(
                        (GET_ARTICLES_URL.replace("{username}", USER)))));

        ArticlesDto articlesDto = response.payloadAs(ArticlesDto.class);

        sayAndAssertThat("We get back all 3 articles of that user ", 3,
                equalTo(articlesDto.articles.size()));

        // /////////////////////////////////////////////////////////////////////
        // Delete an article:
        // /////////////////////////////////////////////////////////////////////
        sayNextSection("Deleting an article");

        say("Deleting an article is a delete request to " + DELETE_ARTICLE_URL);
        say("Please note that you have to be authenticated as an admin in order to be allowed to delete.");

        Article article = articlesDto.articles.get(0);

        response = sayAndMakeRequest(Request.DELETE().url(
                testServerUrl().path(
                        (DELETE_ARTICLE_URL.replace("{id}",
                                article.id.toString())))));

        sayAndAssertThat(
                "You have to be authenticated as an admin in order to delete articles",
                response.httpStatus, equalTo(403));

        doLogin();

        say("Now we are authenticated but not as an admin and still expect to fail...");

        response = sayAndMakeRequest(Request.DELETE().url(
                testServerUrl().path(
                        (DELETE_ARTICLE_URL.replace("{id}",
                                article.id.toString())))));

        sayAndAssertThat(
                "You have to be authenticated as an admin in order to delete articles",
                response.httpStatus, equalTo(403));

        doAdminLogin();

        say("Now we are authenticated but not as an admin and still expect to fail...");

        response = sayAndMakeRequest(Request.DELETE().url(
                testServerUrl().path(
                        (DELETE_ARTICLE_URL.replace("{id}",
                                article.id.toString())))));

        sayAndAssertThat(
                "After successful admin login we are able to delte articles",
                response.httpStatus, equalTo(204));

        // /////////////////////////////////////////////////////////////////////
        // Fetch articles again => assert we got a one less...
        // /////////////////////////////////////////////////////////////////////

        say("If we now fetch the articles again we are getting a new article (the one we have posted successfully");
        response = sayAndMakeRequest(Request.GET().url(
                testServerUrl().path(
                        (GET_ARTICLES_URL.replace("{username}", USER)))));

        articlesDto = getGsonWithLongToDateParsing().fromJson(response.payload,
                ArticlesDto.class);
        // one new result:
        sayAndAssertThat("We are now getting 2 articles.", 2,
                equalTo(articlesDto.articles.size()));

    }


    private Gson getGsonWithLongToDateParsing() {
        // Creates the json object which will manage the information received
        GsonBuilder builder = new GsonBuilder();
        // Register an adapter to manage the date types as long values
        builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            public Date deserialize(JsonElement json,
                                    Type typeOfT,
                                    JsonDeserializationContext context)
                    throws JsonParseException {
                return new Date(json.getAsJsonPrimitive().getAsLong());
            }
        });
        Gson gson = builder.create();

        return gson;
    }

    private void doAdminLogin() {
        doLogin(ADMIN, "secret");
    }

    private void doLogin() {
        doLogin(USER, "secret");
    }

    private void doLogin(String username, String password) {

        say("To authenticate we send our credentials to " + LOGIN_URL);
        say("We are then issued a cookie from the server that authenticates us in further requests");

        Map<String, String> formParameters = Maps.newHashMap();
        formParameters.put("username", username);
        formParameters.put("password", password);

        makeRequest(
                Request
                .POST()
                .url(
                        testServerUrl().path(LOGIN_URL))
                .formParameters(formParameters));

    }

}
