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
package org.r10r.doctester.testbrowser;

/**
 * The one-millionth class for HttpConstant in the universe. We needed it. So
 * much. No joking.
 *
 * @author Raphael A. Bauer
 *
 */
public interface HttpConstants {

    String HEADER_ACCEPT = "Accept";

    String HEADER_CONTENT_TYPE = "Content-Type";

    String APPLICATION_JSON = "application/json";
    String APPLICATION_JSON_WITH_CHARSET_UTF8 = "application/json; charset=utf-8";

    String APPLICATION_XML = "application/xml";
    String APPLICATION_XML_WITH_CHARSET_UTF_8 = "application/xml; charset=utf-8";

    String HEAD = "HEAD";
    String GET = "GET";
    String DELETE = "DELETE";
    String POST = "POST";
    String PUT = "PUT";
    String PATCH = "PATCH";

}
