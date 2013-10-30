Doctester
=========

[![Build Status](https://buildhive.cloudbees.com/job/doctester/job/doctester/badge/icon)](https://buildhive.cloudbees.com/job/doctester/job/doctester/)

## Problem

You are developing a state of the art restful webservice. Everyone loves you
of course. You have several endpoints that deliver Json entities. 
Different payloads, query parameters and response codes.

You of course not only have the working implementation, but also some integration
tests running on a server and being tested by a unit test.

And then you also have some documentation - right? About the entities, the endpoints,
query parameters, the variable parts, Json payload definitions, response codes.
In short: All the information a 3rd party developer needs to use your webservice.

But then you improve and change your webservice. You do of course adjust the tests,
but somehow your documentation gets worse and worse. Outdated and even wrong.

Not a nice prospect.


## Solution

Doctester allows you to write tests and documentation at the same time! 
By doing so you make sure that
your documentation is always in sync with the current webservice. No more defunct
documentation.

In fact Doctester is just an add-on for JUnit that
gives you some additional methods to create a html file and cleanly document
all important parameters of a webservice call into that file.

Documentation is created when your run your tests. That also means that
your documentation is always in sync with your webservice. Horray!


## Some specs

Doctester is heavily inspired by python doctests and also the Devbliss
doctest library. However, doctester is quite different to the alternatives.


 * Plain old Java JUnit tests
 * Hamcrest for unit testing => Slick and simple Api
 * Webservice client with fluent interface for testing Json and Xml interfaces.
 * Simply - only 7 classes to date.
 * Uses Bootstrap for styling


## Example

Basic commands:

 * sayNextSection(): A new headline for your test.
 * say(): Some fluent text wrapped inside a paragraph.
 * sayAndMakeRequest: Make a request and write the documentation of the whole
   request / response cycle to the html file.
 * sayAndAssertThat(): Creates a green bar with a text.

In reality that looks like:

<pre>

  sayNextSection("Retrieving articles for a user (Json)");

  say("Retrieving all articles of a user is a GET request to " + GET_ARTICLES_URL);

  Response response = sayAndMakeRequest(
    Request
      .GET().url(testServerUrl().path(("/api/articles.json"))));
        
  ArticlesDto articlesDto = response.payloadAsJson(ArticlesDto.class);

  sayAndAssertThat("We get back all 3 articles of that user ", 3, equalTo(articlesDto.articles.size()));

</pre>

And generates the following output:

![A screenshot of the generated html output](https://github.com/doctester/doctester/raw/master/img/doctester_example_output.png)



By default the resulting html is generated into src/target/site/doctester/

There is an integration test that uses all of doctester. Check out:
https://github.com/doctester/doctester/blob/master/doctester-integration-test/src/test/java/controllers/ApiControllerDocTest.java


## Contact

We welcome feedback. Don't hesitate to contact us! If you got ideas or find bugs
please file an issue via our issue tracker!

Your Team Doctester :)

Raphael Bauer( raphael@raphaelbauer.com), Stefan Weller (stefan.weller@zanox.com)


