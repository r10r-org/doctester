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

package controllers.utils;

import java.util.List;
import ninja.utils.NinjaConstant;
import ninja.utils.NinjaTestBrowser;
import ninja.utils.NinjaTestServer;
import org.apache.http.cookie.Cookie;

import org.r10r.doctester.DocTester;
import org.r10r.doctester.testbrowser.Request;
import org.r10r.doctester.testbrowser.Response;
import org.r10r.doctester.testbrowser.TestBrowser;
import org.r10r.doctester.testbrowser.TestBrowserImpl;
import org.r10r.doctester.testbrowser.Url;
import org.junit.After;
import org.junit.Before;

public abstract class NinjaTest implements TestBrowser {
	
	public NinjaTestServer ninjaTestServer;
    
    /** A persistent HttpClient that stores cookies to make requests */
	public TestBrowser ninjaTestBrowser;

    public NinjaTest() {
    }

    @Before
    public void startServerInTestMode() {
        System.setProperty(NinjaConstant.MODE_KEY_NAME, NinjaConstant.MODE_TEST);
        ninjaTestServer = new NinjaTestServer();
        ninjaTestBrowser = new TestBrowserImpl();
    }

    @After
    public void shutdownServer() {
    	System.clearProperty(NinjaConstant.MODE_KEY_NAME);
        ninjaTestServer.shutdown();
    }
    
    public final Url testServerUrl() {
        
       return Url.host(ninjaTestServer.getServerAddress());
        
    }

    @Override
    public List<Cookie> getCookies() {
        return ninjaTestBrowser.getCookies();
    }

    @Override
    public Cookie getCookieWithName(String name) {
        return ninjaTestBrowser.getCookieWithName(name);
    }

    @Override
    public void clearCookies() {
        ninjaTestBrowser.clearCookies();
    }

    @Override
    public Response makeRequest(Request httpRequest) {
        return ninjaTestBrowser.makeRequest(httpRequest);
    }
    
    

}
