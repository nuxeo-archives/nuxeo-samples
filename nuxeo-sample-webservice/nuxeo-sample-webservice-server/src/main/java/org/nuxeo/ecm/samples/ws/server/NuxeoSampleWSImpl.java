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

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.security.auth.login.LoginContext;
import javax.security.auth.login.LoginException;

import org.nuxeo.ecm.core.api.ClientException;
import org.nuxeo.ecm.core.api.CoreInstance;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.repository.Repository;
import org.nuxeo.ecm.core.api.repository.RepositoryManager;
import org.nuxeo.ecm.platform.api.ws.DocumentDescriptor;
import org.nuxeo.runtime.api.Framework;
import org.nuxeo.runtime.transaction.TransactionHelper;

/**
 * Nuxeo sample WebService implementation.
 * 
 * @author ataillefer
 */
@WebService(name = "NuxeoSampleWebServiceInterface", serviceName = "NuxeoSampleWebService")
@SOAPBinding(style = Style.DOCUMENT)
public class NuxeoSampleWSImpl implements NuxeoSampleWS {

    /** The serialVersionUID. */
    private static final long serialVersionUID = 7220394261331723630L;

    /**
     * {@inheritDoc}
     */
    @WebMethod
    public DocumentDescriptor createDocument(@WebParam(name = "username")
    String username, @WebParam(name = "password")
    String password, @WebParam(name = "parentPath")
    String parentPath, @WebParam(name = "name")
    String name, @WebParam(name = "type")
    String type) throws LoginException, ClientException {

        // Login
        LoginContext loginContext = Framework.login(username, password);

        // Start transaction
        TransactionHelper.startTransaction();

        CoreSession session = null;
        try {
            // Open a core session
            session = openSession();

            // Do the job: create a doc with the given params
            DocumentModel doc = session.createDocumentModel(parentPath, name,
                    type);
            doc = session.createDocument(doc);
            session.save();

            return new DocumentDescriptor(doc);

        } catch (ClientException ce) {
            // Set transaction for rollback if an exception occurs
            TransactionHelper.setTransactionRollbackOnly();
            throw ce;
        } finally {
            // Close the core session
            if (session != null) {
                closeSession(session);
            }
            // Commit or rollback transaction
            TransactionHelper.commitOrRollbackTransaction();
            // Logout
            if (loginContext != null) {
                loginContext.logout();
            }
        }

    }

    /**
     * Opens a core session on default repository.
     * 
     * @return the core session
     * @throws ClientException the client exception
     */
    protected CoreSession openSession() throws ClientException {

        try {
            RepositoryManager mgr = Framework.getService(RepositoryManager.class);
            Repository repository = mgr.getDefaultRepository();

            return repository.open();

        } catch (Exception e) {
            throw ClientException.wrap(e);
        }
    }

    /**
     * Closes a core session.
     * 
     * @param session the session
     */
    protected void closeSession(CoreSession session) {
        CoreInstance.getInstance().close(session);
    }

}
