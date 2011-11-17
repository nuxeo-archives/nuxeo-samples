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

package org.nuxeo.ecm.samples.ws.test;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jboss.seam.ScopeType;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.annotations.Scope;
import org.jboss.seam.faces.FacesMessages;
import org.jboss.seam.international.StatusMessage;
import org.nuxeo.ecm.core.api.CoreSession;
import org.nuxeo.ecm.core.api.DocumentModel;
import org.nuxeo.ecm.core.api.NuxeoPrincipal;
import org.nuxeo.ecm.platform.ui.web.api.NavigationContext;
import org.nuxeo.ecm.samples.ws.client.WSClientService;
import org.nuxeo.ecm.samples.ws.server.DocumentDescriptor;
import org.nuxeo.ecm.samples.ws.server.NuxeoSampleWebServiceInterface;
import org.nuxeo.ecm.webapp.documentsLists.DocumentsListsManager;
import org.nuxeo.ecm.webapp.helpers.ResourcesAccessor;
import org.nuxeo.runtime.api.Framework;

/**
 * Seam action bean to test Nuxeo WebService sample. Based on a Nuxeo IDE Seam
 * Action Bean artifact.
 * 
 * @author ataillefer
 */
@Name("wSServerAction")
@Scope(ScopeType.EVENT)
public class WSServerActionBean implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final Log log = LogFactory.getLog(WSServerActionBean.class);

    @In(create = true, required = false)
    protected transient CoreSession documentManager;

    @In(create = true)
    protected NavigationContext navigationContext;

    @In(create = true, required = false)
    protected transient FacesMessages facesMessages;

    @In(create = true)
    protected transient ResourcesAccessor resourcesAccessor;

    @In(create = true, required = false)
    protected NuxeoPrincipal currentNuxeoPrincipal;

    @In(create = true)
    protected DocumentsListsManager documentsListsManager;

    // Sample code to show how to retrieve the list of selected documents in the
    // content listing view
    protected List<DocumentModel> getCurrentlySelectedDocuments() {

        if (navigationContext.getCurrentDocument().isFolder()) {
            return documentsListsManager.getWorkingList(DocumentsListsManager.CURRENT_DOCUMENT_SELECTION);
        } else {
            return null;
        }
    }

    // This the method that will be called when the action button/link is
    // clicked
    public String doGet() throws Exception {

        // Get the WSClientService
        WSClientService wsClientService = Framework.getService(WSClientService.class);

        // Make a call to nuxeosample webservice
        NuxeoSampleWebServiceInterface nswsi = wsClientService.getAdapter(NuxeoSampleWebServiceInterface.class);
        DocumentDescriptor dd = nswsi.createDocument("Administrator",
                "Administrator", "/default-domain/workspaces", "myTestFile",
                "File");

        String message = String.format("Document %s has been created! ",
                dd.getTitle());

        facesMessages.add(StatusMessage.Severity.INFO, message);

        return null;
    }

    // this method will be called by the action system to determine if the
    // action should be available
    //
    // the return value can depend on the context,
    // you can use the navigationContext to get the currentDocument,
    // currentWorkspace ...
    // you can cache the value in a member variable as long as the Bean stays
    // Event scoped
    //
    // if you don't need this, you should remove the filter in the associated
    // action contribution
    public boolean accept() {
        return true;
    }

}
