package org.doctester.rendermachine;
import java.util.List;

import org.apache.http.cookie.Cookie;
import org.doctester.testbrowser.Request;
import org.doctester.testbrowser.Response;
import org.hamcrest.Matcher;


public interface RenderMachineCommands {
    
    public void say(String text);
    
    public void sayNextSection(String headline);
   
    /**
     * @return all cookies saved by this TestBrowser.
     */
    public abstract List<Cookie> sayAndGetCookies();

    public abstract Cookie sayAndGetCookieWithName(String name);
    
    public abstract Response sayAndMakeRequest(Request httpRequest);
    
    public abstract <T> void sayAndAssertThat(String message, String reason, T actual, Matcher<? super T> matcher);
    
    public <T> void sayAndAssertThat(String message, T actual, Matcher<? super T> matcher);
}