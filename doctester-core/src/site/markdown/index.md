DocTester
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


