/*
 * Copyright © 2020 "Karthick Balaji T S" and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package org.opendaylight.demo.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.samplenetwork.rev200818.LocationChanged;
import org.opendaylight.yang.gen.v1.urn.opendaylight.params.xml.ns.yang.samplenetwork.rev200818.SamplenetworkListener;

public class NetworkNotificationSubscriber implements SamplenetworkListener {
    private static final Logger  LOG = LoggerFactory.getLogger(NetworkNotificationSubscriber.class);

	@Override
	public void onLocationChanged(LocationChanged notification) {

		LOG.info("NetworkNotificationSubscriber : locationChanged event received !");
		
		LOG.info("Hey some node changed location - here it is --> Node {} moved to {} !! - ", notification.getName(), notification.getChange());
		
	}

}
