package org.doctester.testbrowser;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.utils.URIBuilder;

public class Url {
    
    URIBuilder uriBuilder;
    
    private Url() {
    }
    
    
    public static Url host(String host) {
        
        Url url = new Url();
        
        try {
            url.uriBuilder = new URIBuilder(host);
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return url;
        
    }
    
    public Url path(String path) {
        
        String pathWithLeadingSlash;
           
        if (!path.startsWith("/")) {
            pathWithLeadingSlash = "/" + path;
        } else {
            pathWithLeadingSlash = path;
        }
        
        
        uriBuilder.setPath(pathWithLeadingSlash);
        
        return this;
        
    }
    
    public Url addQueryParameter(String name, String value) {
        
        uriBuilder.setParameter(name, value);
        
        return this;
        
    }
    
    public URI toUri() {
        
        URI uri = null;
        try {
            
            uri = uriBuilder.build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        
        return uri;
        
    }
    
    public String toString() {
        
        return toUri().toString();
        
    }
    
}
