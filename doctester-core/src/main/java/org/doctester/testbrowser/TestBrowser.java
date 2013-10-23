package org.doctester.testbrowser;
import java.util.List;

import org.apache.http.cookie.Cookie;

public interface TestBrowser {

    /**
     * @return all cookies saved by this TestBrowser.
     */
    public abstract List<Cookie> getCookies();

    public abstract Cookie getCookieWithName(String name);
    
    public abstract void clearCookies();
    
    
    ///////////////////////////////////////////////////////////////////////////
    // GET
    ///////////////////////////////////////////////////////////////////////////
    public abstract Response makeRequest(Request httpRequest);

}