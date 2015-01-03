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
