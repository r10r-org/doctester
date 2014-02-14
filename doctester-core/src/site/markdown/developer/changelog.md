Version X.X.X
=============
 
 * 2014-02-14 Reordered dependencies in integration test so that we do not need
   exclusions any more (just cosmetics) (ra).
 * 2014-02-14 Updated dependencies (ra).
   [INFO]   com.fasterxml.jackson.core:jackson-core ............... 2.3.0 -> 2.3.1
   [INFO]   com.fasterxml.jackson.dataformat:jackson-dataformat-xml ...
   [INFO]                                                           2.3.0 -> 2.3.1
   [INFO]   commons-fileupload:commons-fileupload ................... 1.3 -> 1.3.1
   [INFO]   org.apache.httpcomponents:httpclient .................. 4.2.5 -> 4.3.2
   [INFO]   org.apache.httpcomponents:httpmime .................... 4.2.5 -> 4.3.2
   [INFO]   org.slf4j:jcl-over-slf4j .............................. 1.7.5 -> 1.7.6
   [INFO]   org.slf4j:slf4j-api ................................... 1.7.5 -> 1.7.6
   [INFO]   org.slf4j:slf4j-simple ................................ 1.7.5 -> 1.7.6
   [INFO] 

Version 1.1.2
=============

 * 2014-02-14 Added support for HTTP HEAD requests (Jan Rudert).
 * 2014-02-05 Fixed issue #1. Doctester now independent of webjars version on classpath. (ra)
 * 2014-02-05 Bump to guava 16.0.1 in doctester-core and Ninja 2.5.1 in integration tests. (ra)
 * 2014-01-19 Bump to guava 16.0 in doctester-core and Ninja 2.5.1 in integration tests. (ra)

Version 1.1.1
=============

 * 2013-12-14 Bump to 2.3.0 of all jackson libraries (xml, json binding) (ra).

Version 1.1
=============

 * 2013-12-04 Better documentation how to setup DocTester in your own projects (ra).
 * 2013-11-04 Added support so that JUnit falures are marked as red
              in the generated html file. Before they were green what can be
              misleading (ra).
 * 2013-11-04 Integration test bump to Ninja 2.3.0 (ra).

Version 1.0.3
=============

 * 2013-11-07 Better documentation (ra).

Version 1.0.2
=============

 * 2013-11-06 Changed codebase to tabs (ra).
 * 2013-11-06 Json is now rendered with intendation in html reports (pretty printed)(ra).

Version 1.0.1
=============

 * 2013-11-05 Fixed bug with forced logback binding. Binding slf4j should be done by projects using DocTester (ra).

Version XXX
===========

 * 2013-11-03 Added supoort for URI to testbrowser. 
   That way we can plug in other url/uri generating libraries (ra).
