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
import java.io.File;
import java.util.Map;
import org.hamcrest.CoreMatchers;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author ra
 */
public class RequestTest {


    @Test
    public void testGET() {
        Request result = Request.GET();
        
        assertThat(result.httpRequestType, CoreMatchers.equalTo("GET"));

    }

    @Test
    public void testPOST() {

        Request result = Request.POST();
        assertThat(result.httpRequestType, CoreMatchers.equalTo("POST"));
    }

    @Test
    public void testPUT() {
        Request result = Request.PUT();
        assertThat(result.httpRequestType, CoreMatchers.equalTo("PUT"));
    }

    @Test
    public void testDELETE() {
        Request result = Request.DELETE();
        assertThat(result.httpRequestType, CoreMatchers.equalTo("DELETE"));
    }

    @Test
    public void testContentTypeApplicationJson() {

        Request request = Request.GET().contentTypeApplicationJson();
        
        assertThat(request.headers.get(HttpConstants.HEADER_CONTENT_TYPE), CoreMatchers.equalTo(HttpConstants.APPLICATION_JSON_WITH_CHARSET_UTF8));

    }

    @Test
    public void testContentTypeApplicationXml() {
        
        Request request = Request.GET().contentTypeApplicationXml();
        
        assertThat(request.headers.get(HttpConstants.HEADER_CONTENT_TYPE), CoreMatchers.equalTo(HttpConstants.APPLICATION_XML_WITH_CHARSET_UTF_8));
    }

    @Test
    public void testUrl() {
        System.out.println("url");
        Url url = Url.host("http:/test.com");
        Request result = Request.GET().url(url);
        
        assertThat(result.url, CoreMatchers.equalTo(url));
    }

    @Test
    public void testAddFileToUpload() {
        
        
        System.out.println("addFileToUpload");
        String param = "";
        File fileToUpload = new File(".");
        Request request = Request.GET().addFileToUpload("fileParam", fileToUpload);
        
        assertThat(request.filesToUpload.size(), CoreMatchers.equalTo(1));
        assertThat(request.filesToUpload.get("fileParam"), CoreMatchers.equalTo(fileToUpload));

        
        
    }

    @Test
    public void testAddHeader() {

        Request request = 
                    Request
                        .GET()
                        .addHeader("header1", "header1_value")
                        .addHeader("header2", "header2_value");
        
        assertThat(request.headers.get("header1"), CoreMatchers.equalTo("header1_value"));
        assertThat(request.headers.get("header2"), CoreMatchers.equalTo("header2_value"));
        assertThat(request.headers.get("header3"), CoreMatchers.equalTo(null));
        
    }

    @Test
    public void testHeaders() {
        
        Map<String, String> headers = Maps.newHashMap();
        headers.put("header1", "header1_value");
        headers.put("header2", "header2_value");

        Request request = 
                    Request
                        .GET()
                        .headers(headers);
        
        assertThat(request.headers.get("header1"), CoreMatchers.equalTo("header1_value"));
        assertThat(request.headers.get("header2"), CoreMatchers.equalTo("header2_value"));
        assertThat(request.headers.get("header3"), CoreMatchers.equalTo(null));
        
    }

    /**
     * Test of addFormParameter method, of class Request.
     */
    @Test
    public void testAddFormParameter() {
        Request request = 
                    Request
                        .GET()
                        .addFormParameter("param1", "param1_value")
                        .addFormParameter("param2", "param2_value");
        
        assertThat(request.formParameters.get("param1"), CoreMatchers.equalTo("param1_value"));
        assertThat(request.formParameters.get("param2"), CoreMatchers.equalTo("param2_value"));
        assertThat(request.formParameters.get("param3"), CoreMatchers.equalTo(null));
    }

    /**
     * Test of formParameters method, of class Request.
     */
    @Test
    public void testFormParameters() {
        
        Map<String, String> formParameters = Maps.newHashMap();
        formParameters.put("param1", "param1_value");
        formParameters.put("param2", "param2_value");
        
        Request request = 
                    Request
                        .GET()
                        .formParameters(formParameters);
                        
        
        assertThat(request.formParameters.get("param1"), CoreMatchers.equalTo("param1_value"));
        assertThat(request.formParameters.get("param2"), CoreMatchers.equalTo("param2_value"));
        assertThat(request.formParameters.get("param3"), CoreMatchers.equalTo(null));
    }

    @Test
    public void testPayload() {

        Object payload = "funkyPayload";
        Request request = Request.GET().payload(payload);
        
        assertThat(request.payload, CoreMatchers.equalTo(payload));
    }

    @Test
    public void testFollowRedirects() {
        
        Request request = Request.GET().followRedirects(true);
        assertThat(request.followRedirects, CoreMatchers.equalTo(true));
        
        request = Request.GET().followRedirects(false);
        assertThat(request.followRedirects, CoreMatchers.equalTo(false));
    }
    
}
