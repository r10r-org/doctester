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

import static org.junit.Assert.assertTrue;

import org.doctester.testbrowser.Request;
import org.doctester.testbrowser.Response;
import org.junit.Before;
import org.junit.Test;

public class LoginLogoutControllerTest extends NinjaApiDoctester {
    
    @Before
    public void setup() {
        
        makeRequest(Request.GET().url(testServerUrl().path("setup")));
        
    }

    @Test
    public void testLogingLogout() {

        // /////////////////////////////////////////////////////////////////////
        // Test posting of article does not work without login
        // /////////////////////////////////////////////////////////////////////
        Response response = makeRequest(
        		Request
        		.GET()
        		.url(
        				testServerUrl().path("article/new")));

        System.out.println(response);
        assertTrue(response.payload.contains("Error. Forbidden."));

        // /////////////////////////////////////////////////////////////////////
        // Login
        // /////////////////////////////////////////////////////////////////////
        response = makeRequest(
        		Request
        		.POST()
        		.url(
        				testServerUrl().path("login"))
        		.addFormParameter("username", "bob@gmail.com")
        		.addFormParameter("password", "secret"));

        // /////////////////////////////////////////////////////////////////////
        // Test posting of article works when are logged in
        // /////////////////////////////////////////////////////////////////////
        response = makeRequest(
        		Request
        		.GET()
        		.url(
        				testServerUrl().path("article/new")));
        
        assertTrue(response.payload.contains("New article"));

        // /////////////////////////////////////////////////////////////////////
        // Logout
        // /////////////////////////////////////////////////////////////////////
        response = makeRequest(
        		Request
        		.GET()
        		.url(
        				testServerUrl().path("logout")));

        // /////////////////////////////////////////////////////////////////////
        // Assert that posting of article does not work any more...
        // /////////////////////////////////////////////////////////////////////
        response = makeRequest(
        		Request
        		.GET()
        		.url(
        				testServerUrl().path("article/new")));
        
        assertTrue(response.payload.contains("Error. Forbidden."));

    }

}
