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

import java.io.Serializable;
import java.util.Map;

/**
 * Nuxeo WebService client service. Allows to get an adapter for a given
 * WebService client interface.
 * 
 * @author ataillefer
 */
public interface WSClientService extends Serializable {

    /**
     * Gets the contributions.
     * 
     * @return the contributions
     */
    Map<String, WSClientDescriptor> getContributions();

    /**
     * Gets the adapter.
     * 
     * @param <T> the generic type
     * @param adapter the adapter
     * @return the adapter
     */
    <T> T getAdapter(Class<T> adapter);

}
