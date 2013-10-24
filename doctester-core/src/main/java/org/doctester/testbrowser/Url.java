package org.doctester.testbrowser;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * Well. Usually we'd just use URI from the Jdk. But to be honest - we
 * think that URI is hard and not easy to use.
 * 
 * Uri allows for some nice chaining and handles query parameters as well
 * as hosts and paths of your Url in a more natural way than URI.
 * 
 * @author Raphael A. Bauer
 *
 */
public class Url {
	
	private static Logger logger = LoggerFactory.getLogger(Url.class);	
    
    private URIBuilder uriBuilder;
    
    private Url() {
    }
    
    /**
     * Create a Url instance from a host. Host should look like
     * http://myserver:8080 or so.
     * 
     * @param host The host e.g. http://myserver:8080
     * @return The Url you can customize even further with path, query parameters and so on.
     */
    public static Url host(String host) {
        
        Url url = new Url();
        
        try {
            url.uriBuilder = new URIBuilder(host);
        } catch (URISyntaxException e) {
        	logger.error("Error while creating url from host. Your host should look like e.g. http://myserver:8080", e);
        }
        
        return url;
        
    }
   

    /**
     * Set the full path of this Url. Eg. "/my/funky/url"
     * 
     * @param path Eg. "/my/funky/url"
     * @return This Url for chaining.
     */
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
    
    /**
     * Allows you to add query parameters to this url
     * (In your browser something like "?user=bob"
     * 
     * @param key The key for this query parameter.
     * @param value The value for this query parameter.
     * @return This Url for chaining.
     */
    public Url addQueryParameter(String key, String value) {
        
        uriBuilder.setParameter(key, value);
        
        return this;
        
    }
    
    /**
     * Creates a URI from this Uri.
     * 
     * @return The URI you can pass to any lib using Uri.
     */
    public URI toUri() {
        
        URI uri = null;
        try {
            
            uri = uriBuilder.build();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        
        return uri;
        
    }
    
    /**
     * The real life Uri in human readable form.
     */
    public String toString() {
        
        return toUri().toString();
        
    }
    
}
