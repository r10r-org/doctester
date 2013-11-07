Advanced integration
====================

If you are integrating DocTester into your project it often makes sense to wrap
DocTester.java inside another class.

Especially if you are using the integrated testbrowser to test an Api. In that
case this allows you to overwrite <code>testServerUrl()</code> to get the
actual url of the server under test.


## Arquillian / JBoss integration

The key is to run the Arquillian test as client side test via <code>@RunAsClient</code>. 
You can then map the server url into your test via <code>@ArquillianResource</code>


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

