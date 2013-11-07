Basic usage
===========

Including DocTester into your build is dead simple. You just extend your TestClass
with <code>extends DocTester</code> and you are ready to go.

This extension automatically creates the documentation file, and also gives you
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