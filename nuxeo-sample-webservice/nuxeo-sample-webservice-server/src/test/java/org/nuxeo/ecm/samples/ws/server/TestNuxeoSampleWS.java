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
package org.nuxeo.ecm.samples.ws.server;

import javax.security.auth.login.LoginException;

import org.junit.Test;
import org.nuxeo.common.Environment;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.IdRef;
import org.nuxeo.ecm.core.storage.sql.SQLRepositoryTestCase;
import org.nuxeo.ecm.platform.api.ws.DocumentDescriptor;
import org.nuxeo.runtime.jtajca.NuxeoContainer;

/**
 * Tests Nuxeo sample WebService.
 * 
 * @author ataillefer
 */
public class TestNuxeoSampleWS extends SQLRepositoryTestCase {

    /**
     * {@inheritDoc}
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();
        // To be able to use tx manager
        setUpContainer();
        Environment.getDefault().setHostApplicationName(
                Environment.NXSERVER_HOST);
        // Open a session
        openSession();
    }

    /**
     * Sets the up the Nuxeo container to be able to use tx manager.
     * 
     * @throws Exception the exception
     */
    private void setUpContainer() throws Exception {
        NuxeoContainer.install();
    }

    /**
     * Test Nuxeo sample WebService.
     * 
     * @throws LoginException the login exception
     * @throws ClientException the client exception
     */
    @Test
    public void testNuxeoSampleWS() throws LoginException, ClientException {

        NuxeoSampleWS nsws = new NuxeoSampleWSImpl();
        DocumentDescriptor docDesc = nsws.createDocument("Administrator",
                "Administrator", "/", "myTestFile", "File");

        // Check doc descriptor returned by the ws
        assertNotNull(docDesc);
        assertEquals("myTestFile", docDesc.getTitle());
        assertEquals("File", docDesc.getType());

        // Check actual doc creation
        DocumentModel createdDoc = session.getDocument(new IdRef(
                docDesc.getId()));

        assertNotNull(createdDoc);
        assertEquals("myTestFile", createdDoc.getTitle());
        assertEquals("File", createdDoc.getType());

    }
}
