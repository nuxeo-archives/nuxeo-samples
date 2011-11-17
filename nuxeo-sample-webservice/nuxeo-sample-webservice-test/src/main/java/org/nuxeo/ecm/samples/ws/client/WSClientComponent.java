/*
 * (C) Copyright 2011 Nuxeo SA (http://nuxeo.com/) and contributors.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser General Public License
 * (LGPL) version 2.1 which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl.html
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * Contributors:
 *     ataillefer
 */
package org.nuxeo.ecm.samples.ws.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.namespace.QName;

import net.restfulwebservices.servicecontracts._2008._01.IWeatherForecastService;
import net.restfulwebservices.servicecontracts._2008._01.WeatherForecastService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.nuxeo.ecm.platform.ws.NuxeoRemotingInterface;
import org.nuxeo.ecm.platform.ws.NuxeoRemotingService;
import org.nuxeo.ecm.samples.ws.server.NuxeoSampleWebService;
import org.nuxeo.ecm.samples.ws.server.NuxeoSampleWebServiceInterface;
import org.nuxeo.runtime.model.ComponentInstance;
import org.nuxeo.runtime.model.DefaultComponent;

/**
 * Nuxeo WebService client service implementation.
 * 
 * @author ataillefer
 */
public class WSClientComponent extends DefaultComponent implements
        WSClientService {

    private static final long serialVersionUID = 3799437621365295389L;

    private static final Log log = LogFactory.getLog(WSClientComponent.class);

    private static final String WS_CLIENT_POINT = "WSClient";

    /** WSClient contributions. */
    private Map<String, WSClientDescriptor> contributions = new HashMap<String, WSClientDescriptor>();

    @Override
    public void registerContribution(Object contribution,
            String extensionPoint, ComponentInstance contributor)
            throws Exception {
        if (WS_CLIENT_POINT.equals(extensionPoint)) {
            if (contribution instanceof WSClientDescriptor) {
                registerWSClient((WSClientDescriptor) contribution);
            }
        }
        super.registerContribution(contribution, extensionPoint, contributor);
    }

    /**
     * Registers a ws client contrib.
     * 
     * @param contribution the contribution
     */
    private void registerWSClient(WSClientDescriptor contribution) {
        contributions.put(contribution.getId(), contribution);
    }

    @Override
    public void unregisterContribution(Object contribution,
            String extensionPoint, ComponentInstance contributor)
            throws Exception {
        super.unregisterContribution(contribution, extensionPoint, contributor);
    }

    /**
     * {@inheritDoc}
     */
    public Map<String, WSClientDescriptor> getContributions() {
        return contributions;
    }

    /**
     * {@inheritDoc}
     */
    public <T> T getAdapter(Class<T> adapter) {

        // Here we must have one case by webservice

        // nuxeoremoting
        if (adapter.isAssignableFrom(NuxeoRemotingInterface.class)) {
            NuxeoRemotingService nrs = new NuxeoRemotingService(
                    getWsdlUrl("NuxeoRemotingService"),
                    getQName("NuxeoRemotingService"));
            NuxeoRemotingInterface nri = nrs.getNuxeoRemotingInterfacePort();
            return adapter.cast(nri);
        } else if (adapter.isAssignableFrom(IWeatherForecastService.class)) {// weather
                                                                             // forecast
            WeatherForecastService wfs = new WeatherForecastService(
                    getWsdlUrl("WeatherForecastService"),
                    getQName("WeatherForecastService"));
            IWeatherForecastService iwfs = wfs.getBasicHttpBindingIWeatherForecastService();
            return adapter.cast(iwfs);
        } else if (adapter.isAssignableFrom(NuxeoSampleWebServiceInterface.class)) {// nuxeosample
            NuxeoSampleWebService nsws = new NuxeoSampleWebService(
                    getWsdlUrl("NuxeoSampleWebService"),
                    getQName("NuxeoSampleWebService"));
            NuxeoSampleWebServiceInterface nswsi = nsws.getNuxeoSampleWebServiceInterfacePort();
            return adapter.cast(nswsi);
        }
        return super.getAdapter(adapter);
    }

    /**
     * Gets the wsdl url given a WSClient contribution id.
     * 
     * @param wsClientId the ws client id
     * @return the wsdl url
     */
    protected URL getWsdlUrl(String wsClientId) {

        URL url = null;

        WSClientDescriptor wsClientDesc = contributions.get(wsClientId);
        if (wsClientDesc != null) {
            String wsdlUrl = wsClientDesc.getWsdlUrl();
            try {
                url = new URL(wsdlUrl);
            } catch (MalformedURLException e) {
                log.warn("Failed to create URL for the wsdl Location: '"
                        + wsdlUrl + "', retrying as a local file");
                log.warn(e.getMessage());
            }
        }

        return url;
    }

    /**
     * Gets the QName given a WSClient contribution id.
     * 
     * @param wsClientId the ws client id
     * @return the q name
     */
    protected QName getQName(String wsClientId) {

        WSClientDescriptor wsClientDesc = contributions.get(wsClientId);
        if (wsClientDesc != null) {
            return new QName(wsClientDesc.getNamespaceURI(),
                    wsClientDesc.getLocalPart());
        }
        return null;

    }

}
