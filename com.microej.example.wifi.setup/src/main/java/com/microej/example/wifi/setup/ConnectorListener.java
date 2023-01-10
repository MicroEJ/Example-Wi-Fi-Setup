/*
 * Java
 *
 * Copyright 2017-2022 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.example.wifi.setup;

import java.io.IOException;

import ej.ecom.wifi.AccessPoint;
import ej.ecom.wifi.SoftAPConfiguration;
import ej.net.util.wifi.AccessPointConfiguration;

/**
 * This interface provides methods for notifying when connection events occur while connecting to an access point.
 */
public interface ConnectorListener {

	/**
	 * Notifies that the softAP is now mounted using the given configuration.
	 *
	 * @param softAPConfiguration
	 *            the {@link SoftAPConfiguration} used, not <code>null</code>.
	 */
	void onSoftAPMount(SoftAPConfiguration softAPConfiguration);

	/**
	 * Notifies that a join failed using the given configuration.
	 *
	 * @param apConfiguration
	 *            the {@link AccessPointConfiguration} used.
	 * @param e
	 *            the exception, can be <code>null</code>.
	 */
	void onJoinError(AccessPointConfiguration apConfiguration, Exception e);

	/**
	 * Notifies that a join was successful using the given configuration.
	 *
	 * @param apConfiguration
	 *            the {@link AccessPointConfiguration} used, not <code>null</code>.
	 */
	void onSuccessfulJoin(AccessPointConfiguration apConfiguration);

	/**
	 * Notifies that the mount of the Soft AP failed using the given configuration.
	 *
	 * @param softAPConfiguration
	 *            the {@link SoftAPConfiguration} used.
	 * @param e
	 *            the {@link Exception}, can be <code>null</code>.
	 */
	void onSoftAPMountError(SoftAPConfiguration softAPConfiguration, IOException e);

	/**
	 * Notifies that the Soft AP has been unmounted.
	 */
	void onSoftAPUnmount();

	/**
	 * Notifies that a scan has been done.
	 *
	 * @param accessPoints
	 *            the access points scanned.
	 */
	void onScan(AccessPoint[] accessPoints);

	/**
	 * Notifies that a join will be done with the given configuration.
	 *
	 * @param apConfiguration
	 *            the configuration to join.
	 */
	void onTryingJoin(AccessPointConfiguration apConfiguration);

}
