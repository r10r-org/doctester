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
package org.r10r.doctester.rendermachine;

import java.util.List;

import org.apache.http.cookie.Cookie;
import org.r10r.doctester.testbrowser.Request;
import org.r10r.doctester.testbrowser.Response;
import org.hamcrest.Matcher;

public interface RenderMachineCommands {

    /**
     * A text that will be wrapped inside a paragraph. No escaping is done. You
     * can use your own html tags inside the text.
     *
     * @param text A text that may contain html tags like "This is my
     * <b>bold</b>
     * text".
     */
    public void say(String text);

    /**
     * A text that will be wrapped inside a h1. No escaping is done. You can use
     * your own html tags inside the text.
     *
     * @param headline A text that may contain html tags like "This is my
     * headline text".
     */
    public void sayNextSection(String headline);

    /**
     * If you want to let the renderer render some raw custom html use this
     * method.
     *
     * @param rawHtml A raw plain html String like "<h3>Another headline</h3>"
     */
    public void sayRaw(String rawHtml);

    /**
     * @return all cookies saved by this TestBrowser.
     */
    public List<Cookie> sayAndGetCookies();

    public Cookie sayAndGetCookieWithName(String name);

    public Response sayAndMakeRequest(Request httpRequest);

    public <T> void sayAndAssertThat(String message, String reason, T actual, Matcher<? super T> matcher);

    public <T> void sayAndAssertThat(String message, T actual, Matcher<? super T> matcher);
}
