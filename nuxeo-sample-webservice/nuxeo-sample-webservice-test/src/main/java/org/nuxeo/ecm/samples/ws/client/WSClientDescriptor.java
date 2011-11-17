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

import org.nuxeo.common.xmap.annotation.XNode;
import org.nuxeo.common.xmap.annotation.XObject;

/**
 * WSClient descriptor.
 * 
 * @author ataillefer
 */
@XObject("WSClientDescriptor")
public class WSClientDescriptor {

    /** The id. */
    @XNode("@id")
    private String id;

    /** The wsdl url. */
    @XNode("wsdlUrl")
    private String wsdlUrl;

    /** The namespace uri. */
    @XNode("namespaceURI")
    private String namespaceURI;

    /** The local part. */
    @XNode("localPart")
    private String localPart;

    /**
     * Gets the id.
     * 
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the id.
     * 
     * @param id the new id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets the wsdl url.
     * 
     * @return the wsdl url
     */
    public String getWsdlUrl() {
        return wsdlUrl;
    }

    /**
     * Sets the wsdl url.
     * 
     * @param wsdlUrl the new wsdl url
     */
    public void setWsdlUrl(String wsdlUrl) {
        this.wsdlUrl = wsdlUrl;
    }

    /**
     * Gets the namespace uri.
     * 
     * @return the namespace uri
     */
    public String getNamespaceURI() {
        return namespaceURI;
    }

    /**
     * Sets the namespace uri.
     * 
     * @param namespaceURI the new namespace uri
     */
    public void setNamespaceURI(String namespaceURI) {
        this.namespaceURI = namespaceURI;
    }

    /**
     * Gets the local part.
     * 
     * @return the local part
     */
    public String getLocalPart() {
        return localPart;
    }

    /**
     * Sets the local part.
     * 
     * @param localPart the new local part
     */
    public void setLocalPart(String localPart) {
        this.localPart = localPart;
    }

}
