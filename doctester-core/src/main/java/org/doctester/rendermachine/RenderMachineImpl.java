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
import java.io.FileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class RenderMachineImpl implements RenderMachine {
  
    private final static Logger logger = LoggerFactory.getLogger(RenderMachineImpl.class);

    private final String BASE_DIR = "target/site/doctester";
    
    private final String INDEX_FILE_WITHOUT_SUFFIX = "index";
    
    List<String> htmlDocument;
    
    List<String> headerTitle;
    List<String> headerId;

    private TestBrowser testBrowser = null;
    
    private String fileName = null;
    
    public RenderMachineImpl() {
        headerTitle = Lists.newArrayList();
        headerId = Lists.newArrayList();
        htmlDocument = Lists.newArrayList();
        
    }
    
     @Override
    public void say(String textAsParagraph) {
        
        htmlDocument.add("<p>");
        htmlDocument.add(textAsParagraph);
        htmlDocument.add("</p>");
        
    }
    
    @Override
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
    
    @Override
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

    @Override
    public Cookie sayAndGetCookieWithName(String name) {
        
        Cookie cookie = testBrowser.getCookieWithName(name);
        htmlDocument.add("<b>Cookie</b><br/>");
        printCookie(cookie);
        
        return cookie;

    }
    
    
    @Override
    public Response sayAndMakeRequest(Request httpRequest) {
        
        Response httpResponse = testBrowser.makeRequest(httpRequest); 
        
        printHttpRequestAndHttpResponse(httpRequest, httpResponse);
        
        return httpResponse;
        
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
 
    
    @Override
    public void setTestBrowser(TestBrowser testBrowser) {
        this.testBrowser = testBrowser;
    }

        
    private void printCookie(Cookie cookie) {
        
        htmlDocument.add("Name: " + cookie.getName() + "<br/>");
        htmlDocument.add("Path: " + cookie.getPath() + "<br/>");
        htmlDocument.add("Domain: " + cookie.getDomain() + "<br/>");
        htmlDocument.add("Value: " + cookie.getValue() + "<br/>");
        
    }
    
    
    @Override
    public void finishAndWriteOut() {
        
        unzipCssFromJarIntoBaseDirectory();      
        unzipJQueryFromJarIntoBaseDirectory();
        
        doCreateHtmlPageforThisDoctest();  
        doCreateIndexPage();
        
    }
    
    @Override
    public void setFileName(String fileName) {
        this.fileName = fileName;
        
    }
    
    
    private void doCreateHtmlPageforThisDoctest() {
    
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
        
        
        finalHtmlDocument.add(RenderMachineHtml.BOOTSTRAP_RIGHT_CONTENT_END);
        finalHtmlDocument.add(RenderMachineHtml.BOOTSTRAP_CONTAINER_END);
        finalHtmlDocument.add(RenderMachineHtml.BODY_END); 
        finalHtmlDocument.add(RenderMachineHtml.HTML_END); 
        
   
        writeOutListOfHtmlStringsIntoFile(finalHtmlDocument, fileName);
    
    }
    
    
    private void doCreateIndexPage() {
        
        File [] files = collectAllDoctestsToCreateIndexFile(BASE_DIR);
      
        
        List<String> finalHtmlDocument = Lists.newArrayList();
        

        finalHtmlDocument.add(RenderMachineHtml.HTML_BEGIN);
        finalHtmlDocument.add(String.format(RenderMachineHtml.HTML_HEAD, INDEX_FILE_WITHOUT_SUFFIX));
        finalHtmlDocument.add(RenderMachineHtml.BODY_BEGIN);
        
        String headerFormattedWithTitle = String.format(
                RenderMachineHtml.BOOTSTRAP_HEADER,
                INDEX_FILE_WITHOUT_SUFFIX);
        
        finalHtmlDocument.add(headerFormattedWithTitle);
        
        finalHtmlDocument.add(RenderMachineHtml.BOOTSTRAP_CONTAINER_BEGIN);
        finalHtmlDocument.add(RenderMachineHtml.BOOTSTRAP_LEFT_NAVBAR_EMPTY);                
        
        finalHtmlDocument.add(RenderMachineHtml.BOOTSTRAP_RIGHT_CONTENT_BEGIN);
        
        
        for (File file : files) {
        
            String link = String.format("<a href=\"%s\">%s</a>", file.getName(), file.getName());
            
            finalHtmlDocument.add(link);
            finalHtmlDocument.add(RenderMachineHtml.HTML_NEWLINE);
        }
        
        
        finalHtmlDocument.add(RenderMachineHtml.BOOTSTRAP_RIGHT_CONTENT_END);
        finalHtmlDocument.add(RenderMachineHtml.BOOTSTRAP_CONTAINER_END);
        finalHtmlDocument.add(RenderMachineHtml.BODY_END); 
        finalHtmlDocument.add(RenderMachineHtml.HTML_END); 
        
   
        writeOutListOfHtmlStringsIntoFile(finalHtmlDocument, INDEX_FILE_WITHOUT_SUFFIX);
    
    
    }
    
    
    private File [] collectAllDoctestsToCreateIndexFile(String baseDirectoryForCollectingDoctesterHtmlFiles) {
    
            File [] files = new File(baseDirectoryForCollectingDoctesterHtmlFiles).listFiles(new FileFilter() {

            @Override
            public boolean accept(File pathname) {
               
                if (!pathname.getName().endsWith(".html")) {
                    return false;
                } else if (pathname.getName().equals(INDEX_FILE_WITHOUT_SUFFIX + ".html")) {
                   return false; 
                } else {
                    return true;
                }
                
            }
        });
        
        
        return files;
    
    
    }
    
    
    private void writeOutListOfHtmlStringsIntoFile(
            List<String> finalHtmlDocument, 
            String fileNameWithoutSuffix) {
        
        String completeHtmlOutput = Joiner.on("\n").join(finalHtmlDocument);
        
        try {
            
            Files.write(
                    completeHtmlOutput, 
                    new File(
                            BASE_DIR 
                                    + File.separator 
                                    + fileNameWithoutSuffix + ".html"), 
                    Charsets.UTF_8);
            
        } catch (IOException e) {
            
            logger.error("An error ocurred while writing out html to file", e);
            
        }
        
    }




    
    private void printHttpRequestAndHttpResponse(Request httpRequest, Response response) {

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
    
    private void unzipCssFromJarIntoBaseDirectory() {
        
        unzipFromJar("META-INF/resources/webjars/bootstrap/3.0.0/css", BASE_DIR);
        
    }
    
    private void unzipJQueryFromJarIntoBaseDirectory() {
        
        unzipFromJar("META-INF/resources/webjars/jquery/1.9.0", BASE_DIR);
        
    }
    
    
    private void unzipFromJar(String classpathLocation, String destinationDirectory) {

        try {

            URL url = this.getClass().getClassLoader().getResource(classpathLocation);

            JarURLConnection urlcon = (JarURLConnection) (url.openConnection());
            try (JarFile jar = urlcon.getJarFile();) {
                Enumeration<JarEntry> entries = jar.entries();
                while (entries.hasMoreElements()) {
                    JarEntry jarEntry = entries.nextElement();

                    if (jarEntry.isDirectory()) {

                        new File(destinationDirectory + File.separator + jarEntry.getName()).mkdirs();

                    } else {

                        ByteStreams.copy(
                                jar.getInputStream(jarEntry),
                                new FileOutputStream(new File(destinationDirectory + File.separator + jarEntry.getName())));

                    }

                }
            }

        } catch (IOException e) {
            logger.error("An error occurred while copying from webjars archive to site directory", e);
        }

    }
    
}