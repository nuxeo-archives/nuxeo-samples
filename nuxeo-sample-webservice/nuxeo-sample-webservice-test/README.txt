------------------------------------------------------------------
How to test a (previously generated) SOAP based WebService client.
------------------------------------------------------------------

Lets say you have generated a WebService client in the nuxeo-sample-webservice-client project.

* Add a contribution to the extension point <extension target="org.nuxeo.ecm.samples.ws.test.webServiceClient" point="WSClient">
  eg: contrib-ws-client.xml
  
  - set an id for your webservice client contribution
  - set the wsdlUrl: this is the URL used to download the wsdl file
  - set the namespaceURI: this is the targetNamespace attribute from the <wsdl:definitions> element of the wsdl file
  - set the localPart: this is the name attribute from the <wsdl:definitions> element of the wsdl file

* Add the code required for the webservice client in WSClientComponent#getAdapter:
  eg:
  
  if (adapter.isAssignableFrom(IWeatherForecastService.class)) {// weather forecast
	WeatherForecastService wfs = new WeatherForecastService(
                    getWsdlUrl("WeatherForecastService"),
                    getQName("WeatherForecastService"));
	IWeatherForecastService iwfs = wfs.getBasicHttpBindingIWeatherForecastService();
    return adapter.cast(iwfs);
  }
  
  - You need to find the webservice client interface in the generated classes: IWeatherForecastService.
    It is the class with the following annotation: @WebService(name = "IWeatherForecastService",...
  
  - You need to find the actual webservice client in the generated classes: WeatherForecastService.
    It is the class with the following annotation: @WebServiceClient(name = "WeatherForecastService",...
  
  - Then just instantiate the webservice client, using the id of the XML contribution as parameter for the
    getWsdlUrl() and getQName() methods.
   
  - On the webservice client, call the method that returns an object of type the webservice client interface.
  
  - Return the casted webservice client interface.

* To make a client call to the webservice, just use WSClientComponent#getAdapter
  with the webservice client interface class as a parameter.
  eg: IWeatherForecastService iwfs = wsClientService.getAdapter(IWeatherForecastService.class);
  
  You are now free to call any method available in the webservice client interface.
  eg: iwfs.getCitiesByCountry("FRANCE");
  
  See WSClientActionBean#doGet for an example.

-------------------------------------------------------
How to test a (previously built) Nuxeo webservice.
-------------------------------------------------------

Lets say you have built a Nuxeo WebService in the nuxeo-sample-webservice-server project.

Just generate a client for this WebServide using previous section and use it to make a call to the Nuxe WebService.
See WSServerActionBean#doGet for an example.
