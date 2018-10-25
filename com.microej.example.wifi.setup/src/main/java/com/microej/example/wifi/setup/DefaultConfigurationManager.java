/*
 * Java
 *
 * Copyright 2018 IS2T. All rights reserved.
 * This library is provided in source code for use, modification and test, subject to license terms.
 * Any modification of the source code will break IS2T warranties on the whole library.
 */
package com.microej.example.wifi.setup;

import ej.ecom.wifi.SoftAPConfiguration;
import ej.net.util.wifi.AccessPointConfiguration;

/**
 * Default implementation of a {@link ConfigurationManager}.
 */
public class DefaultConfigurationManager implements ConfigurationManager {

	private SoftAPConfiguration softAPConfiguration;
	private AccessPointConfiguration apConfiguration;

	/**
	 * Instantiates a {@link DefaultConfigurationManager} creating a basic {@link SoftAPConfiguration} with a default
	 * SSID.
	 */
	public DefaultConfigurationManager() {
		this(new SoftAPConfiguration());
		this.softAPConfiguration.setSSID(DefaultConfigurationManager.class.getSimpleName());
	}

	/**
	 * Instantiates a {@link DefaultConfigurationManager} with a {@link SoftAPConfiguration}.
	 *
	 * @param softAPConfiguration
	 *            the {@link SoftAPConfiguration}, cannot be <code>null</code>.
	 */
	public DefaultConfigurationManager(SoftAPConfiguration softAPConfiguration) {
		if (softAPConfiguration == null) {
			throw new NullPointerException();
		}
		this.softAPConfiguration = softAPConfiguration;
	}

	@Override
	public SoftAPConfiguration loadSoftAPConfiguration() {
		return this.softAPConfiguration;
	}

	@Override
	public AccessPointConfiguration getAPConfiguration() {
		return this.apConfiguration;
	}

	@Override
	public void storeAPConfiguration(AccessPointConfiguration config) {
		// Do nothing.
	}

	/**
	 * Sets the softAPConfiguration.
	 *
	 * @param softAPConfiguration
	 *            the softAPConfiguration to set.
	 */
	public void setSoftAPConfiguration(SoftAPConfiguration softAPConfiguration) {
		this.softAPConfiguration = softAPConfiguration;
	}

	/**
	 * Sets the apConfiguration.
	 *
	 * @param apConfiguration
	 *            the apConfiguration to set.
	 */
	public void setApConfiguration(AccessPointConfiguration apConfiguration) {
		this.apConfiguration = apConfiguration;
	}
}
