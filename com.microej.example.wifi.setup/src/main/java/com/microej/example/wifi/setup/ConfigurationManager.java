/*
 * Java
 *
 * Copyright 2017-2018 IS2T. All rights reserved.
 * This library is provided in source code for use, modification and test, subject to license terms.
 * Any modification of the source code will break IS2T warranties on the whole library.
 */
package com.microej.example.wifi.setup;

import ej.ecom.wifi.SoftAPConfiguration;
import ej.net.util.wifi.AccessPointConfiguration;

/**
 * Manages the configuration of the SoftAP and AP.
 */
public interface ConfigurationManager {

	/**
	 * Loads the {@link SoftAPConfiguration} to use.
	 *
	 * @return a {@link SoftAPConfiguration}, not <code>null</code>.
	 */
	SoftAPConfiguration loadSoftAPConfiguration();

	/**
	 * Loads the {@link AccessPointConfiguration} to connect to.
	 *
	 * @return the {@link AccessPointConfiguration}, <code>null</code> if none.
	 */
	AccessPointConfiguration getAPConfiguration();

	/**
	 * Stores the {@link AccessPointConfiguration} when the join has been successful.
	 *
	 * @param config
	 *            the {@link AccessPointConfiguration} to store, cannot be <code>null</code>.
	 */
	void storeAPConfiguration(AccessPointConfiguration config);
}
