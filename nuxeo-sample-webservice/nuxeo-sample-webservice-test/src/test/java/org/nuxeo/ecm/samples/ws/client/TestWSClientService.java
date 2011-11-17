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

import java.util.List;
import java.util.Map;

import net.restfulwebservices.servicecontracts._2008._01.IWeatherForecastService;

import org.nuxeo.ecm.core.storage.sql.SQLRepositoryTestCase;
import org.nuxeo.runtime.api.Framework;

import com.microsoft.schemas._2003._10.serialization.arrays.ArrayOfstring;

/**
 * Tests WSClientService.
 * 
 * @author ataillefer
 */
public class TestWSClientService extends SQLRepositoryTestCase {

    private static final String NUXEO_SAMPLE_WEBSERVICE_TEST_BUNDLE = "nuxeo-sample-webservice-test";

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();
        deployBundle(NUXEO_SAMPLE_WEBSERVICE_TEST_BUNDLE);
    }

    /**
     * Tests ws client service contrib.
     * 
     * @throws Exception the exception
     */
    public void testWSClientServiceContrib() throws Exception {

        // Get the WSClientService
        WSClientService wsClientService = Framework.getService(WSClientService.class);
        assertNotNull(wsClientService);

        // Check contribs
        Map<String, WSClientDescriptor> contribs = wsClientService.getContributions();
        assertNotNull(contribs);
        assertEquals(3, contribs.size());

        // Check a specific contrib
        WSClientDescriptor wsClientDesc = contribs.get("WeatherForecastService");
        assertNotNull(wsClientDesc);

        assertEquals("WeatherForecastService", wsClientDesc.getId());
        assertEquals(
                "http://www.restfulwebservices.net/wcf/WeatherForecastService.svc?wsdl",
                wsClientDesc.getWsdlUrl());
        assertEquals(
                "http://www.restfulwebservices.net/ServiceContracts/2008/01",
                wsClientDesc.getNamespaceURI());
        assertEquals("WeatherForecastService", wsClientDesc.getLocalPart());

    }

    /**
     * Test a ws client call.
     * 
     * @throws Exception the exception
     */
    public void testWSClientCall() throws Exception {

        // Get the WSClientService
        WSClientService wsClientService = Framework.getService(WSClientService.class);

        // Make a call to the weather forecast WebService
        IWeatherForecastService iwfs = wsClientService.getAdapter(IWeatherForecastService.class);
        ArrayOfstring citiesAS = iwfs.getCitiesByCountry("FRANCE");
        List<String> cities = citiesAS.getString();

        // Check results
        assertNotNull(cities);
        assertTrue(cities.contains("PARIS"));
        assertTrue(cities.contains("LIMOGES"));

    }
}
