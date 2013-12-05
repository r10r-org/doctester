Advanced integration
====================

If you are integrating DocTester into your project it often makes sense to wrap
DocTester.java inside another class.

Especially if you are using the integrated testbrowser to test an Api. In that
case this allows you to overwrite **testServerUrl()** to get the
actual url of the server under test.

## Basic library dependencies

Because DocTester is a library we do not specify 
any hard dependencies to libraries by default.

In general you'll need to configure and satisfy least the following dependencies:

 * DocTester itself uses SLF4J.
 * Some Apache components use Log4J.
 * JUnit
 * Doctester itself.

It dependes what you want to achieve. A really basic setup would just tell use
a Console based logger for SLF4J and add a Log4J implementation that will use
that logger.

The following example will get you started. But in general DocTester is not
intrusive. Simply configure it the way it fits your project:

<pre class="prettyprint languague-xml">

	&lt;dependencies&gt;
		&lt;dependency&gt;
			&lt;groupId&gt;junit&lt;/groupId&gt;
			&lt;artifactId&gt;junit&lt;/artifactId&gt;
			&lt;version&gt;4.11&lt;/version&gt;
			&lt;scope&gt;test&lt;/scope&gt;
		&lt;/dependency&gt;

		&lt;dependency&gt;
			&lt;groupId&gt;org.doctester&lt;/groupId&gt;
			&lt;artifactId&gt;doctester-core&lt;/artifactId&gt;
			&lt;version&gt;1.0.3&lt;/version&gt;
			&lt;scope&gt;test&lt;/scope&gt;
		&lt;/dependency&gt;

		&lt;dependency&gt;
			&lt;groupId&gt;org.slf4j&lt;/groupId&gt;
			&lt;artifactId&gt;slf4j-simple&lt;/artifactId&gt;
			&lt;version&gt;1.7.5&lt;/version&gt;
			&lt;scope&gt;test&lt;/scope&gt;
		&lt;/dependency&gt;

		&lt;dependency&gt;
			&lt;groupId&gt;org.slf4j&lt;/groupId&gt;
			&lt;artifactId&gt;jcl-over-slf4j&lt;/artifactId&gt;
			&lt;version&gt;1.7.5&lt;/version&gt;
			&lt;scope&gt;test&lt;/scope&gt;
		&lt;/dependency&gt;

	&lt;/dependencies&gt;

</pre>



## Arquillian / JBoss integration

The key is to run the Arquillian test as client side test via **@RunAsClient**. 
You can then map the server url into your test via **@ArquillianResource**


<pre class="prettyprint languague-java">
@RunWith(Arquillian.class)
@RunAsClient
public abstract class ArquillianDocTester extends DocTester {
	
	@ArquillianResource
	private URL baseUrl;

	@Override
	public org.doctester.testbrowser.Url testServerUrl() {
		
		try {
				return Url.host(baseUrl.toURI().toString());
		} catch (URISyntaxException ex) {
				throw new IllegalStateException(
					"Something is wrong with the Uri Arquillian provides us for this JBoss.", ex);
		}
		
	}
	
} 
</pre>



