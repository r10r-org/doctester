package org.doctester;
import java.util.List;
import java.util.UUID;

import javax.management.RuntimeErrorException;

import org.apache.http.cookie.Cookie;
import org.doctester.rendermachine.RenderMachine;
import org.doctester.rendermachine.RenderMachineCommands;
import org.doctester.rendermachine.RenderMachineImpl;
import org.doctester.testbrowser.Request;
import org.doctester.testbrowser.Response;
import org.doctester.testbrowser.TestBrowser;
import org.doctester.testbrowser.TestBrowserImpl;
import org.doctester.testbrowser.Url;
import org.hamcrest.Matcher;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;


public abstract class DocTester implements TestBrowser, RenderMachineCommands {
    
    /**
     * Use this system variable to configure your own RenderMachine.
     */
    private static String RENDER_MACHINE_KEY = "org.doctester.rendermachine";
    
    private static String RENDER_MACHINE_DEFAULT = RenderMachineImpl.class.getName();

    // Unique for each test
    private TestBrowser testBrowser;
    
    // Unique for whole testClass => one outputfile per testClass.
    public static RenderMachine renderMachine;

    @BeforeClass
    public static void beginDoctest() {
        
        // We create a new instance from the RenderMachine / or take the default one.
        String renderMachineClass = System.getProperty(RENDER_MACHINE_KEY, RENDER_MACHINE_DEFAULT);

        Class<?> clazz;
        
        try {
            clazz = Class.forName(renderMachineClass);
            renderMachine = (RenderMachine) clazz.newInstance();
        } catch (Exception e) {
            
            e.printStackTrace();
            throw new RuntimeErrorException(
                    new Error(
                            "Something went wrong with configuration of RenderMachine via " 
                                    + RENDER_MACHINE_KEY));
            
        } 
        

    }


    @Before
    public void setupForTestCaseMethod() {

        // This is all a bit strange. But JUnit's @BeforeClass
        // is static. Therefore the only possibility to transmit
        // the filename to the renderMachine.
        // We accept that we set the fileName too often.
        testBrowser = getTestBrowser();
        renderMachine.setTestBrowser(testBrowser);
        renderMachine.setFileName(getName());

    }
    
    @AfterClass
    public static void finishDocTest() {

        renderMachine.finishAndWriteOut();

    }

    // ////////////////////////////////////////////////////////////////////////
    // Say methods to print stuff into html
    // ////////////////////////////////////////////////////////////////////////
    @Override
    public final void say(String textAsParagraph) {
        renderMachine.say(textAsParagraph);
    }
    
    @Override
    public final void sayNextSection(String textAsH1) {
        renderMachine.sayNextSection(textAsH1);
    }
    
    @Override
    public final void sayRaw(String rawHtml) {
        renderMachine.sayRaw(rawHtml);
    }
    
    @Override
    public final <T> void sayAndAssertThat(String message, 
                               String reason,
                               T actual,
                               Matcher<? super T> matcher) {
        
        renderMachine.sayAndAssertThat(message, reason, actual, matcher);
  
    }
    
    @Override
    public final <T> void sayAndAssertThat(String message,
                               T actual,
                               Matcher<? super T> matcher) {
        
        sayAndAssertThat(message, "", actual, matcher);
  
    }


    // //////////////////////////////////////////////////////////////////////////
    // Inlined methods of the TestBrowser (for convenience)
    // //////////////////////////////////////////////////////////////////////////
    /**
     * @return all cookies saved by this TestBrowser.
     */
    @Override
    public final List<Cookie> getCookies() {
        return testBrowser.getCookies();
    }
    
    @Override
    public final List<Cookie> sayAndGetCookies() {
        return testBrowser.getCookies();
    }

    @Override
    public final Cookie getCookieWithName(String name) {
        return testBrowser.getCookieWithName(name);
    }
    
    @Override
    public final Cookie sayAndGetCookieWithName(String name) {
        return testBrowser.getCookieWithName(name);
    }

    @Override
    public final void clearCookies() {
        testBrowser.clearCookies();
    }

    @Override
    public final Response makeRequest(Request httpRequest) {
        return testBrowser.makeRequest(httpRequest);
    }
    
    @Override
    public final Response sayAndMakeRequest(Request httpRequest) {
        return renderMachine.sayAndMakeRequest(httpRequest);
    }
    
    /**
     * Convenience method that allows you to write tests in a fluent way. 
     * <p>
     * For instance:
     * <code>
     * sayAndMakeRequest(
     *           Request
     *               .GET()
     *               .url(testServerUrl().path("search").addQueryParameter("q", "toys")));
     * </code>
     */
    public final Url testServerUrl() {
        
       return Url.host(getTestServerUrl());
        
    }
    
    // //////////////////////////////////////////////////////////////////////////
    // Configuration of DoctestJ
    // //////////////////////////////////////////////////////////////////////////
    /**
     * You may override this method if you want to supply your own testbrowser
     * for your class or classes.
     * 
     * @return
     */
    public TestBrowser getTestBrowser() {

        return new TestBrowserImpl();

    }
    
    /**
     * @return a valid host name of your test server (eg http://localhost:8127). 
     *          This will be used in the testServerUrl() method.
     */
    public abstract String getTestServerUrl();

    /**
     * Override me if you want to give me a name. You should...
     * @return The name of the file. MyTest.class.getSimpleName() often makes sense.
     */
    public String getName() {
        System.out.println("Please override getName() method of DocTester to get a better fileName for doctest");       
        return UUID.randomUUID().toString();
    }

}