DocTester [![Build Status](https://api.travis-ci.org/r10r-org/doctester.svg)](https://travis-ci.org/r10r-org/doctester)
=========

DocTester is a Java Testing Framework. DocTester tests and generates documentation at the same time.
Built on top of JUnit. Works as simple drop-in for your build process.

[![Build Status](https://buildhive.cloudbees.com/job/doctester/job/doctester/badge/icon)](https://buildhive.cloudbees.com/job/doctester/job/doctester/)

## An example says more than a thousand words.

When your test class extends DocTester you get some useful commands like:

 * sayNextSection(): A new headline for your test.
 * say(): Some fluent text wrapped inside a paragraph.
 * sayAndMakeRequest: Make a request and write the documentation of the whole
   request / response cycle to the html file.
 * sayAndAssertThat(): Creates a green bar with a text.

And in reality this would be used in the following manner:

<pre class="prettyprint languague-java">
@Test
public void testMyApi() {

	String articlesUrl = "/api/articles.json";

	sayNextSection("Retrieving articles for a user (Json)");

	say("Retrieving all articles of a user is a GET request to " + articlesUrl);

	Response response = sayAndMakeRequest(
		Request
			GET().url(testServerUrl().path(("/api/articles.json"))));

	ArticlesDto articlesDto = response.payloadAsJson(ArticlesDto.class);

	sayAndAssertThat("We get back all 3 articles of that user ", 3, equalTo(articlesDto.articles.size()));

}
</pre>

This in turn generates the following output:

![A screenshot of the generated html output](../images/doctester_example_output.png)


By default the resulting html is generated into src/target/site/doctester/


There is an integration test that uses all of doctester. Check out for a more
comprehensive example:

https://github.com/doctester/doctester/blob/master/doctester-integration-test/src/test/java/controllers/ApiControllerDocTest.java


## In-depth description of the problem we solve

You are developing a state of the art restful webservice. 
That webservce offers several endpoints that deliver Json entities via
different payloads, query parameters and response codes.

You of course not only have the working implementation, but also some integration
tests running on a server and being tested by a unit test.

And then you also have some documentation - right? About the entities, the endpoints,
query parameters, the variable parts, Json payload definitions, response codes - 
in short: All the information a 3rd party developer needs to use your webservice.

But then you improve and change your webservice. You do of course adjust the tests,
but somehow your documentation gets worse and worse. Outdated and even wrong.

Not a nice prospect.


## Our solution

DocTester allows you to write tests and documentation at the same time! 
By doing so you make sure that
your documentation is always in sync with the current webservice. No more defunct
documentation.

In fact DocTester is just an add-on for JUnit that
gives you some additional methods to create a html file and cleanly document
all important parameters of a webservice call into that file.

Documentation is created when your run your tests. That also means that
your documentation is always in sync with your webservice. Horray!


## Some specs

DocTester is heavily inspired by python doctests and also the Devbliss
doctest library. However, DocTester is quite different to the alternatives.

 * DocTests are just plain old Java JUnit tests.
 * Hamcrest for unit testing. Keeps DocTester Api slick and simple.
 * Webservice client with fluent interface for testing Json and Xml interfaces.
 * Simple - only a few classes.
 * Uses Bootstrap for styling
 * Works with Arquillian / JBoss 6.1EAP, JBehave, Ninja, simple servlet 
   setups and probably any test setup your throw it at.


## Contact

We welcome feedback. Don't hesitate to contact us! If you got ideas or find bugs
please file an issue via our issue tracker!

Your Team DocTester :)

<a href="http://www.raphaelbauer.com">Raphael A. Bauer</a>, Stefan Weller (stefan.weller@zanox.com)





Basic usage
===========

## Introduction

Including DocTester into your build is dead simple. You just extend your TestClass
with <code>extends DocTester</code> and you are ready to go.

This creates documentation when you run the testcase, and also gives you
access to a range of useful commands. 

You can see best how DocTester works by looking at the following example:

<pre class="prettyprint languague-java">
public class ApiControllerDocTest extends DocTester {
    
    String GET_ARTICLES_URL = "/api/{username}/articles.json";
    String POST_ARTICLE_URL = "/api/{username}/article.json";

    @Test
    public void testGetAndPostArticleViaJson() {
        
        sayNextSection("Retrieving articles for a user (Json)");
        
        say("Retrieving all articles of a user is a GET request to " + GET_ARTICLES_URL);
        
        Response response = sayAndMakeRequest(
               Request
                    .GET().url(testServerUrl().path((GET_ARTICLES_URL.replace("{username}", USER)))));
        
        ArticlesDto articlesDto = response.payloadAs(ArticlesDto.class);

        sayAndAssertThat("We get back all 3 articles of that user ", 3, equalTo(articlesDto.articles.size()));

		}
}

</pre>

This in turn generates the following documentation:

![A screenshot of the generated html output](../images/doctester_example_output.png)


## What DocTester generates

Once your ran your testcase that extends DocTester you can find two files inside
directory **target/site/doctester**. One file is named after your test.

For instance the doctest of class com.mycompany.ApiControllerDocTest will be written to a file
**target/site/doctester/com.mycompany.ApiControllerDocTest.html**.

The second file named **index.html** contains a listing of all DocTests
you executed. This comes in handy if you want to publish your DocTests on a website.
You can simply point the users to the index.html and they have access to all
DocTester documentation of your project.


## The Api of DocTester and what it means

The basic idiom of DocTester is **say**. If you write DocTests always have in mind
that you are not writing a traditional test, but documentation for the user.
Therefore your tests should depict a typical usecase. Getting data, querying and
interface, posting data and so on.

Always use natural language inside your say commands.

And a bonus tip: Please actually read the documentation DocTester generates.
It should make sense and should really help to understand your application and your
interface.

### sayNextSection(String string)

Renders a headline with html formatting &lt;h1&gt; to your page.

### say(String string)

Renders a paragraph with html formatting &lt;p&gt; to your page.


### say(String string)

Renders a paragraph with html formatting &lt;p&gt; to your page.

### sayRaw(String string)

Renders the provided string directly into the document. Nice if you want
to render supercustom html.


### sayAndAssertThat(...)

This is a typical Hamcrest assert. Simply add a message and use any
matchers like

<pre class="prettyprint languague-java">
sayAndAssertThat("We get back all 3 articles of that user ", 3, equalTo(articlesDto.articles.size()));</code>.
</pre>

The big difference between a DocTester assert and a Hamcrest assert is that
DocTester renders a green box with the message.

You can of course skip using DocTesters sayAndAssert and simply use 
the regular assertThat. 

But sometimes it is just nice to assert something important visually.


### sayAndMakeRequest(Request request)

This method executes a request via a headless browser. You can configure
the Request yourself by providing url, path, query parameters and payload.

The cool thing is that DocTester automatically generates a nice box that
will document the request (headers, cookies etc) and the response 
(headers, payload...).

This means users of your Api can easily see what is going on and what they
can expect from your Api.

sayAndMakeRequest has a little cousin called makeRequest which works exactly
the same way, but does not document anything. 
Sometimes handy when you want to do stuff you don't want to document all the
time like logging into your Api for instance.

Some examples.

Performing a **GET** request:

<pre class="prettyprint languague-java">

//Let's assume we want to list all user via our Api.
Response response = sayAndMakeRequest(
               Request
                    .GET().url(testServerUrl().path("/api/users")));

Users users = response.payloadAs(Users.class);
</pre>

Performing a **GET** request with query parameters:

<pre class="prettyprint languague-java">

//Let's assume we want to list all user via our Api
// and add a query parameter:
Response response = sayAndMakeRequest(
               Request
                    .GET().url(testServerUrl().path("/api/users")
														.addQueryParameter("search", "Bob Mossley"));

Users users = response.payloadAs(Users.class);
</pre>

Performing a **POST** request:

<pre class="prettyprint languague-java">

// Let's say we want to create a new user:
User user = new User("username");

Response response = sayAndMakeRequest(
               Request
                    .POST().url(testServerUrl().path("/api/user"))
                    .contentTypeApplicationJson()
                    .payload(username));

</pre>

The Api should be fluent and your IDE will help you to discover
how you can modify requests and inspect responses.


### More

There are more methods, and you can easily find out more about them via
code completion and JavaDocs in your favorite IDE.

## Some more information

You usually should extend DocTester and overwrite at least **testServerUrl()**.

<pre class="prettyprint languague-java">
public Url testServerUrl() {

	return Url.host("http://localhost:xxxx");
}
</pre>

That way you can use nice chaining when assembling your calls via makeRequest(...)
and sayAndMakeRequest(...).

Please find more information in chapter advanced integration.


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



## Customizing the html generated by DocTester via css

You can easily brand the css generated by DocTester. All css of 
DocTester is built with Bootstrap. You can add your branding by adding
a file **/src/test/resources/org/doctester/custom_doctester_stylesheet.css**
to your project.

For instance the following css adds a logo and changes the color of the headline
in the header:

<pre class="prettyprint languague-css">
body {
	background-repeat: no-repeat;
	background-image: url(data:image/png;base64,iVC);
	background-attachment: fixed;
	background-position: 10px 55px;
}

a.navbar-brand {
	color: #F39300 !important; 
}
</pre>



How to contribute
=================

Great you want to contribute!!
------------------------------

Contributing to DocTester is really simple. Well. There are some rules that you should follow:

- Make sure your feature is well tested.
- Make sure your feature is well documented (Javadoc).
- Make sure there is a tutorial inside the website (doctester-core/src/site/markdown).
- Make sure you are following the code style below.
- Add your changes to changelog.md and your name to team.md.

Then send us a pull request and you become a happy member of the DocTester family :)


Code style
----------

- Default Sun Java / Eclipse code style
- If you change only tiny things only reformat stuff you actually changed. Otherwise reviewing is really hard.
- We use 4 spaces as one tab.
- All files are UTF-8.


Making a release
-----------------

Making a Doctester release
 
1) Preliminary

- Make sure changelog.md is updated

2) Release to central maven repo

- Make sure you are using http://semver.org/ for versioning.

- mvn release:clean
- mvn release:prepare
- mvn release:perform
- Log into http://oss.sonatype.org and release the packages

3) publish website

- git checkout TAG
- cd doctester-core
- mvn site site:deploy

And back to develop:

- git checkout develop
