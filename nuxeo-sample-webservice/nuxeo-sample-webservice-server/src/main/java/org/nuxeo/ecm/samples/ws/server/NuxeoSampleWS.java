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

import java.io.Serializable;

import javax.security.auth.login.LoginException;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.platform.api.ws.DocumentDescriptor;

/**
 * Nuxeo sample webservice that exposes a method to create a document. The aim
 * is to provide a simple example of a WebService provided by Nuxeo.
 * 
 * @author ataillefer
 */
public interface NuxeoSampleWS extends Serializable {

    /**
     * Creates the document using the given params.
     * 
     * @param username the username
     * @param password the password
     * @param parentPath the parent path
     * @param name the name
     * @param type the type
     * @return the document descriptor
     * @throws LoginException the login exception
     * @throws ClientException the client exception
     */
    DocumentDescriptor createDocument(String username, String password,
            String parentPath, String name, String type) throws LoginException,
            ClientException;

}
