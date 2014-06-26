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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map;

import models.Article;
import models.ArticleDto;
import models.ArticlesDto;

import org.doctester.testbrowser.Request;
import org.doctester.testbrowser.Response;
import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import controllers.utils.NinjaTest;

public class ApiControllerTest extends NinjaTest {
    
    @Before
    public void setup() {
    	
    	makeRequest(Request.GET().url(testServerUrl().path("setup")));
        
    }

    @Test
    public void testGetAndPostArticleViaJson() throws Exception {

        // /////////////////////////////////////////////////////////////////////
        // Test initial data:
        // /////////////////////////////////////////////////////////////////////
    	Response response 
    		= makeRequest(
    				Request
    				.GET()
    				.contentTypeApplicationJson()
    				.url(
    						testServerUrl().path("api/bob@gmail.com/articles.json")));


        ArticlesDto articlesDto = getGsonWithLongToDateParsing().fromJson(
                response.payload, ArticlesDto.class);

        assertEquals(3, articlesDto.articles.size());

        // /////////////////////////////////////////////////////////////////////
        // Post new article:
        // /////////////////////////////////////////////////////////////////////
        ArticleDto articleDto = new ArticleDto();
        articleDto.content = "contentcontent";
        articleDto.title = "new title new title";

        response = makeRequest(
        		Request
        		.POST()
        		.url(testServerUrl().path("api/bob@gmail.com/article.json"))
        		.payload(articleDto));


        assertTrue(response.payload.contains("Error. Forbidden."));

        doLogin();

        response = makeRequest(
        		Request
        		.POST()
        		.contentTypeApplicationJson()
        		.url(testServerUrl().path("api/bob@gmail.com/article.json"))
        		.payload(articleDto));

        assertFalse(response.payload.contains("Error. Forbidden."));

        // /////////////////////////////////////////////////////////////////////
        // Fetch articles again => assert we got a new one ...
        // /////////////////////////////////////////////////////////////////////
        response = makeRequest(
        		Request
        		.GET()
        		.url(testServerUrl().path("api/bob@gmail.com/articles.json")));
        

        articlesDto = getGsonWithLongToDateParsing().fromJson(response.payload, ArticlesDto.class);
        // one new result:
        assertEquals(4, articlesDto.articles.size());

    }

    @Test
    public void testGetAndDeleteArticle() throws Exception {

        // /////////////////////////////////////////////////////////////////////
        // Test initial data:
        // /////////////////////////////////////////////////////////////////////
        Response response = makeRequest(
                Request
                .GET()
                .contentTypeApplicationJson()
                .url(
                        testServerUrl().path("api/bob@gmail.com/articles.json")));

        ArticlesDto articlesDto = getGsonWithLongToDateParsing().fromJson(
                response.payload, ArticlesDto.class);

        assertEquals(3, articlesDto.articles.size());

        // /////////////////////////////////////////////////////////////////////
        // Delete an article:
        // /////////////////////////////////////////////////////////////////////
        Article article = articlesDto.articles.get(0);

        response = makeRequest(
                Request
                .DELETE()
                .url(
                        testServerUrl().path("api/article/" + article.id)));

        assertEquals(403, response.httpStatus);
        assertTrue(response.payload.contains("Error. Forbidden."));

        doLogin();

        response = makeRequest(
                Request
                .DELETE()
                .url(
                        testServerUrl().path("api/article/" + article.id)));

        assertEquals(403, response.httpStatus);
        assertTrue(response.payload.contains("Error. Forbidden."));

        doAdminLogin();

        response = makeRequest(
                Request
                .DELETE().url(
                        testServerUrl().path("api/article/" + article.id)));

        assertEquals(204, response.httpStatus);
        assertNull(response.payload);

        // /////////////////////////////////////////////////////////////////////
        // Fetch articles again => assert we got a one less ...
        // /////////////////////////////////////////////////////////////////////
        response = makeRequest(
                Request
                .GET()
                .url(
                        testServerUrl().path("api/bob@gmail.com/articles.json")));

        articlesDto = getGsonWithLongToDateParsing().fromJson(response.payload,
                ArticlesDto.class);
        // one new result:
        assertEquals(2, articlesDto.articles.size());

    }

    @Test
    public void testGetAndPostArticleViaXml() throws Exception {

        // /////////////////////////////////////////////////////////////////////
        // Test initial data:
        // /////////////////////////////////////////////////////////////////////
    	Response response = makeRequest(
    			Request
    			.GET()
    			.contentTypeApplicationXml()
    			.url(
    					testServerUrl().path("api/bob@gmail.com/articles.xml")));

        ArticlesDto articlesDto = response.payloadAs(ArticlesDto.class);

        assertEquals(3, articlesDto.articles.size());

        // /////////////////////////////////////////////////////////////////////
        // Post new article:
        // /////////////////////////////////////////////////////////////////////
        ArticleDto articleDto = new ArticleDto();
        articleDto.content = "contentcontent";
        articleDto.title = "new title new title";

        
    	response = makeRequest(
    			Request
    			.POST()
    			.contentTypeApplicationXml()
    			.url(
    					testServerUrl().path("api/bob@gmail.com/article.xml"))
    			.payload(articleDto));

        assertTrue(response.payload.contains("Error. Forbidden."));

        doLogin();

    	response = makeRequest(
    			Request
    			.POST()
    			.contentTypeApplicationXml()
    			.url(
    					testServerUrl().path("api/bob@gmail.com/article.xml"))
    			.payload(articleDto));

        assertFalse(response.payload.contains("Error. Forbidden."));

        // /////////////////////////////////////////////////////////////////////
        // Fetch articles again => assert we got a new one ...
        // /////////////////////////////////////////////////////////////////////
    	response = makeRequest(
    			Request
    			.GET()
    			.url(
    					testServerUrl().path("api/bob@gmail.com/articles.xml")));

        articlesDto = response.payloadAs(ArticlesDto.class);
        // one new result:
        assertEquals(4, articlesDto.articles.size());

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

    private void doLogin() throws Exception {

        doLogin("bob@gmail.com", "secret");

    }

    private void doAdminLogin() throws Exception {

        doLogin("tom@domain.com", "secret");

    }

    private void doLogin(String username, String password) {
        Map<String, String> formParameters = Maps.newHashMap();
        formParameters.put("username", username);
        formParameters.put("password", password);

        makeRequest(
                Request
                .POST()
                .url(
                        testServerUrl().path("login"))
                .formParameters(formParameters));
    }

}
