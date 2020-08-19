/*
 * Copyright © 2020 "Karthick Balaji T S" and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.demo.impl;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.eclipse.jdt.annotation.NonNull;
import org.opendaylight.mdsal.binding.api.DataObjectModification;
import org.opendaylight.mdsal.binding.api.DataObjectModification.ModificationType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.samplenetwork.rev200818.LocationChanged;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.samplenetwork.rev200818.LocationChangedBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.samplenetwork.rev200818.network.Nodes;
import org.opendaylight.mdsal.binding.api.DataTreeChangeListener;
import org.opendaylight.mdsal.binding.api.DataTreeModification;
import org.opendaylight.mdsal.binding.api.NotificationPublishService;

public class NetworkDataChangeListener implements DataTreeChangeListener<Nodes> {

    private static final Logger LOG = LoggerFactory.getLogger(NetworkDataChangeListener.class);
    NotificationPublishService notificationService;
    
	public NotificationPublishService getNotificationService() {
		return notificationService;
	}

	public void setNotificationService(NotificationPublishService notificationService) {
		this.notificationService = notificationService;
	}


	@Override
	public void onDataTreeChanged(@NonNull Collection<DataTreeModification<Nodes>> changes) {
		
		for (DataTreeModification<Nodes> change : changes) {
	        LOG.info("Network seem changed !!");
	        
	        DataObjectModification<Nodes> chgNode = change.getRootNode();
	        ModificationType type = chgNode.getModificationType();
	        if(type != null) {
		        if(type.equals(ModificationType.WRITE)) {
		        	
		        	//check and publish notification event if location changed
	        		if(chgNode.getDataBefore() != null) {
						
			        	if (!chgNode.getDataBefore().getLocation().equalsIgnoreCase(chgNode.getDataAfter().getLocation())) {
				        	LOG.info("What changed -- Node:{} location from - {} to {}", 
									chgNode.getDataBefore().getName(), 
									chgNode.getDataBefore().getLocation(), 
									chgNode.getDataAfter().getLocation());
							//build change notification
							LocationChanged chgNotification = new LocationChangedBuilder()
																.setName(chgNode.getDataBefore().getName())
																.setChange(chgNode.getDataAfter().getLocation())
																.build();
							
							//publish
							notificationService.offerNotification(chgNotification);
			        	}
		        		
		        	} else {
		        		// ?? try
				        LOG.info("What changed --  Node created - {} ", chgNode.getDataAfter().getName());
		        	}
			        
		        } else if (type.equals(ModificationType.DELETE)) {
			        LOG.info("What changed --  Node deleted - {} ", chgNode.getDataBefore().getName());	        	
		        }	        	
	        }
		}
	}

}
