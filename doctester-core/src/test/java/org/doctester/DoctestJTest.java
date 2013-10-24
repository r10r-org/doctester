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

package org.doctester;
import static org.hamcrest.CoreMatchers.equalTo;

import org.doctester.testbrowser.Request;
import org.doctester.testbrowser.Url;
import org.junit.Test;


public class DoctestJTest extends DocTester {
    
    @Test
    public void runTest() {
        
        sayNextSection("another fun heading!");
        say("and a very long text...!");
                 
        sayAndMakeRequest(
                Request
                    .GET()
                    .url(testServerUrl().path("search").addQueryParameter("q", "raphael bauer")));
        
        sayAndMakeRequest(Request.GET().contentTypeApplicationJson().url(Url.host("http://ip.jsontest.com/")));
        
        sayNextSection("another fun heading is there...");
        say("and a very long text...!");
        
        sayAndMakeRequest(Request.GET().contentTypeApplicationJson().url(Url.host("http://ip.jsontest.com/")));
        
        say("<code>This is code!</code>");
        say("and another description!"); 
        
        sayAndAssertThat("me!", "", 10, equalTo(10));

    }

    /**
     * Override me if you want to give me a name. You should...
     * @return
     */
    @Override
    public String getName() {
        return DoctestJTest.class.getSimpleName();
    }

    @Override
    public String getTestServerUrl() {
   
        return "http://www.google.com";
    }
    
}
