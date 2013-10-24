package org.doctester.rendermachine;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.http.cookie.Cookie;
import org.doctester.testbrowser.Request;
import org.doctester.testbrowser.Response;
import org.doctester.testbrowser.TestBrowser;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.html.HtmlEscapers;
import com.google.common.io.ByteStreams;
import com.google.common.io.Files;



public class RenderMachineImpl implements RenderMachine {
    
    String BASE_DIR = "target/site";
    
    List<String> htmlDocument;
    
    List<String> headerTitle = Lists.newArrayList();
    List<String> headerId = Lists.newArrayList();

    private TestBrowser testBrowser = null;
    
    private String fileName = null;
    
    public RenderMachineImpl() {
        
        htmlDocument = Lists.newArrayList();
        
    }
    
    
    private void prepareCss() {
        
        copyFromJar("META-INF/resources/webjars/bootstrap/3.0.0/css");
        
    }
    
    private void prepareJQuery() {
        
        copyFromJar("META-INF/resources/webjars/jquery/1.9.0");
        
    }
    
    
    public void copyFromJar(String path) {
        
        try {
            
            URL url = this.getClass().getClassLoader().getResource(path);

            System.out.println("url" + url.getFile());
            //if (en.hasMoreElements()) {
              //  URL url = en.nextElement();
                JarURLConnection urlcon = (JarURLConnection) (url.openConnection());
                try (JarFile jar = urlcon.getJarFile();) {
                    Enumeration<JarEntry> entries = jar.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry jarEntry = entries.nextElement();
                                
                        System.out.println(jarEntry.getName());
                        
                        if (jarEntry.isDirectory()) {
                            
                            new File(BASE_DIR + File.separator + jarEntry.getName()).mkdirs();
                            
                        } else {
                            

                            ByteStreams.copy(
                                    jar.getInputStream(jarEntry), 
                                    new FileOutputStream(new File(BASE_DIR + File.separator + jarEntry.getName())));
                            
                        }
                        
                    }
                }

            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    public void setTestBrowser(TestBrowser testBrowser) {
        this.testBrowser = testBrowser;
    }
    
    
    public void finishAndWriteOut() {
        
        prepareCss();
        
        prepareJQuery();
        
        List<String> finalHtmlDocument = Lists.newArrayList();
        

        finalHtmlDocument.add(RenderMachineHtml.HTML_BEGIN);
        finalHtmlDocument.add(String.format(RenderMachineHtml.HTML_HEAD, fileName));
        finalHtmlDocument.add(RenderMachineHtml.BODY_BEGIN);
        
        String headerFormattedWithTitle = String.format(
                RenderMachineHtml.BOOTSTRAP_HEADER,
                fileName);
        
        finalHtmlDocument.add(headerFormattedWithTitle);
         
        finalHtmlDocument.add(RenderMachineHtml.BOOTSTRAP_CONTAINER_BEGIN);
        finalHtmlDocument.add(RenderMachineHtml.BOOTSTRAP_LEFT_NAVBAR_BEGIN);
        
        for (String string : headerTitle) {
            
            String elementToAdd = String.format(
                    RenderMachineHtml.BOOTSTRAP_LEFT_NAVBAR_ELEMENT,
                    convertTextToId(string),
                    string);
                    
            finalHtmlDocument.add(elementToAdd);
            
        }
        
        finalHtmlDocument.add(RenderMachineHtml.BOOTSTRAP_LEFT_NAVBAR_END);
        
        
        finalHtmlDocument.add(RenderMachineHtml.BOOTSTRAP_RIGHT_CONTENT_BEGIN);
        
        
        
        for (String string : htmlDocument) {
            finalHtmlDocument.add(string);
        }
        
        
        finalHtmlDocument.add(RenderMachineHtml.BOOTSTRAP_RIGHT_CONTENT_BEGIN);
        finalHtmlDocument.add(RenderMachineHtml.BOOTSTRAP_CONTAINER_END);
        finalHtmlDocument.add(RenderMachineHtml.BODY_END); 
        finalHtmlDocument.add(RenderMachineHtml.HTML_END); 
        
   
        doWriteOut(finalHtmlDocument);
        
    }
    
    private void doWriteOut(List<String> finalHtmlDocument) {
        
        String completeHtmlOutput = Joiner.on("\n").join(finalHtmlDocument);
        
        try {
            
            Files.write(completeHtmlOutput, new File(BASE_DIR + File.separator + fileName + ".html"), Charsets.UTF_8);
            
        } catch (IOException e) {
            
            e.printStackTrace();
            
        }
        
    }
    
    
    public void say(String textAsParagraph) {
        
        htmlDocument.add("<p>");
        htmlDocument.add(textAsParagraph);
        htmlDocument.add("</p>");
        
    }
    
    public void sayNextSection(String textAsH1) {
        
        headerTitle.add(textAsH1);
        
        String h1WithId = "<h1 id=\"%s\">";
        String textAsH1Id = convertTextToId(textAsH1);
        
        htmlDocument.add(String.format(h1WithId, textAsH1Id));
        htmlDocument.add(textAsH1);
        htmlDocument.add("</h1>");
        
    }
    
    public String convertTextToId(String textAsH1) {
        
        String textAsH1Converted = textAsH1.toLowerCase();
        textAsH1Converted = textAsH1Converted.replaceAll("\\W", "");
        
        return textAsH1Converted;
        
    }

    public List<Cookie> sayAndGetCookies() {
        List<Cookie> cookies = testBrowser.getCookies();
        
        htmlDocument.add("<p>");
        
        for (Cookie cookie: cookies) {
            
            htmlDocument.add("<b>Cookies</b><br/>");
            printCookie(cookie);

            
        }
        
        htmlDocument.add("</p>");
        
        return cookies;
    }

    public Cookie sayAndGetCookieWithName(String name) {
        
        Cookie cookie = testBrowser.getCookieWithName(name);
        htmlDocument.add("<b>Cookie</b><br/>");
        printCookie(cookie);
        
        return cookie;

    }
    
    private void printCookie(Cookie cookie) {
        
        htmlDocument.add("Name: " + cookie.getName() + "<br/>");
        htmlDocument.add("Path: " + cookie.getPath() + "<br/>");
        htmlDocument.add("Domain: " + cookie.getDomain() + "<br/>");
        htmlDocument.add("Value: " + cookie.getValue() + "<br/>");
        
    }

   

    @Override
    public void setFileName(String fileName) {
        this.fileName = fileName;
        
    }


    @Override
    public Response sayAndMakeRequest(Request httpRequest) {
        
        Response httpResponse = testBrowser.makeRequest(httpRequest); 
        
        printHttpRequestAndHttpResponse(httpRequest, httpResponse);
        
        return httpResponse;
        
    }

    
    public void printHttpRequestAndHttpResponse(Request httpRequest, Response response) {

        htmlDocument.add("<div class=\"panel panel-default\">");
        htmlDocument.add("<div class=\"panel-body\">");
      
      
        htmlDocument.add("<div class=\"panel panel-info\">");
        
        htmlDocument.add("<div class=\"panel-heading\">");
        htmlDocument.add("<h3 class=\"panel-title\">Http Request</h3>");
        htmlDocument.add("</div>");
        
        htmlDocument.add("<div class=\"panel-body\">");
        htmlDocument.add("<dl class=\"dl-horizontal\">");
        htmlDocument.add("<dt>Type</dt><dd>" + httpRequest.httpRequestType + "</dd>");
        htmlDocument.add("<dt>Url</dt><dd>" + httpRequest.url.toString() + "</dd>");
        
        htmlDocument.addAll(getHtmlFormattedHeaders(httpRequest.headers));
        
        
        htmlDocument.add("</dl>");
        
       
        htmlDocument.add("</div>");
        htmlDocument.add("</div>");
        
        
        
        htmlDocument.add("<div class=\"panel panel-info\">");
        
        htmlDocument.add("<div class=\"panel-heading\">");
        htmlDocument.add("<h3 class=\"panel-title\">Http Response</h3>");
        htmlDocument.add("</div>");
        
        htmlDocument.add("<div class=\"panel-body\">");
        htmlDocument.add("<dl class=\"dl-horizontal\">");
        htmlDocument.add("<dt>Status</dt><dd>" + response.httpStatus + "</dd>");
        
        htmlDocument.addAll(getHtmlFormattedHeaders(response.headers));
        
        if (response.payload == null) {
        	htmlDocument.add("<dt>Content</dt><dd>No Body content.</dd>");
        } else {
        	htmlDocument.add("<dt>Content</dt><dd><div class=\"http-response-body\">" + HtmlEscapers.htmlEscaper().escape(response.payload) + "</div></dd>");
        }
        
        
        htmlDocument.add("</dl>");
        htmlDocument.add("</div>");
        
        htmlDocument.add("</div>");
        
        htmlDocument.add("</div>");
        htmlDocument.add("</div>");
        
    }
    
    private List<String> getHtmlFormattedHeaders(Map<String, String> headers) {
        
        
        List<String> htmlStuff = Lists.newArrayList(); 
        
        
        if (!headers.isEmpty()) {
            
            htmlStuff.add("<dt>Header</dt>");
            htmlStuff.add("<dd>");
        
            htmlStuff.add("<div class=\"panel-body\">");
            htmlStuff.add("<dl class=\"dl-horizontal\">");
        
            for (Entry<String, String> header : headers.entrySet()) {
                
                htmlStuff.add("<dt>" + header.getKey() + "</dt>");
                htmlStuff.add("<dd><div class=\"http-response-body\">" + header.getValue() + "</div></dd>");   
                
            }
        

           
            htmlStuff.add("</dl>");
            htmlStuff.add("</div>");
            htmlStuff.add("</dd>");
            
        }
        
        return htmlStuff;
        
        
        
    }

    @Override
    public <T> void sayAndAssertThat(String message, 
                               T actual,
                               Matcher<? super T> matcher) {
        
        sayAndAssertThat(message, "", actual, matcher);

    }
       
    @Override
    public <T> void sayAndAssertThat(String message, 
                               String reason,
                               T actual,
                               Matcher<? super T> matcher) {
        
        htmlDocument.add("<div class=\"alert alert-success\">");
        htmlDocument.add(message);
        htmlDocument.add("</div>");

        MatcherAssert.assertThat(reason, actual, matcher);

    }


    @Override
    public void sayRaw(String rawHtml) {
        htmlDocument.add(rawHtml);
        
    }
    
}