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