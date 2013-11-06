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
package org.doctester.testbrowser;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.apache.http.client.utils.URIBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

/**
 *
 * Well. Usually we'd just use URI from the Jdk. But to be honest - we think
 * that URI is hard and not easy to use.
 *
 * Uri allows for some nice chaining and handles query parameters as well as
 * hosts and paths of your Url in a more natural way than URI.
 *
 * @author Raphael A. Bauer
 *
 */
public class Url {

	private static Logger logger = LoggerFactory.getLogger(Url.class);

	private StringBuilder simpleUrlBuilder;

	private Map<String, String> queryParameters;

	private Url() {
		simpleUrlBuilder = new StringBuilder();
		queryParameters = Maps.newHashMap();

	}

	/**
	 * Create a Url instance from a host. Host should look like
	 * http://myserver:8080 or http://myserver:8080/application.
	 *
	 * @param host The host e.g. http://myserver:8080 may contain trailing slash
	 * or parts of a path.
	 * @return The Url you can customize even further with path, query parameters
	 * and so on.
	 */
	public static Url host(String host) {

		Url url = new Url();

		String hostWithoutTrailingSlash;

		if (host.endsWith("/")) {
			hostWithoutTrailingSlash = host.substring(0, host.length() - 1);
		} else {
			hostWithoutTrailingSlash = host;
		}

		url.simpleUrlBuilder.append(hostWithoutTrailingSlash);

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

		simpleUrlBuilder.append(pathWithLeadingSlash);

		return this;

	}

	/**
	 * Allows you to add query parameters to this url (In your browser something
	 * like "?user=bob"
	 *
	 * @param key The key for this query parameter.
	 * @param value The value for this query parameter.
	 * @return This Url for chaining.
	 */
	public Url addQueryParameter(String key, String value) {

		queryParameters.put(key, value);

		return this;

	}

	/**
	 * Creates a URI from this Uri.
	 *
	 * @return The URI you can pass to any lib using Uri.
	 */
	public URI uri() {

		URI uri = null;

		try {

			URIBuilder uriBuilder = new URIBuilder(simpleUrlBuilder.toString());

			for (Map.Entry<String, String> queryParameter : queryParameters.entrySet()) {

				uriBuilder.addParameter(queryParameter.getKey(), queryParameter.getValue());

			}

			uri = uriBuilder.build();

		} catch (URISyntaxException e) {

			String message = "Something strange happend when creating a URI from your Url (host, query parameters, path and so on)";
			logger.error(message);

			throw new IllegalStateException(message, e);
		}

		return uri;

	}

	/**
	 * The real life Uri in human readable form.
	 */
	public String toString() {

		return uri().toString();

	}

}
