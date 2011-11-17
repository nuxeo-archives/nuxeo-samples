--------------------------------------------------------------
How to generate a SOAP based WebService client in Nuxeo.
--------------------------------------------------------------

Check Nuxeo doc at http://doc.nuxeo.com/display/NXDOC/Building+a+SOAP-based+WebService+client+in+Nuxeo.
But here is a summary anyway:

* First download the wsdl file describing the webservice you want to generate a client for.
  eg: simple weather forecast webservice at http://www.restfulwebservices.net/wcf/WeatherForecastService.svc?wsdl

* Copy the downloaded file into /src/main/resources/wsdls.

* Build the project by running mvn clean install.
  => This will read the wsdl files from the /src/main/resources/wsdls directory and generate
  the Java source files required for the webservice client(s).
  
  This is done by the wsimport goal from the jaxws-maven-plugin configured in pom.xml.
  Doc for wsimport available at http://jax-ws-commons.java.net/jaxws-maven-plugin/wsimport-mojo.html.
  
  Java classes are generated in target/generated-sourecs/wsimport since we configured:
  <sourceDestDir>${project.build.directory}/generated-sources/wsimport</sourceDestDir>
  
* Your done! This artifact can now be used as a third-party lib.
  
  See nuxeo-sample-webservice-test for an example of using a WebService client in Nuxeo.
