package org.doctester.testbrowser;

import java.io.File;
import java.util.Map;



import com.google.common.collect.Maps;

public class Request {
    
    public String httpRequestType;
    
    public Url url;
    
    public Map<String, File> filesToUpload;
    
    public Map<String, String> headers;
    
    public Map<String, String> formParameters;
    
    public Object payload;
    
    private Request(){
        
        filesToUpload = Maps.newHashMap();
        headers = Maps.newHashMap();
        formParameters = Maps.newHashMap();
        
    }
    
    public static Request GET() {
        
        Request httpRequest = new Request();
        httpRequest.httpRequestType = "GET";
        
        return httpRequest;
        
    }
    
    public static Request POST() {
        
        Request httpRequest = new Request();
        httpRequest.httpRequestType = "POST";
        
        return httpRequest;
        
    }
    
    public static Request PUT() {
        
        Request httpRequest = new Request();
        httpRequest.httpRequestType = "PUT";
        
        return httpRequest;
        
    }
    
    public static Request DELETE() {
        
        Request httpRequest = new Request();
        httpRequest.httpRequestType = "DELETE";
        
        return httpRequest;
        
    }
    
    public Request applicationJson() {
        
        addHeader(
                HttpConstants.HEADER_ACCEPT, HttpConstants.APPLICATION_JSON);
        
        return this;
        
    }
    
    public Request applicationXml() {
        
        addHeader(
                HttpConstants.HEADER_ACCEPT, HttpConstants.APPLICATION_XML);
        
        return this;
        
    }
    

    public Request addHeader(String name, String value) {
        if (headers == null) {
            headers = Maps.newHashMap();
        }
        headers.put(name, value);
        return this;
    }

    public Request url(Url url) {
        this.url = url;
        return this;
    }

    public Request addFileToUpload(String param, File fileToUpload) {
        if (filesToUpload == null) {
            filesToUpload = Maps.newHashMap();
        }
        filesToUpload.put(param, fileToUpload);

        return this;
    }

    public Request headers(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public Request formParameters(Map<String, String> formParameters) {
        this.formParameters = formParameters;
        return this;
    }

    public Request payload(Object payload) {
        this.payload = payload;
        return this;
    }

}
