/*
 * Java
 *
 * Copyright 2017-2019 MicroEJ Corp. All rights reserved.
 * For demonstration purpose only.
 * MicroEJ Corp. PROPRIETARY. Use is subject to license terms.
 */
package com.microej.example.wifi.setup;

import java.io.IOException;

import ej.annotation.NonNull;
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
	private final WifiNetworkManager wifiNetworkManager;
	private ConfigurationManager configurationManager;
	private IPConfiguration ipConfig;
	private IPConfiguration softApConfig;
	private ConnectorListener[] listeners;

	/**
	 *
	 * Instantiates a {@link SoftAPConnector} with a {@link DefaultConfigurationManager} as default implementation for
	 * managing access points configuration.
	 *
	 * @throws IOException
	 *             When initialisation fails.
	 */
	public SoftAPConnector() throws IOException {
		this(new DefaultConfigurationManager(), new WifiNetworkManager());
	}

	/**
	 * Instantiates a {@link SoftAPConnector} with a given {@link ConfigurationManager}.
	 *
	 * @param configurationManager
	 *            the {@link ConfigurationManager} to use to get the softAp and AP parameters.
	 * @throws IOException
	 *             When initialization fails.
	 * @throws NullPointerException
	 *             If configurationManager or {@link WifiNetworkManager} is <code>null</code>.
	 */
	public SoftAPConnector(@NonNull ConfigurationManager configurationManager)
			throws IOException, NullPointerException {
		this(configurationManager, new WifiNetworkManager());
	}

	/**
	 * Instantiates a {@link SoftAPConnector} with a given {@link ConfigurationManager}.
	 *
	 * @param configurationManager
	 *            the {@link ConfigurationManager} to use to get the softAp and AP parameters.
	 * @param wifiNetworkManager
	 *            the {@link WifiNetworkManager}, cannot be <code>null</code>
	 * @throws IOException
	 *             When initialization fails.
	 * @throws NullPointerException
	 *             If configurationManager or {@link WifiNetworkManager} is <code>null</code>.
	 */
	public SoftAPConnector(@NonNull ConfigurationManager configurationManager,
			@NonNull WifiNetworkManager wifiNetworkManager) throws IOException, NullPointerException {
		super();

		if (configurationManager == null) {
			throw new NullPointerException();
		}
		this.configurationManager = configurationManager;
		this.listeners = new ConnectorListener[0];
		this.wifiNetworkManager = wifiNetworkManager;
	}

	/**
	 * Sets the configuration manager, can not be <code>null</code>.
	 *
	 * @param configurationManager
	 *            The {@link ConfigurationManager} to use for managing access points configuration.
	 * @throws NullPointerException
	 *             If configurationManager is <code>null</code>.
	 */
	public void setConfigurationManager(ConfigurationManager configurationManager) {
		if (configurationManager == null) {
			throw new NullPointerException();
		}
		this.configurationManager = configurationManager;
	}

	/**
	 * Gets the configuration manager.
	 *
	 * @return the configuration manager.
	 */
	public ConfigurationManager getConfigurationManager() {
		return this.configurationManager;
	}

	/**
	 * Starts the {@link SoftAPConnector} :
	 * <ol>
	 * <li>Sets the Wi-Fi network manager configuration.</li>
	 * <li>Tries to join the {@link ConfigurationManager}</li>
	 * <li>If the {@link ConfigurationManager} does not provide one or if the join fails, mounts the SoftAP.</li>
	 * </ol>
	 *
	 * @throws IOException
	 *             if an {@link IOException} occurs.
	 * @see SoftAPConnector#join(AccessPointConfiguration)
	 * @see ConfigurationManager#loadAPConfiguration()
	 */
	public synchronized void start() throws IOException {
		this.wifiNetworkManager.setClientIPConfigure(this.ipConfig);
		this.wifiNetworkManager.setSoftAPIPConfigure(this.softApConfig);
		join(this.configurationManager.loadAPConfiguration());
	}

	/**
	 * Stops the {@link SoftAPConnector}.
	 */
	public synchronized void stop() {
		if (this.wifiNetworkManager != null) {
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
		AccessPoint[] accessPoints = this.wifiNetworkManager.scanAccessPoints();
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
		if (apConfiguration != null && this.wifiNetworkManager != null) {
			try {
				this.wifiNetworkManager.setAPConfiguration(apConfiguration);
				onJoin(apConfiguration);
				this.wifiNetworkManager.joinAccessPoint(TIMEOUT);
				joined = true;
				onSuccessfulJoin(apConfiguration);
				this.configurationManager.storeAPConfiguration(apConfiguration);
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
			connectorListener.onTryingJoin(apConfiguration);
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
		if (this.wifiNetworkManager != null) {
			SoftAPConfiguration softAPConfiguration = this.configurationManager.getSoftAPConfiguration();
			if (softAPConfiguration == null) {
				throw new NullPointerException();
			}
			try {
				this.wifiNetworkManager.mountSoftAccessPoint(softAPConfiguration);
				onMount(softAPConfiguration);
			} catch (IOException e) {
				for (ConnectorListener connectorListener : this.listeners) {
					connectorListener.onSoftAPMountError(softAPConfiguration, e);
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
		this.wifiNetworkManager.unmountSoftAccessPoint();
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
	 * @param connectorListener
	 *            the listener, cannot be <code>null</code>.
	 */
	public void addListener(ConnectorListener connectorListener) {
		this.listeners = ArrayTools.add(this.listeners, connectorListener);
	}

	/**
	 * Removes a {@link ConnectorListener}.
	 *
	 * @param connectorListener
	 *            the listener, cannot be <code>null</code>.
	 */
	public void removeListener(ConnectorListener connectorListener) {
		this.listeners = ArrayTools.remove(this.listeners, connectorListener);
	}

	/**
	 * Gets the Wi-Fi manager.
	 *
	 * @return the {@link WifiNetworkManager}.
	 */
	public WifiNetworkManager getManager() {
		return this.wifiNetworkManager;
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
