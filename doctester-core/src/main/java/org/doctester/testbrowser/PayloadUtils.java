/**
 * Copyright (C) 2015 the original author or authors.
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

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Map;

public class PayloadUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String prettyPrintRequestPayload(Object payload, Map<String, String> headers) throws IOException {

        if (isContentTypeApplicationJson(headers)) {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(payload);

        } else {
            return "Payload not pretty-printable.";
        }

    }

    public static String prettyPrintResponsePayload(String payload, Map<String, String> headers) throws IOException {

        if (isContentTypeApplicationJson(headers)) {
            return getPrettyFormattedJson(payload);

        } else {
            return payload;
        }
    }

    public static boolean isContentTypeApplicationJson(Map<String, String> headers) {

        String contentType = headers.get(HttpConstants.HEADER_CONTENT_TYPE);

        return contentType != null
                && contentType.contains(HttpConstants.APPLICATION_JSON);

    }

    public static boolean isContentTypeApplicationXml(Map<String, String> headers) {

        String contentType = headers.get(HttpConstants.HEADER_CONTENT_TYPE);

        return contentType != null
                && contentType.contains(HttpConstants.APPLICATION_XML);

    }

    private static String getPrettyFormattedJson(String stringToFormatAsPrettyJson) throws IOException {
        Object json = objectMapper.readValue(stringToFormatAsPrettyJson, Object.class);
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
    }

}
