package org.doctester.testbrowser;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * 
 * The response from a call done with the TestBrowser.
 * 
 * @author Raphael A. Bauer
 *
 */
public class Response {
	
	private static Logger logger = LoggerFactory.getLogger(Response.class);	

    public Map<String, String> headers;
    
    public int httpStatus;
    
    public String payload;
    
    
    public Response(Map<String, String> headers, int httpStatus, String payload) {
       
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
     * The payload of this request de-serialized into the specified class type.
     * The payload must be Xml.
     * 
     * @param clazz The class type that should be used to de-serialze the payload.
     * @return An instance of clazz filled with data from the payload.
     */
    public <T> T payloadAsXml(Class<T> clazz) {

        try {
            new XmlMapper().readValue(payload, clazz);
            
        } catch (Exception e) {

        	logger.error("Something went wrong parsing the payload of this response into Xml", e);

        } 

        return null;
    }

    /**
     * The payload of this request de-serialized into the specified TypeReference.
     * The payload must be Xml.
     * 
     * @param typeReference The TypeReference that should be used to de-serialze the payload.
     * @return An instance of clazz filled with data from the payload.
     */
    public <T> T payloadAsXml(TypeReference<T> typeReference) {
        
    	T parsedBody = null;
    	
    	try {
    		parsedBody = new XmlMapper().readValue(payload, typeReference);

        } catch (Exception e) {

        	logger.error("Something went wrong parsing the payload of this response into Xml", e);
        } 

        return parsedBody;
    }

    /**
     * The payload of this request de-serialized into the specified class type.
     * The payload must be Json.
     * 
     * @param clazz The class type that should be used to de-serialze the payload.
     * @return An instance of clazz filled with data from the payload.
     */
    public <T> T payloadAsJson(Class<T> clazz) {

    	T parsedBody = null;
    	
        try {
        	parsedBody = new ObjectMapper().readValue(payload, clazz);
        } catch (Exception e) {
        	logger.error("Something went wrong parsing the payload of this response into Json", e);
        } 
        
        return parsedBody;
        

    }

    /**
     * The payload of this request de-serialized into the specified TypeReference.
     * The payload must be Json.
     * 
     * @param typeReference The TypeReference that should be used to de-serialze the payload.
     * @return An instance of clazz filled with data from the payload.
     */
    public <T> T payloadAsJson(TypeReference<T> typeReference) {

        try {
            new ObjectMapper().readValue(payload, typeReference);
        } catch (Exception e) {
        	logger.error("Something went wrong parsing the payload of this response into Json", e);
        } 
        
        return null;
    }

}
