package org.doctester.rendermachine;

import com.google.common.collect.ImmutableMap;
import com.google.common.html.HtmlEscapers;
import org.doctester.testbrowser.Request;
import org.doctester.testbrowser.Response;
import org.doctester.testbrowser.TestBrowser;
import org.doctester.testbrowser.Url;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Map;
import org.doctester.testbrowser.testmodels.Article;
import org.doctester.testbrowser.testmodels.User;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RenderMachineImplTest {

    @Mock
    TestBrowser testBrowser;

    RenderMachineImpl renderMachine;

    @Before
    public void setupTest() {
        renderMachine = new RenderMachineImpl();
        renderMachine.setTestBrowser(testBrowser);
    }

    @Test
    public void testThatFormParametersArePrintedWhenPresent() {
        Map formParameters = ImmutableMap.of("param1", "value1", "param2", "value2");
        Request requestWithoutParameters = Request.POST().url(Url.host("host"));
        Request requestWithParameters = Request.POST().url(Url.host("host")).formParameters(formParameters);

        when(testBrowser.makeRequest(requestWithoutParameters)).thenReturn(new Response(ImmutableMap.of("header", "header"), 200, "payload"));
        when(testBrowser.makeRequest(requestWithParameters)).thenReturn(new Response(ImmutableMap.of("header", "header"), 200, "payload"));

        renderMachine.sayAndMakeRequest(requestWithoutParameters);
        assertFalse(renderMachine.htmlDocument.contains("<dt>Parameters</dt><dd>" + formParameters.toString() + "</dd>"));
        assertFalse(renderMachine.htmlDocument.contains("<dt>Parameters</dt><dd></dd>"));

        renderMachine.sayAndMakeRequest(requestWithParameters);
        assertTrue(renderMachine.htmlDocument.contains("<dt>Parameters</dt><dd>" + formParameters.toString() + "</dd>"));
    }

    @Test
    public void testThatJsonPayloadIsPrinted() {
        User user = new User("jsonPayloadTester", "test123", "JSON Payload Tester");
        Request requestWithJsonPayload = Request.POST().url(Url.host("host")).contentTypeApplicationJson().payload(user);

        when(testBrowser.makeRequest(requestWithJsonPayload)).thenReturn(new Response(ImmutableMap.of("header", "header"), 200, "payload"));

        renderMachine.sayAndMakeRequest(requestWithJsonPayload);

        String prettyPrintJson = requestWithJsonPayload.payloadAsPrettyString();
        assertTrue(prettyPrintJson.startsWith("{"));
        assertTrue(prettyPrintJson.contains("\"jsonPayloadTester\""));
        assertTrue(renderMachine.htmlDocument.contains(
                "<dt>Content</dt><dd><div class=\"http-body\"><pre>"
                        + HtmlEscapers.htmlEscaper().escape(prettyPrintJson)
                        + "</pre></div></dd>")
        );
    }

    @Test
    public void testThatXmlPayloadIsPrinted() {
        User user = new User("xmlPayloadTester", "test456", "XML Payload Tester");
        Request requestWithXmlPayload = Request.POST().url(Url.host("host")).contentTypeApplicationXml().payload(user);

        when(testBrowser.makeRequest(requestWithXmlPayload)).thenReturn(new Response(ImmutableMap.of("header", "header"), 200, "payload"));

        renderMachine.sayAndMakeRequest(requestWithXmlPayload);

        String prettyPrintXml = requestWithXmlPayload.payloadAsPrettyString();
        System.err.println(prettyPrintXml);
        assertTrue(prettyPrintXml.startsWith("<"));
        assertTrue(prettyPrintXml.contains("<username>xmlPayloadTester</username>"));
        assertTrue(renderMachine.htmlDocument.contains(
                "<dt>Content</dt><dd><div class=\"http-body\"><pre>"
                        + HtmlEscapers.htmlEscaper().escape(prettyPrintXml)
                        + "</pre></div></dd>")
        );
    }
}
