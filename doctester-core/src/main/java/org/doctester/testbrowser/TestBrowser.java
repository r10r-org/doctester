package org.doctester.testbrowser;
import java.util.List;

import org.apache.http.cookie.Cookie;

public interface TestBrowser {

    /**
     * @return all cookies saved by this TestBrowser.
     */
    public abstract List<Cookie> getCookies();

    /**
     * Get cookie with a specific name.
     * 
     * @param name Name of the cookie
     * @return The value of the cookie or null if not there
     */
    public abstract Cookie getCookieWithName(String name);
    
    /**
     * The TestBrowser persists the cookie storage during
     * multiple calls.
     * If you want to wipe out the CookieStorage you can call this method.
     */
    public abstract void clearCookies();
    

    /**
     * Make calls via this testBrowser. Use Request.GET(), Request.POST()... and so on
     * to make GET, POST and so on requests.
     * 
     * @param httpRequest The request to perform
     * @return The response of this request.
     */
    public abstract Response makeRequest(Request httpRequest);

}