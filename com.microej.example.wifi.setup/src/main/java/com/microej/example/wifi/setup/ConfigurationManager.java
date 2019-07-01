/*
 * Java
 *
 * Copyright 2017-2019 MicroEJ Corp. All rights reserved.
 * For demonstration purpose only.
 * MicroEJ Corp. PROPRIETARY. Use is subject to license terms.
 */
package com.microej.example.wifi.setup;

import ej.ecom.wifi.SoftAPConfiguration;
import ej.net.util.wifi.AccessPointConfiguration;

/**
 * Interface used by a {@link SoftAPConnector} to load existing configuration and store successful configuration.
 */
public interface ConfigurationManager {

	/**
	 * Gets the {@link SoftAPConfiguration} to use for the Soft AP.
	 *
	 * @return a {@link SoftAPConfiguration}, not <code>null</code>.
	 */
	SoftAPConfiguration getSoftAPConfiguration();

	/**
	 * Loads the {@link AccessPointConfiguration} to connect to if any available.
	 *
	 * @return the {@link AccessPointConfiguration}, <code>null</code> if none.
	 */
	AccessPointConfiguration loadAPConfiguration();

	/**
	 * Stores the {@link AccessPointConfiguration} when the join has been successful.
	 *
	 * @param config
	 *            the {@link AccessPointConfiguration} to store, cannot be <code>null</code>.
	 */
	void storeAPConfiguration(AccessPointConfiguration config);
}
