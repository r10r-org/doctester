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
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.IOException;
import java.util.Map;

/**
 * Provides some utility methods for HTTP request/response
 * payload type detection and pretty-printing.
 */
public class PayloadUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private static final XmlMapper xmlMapper;
    static {
        JacksonXmlModule module = new JacksonXmlModule();
        module.setDefaultUseWrapper(false);
        xmlMapper = new XmlMapper(module);
    }

    /**
     * Attempts to pretty print the payload of an
     * HTTP request based on the headers. Only JSON
     * and XML are supported at the moment.
     *
     * @param payload Payload to be serialized in the request
     * @param headers Headers, should contain "Content-Type"
     * @return Pretty printed payload
     * @throws IOException If something went wrong when serializing the object
     */
    public static String prettyPrintRequestPayload(Object payload, Map<String, String> headers) throws IOException {

        if (isContentTypeApplicationJson(headers)) {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(payload);

        } else if (isContentTypeApplicationXml(headers)) {
            return xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(payload);

        } else {
            return "Payload not printable in the format specified by the Content-Type header.";
        }

    }

    /**
     * Attempts to pretty print the payload of an
     * HTTP response based on the headers. Only JSON
     * is supported at the moment (XML is not because
     * its tree model cannot be read without proper target
     * class information).
     *
     * @param payload Payload to be pretty printed
     * @param headers Headers, should contain "Content-Type"
     * @return Pretty printed payload
     * @throws IOException If something went wrong when (de)serializing 
     */
    public static String prettyPrintResponsePayload(String payload, Map<String, String> headers) throws IOException {

        if (isContentTypeApplicationJson(headers)) {
            return getPrettyFormattedJson(payload);

        } else {
            return payload;
        }
    }

    /**
     * Checks whether the headers define the content type to be JSON.
     *
     * @param headers Headers of a request or response
     * @return true if content-type is JSON
     */
    public static boolean isContentTypeApplicationJson(Map<String, String> headers) {

        String contentType = headers.get(HttpConstants.HEADER_CONTENT_TYPE);

        return contentType != null
                && contentType.contains(HttpConstants.APPLICATION_JSON);

    }

        /**
     * Checks whether the headers define the content type to be XML.
     *
     * @param headers Headers of a request or response
     * @return true if content-type is XML
     */
    public static boolean isContentTypeApplicationXml(Map<String, String> headers) {

        String contentType = headers.get(HttpConstants.HEADER_CONTENT_TYPE);

        return contentType != null
                && contentType.contains(HttpConstants.APPLICATION_XML);

    }

    /**
     * Pretty prints a JSON formatted string.
     *
     * @param stringToFormatAsPrettyJson Source JSON string
     * @return Pretty printed JSON string
     * @throws IOException If an error occurred when (de)serializing
     */
    private static String getPrettyFormattedJson(String stringToFormatAsPrettyJson) throws IOException {
        Object json = objectMapper.readValue(stringToFormatAsPrettyJson, Object.class);
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(json);
    }

}
