/**
 * Copyright (C) 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
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
     * The TestBrowser persists the cookie storage during multiple calls. If you
     * want to wipe out the CookieStorage you can call this method.
     */
    public abstract void clearCookies();

    /**
     * Make calls via this testBrowser. Use Request.GET(), Request.POST()... and
     * so on to make GET, POST and so on requests.
     *
     * @param httpRequest The request to perform
     * @return The response of this request.
     */
    public abstract Response makeRequest(Request httpRequest);

}
