/**
 * Copyright (C) 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.doctester;

import org.apache.http.cookie.Cookie;
import org.doctester.rendermachine.RenderMachine;
import org.doctester.rendermachine.RenderMachineCommands;
import org.doctester.rendermachine.RenderMachineImpl;
import org.doctester.testbrowser.*;
import org.hamcrest.Matcher;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public abstract class DocTester implements TestBrowser, RenderMachineCommands {

	@Rule
	public TestRule testWatcher = new TestWatcher() {
		@Override
		protected void starting(Description description) {
			classNameForDocTesterOutputFile = description.getClassName();
		}
	};

	/**
	 * classNameForDocTesterOutputFile will be set by the testWatcher. That way we
	 * can easily generate a filename as output filename. Usually it is something
	 * like "com.mycompany.NameOfClassTest".
	 */
	private String classNameForDocTesterOutputFile;

	private final Logger logger = LoggerFactory.getLogger(DocTester.class);

	// Unique for each test method.
	private TestBrowser testBrowser;

	// Unique for whole testClass => one outputfile per testClass.
        // Protected only for testing
	protected static RenderMachine renderMachine = null;

	@Before
	public void setupForTestCaseMethod() {

		initRenderingMachineIfNull();

		// Set a fresh TestBrowser for each testmethod.
		testBrowser = getTestBrowser();
		renderMachine.setTestBrowser(testBrowser);

        // This is all a bit strange. But JUnit's @BeforeClass
		// is static. Therefore the only possibility to transmit
		// the filename to the renderMachine is here.
		// We accept that we set the fileName too often.
		renderMachine.setFileName(classNameForDocTesterOutputFile);

	}

	public void initRenderingMachineIfNull() {

		if (renderMachine == null) {
			renderMachine = getRenderMachine();
		}

	}

	@AfterClass
	public static void finishDocTest() {

            if (renderMachine != null) {
                renderMachine.finishAndWriteOut();
                renderMachine = null;
            }

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

    // //////////////////////////////////////////////////////////////////////////
	// Configuration of DoctestJ
	// //////////////////////////////////////////////////////////////////////////
	/**
	 * You may override this method if you want to supply your own testbrowser for
	 * your class or classes.
	 *
	 * @return a TestBrowser that will be used for each test method.
	 */
	public TestBrowser getTestBrowser() {

		return new TestBrowserImpl();

	}

	/**
	 * You may override this method if you want to supply your own rendering
	 * machine for your class or classes.
	 *
	 * @return a RenderMachine that generates output and lives for a whole test
	 * class.
	 */
	public RenderMachine getRenderMachine() {

		return new RenderMachineImpl();

	}

	/**
	 * Convenience method that allows you to write tests with the testbrowser in a
	 * fluent way.
	 *
	 * <code>
	 *
	 * sayAndMakeRequest(
	 *           Request
	 *               .GET()
	 *               .url(testServerUrl().path("search").addQueryParameter("q", "toys")));
	 * </code>
	 *
	 *
	 * @return a valid host name of your test server (eg http://localhost:8127).
	 * This will be used in the testServerUrl() method.
	 */
	public Url testServerUrl() {

		final String errorText = "If you want to use the TestBrowser you have to override getTestServerUrl().";
		logger.error(errorText);

		throw new IllegalStateException(errorText);
	}

    /**
     * Alternative way to set the output file name. This can be handy when DocTester is not part of JUnit lifecycle.
     *
     * @param name alternative name of output file
     */
    public void setClassNameForDocTesterOutputFile(final String name) {
        this.classNameForDocTesterOutputFile = name;
    }
}
