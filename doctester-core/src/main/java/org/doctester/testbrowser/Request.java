/**
 * Copyright (C) 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.doctester.testbrowser;

import java.io.File;
import java.util.Map;







import com.google.common.collect.Maps;

/**
 * This represents a Request we can pass then to the TestBrowser.
 * 
 * @author Raphael A. Bauer
 *
 */
public class Request {
    
    public String httpRequestType;
    
    public Url url;
    
    public Map<String, File> filesToUpload;
    
    public Map<String, String> headers;
    
    public Map<String, String> formParameters;
    
    public Object payload;
    
    public boolean followRedirects;
    
    /**
     * I am private. Please use GET(), POST() and so on to get an instance of this class.
     */
    private Request() {
        
        filesToUpload = null;
        headers = Maps.newHashMap();
        formParameters = null;
        followRedirects = true;
        
    }
    
    /**
     * 
     * Get a request to perform a Http GET request via the TestBrowser.
     * 
     * @return A request configured for a Http GET request.
     * 
     */
    public static Request GET() {
        
        Request httpRequest = new Request();
        httpRequest.httpRequestType = HttpConstants.GET;
        
        return httpRequest;
        
    }
    
    /**
     * 
     * Get a request to perform a Http POST request via the TestBrowser.
     * 
     * @return A request configured for a Http POST request.
     * 
     */
    public static Request POST() {
        
        Request httpRequest = new Request();
        httpRequest.httpRequestType = HttpConstants.POST;
        
        return httpRequest;
        
    }
    
    /**
     * 
     * Get a request to perform a Http PUT request via the TestBrowser.
     * 
     * @return A request configured for a Http PUT request.
     * 
     */
    public static Request PUT() {
        
        Request httpRequest = new Request();
        httpRequest.httpRequestType = HttpConstants.PUT;
        
        return httpRequest;
        
    }
    
    /**
     * 
     * Get a request to perform a Http DELETE request via the TestBrowser.
     * 
     * @return A request configured for a DELETE request.
     * 
     */
    public static Request DELETE() {
        
        Request httpRequest = new Request();
        httpRequest.httpRequestType = HttpConstants.DELETE;
        
        return httpRequest;
        
    }
    
    /**
     * 
     * Set the Content-Type header to application/json; charset=utf-8.
     * 
     * @return This request for joining.
     * 
     */
    public Request contentTypeApplicationJson() {
        
        addHeader(
                HttpConstants.HEADER_CONTENT_TYPE, HttpConstants.APPLICATION_JSON);
        
        return this;
        
    }
    
    /**
     * 
     * Set the Content-Type header to application/xml; charset=utf-8.
     * 
     * @return This request for joining.
     * 
     */
    public Request contentTypeApplicationXml() {
        
        addHeader(
                HttpConstants.HEADER_CONTENT_TYPE, HttpConstants.APPLICATION_XML);
        
        return this;
        
    }
    


    /**
     * 
     * Set the Url of this request.
     * 
     * @param url The Url of this request.
     * @return This Request for chaining.
     */
    public Request url(Url url) {
        this.url = url;
        return this;
    }

    /**
     * 
     * Add a file to be sent as multipart form post/put.
     * Only makes sense for POST and PUT Http requests.
     * 
     * @param param The parameter for this file.
     * @param fileToUpload The file to upload
     * @return This request for chaining.
     */
    public Request addFileToUpload(String param, File fileToUpload) {
        if (filesToUpload == null) {
            filesToUpload = Maps.newHashMap();
        }
        filesToUpload.put(param, fileToUpload);

        return this;
    }
    
    /**
     * 
     * Add an arbitrary header to this request.
     * 
     * @param key The header key.
     * @param value The header value.
     * @return This request for chaining.
     */
    public Request addHeader(String key, String value) {
        if (headers == null) {
            headers = Maps.newHashMap();
        }
        headers.put(key, value);
        return this;
    }

    /**
     * Set headers for this request. This will wipe out all previously 
     * set headers.
     * 
     * @param headers A map of header keys and header values to use.
     * @return This request for chaining.
     */
    public Request headers(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }
    
    /**
     * 
     * Add an arbitrary form parameter to this request.
     * Only makes sense for POST or PUT requests.
     * 
     * @param key The header key.
     * @param value The header value.
     * @return This request for chaining.
     */
    public Request addFormParameter(String key, String value) {
        if (formParameters == null) {
        	formParameters = Maps.newHashMap();
        }
        formParameters.put(key, value);
        return this;
    }

    /**
     * Set form parameters for this request. Only makes sense for POST or
     * PUT requests. Wipes out all previously set formParameters.
     * 
     * @param formParameters Map of formParemter keys and values to use.
     * @return This request for chaining.
     */
    public Request formParameters(Map<String, String> formParameters) {
        this.formParameters = formParameters;
        return this;
    }

    /**
     * Set the payload for this request. String will be just sent as strings.
     * If you set Content-Type json or xml this Object will converted into
     * the appropriate representation.
     * 
     * Calling this method multiple times will overwrite the payload.
     * 
     * @param payload The payload to use.
     * @return This request for chaining.
     */
    public Request payload(Object payload) {
        this.payload = payload;
        return this;
    }
    
    /**
     * Follow redirects automatically. Or simply do only one request and stop then.
     * 
     * @param followRedirects Whether to follow redirects or no.
     * @return This request for chaining.
     */
    public Request followRedirects(boolean followRedirects) {
    	this.followRedirects = followRedirects;
        return this;
    }

}
