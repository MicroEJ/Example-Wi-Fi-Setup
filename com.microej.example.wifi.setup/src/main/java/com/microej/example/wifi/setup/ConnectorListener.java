/*
 * Java
 *
 * Copyright 2017-2018 IS2T. All rights reserved.
 * This library is provided in source code for use, modification and test, subject to license terms.
 * Any modification of the source code will break IS2T warranties on the whole library.
 */
package com.microej.example.wifi.setup;

import java.io.IOException;

import ej.ecom.wifi.AccessPoint;
import ej.ecom.wifi.SoftAPConfiguration;
import ej.net.util.wifi.AccessPointConfiguration;

/**
 * Listener of the connector steps.
 */
public interface ConnectorListener {

	/**
	 * Called method when the softAp is Mounted.
	 *
	 * @param configuration
	 *            the {@link SoftAPConfiguration} used, not <code>null</code>.
	 */
	void onSoftAPMount(SoftAPConfiguration configuration);

	/**
	 * Called when an error occurs during join.
	 *
	 * @param config
	 *            the {@link AccessPointConfiguration} used.
	 * @param e
	 *            the exception, can be <code>null</code>.
	 */
	void onJoinError(AccessPointConfiguration config, Exception e);

	/**
	 * Called after a successful join.
	 *
	 * @param config
	 *            the {@link AccessPointConfiguration} used, not <code>null</code>.
	 */
	void onSuccessfulJoin(AccessPointConfiguration config);

	/**
	 * Called when the softAP cannot be mounted.
	 *
	 * @param softAPConfiguration
	 *            the {@link SoftAPConfiguration} used.
	 * @param e
	 *            the {@link Exception}, can be <code>null</code>.
	 */
	void onMountError(SoftAPConfiguration softAPConfiguration, IOException e);

	/**
	 * Called when the softAP is unmounted.
	 */
	void onSoftAPUnmount();

	/**
	 * Called when a scan is done.
	 *
	 * @param accessPoints
	 *            the access points scanned.
	 */
	void onScan(AccessPoint[] accessPoints);

	/**
	 * Called before a join.
	 *
	 * @param apConfiguration
	 *            the configuration to join.
	 */
	void onJoin(AccessPointConfiguration apConfiguration);

}
