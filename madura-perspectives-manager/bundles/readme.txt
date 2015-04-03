Bundles should be placed in this directory to be monitored by the main program and invoked when requested.
The location of this directory is defined in ${basedir}/src/main/webapp/WEB-INF/applicationContext.xml bean bundlesDir
which refers to a JNDI name like this:

<jee:jndi-lookup id="bundlesDir" jndi-name="java:/comp/env/BundlesDir" expected-type="java.lang.String" />

That needs to be defined in your server. In Tomcat this is done by adding the following to context.xml:

	<Environment 
		name="BundlesDir" value="MY_DIRECTORY"
		type="java.lang.String" 
		override="true"/>

(adjust MY_DIRECTORY to point to your location, but re recommend making it this directory)

Your app server might need something different, but if you want to keep it simple just edit the applicationContext.xml
file and add the literal directory name instead of the JNDI reference. Not a good approach for production but
it will get the demo working.