/*
 * Copyright 2013 ra.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.doctester.testbrowser;

import com.google.common.collect.Maps;
import org.doctester.testbrowser.testmodels.ArticlesDto;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Map;

import static org.junit.Assert.assertThat;

/**
 *
 * @author ra
 */
@RunWith(MockitoJUnitRunner.class)
public class ResponseTest {

    @Before
    public void init() {

    }

    /**
     * Test of payloadAsString method, of class Response.
     */
    @Test
    public void testPayloadAsXml() {

        Map<String, String> headers = Maps.newHashMap();
        headers.put(HttpConstants.HEADER_CONTENT_TYPE, HttpConstants.APPLICATION_XML_WITH_CHARSET_UTF_8);

        Response response = new Response(headers, 200, ARTICLES_XML);

        assertThat(response.httpStatus, CoreMatchers.equalTo(200));

        ArticlesDto articlesDto = response.payloadXmlAs(ArticlesDto.class);
        assertThat(articlesDto.articles.size(), CoreMatchers.equalTo(3));

        assertThat(headers.size(), CoreMatchers.equalTo(1));

    }

    @Test
    public void testPayloadAsJson() {

        Map<String, String> headers = Maps.newHashMap();
        headers.put(HttpConstants.HEADER_CONTENT_TYPE, HttpConstants.APPLICATION_JSON_WITH_CHARSET_UTF8);

        Response response = new Response(headers, 200, ARTICLES_JSON);

        ArticlesDto articlesDto = response.payloadJsonAs(ArticlesDto.class);

        assertThat(response.httpStatus, CoreMatchers.equalTo(200));
        assertThat(articlesDto.articles.size(), CoreMatchers.equalTo(3));
        assertThat(headers.size(), CoreMatchers.equalTo(1));

    }

    @Test
    public void testJsonPayloadAsPrettyString() {

        Map<String, String> headers = Maps.newHashMap();
        headers.put(HttpConstants.HEADER_CONTENT_TYPE, HttpConstants.APPLICATION_JSON_WITH_CHARSET_UTF8);

        Response response = new Response(headers, 200, ARTICLES_JSON);

        String prettyPayload = response.payloadAsPrettyString();
        System.out.println("re" + prettyPayload);

        assertThat(prettyPayload.replace("\r", ""), CoreMatchers.equalTo(ARTICLES_JSON_FORMATTED));

    }

    String ARTICLES_XML = "<ArticlesDto xmlns=\"\"><articles><id>1</id><title>My third post</title><postedAt>1382972199545</postedAt><content>Lorem ipsum dolor sit amet, consectetur adipiscing elit sed nisl sed lorem commodo elementum in a leo. Aliquam erat volutpat. Nulla libero odio, consectetur eget rutrum ac, varius vitae orci. Suspendisse facilisis tempus elit, facilisis ultricies massa condimentum in. Aenean id felis libero. Quisque nisl eros, accumsan eget ornare id, pharetra eget felis. Aenean purus erat, egestas nec scelerisque non, eleifend id ligula. eget ornare id, pharetra eget felis. Aenean purus erat, egestas nec scelerisque non, eleifend id ligula. eget ornare id, pharetra eget felis. Aenean purus erat, egestas nec scelerisque non, eleifend id ligula. eget ornare id, pharetra eget felis. Aenean purus erat, egestas nec scelerisque non, eleifend id ligula. eget ornare id, pharetra eget felis. Aenean purus erat, egestas nec scelerisque non, eleifend id ligula. eget ornare id, pharetra eget felis. Aenean purus erat, egestas nec scelerisque non, eleifend id ligula. eget ornare id, pharetra eget felis. Aenean purus erat, egestas nec scelerisque non, eleifend id ligula. eget ornare id, pharetra eget felis. Aenean purus erat, egestas nec scelerisque non, eleifend id ligula.</content><authorIds>1</authorIds></articles><articles><id>2</id><title>My second post</title><postedAt>1382972199710</postedAt><content>Lorem ipsum dolor sit amet, consectetur adipiscing elit sed nisl sed lorem commodo elementum in a leo. Aliquam erat volutpat. Nulla libero odio, consectetur eget rutrum ac, varius vitae orci. Suspendisse facilisis tempus elit, facilisis ultricies massa condimentum in. Aenean id felis libero. Quisque nisl eros, accumsan eget ornare id, pharetra eget felis. Aenean purus erat, egestas nec scelerisque non, eleifend id ligula. eget ornare id, pharetra eget felis. Aenean purus erat, egestas nec scelerisque non, eleifend id ligula. eget ornare id, pharetra eget felis. Aenean purus erat, egestas nec scelerisque non, eleifend id ligula. eget ornare id, pharetra eget felis. Aenean purus erat, egestas nec scelerisque non, eleifend id ligula. eget ornare id, pharetra eget felis. Aenean purus erat, egestas nec scelerisque non, eleifend id ligula. eget ornare id, pharetra eget felis. Aenean purus erat, egestas nec scelerisque non, eleifend id ligula. eget ornare id, pharetra eget felis. Aenean purus erat, egestas nec scelerisque non, eleifend id ligula. eget ornare id, pharetra eget felis. Aenean purus erat, egestas nec scelerisque non, eleifend id ligula.</content><authorIds>1</authorIds></articles><articles><id>3</id><title>Hello to the blog example!</title><postedAt>1382972199712</postedAt><content>&lt;p&gt;Hi and welcome to the demo of Ninja!&lt;/p&gt; &lt;p&gt;This example shows how you can use Ninja in the wild. Some things you can learn:&lt;/p&gt;&lt;ul&gt;&lt;li&gt;How to use the templating system (header, footer)&lt;/li&gt;&lt;li&gt;How to test your application with ease./li&gt;&lt;li&gt;Setting up authentication (login / logout)&lt;/li&gt;&lt;li&gt;Internationalization (i18n)&lt;/li&gt;&lt;li&gt;Static assets / using webjars&lt;/li&gt;&lt;li&gt;Persisting data&lt;/li&gt;&lt;li&gt;Beautiful &lt;a href=\"/article/3\"&gt;html routes&lt;/a&gt; for your application&lt;/li&gt;&lt;li&gt;How to design your restful Api (&lt;a href=\"/api/bob@gmail.com/articles.json\"&gt;Json&lt;/a&gt; and &lt;a href=\"/api/bob@gmail.com/articles.xml\"&gt;Xml&lt;/a&gt;)&lt;/li&gt;&lt;li&gt;... and much much more.&lt;/li&gt;&lt;/ul&gt;&lt;p&gt;We are always happy to see you on our mailing list! Check out &lt;a href=\"http://www.ninjaframework.org\"&gt;our website for more&lt;/a&gt;.&lt;/p&gt;</content><authorIds>1</authorIds></articles></ArticlesDto>";

    String ARTICLES_JSON = "{\"articles\":[{\"id\":1,\"title\":\"My third post\",\"postedAt\":1382972199545,\"content\":\"Lorem ipsum dolor sit amet, consectetur adipiscing elit sed nisl sed lorem commodo elementum in a leo. Aliquam erat volutpat. Nulla libero odio, consectetur eget rutrum ac, varius vitae orci. Suspendisse facilisis tempus elit, facilisis ultricies massa condimentum in. Aenean id felis libero. Quisque nisl eros, accumsan eget ornare id, pharetra eget felis. Aenean purus erat, egestas nec scelerisque non, eleifend id ligula. eget ornare id, pharetra eget felis. Aenean purus erat, egestas nec scelerisque non, eleifend id ligula. eget ornare id, pharetra eget felis. Aenean purus erat, egestas nec scelerisque non, eleifend id ligula. eget ornare id, pharetra eget felis. Aenean purus erat, egestas nec scelerisque non, eleifend id ligula. eget ornare id, pharetra eget felis. Aenean purus erat, egestas nec scelerisque non, eleifend id ligula. eget ornare id, pharetra eget felis. Aenean purus erat, egestas nec scelerisque non, eleifend id ligula. eget ornare id, pharetra eget felis. Aenean purus erat, egestas nec scelerisque non, eleifend id ligula. eget ornare id, pharetra eget felis. Aenean purus erat, egestas nec scelerisque non, eleifend id ligula.\",\"authorIds\":[1]},{\"id\":2,\"title\":\"My second post\",\"postedAt\":1382972199710,\"content\":\"Lorem ipsum dolor sit amet, consectetur adipiscing elit sed nisl sed lorem commodo elementum in a leo. Aliquam erat volutpat. Nulla libero odio, consectetur eget rutrum ac, varius vitae orci. Suspendisse facilisis tempus elit, facilisis ultricies massa condimentum in. Aenean id felis libero. Quisque nisl eros, accumsan eget ornare id, pharetra eget felis. Aenean purus erat, egestas nec scelerisque non, eleifend id ligula. eget ornare id, pharetra eget felis. Aenean purus erat, egestas nec scelerisque non, eleifend id ligula. eget ornare id, pharetra eget felis. Aenean purus erat, egestas nec scelerisque non, eleifend id ligula. eget ornare id, pharetra eget felis. Aenean purus erat, egestas nec scelerisque non, eleifend id ligula. eget ornare id, pharetra eget felis. Aenean purus erat, egestas nec scelerisque non, eleifend id ligula. eget ornare id, pharetra eget felis. Aenean purus erat, egestas nec scelerisque non, eleifend id ligula. eget ornare id, pharetra eget felis. Aenean purus erat, egestas nec scelerisque non, eleifend id ligula. eget ornare id, pharetra eget felis. Aenean purus erat, egestas nec scelerisque non, eleifend id ligula.\",\"authorIds\":[1]},{\"id\":3,\"title\":\"Hello to the blog example!\",\"postedAt\":1382972199712,\"content\":\"<p>Hi and welcome to the demo of Ninja!</p> <p>This example shows how you can use Ninja in the wild. Some things you can learn:</p><ul><li>How to use the templating system (header, footer)</li><li>How to test your application with ease./li><li>Setting up authentication (login / logout)</li><li>Internationalization (i18n)</li><li>Static assets / using webjars</li><li>Persisting data</li><li>Beautiful <a href=\\\"/article/3\\\">html routes</a> for your application</li><li>How to design your restful Api (<a href=\\\"/api/bob@gmail.com/articles.json\\\">Json</a> and <a href=\\\"/api/bob@gmail.com/articles.xml\\\">Xml</a>)</li><li>... and much much more.</li></ul><p>We are always happy to see you on our mailing list! Check out <a href=\\\"http://www.ninjaframework.org\\\">our website for more</a>.</p>\",\"authorIds\":[1]}]}";

    String ARTICLES_JSON_FORMATTED = "{\n"
            + "  \"articles\" : [ {\n"
            + "    \"id\" : 1,\n"
            + "    \"title\" : \"My third post\",\n"
            + "    \"postedAt\" : 1382972199545,\n"
            + "    \"content\" : \"Lorem ipsum dolor sit amet, consectetur adipiscing elit sed nisl sed lorem commodo elementum in a leo. Aliquam erat volutpat. Nulla libero odio, consectetur eget rutrum ac, varius vitae orci. Suspendisse facilisis tempus elit, facilisis ultricies massa condimentum in. Aenean id felis libero. Quisque nisl eros, accumsan eget ornare id, pharetra eget felis. Aenean purus erat, egestas nec scelerisque non, eleifend id ligula. eget ornare id, pharetra eget felis. Aenean purus erat, egestas nec scelerisque non, eleifend id ligula. eget ornare id, pharetra eget felis. Aenean purus erat, egestas nec scelerisque non, eleifend id ligula. eget ornare id, pharetra eget felis. Aenean purus erat, egestas nec scelerisque non, eleifend id ligula. eget ornare id, pharetra eget felis. Aenean purus erat, egestas nec scelerisque non, eleifend id ligula. eget ornare id, pharetra eget felis. Aenean purus erat, egestas nec scelerisque non, eleifend id ligula. eget ornare id, pharetra eget felis. Aenean purus erat, egestas nec scelerisque non, eleifend id ligula. eget ornare id, pharetra eget felis. Aenean purus erat, egestas nec scelerisque non, eleifend id ligula.\",\n"
            + "    \"authorIds\" : [ 1 ]\n"
            + "  }, {\n"
            + "    \"id\" : 2,\n"
            + "    \"title\" : \"My second post\",\n"
            + "    \"postedAt\" : 1382972199710,\n"
            + "    \"content\" : \"Lorem ipsum dolor sit amet, consectetur adipiscing elit sed nisl sed lorem commodo elementum in a leo. Aliquam erat volutpat. Nulla libero odio, consectetur eget rutrum ac, varius vitae orci. Suspendisse facilisis tempus elit, facilisis ultricies massa condimentum in. Aenean id felis libero. Quisque nisl eros, accumsan eget ornare id, pharetra eget felis. Aenean purus erat, egestas nec scelerisque non, eleifend id ligula. eget ornare id, pharetra eget felis. Aenean purus erat, egestas nec scelerisque non, eleifend id ligula. eget ornare id, pharetra eget felis. Aenean purus erat, egestas nec scelerisque non, eleifend id ligula. eget ornare id, pharetra eget felis. Aenean purus erat, egestas nec scelerisque non, eleifend id ligula. eget ornare id, pharetra eget felis. Aenean purus erat, egestas nec scelerisque non, eleifend id ligula. eget ornare id, pharetra eget felis. Aenean purus erat, egestas nec scelerisque non, eleifend id ligula. eget ornare id, pharetra eget felis. Aenean purus erat, egestas nec scelerisque non, eleifend id ligula. eget ornare id, pharetra eget felis. Aenean purus erat, egestas nec scelerisque non, eleifend id ligula.\",\n"
            + "    \"authorIds\" : [ 1 ]\n"
            + "  }, {\n"
            + "    \"id\" : 3,\n"
            + "    \"title\" : \"Hello to the blog example!\",\n"
            + "    \"postedAt\" : 1382972199712,\n"
            + "    \"content\" : \"<p>Hi and welcome to the demo of Ninja!</p> <p>This example shows how you can use Ninja in the wild. Some things you can learn:</p><ul><li>How to use the templating system (header, footer)</li><li>How to test your application with ease./li><li>Setting up authentication (login / logout)</li><li>Internationalization (i18n)</li><li>Static assets / using webjars</li><li>Persisting data</li><li>Beautiful <a href=\\\"/article/3\\\">html routes</a> for your application</li><li>How to design your restful Api (<a href=\\\"/api/bob@gmail.com/articles.json\\\">Json</a> and <a href=\\\"/api/bob@gmail.com/articles.xml\\\">Xml</a>)</li><li>... and much much more.</li></ul><p>We are always happy to see you on our mailing list! Check out <a href=\\\"http://www.ninjaframework.org\\\">our website for more</a>.</p>\",\n"
            + "    \"authorIds\" : [ 1 ]\n"
            + "  } ]\n"
            + "}";

}
