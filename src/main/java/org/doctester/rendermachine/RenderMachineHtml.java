package org.doctester.rendermachine;

public interface RenderMachineHtml {
    
    String BOOTSTRAP_BASE = "META-INF/resources/webjars/bootstrap/3.0.0/";
    String JQUERY_BASE = "META-INF/resources/webjars/jquery/1.9.0/";
    
     
    String HTML_BEGIN = 
            "<!DOCTYPE html>\n" + 
            "<html lang=\"en\">\n";
    
    String HTML_END = "</html>";
    
    String CUSTOM_CSS =
        "<style>" +
        "body {\n" + 
    		"  padding-top: 100px;\n" + 
    		"  padding-bottom: 20px;\n" + 
    		"}\n" +
    		"div.http-response-body {\n" +
    		"  max-height:200px;\n" + 
    		"  overflow:auto;\n" +
    		"}\n" +
    		"</style>";

    
    String HTML_HEAD = 
    		"  <head>\n" + 
    		"    <meta charset=\"utf-8\">\n" + 
    		"    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" + 
    		"    <meta name=\"description\" content=\"\">\n" + 
    		"    <meta name=\"author\" content=\"\">\n" + 
    		"    <link rel=\"shortcut icon\" href=\"../../assets/ico/favicon.png\">\n" + 
    		"\n" + 
    		"    <title>%s</title>\n" + 
    		"\n" + 
    		CUSTOM_CSS +
    		"    <!-- Bootstrap core CSS -->\n" + 
    		"    <link href=\"" + BOOTSTRAP_BASE + "css/bootstrap.css\" rel=\"stylesheet\">\n" + 
    		"\n" + 
    		"    <!-- Custom styles for this template -->\n" + 
    		"    <link href=\"starter-template.css\" rel=\"stylesheet\">\n" + 
    		"\n" + 
    		"  </head>";
    
    String BOOTSTRAP_HEADER =
        "<div class=\"navbar navbar-inverse navbar-fixed-top\">\n" + 
    		"  <div class=\"container\">\n" + 
    		"    <div class=\"navbar-header\">\n" + 
    		"      <a class=\"navbar-brand\" href=\"#\">&#10084; %s</a>\n" + 
    		"    </div>\n" + 
    		"    <div class=\"navbar-collapse collapse\"></div>" +	
    		"  </div>\n" +     		
    		"</div>";
    
    
    String BOOTSTRAP_CONTAINER_BEGIN = "<div class=\"container\"><div class=\"row\">";
    
    String BOOTSTRAP_LEFT_NAVBAR_BEGIN = "<div class=\"col-md-3\">\n" + 
    		"          <div class=\"panel panel-default\">\n" +
        "<div class=\"panel-body\">" +
    		"            <ul class=\"nav nav-pills nav-stacked\">\n";
    
    String BOOTSTRAP_LEFT_NAVBAR_ELEMENT = 
            "                <li>\n" + 
            "  <a href=\"#%s\">%s</a>\n" + 
            "</li>\n";
    
    String BOOTSTRAP_LEFT_NAVBAR_END = 
            "            </ul>\n" + 
            "          </div>\n" + 
            "          </div>\n" + 
            "        </div>";
    
    String BOOTSTRAP_RIGHT_CONTENT_BEGIN = "<div class=\"col-md-9\" role=\"main\">";
    
    
    String BOOTSTRAP_RIGHT_CONTENT_END = "</div>";
    
    
    String BOOTSTRAP_CONTAINER_END = "      </div></div></div><footer>\n" + 
    		"        Made with &#10084; and DoctestJ\n" + 
    		"      </footer>\n" + 
    		"    </div> <!-- /container -->";

    
    String BODY_BEGIN = "<body>";
    
    String BODY_END = "    <!-- Bootstrap core JavaScript\n" + 
    		"    ================================================== -->\n" + 
    		"    <!-- Placed at the end of the document so the pages load faster -->\n" + 
    		"    <script src=\"" + JQUERY_BASE + "jquery.min.js\"></script>\n" + 
    		"    <script src=\"" + BOOTSTRAP_BASE + "js/bootstrap.min.js\"></script>\n" + 
    		"  </body>";

}