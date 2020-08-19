/*
 * Copyright © 2020 "Karthick Balaji T S" and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.demo.impl;

import org.opendaylight.mdsal.binding.api.DataBroker;
import org.opendaylight.mdsal.binding.api.DataTreeChangeListener;
import org.opendaylight.mdsal.binding.api.DataTreeIdentifier;
import org.opendaylight.mdsal.common.api.LogicalDatastoreType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.samplenetwork.rev200818.Network;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.samplenetwork.rev200818.network.Nodes;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SampleNetworkProvider {

    private static final Logger LOG = LoggerFactory.getLogger(SampleNetworkProvider.class);

    private final DataBroker dataBroker;
    private DataTreeChangeListener<Nodes> networkDataChangeListener;

    public DataTreeChangeListener<Nodes> getNetworkDataChangeListener() {
		return networkDataChangeListener;
	}

	public void setNetworkDataChangeListener(DataTreeChangeListener<Nodes> networkDataChangeListener) {
		this.networkDataChangeListener = networkDataChangeListener;
	}

    public SampleNetworkProvider(final DataBroker dataBroker) {
        this.dataBroker = dataBroker;
    }

    /**
     * Method called when the blueprint container is created.
     */
    public void init() {
        LOG.info("SampleNetworkProvider Session Initiated");
		InstanceIdentifier<Nodes> nodes = InstanceIdentifier
				.builder(Network.class)
				.child(Nodes.class)
				.build();
		DataTreeIdentifier<Nodes> dtiNodes = DataTreeIdentifier.create(LogicalDatastoreType.CONFIGURATION, nodes);		
        dataBroker.registerDataTreeChangeListener(dtiNodes, networkDataChangeListener);        
    }

    /**
     * Method called when the blueprint container is destroyed.
     */
    public void close() {
        LOG.info("SampleNetworkProvider Closed");
    }

}