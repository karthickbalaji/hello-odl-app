/*
 * Copyright © 2020 "Karthick Balaji T S" and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.demo.cli.impl;

import org.opendaylight.controller.md.sal.binding.api.DataBroker;
import org.opendaylight.demo.cli.api.SampleNetworkCliCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SampleNetworkCliCommandsImpl implements SampleNetworkCliCommands {

    private static final Logger LOG = LoggerFactory.getLogger(SampleNetworkCliCommandsImpl.class);
    private final DataBroker dataBroker;

    public SampleNetworkCliCommandsImpl(final DataBroker db) {
        this.dataBroker = db;
        LOG.info("SampleNetworkCliCommandImpl initialized");
    }

    @Override
    public Object testCommand(Object testArgument) {
        return "This is a test implementation of test-command";
    }
}
