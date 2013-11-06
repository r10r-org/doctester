/*
 * Copyright 2013 ra.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.doctester.testbrowser;

import static org.hamcrest.CoreMatchers.equalTo;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ra
 */
public class UrlTest {

	/**
	 * Test of host method, of class Url.
	 */
	@Test
	public void testHostWithoutPath() {
		String host = "http://www.google.com:8080";

		Url url = Url.host(host);

		assertThat(url.toString(), equalTo("http://www.google.com:8080"));

	}

	/**
	 * Test of host method, of class Url.
	 */
	@Test
	public void testHostWithoutPathAndTrailingSlash() {
		String host = "http://www.google.com:8080/";

		Url url = Url.host(host);

		assertThat(url.toString(), equalTo("http://www.google.com:8080"));

	}

	/**
	 * Test of host method, of class Url.
	 */
	@Test
	public void testHostWithPath() {
		String host = "http://www.google.com:8080/";
		Url url = Url.host(host);
		url.path("testing");
		assertThat(url.toString(), equalTo("http://www.google.com:8080/testing"));
		url.path("/");
		assertThat(url.toString(), equalTo("http://www.google.com:8080/testing/"));
	}

	/**
	 * Test of addQueryParameter method, of class Url.
	 */
	@Test
	public void testAddQueryParameter() {
		String host = "http://www.google.com:8080/";
		Url url = Url.host(host);
		url.path("testing");
		assertThat(url.toString(), equalTo("http://www.google.com:8080/testing"));
		url.addQueryParameter("search", "term");
		assertThat(url.toString(), equalTo("http://www.google.com:8080/testing?search=term"));
		url.addQueryParameter("search2", "term2");
		assertThat(url.toString(), equalTo("http://www.google.com:8080/testing?search=term&search2=term2"));
	}

}
