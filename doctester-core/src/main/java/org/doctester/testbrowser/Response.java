package org.doctester.testbrowser;

import java.io.IOException;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class Response {

    public Map<String, String> headers;
    
    public int httpStatus;
    
    public String body;
    
    
    public Response(Map<String, String> headers, int httpStatus, String body) {
       
        this.headers = headers;
        this.httpStatus = httpStatus;
        this.body = body;
        
    }

    String bodyAsString() {

        return body;

    }

    public <T> T bodyAsXml(Class<T> clazz) {

        try {
            new XmlMapper().readValue(body, clazz);
        } catch (JsonParseException e) {

            e.printStackTrace();
        } catch (JsonMappingException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }

        return null;
    }

    public <T> T bodyAsXml(TypeReference<T> typeReference) {
        
    	T parsedBody = null;
    	
    	try {
    		parsedBody = new XmlMapper().readValue(body, typeReference);

        } catch (JsonParseException e) {

            e.printStackTrace();
        } catch (JsonMappingException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }

        return parsedBody;
    }

    public <T> T bodyAsJson(Class<T> clazz) {

    	T parsedBody = null;
    	
        try {
        	parsedBody = new ObjectMapper().readValue(body, clazz);
        } catch (JsonParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return parsedBody;
        

    }

    public <T> T payloadAsJson(TypeReference<T> typeReference) {

        try {
            new ObjectMapper().readValue(body, typeReference);
        } catch (JsonParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return null;
    }

}
