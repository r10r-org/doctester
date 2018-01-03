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

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.JacksonXmlModule;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import java.io.IOException;

/**
 *
 * The response from a call done with the TestBrowser.
 *
 * @author Raphael A. Bauer
 *
 */
public class Response {

    private final Logger logger = LoggerFactory.getLogger(Response.class);

    public final Map<String, String> headers;

    public final int httpStatus;

    public final String payload;

    private final XmlMapper xmlMapper;

    private final ObjectMapper objectMapper;

    public Response(Map<String, String> headers, int httpStatus, String payload) {

        // configure the JacksonXml mapper in a cleaner way...
        JacksonXmlModule module = new JacksonXmlModule();
        // Check out: https://github.com/FasterXML/jackson-dataformat-xml
        // setDefaultUseWrapper produces more similar output to
        // the Json output. You can change that with annotations in your
        // models.
        module.setDefaultUseWrapper(false);
        this.xmlMapper = new XmlMapper(module);

        this.objectMapper = new ObjectMapper();

        this.headers = headers;
        this.httpStatus = httpStatus;
        this.payload = payload;

    }

    /**
     *
     * @return The payload of this response as String. Just the raw String.
     *
     */
    public String payloadAsString() {

        return payload;

    }

    /**
     *
     * @return The payload of this response as String. It tries to determine the
     * content and format the content in a pretty way. Currently works for json.
     *
     */
    public String payloadAsPrettyString() {

        try {
            return PayloadUtils.prettyPrintResponsePayload(payload, headers);
        } catch (IOException ex) {
            logger.error("Something went wrong when pretty printing response payload: " + ex.toString());
            return "Error pretty printing the payload.";
        }
    }

    /**
     * Parses response into Plain old Java objects. First checks header
     * Content-Type and then converts object into suitable Pojo object. Can
     * handle "application/xmlObject" and "application/xml". If you want to
     * force the parsing of a certain content type check out payloadJsonAs/...)
     * and payloadXmlAs(...).
     *
     * @param clazz The class to use as blueprint for parsing the response body.
     * @return An instance of the class or null if parsing went wrong.
     */
    public <T> T payloadAs(Class<T> clazz) {

        T parsedBody = null;

        if (PayloadUtils.isContentTypeApplicationXml(headers)) {
            parsedBody = payloadXmlAs(clazz);
        } else if (PayloadUtils.isContentTypeApplicationJson(headers)) {
            parsedBody = payloadJsonAs(clazz);
        } else {
            logger.error("Could neither find application/json or application/xml content type in response. Returning null.");
        }

        return parsedBody;

    }

    /**
     * The payload of this request de-serialized into the specified class type.
     * The payload must be valid Xml.
     *
     * @param clazz The class type that should be used to de-serialize the
     * payload.
     * @return An instance of clazz filled with data from the payload.
     */
    public <T> T payloadXmlAs(Class<T> clazz) {

        T parsedBody = null;

        try {
            parsedBody = this.xmlMapper.readValue(payload, clazz);

        } catch (Exception e) {

            logger.error("Something went wrong parsing the payload of this response into Xml", e);

        }

        return parsedBody;
    }

    /**
     * The payload of this request de-serialized into the specified
     * TypeReference. The payload must be Xml.
     *
     * @param typeReference The TypeReference that should be used to
     * de-serialize the payload.
     * @return An instance of clazz filled with data from the payload.
     */
    public <T> T payloadXmlAs(TypeReference<T> typeReference) {

        T parsedBody = null;

        try {
            parsedBody = xmlMapper.readValue(payload, typeReference);

        } catch (IOException e) {

            logger.error("Something went wrong parsing the payload of this response into Xml", e);
        }

        return parsedBody;
    }

    /**
     * The payload of this request de-serialized into the specified class type.
     * The payload must be Json.
     *
     * @param clazz The class type that should be used to de-serialize the
     * payload.
     * @return An instance of clazz filled with data from the payload.
     */
    public <T> T payloadJsonAs(Class<T> clazz) {

        T parsedBody = null;

        try {
            parsedBody = objectMapper.readValue(payload, clazz);
        } catch (IOException e) {
            logger.error("Something went wrong parsing the payload of this response into Json", e);
        }

        return parsedBody;

    }

    /**
     * The payload of this request de-serialized into the specified
     * TypeReference. The payload must be Json.
     *
     * @param typeReference The TypeReference that should be used to de-serialize
     * the payload.
     * @return An instance of clazz filled with data from the payload.
     */
    public <T> T payloadJsonAs(TypeReference<T> typeReference) {

        T parsedBody = null;

        try {
            parsedBody = objectMapper.readValue(payload, typeReference);
        } catch (IOException e) {
            logger.error("Something went wrong parsing the payload of this response into Json", e);
        }

        return parsedBody;
    }

}
