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
        
        
        sayAndMakeRequest(Request.GET().applicationJson().url(Url.host("http://ip.jsontest.com/")));
        
        sayNextSection("another fun heading is there...");
        say("and a very long text...!");
        
        sayAndMakeRequest(Request.GET().applicationJson().url(Url.host("http://ip.jsontest.com/")));
        
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
