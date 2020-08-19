/*
 * Copyright © 2020 "Karthick Balaji T S" and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.demo.impl;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.opendaylight.mdsal.binding.api.DataBroker;
import org.opendaylight.mdsal.binding.api.WriteTransaction;
import org.opendaylight.mdsal.common.api.LogicalDatastoreType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.samplenetwork.rev200818.CreateNetworkInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.samplenetwork.rev200818.CreateNetworkOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.samplenetwork.rev200818.CreateNetworkOutputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.samplenetwork.rev200818.Network;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.samplenetwork.rev200818.NetworkBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.samplenetwork.rev200818.SamplenetworkService;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.samplenetwork.rev200818.UpdateLocationInput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.samplenetwork.rev200818.UpdateLocationOutput;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.samplenetwork.rev200818.UpdateLocationOutputBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.samplenetwork.rev200818.create.network.input.net.Devices;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.samplenetwork.rev200818.network.Nodes;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.samplenetwork.rev200818.network.NodesBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.samplenetwork.rev200818.network.NodesKey;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.opendaylight.yangtools.yang.common.RpcResultBuilder;

import com.google.common.util.concurrent.ListenableFuture;

public class SampleNetworkServiceImpl implements SamplenetworkService {
	
	private DataBroker dataBroker;

	public DataBroker getDataBroker() {
		return dataBroker;
	}

	public void setDataBroker(DataBroker dataBroker) {
		this.dataBroker = dataBroker;
	}

	@Override
	public ListenableFuture<RpcResult<UpdateLocationOutput>> updateLocation(UpdateLocationInput input) {
		WriteTransaction writeTx = dataBroker.newWriteOnlyTransaction();
		
		InstanceIdentifier<Nodes> nodePath = InstanceIdentifier.create(Network.class).child(Nodes.class, new NodesKey(input.getDevice().getName()));

		writeTx.merge(LogicalDatastoreType.CONFIGURATION, nodePath, new NodesBuilder()
				.setName(input.getDevice().getName())
				.setLocation(input.getDevice().getLocation())
				.build());

		try {
			writeTx.commit().get();
			return RpcResultBuilder.success(new UpdateLocationOutputBuilder().setStatus("SUCCESS").build()).buildFuture();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			return RpcResultBuilder.success(new UpdateLocationOutputBuilder().setStatus("FAILED").build()).buildFuture();
		}
	}

	@Override
	public ListenableFuture<RpcResult<CreateNetworkOutput>> createNetwork(CreateNetworkInput input) {
		
		List<Devices> devices = input.getNet().getDevices();
		List<Nodes> nodes = new ArrayList<Nodes>();
		
		WriteTransaction writeTx = dataBroker.newWriteOnlyTransaction();
		
		for (Devices device : devices) {
			nodes.add(new NodesBuilder()
						.setName(device.getName())
						.setLocation(device.getLocation())
						.build());
		}

		InstanceIdentifier<Network> netPath = InstanceIdentifier.create(Network.class);
		
		writeTx.merge(LogicalDatastoreType.CONFIGURATION, netPath, new NetworkBuilder()
																		.setNodes(nodes)
																		.build());
		
		try {
			writeTx.commit().get();
			return RpcResultBuilder.success(new CreateNetworkOutputBuilder().setStatus("SUCCESS").build()).buildFuture();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
			return RpcResultBuilder.success(new CreateNetworkOutputBuilder().setStatus("FAILED").build()).buildFuture();
		}
		
		
	}
	
}