/*
 * (C) Copyright 2008 Nuxeo SAS (http://nuxeo.com/) and contributors.
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
 *     Florent Guillaume
 */

package org.nuxeo.ecm.sample.client;

import org.jboss.remoting.CannotConnectException;
import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.DocumentModelList;
import org.nuxeo.ecm.core.api.repository.RepositoryManager;
import org.nuxeo.ecm.core.client.NuxeoClient;
import org.nuxeo.runtime.api.Framework;

/**
 * Sample class that connects to a remote Nuxeo EP.
 * <p>
 * It just prints the titles of the domains.
 *
 * @author Florent Guillaume
 */
public class Main {

    public static String host = "localhost";

    public static int port = 62474;

    public static CoreSession documentManager;

    public static void main(String[] args) throws Exception {
        if (args.length == 2) {
            host = args[0];
            port = Integer.parseInt(args[1]);
        } else if (args.length != 0) {
            System.err.println("Arguments: hostname port");
            System.exit(1);
        }

        try {
            connect();
        } catch (CannotConnectException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }

        try {
            run();
        } finally {
            disconnect();
        }
    }

    /**
     * Connect to a remote Nuxeo EP and open a connection (documentManager).
     */
    public static void connect() throws Exception {
        NuxeoClient.getInstance().connect(host, port);
        documentManager = Framework.getService(RepositoryManager.class).getDefaultRepository().open();
    }

    /**
     * Disconnect from a remote Nuxeo EP.
     */
    public static void disconnect() {
        if (documentManager != null) {
            try {
                Framework.getService(RepositoryManager.class).getDefaultRepository().close(
                        documentManager);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            NuxeoClient.getInstance().tryDisconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Example code using documentManager.
     */
    public static void run() throws ClientException {
        DocumentModel root = documentManager.getRootDocument();
        DocumentModelList children = documentManager.getChildren(root.getRef());
        for (DocumentModel domain : children) {
            String path = domain.getPathAsString();
            String title = (String) domain.getProperty("dublincore", "title");
            System.out.println(String.format("Domain: %s (%s)", title, path));
        }
    }

}
