package org.doctester.testbrowser;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.management.RuntimeErrorException;

import org.apache.http.Header;
import org.apache.http.HttpVersion;
import org.apache.http.ParseException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class TestBrowserImpl implements TestBrowser {

	static final String HANDLE_REDIRECTS = "http.protocol.handle-redirects";
	
    private DefaultHttpClient httpClient;

    public TestBrowserImpl() {
        httpClient = new DefaultHttpClient();
    }

    public List<Cookie> getCookies() {
        return httpClient.getCookieStore().getCookies();
    }

    public Cookie getCookieWithName(String name) {

        List<Cookie> cookies = getCookies();

        // skip through cookies and return cookie you want
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(name)) {
                return cookie;
            }
        }

        return null;
    }

    public void clearCookies() {
        httpClient.getCookieStore().clear();

    }
    
    public Response makeRequest(Request httpRequest) {
        
        Response httpResponse;
        
        if ("GET".equalsIgnoreCase(httpRequest.httpRequestType)
                || "DELETE".equalsIgnoreCase(httpRequest.httpRequestType)) {
            
            httpResponse = makeGetOrDeleteRequest(httpRequest);

        }

        else if ("POST".equalsIgnoreCase(httpRequest.httpRequestType)
                || "PUT".equalsIgnoreCase(httpRequest.httpRequestType)) {
            
            httpResponse = makePostOrPutRequest(httpRequest);

        }  else {
            
            throw new RuntimeErrorException(new Error("Your requested httpRequest.httpRequestType is not supported"));
        }
        

        return httpResponse;

    }
    

    private Response makeGetOrDeleteRequest(Request request) {

        org.apache.http.HttpResponse response;

        try {
            
            HttpUriRequest apacheHttpRequest = null;
            

            httpClient.getParams().setParameter(
                    CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

            if ("GET".equalsIgnoreCase(request.httpRequestType)) {
                
                apacheHttpRequest = new HttpGet(request.url.toUri());
                
            } else if ("DELETE".equalsIgnoreCase(request.httpRequestType)){
                
                apacheHttpRequest = new HttpDelete(request.url.toUri());
            }

            if (request.headers != null) {

                // add all headers
                for (Entry<String, String> header : request.headers
                        .entrySet()) {
                    apacheHttpRequest.addHeader(header.getKey(), header.getValue());
                }

            }
            
            setHandleRedirect(apacheHttpRequest, request.followRedirects);

            response = httpClient.execute(apacheHttpRequest);
            

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Response httpResponse = convertFromApacheHttpResponseToDocTestJHttpResponse(response);

        return httpResponse;
    }



    private Response makePostOrPutRequest(Request httpRequest) {

        org.apache.http.HttpResponse response = null;

        try {

            httpClient.getParams().setParameter(
                    CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

            
            HttpEntityEnclosingRequestBase apacheHttpRequest;
            
            if ("POST".equalsIgnoreCase(httpRequest.httpRequestType)) {
                
                apacheHttpRequest =  new HttpPost(httpRequest.url.toUri());
                
            } else {
                
                apacheHttpRequest =  new HttpPut(httpRequest.url.toUri());
            }
            

            if (httpRequest.headers != null) {
                // add all headers
                for (Entry<String, String> header : httpRequest.headers
                        .entrySet()) {
                    apacheHttpRequest.addHeader(header.getKey(), header.getValue());
                }
            }

            ///////////////////////////////////////////////////////////////////
            // Either add form parameters...
            ///////////////////////////////////////////////////////////////////
            List<BasicNameValuePair> formparams = Lists.newArrayList();
            if (httpRequest.formParameters != null) {

                for (Entry<String, String> parameter : httpRequest.formParameters
                        .entrySet()) {

                    formparams.add(new BasicNameValuePair(parameter.getKey(),
                            parameter.getValue()));
                }
                
                // encode form parameters and add
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams);
                apacheHttpRequest.setEntity(entity);

            }


            ///////////////////////////////////////////////////////////////////
            // Or add multipart file upload
            ///////////////////////////////////////////////////////////////////
            if (httpRequest.filesToUpload != null) {

                MultipartEntity entity = new MultipartEntity(
                        HttpMultipartMode.BROWSER_COMPATIBLE);

                for (Map.Entry<String, File> entry : httpRequest.filesToUpload
                        .entrySet()) {

                    // For File parameters
                    entity.addPart(entry.getKey(),
                            new FileBody((File) entry.getValue()));

                }
                
                

                // post.setEntity(entity);

                apacheHttpRequest.setEntity(entity);

            }
            
            
            ///////////////////////////////////////////////////////////////////
            // Or add payload and convert if Json or Xml
            ///////////////////////////////////////////////////////////////////
            if (httpRequest.payload != null) {
                
                if (httpRequest.headers.containsKey(HttpConstants.APPLICATION_JSON)) {
                    
                    String string = new ObjectMapper().writeValueAsString(httpRequest.payload);
                    apacheHttpRequest.setEntity(new StringEntity(string, "utf-8"));
                    
                } else if (httpRequest.headers.containsKey(HttpConstants.APPLICATION_XML)) {
                    
                    String string = new XmlMapper().writeValueAsString(httpRequest.payload);
                    apacheHttpRequest.setEntity(new StringEntity(string, "utf-8"));
                    
                } else if (httpRequest.payload instanceof String) {
                        
                    StringEntity entity = new StringEntity((String) httpRequest.payload, "utf-8");
                    apacheHttpRequest.setEntity(entity);
                            
                } else {
                    
                    StringEntity entity = new StringEntity(httpRequest.payload.toString(), "utf-8");
                    apacheHttpRequest.setEntity(entity);
                    
                }
                
            }   
            
            setHandleRedirect(apacheHttpRequest, httpRequest.followRedirects);

            // Here we go!
            response = httpClient.execute(apacheHttpRequest);
            apacheHttpRequest.releaseConnection();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Response httpResponse = convertFromApacheHttpResponseToDocTestJHttpResponse(response);
        return httpResponse;

    }
  



    private org.doctester.testbrowser.Response convertFromApacheHttpResponseToDocTestJHttpResponse(org.apache.http.HttpResponse httpResponse) {

        Map<String, String> headers = Maps.newHashMap();

        for (Header header : httpResponse.getAllHeaders()) {

            headers.put(header.getName(), header.getValue());

        }

        int httpStatus = httpResponse.getStatusLine().getStatusCode();

        String body = null;
        try {

            body = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");

        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        org.doctester.testbrowser.Response doctestJHttpResponse = new org.doctester.testbrowser.Response(
                headers, httpStatus, body);

        return doctestJHttpResponse;

    }
    
    /**
     * Tells ApacheHttpClient whether to follow redirects.
     * See also: http://stackoverflow.com/questions/1519392/how-to-prevent-apache-http-client-from-following-a-redirect
     */
    private void setHandleRedirect(HttpUriRequest httpUriRequest, boolean handleRedirect) {
    	
        HttpParams params = new BasicHttpParams();
        params.setParameter(HANDLE_REDIRECTS, handleRedirect);
        httpUriRequest.setParams(params);
    	
    }

}