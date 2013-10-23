package org.doctester.rendermachine;
import java.util.List;

import org.apache.http.cookie.Cookie;
import org.doctester.testbrowser.Request;
import org.doctester.testbrowser.Response;
import org.hamcrest.Matcher;


public interface RenderMachineCommands {
    
    /**
     * A text that will be wrapped inside a paragraph.
     * No escaping is done. You can use your own html tags inside the text.
     * 
     * @param text A text that may contain html tags like "This is my <b>bold</b> text".
     */
    public void say(String text);
    
    /**
     * A text that will be wrapped inside a h1.
     * No escaping is done. You can use your own html tags inside the text.
     * 
     * @param headline A text that may contain html tags like "This is my headline text".
     */
    public void sayNextSection(String headline);
    
    /**
     * If you want to let the renderer render some raw custom
     * html use this method.
     * 
     * @param rawHtml A raw plain html String like "<h3>Another headline</h3>"
     */
    public void sayRaw(String rawHtml);
   
    /**
     * @return all cookies saved by this TestBrowser.
     */
    public abstract List<Cookie> sayAndGetCookies();

    public abstract Cookie sayAndGetCookieWithName(String name);
    
    public abstract Response sayAndMakeRequest(Request httpRequest);
    
    public abstract <T> void sayAndAssertThat(String message, String reason, T actual, Matcher<? super T> matcher);
    
    public <T> void sayAndAssertThat(String message, T actual, Matcher<? super T> matcher);
}