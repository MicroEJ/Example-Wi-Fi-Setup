/*
 * Java
 *
 * Copyright 2017-2018 IS2T. All rights reserved.
 * This library is provided in source code for use, modification and test, subject to license terms.
 * Any modification of the source code will break IS2T warranties on the whole library.
 */
package com.microej.example.wifi.setup;

import java.io.IOException;

import ej.basictool.ArrayTools;
import ej.ecom.network.IPConfiguration;
import ej.ecom.wifi.AccessPoint;
import ej.ecom.wifi.SoftAPConfiguration;
import ej.net.util.wifi.AccessPointConfiguration;
import ej.net.util.wifi.WifiNetworkManager;
import ej.util.message.Level;

/**
 * A connector mounting a softAP to gets the AP information.
 */
public class SoftAPConnector {

	private static final int TIMEOUT = 30_000;
	private final WifiNetworkManager manager;
	private ConfigurationManager config;
	private IPConfiguration ipConfig;
	private IPConfiguration softApConfig;
	private ConnectorListener[] listeners;

	/**
	 *
	 * Instantiates a {@link SoftAPConnector} with a {@link DefaultConfigurationManager}.
	 *
	 * @throws IOException
	 *             When initialise fail.
	 */
	public SoftAPConnector() throws IOException {
		this(new DefaultConfigurationManager());
	}

	/**
	 * Instantiates a {@link SoftAPConnector} with a {@link ConfigurationManager}.
	 *
	 * @param config
	 *            the {@link ConfigurationManager} to use to get the softAp and AP parameters.
	 * @throws IOException
	 *             When initialise fail.
	 * @throws NullPointerException
	 *             If config is null.
	 */
	public SoftAPConnector(ConfigurationManager config) throws IOException, NullPointerException {
		super();

		if (config == null) {
			throw new NullPointerException();
		}
		this.config = config;
		this.listeners = new ConnectorListener[0];
		this.manager = new WifiNetworkManager();
	}

	/**
	 * Sets the configuration manager, can not be <code>null</code>.
	 *
	 * @param config
	 *            the config to set.
	 * @throws NullPointerException
	 *             If config is null.
	 */
	public void setConfigurationManager(ConfigurationManager config) {
		if (config == null) {
			throw new NullPointerException();
		}
		this.config = config;
	}

	/**
	 * Gets the configuration manager.
	 *
	 * @return the config.
	 */
	public ConfigurationManager getConfigurationManager() {
		return this.config;
	}

	/**
	 * Starts the {@link SoftAPConnector}. If the {@link ConfigurationManager} does not provide a base
	 * {@link AccessPoint} or the connection fails, mount the SoftAP.
	 *
	 * @throws IOException
	 *             if an {@link IOException} occurs.
	 * @see SoftAPConnector#join(AccessPointConfiguration)
	 *
	 */
	public synchronized void start() throws IOException {
		this.manager.setClientIPConfigure(this.ipConfig);
		this.manager.setSoftAPIPConfigure(this.softApConfig);
		join(this.config.getAPConfiguration());
	}

	/**
	 * Stops the {@link SoftAPConnector}.
	 */
	public synchronized void stop() {
		if (this.manager != null) {
			try {
				unmountSoftAP();
			} catch (IOException e) {
				Messages.LOGGER.log(Level.INFO, Messages.CATEGORY, Messages.ERROR_FAILED_TO_UNMOUNT, e);
			}
		}
	}

	/**
	 * Scans the available {@link AccessPoint}.
	 *
	 * @return the available {@link AccessPoint}.
	 * @throws IOException
	 *             if an {@link IOException} occurs.
	 * @see WifiNetworkManager#scanAccessPoints()
	 */
	public synchronized AccessPoint[] scan() throws IOException {
		AccessPoint[] accessPoints = this.manager.scanAccessPoints();
		onScan(accessPoints);
		return accessPoints;
	}

	/**
	 * Joins an {@link AccessPoint}, if the join fail, mount a SoftAP.
	 *
	 * @param apConfiguration
	 *            the {@link AccessPointConfiguration} to use.
	 * @return true if the AP has been joined.
	 * @see WifiNetworkManager#joinAccessPoint(int)
	 * @see WifiNetworkManager#mountSoftAccessPoint()
	 */
	public synchronized boolean join(AccessPointConfiguration apConfiguration) {
		boolean joined = false;
		if (apConfiguration != null && this.manager != null) {
			try {
				this.manager.setAPConfiguration(apConfiguration);
				onJoin(apConfiguration);
				this.manager.joinAccessPoint(TIMEOUT);
				joined = true;
				onSuccessfulJoin(apConfiguration);
				this.config.storeAPConfiguration(apConfiguration);
			} catch (NullPointerException | IOException e) {
				for (ConnectorListener connectorListener : this.listeners) {
					connectorListener.onJoinError(apConfiguration, e);
				}
			}
		}
		if (!joined) {
			try {
				mountSoftAP();
			} catch (IOException | NullPointerException e) {
				Messages.LOGGER.log(Level.SEVERE, Messages.CATEGORY, Messages.ERROR_FAILED_TO_MOUNT, e);
			}
		} else {
			stop();
		}
		return joined;
	}

	/**
	 * Called before a join.
	 *
	 * @param apConfiguration
	 *            the apConfiguration joined.
	 */
	protected void onJoin(AccessPointConfiguration apConfiguration) {
		for (ConnectorListener connectorListener : this.listeners) {
			connectorListener.onJoin(apConfiguration);
		}
	}

	/**
	 * Called when a successful join is done.
	 *
	 * @param apConfiguration
	 *            the apConfiguration joined.
	 */
	protected void onSuccessfulJoin(AccessPointConfiguration apConfiguration) {
		for (ConnectorListener connectorListener : this.listeners) {
			connectorListener.onSuccessfulJoin(apConfiguration);
		}
	}

	/**
	 * Called when a scan is done.
	 *
	 * @param accessPoints
	 *            the accesspoints.
	 */
	protected void onScan(AccessPoint[] accessPoints) {
		for (ConnectorListener connectorListener : this.listeners) {
			connectorListener.onScan(accessPoints);
		}
	}

	/**
	 * Mounts the SoftAp.
	 *
	 * @throws IOException
	 *             if an IOException occured during the mount.
	 */
	protected void mountSoftAP() throws IOException {
		if (this.manager != null) {
			SoftAPConfiguration softAPConfiguration = this.config.loadSoftAPConfiguration();
			if (softAPConfiguration == null) {
				throw new NullPointerException();
			}
			try {
				this.manager.mountSoftAccessPoint(softAPConfiguration);
				onMount(softAPConfiguration);
			} catch (IOException e) {
				for (ConnectorListener connectorListener : this.listeners) {
					connectorListener.onMountError(softAPConfiguration, e);
				}
				throw new IOException(e);
			}
		}
	}

	/**
	 * Unmounts the softAp.
	 *
	 * @throws IOException
	 *             if an {@link IOException} occurs during unmount.
	 */
	protected void unmountSoftAP() throws IOException {
		this.manager.unmountSoftAccessPoint();
		onUnmount();
	}

	/**
	 * Called when the softAP has been unmounted.
	 */
	protected void onUnmount() {
		for (ConnectorListener connectorListener : this.listeners) {
			connectorListener.onSoftAPUnmount();
		}
	}

	/**
	 * Called when the softAP has been mounted.
	 *
	 * @param softAPConfiguration
	 *            the configuration used, not <code>null</code>.
	 *
	 */
	protected void onMount(SoftAPConfiguration softAPConfiguration) {
		if (softAPConfiguration == null) {
			throw new NullPointerException();
		}
		for (ConnectorListener connectorListener : this.listeners) {
			connectorListener.onSoftAPMount(softAPConfiguration);
		}
	}

	/**
	 * Adds a {@link ConnectorListener}.
	 *
	 * @param e
	 *            the listener, cannot be <code>null</code>.
	 */
	public synchronized void addListener(ConnectorListener e) {
		this.listeners = ArrayTools.add(this.listeners, e);
	}

	/**
	 * Removes a {@link ConnectorListener}.
	 *
	 * @param o
	 *            the listener, cannot be <code>null</code>.
	 */
	public synchronized void removeListener(ConnectorListener o) {
		this.listeners = ArrayTools.remove(this.listeners, o);
	}

	/**
	 * Gets the Wi-Fi manager.
	 *
	 * @return the {@link WifiNetworkManager}.
	 */
	public WifiNetworkManager getManager() {
		return this.manager;
	}

	/**
	 * Sets the IP configuration. Should be called before the {@link #start()}.
	 *
	 * @param configuration
	 *            the {@link IPConfiguration} to use for the client mode, can be <code>null</code>.
	 * @param softAPConfiguration
	 *            {@link IPConfiguration} to use for the softAP, can be <code>null</code>.
	 *
	 */
	public void setIPConfiguration(IPConfiguration configuration, IPConfiguration softAPConfiguration) {
		this.ipConfig = configuration;
		this.softApConfig = configuration;
	}

}
