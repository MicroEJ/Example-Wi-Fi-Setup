/*
 * Java
 *
 * Copyright 2017-2018 IS2T. All rights reserved.
 * This library is provided in source code for use, modification and test, subject to license terms.
 * Any modification of the source code will break IS2T warranties on the whole library.
 */
package com.microej.example.wifi.setup.rest;

import java.io.IOException;

import com.microej.example.wifi.setup.ConfigurationManager;
import com.microej.example.wifi.setup.Messages;
import com.microej.example.wifi.setup.SoftAPConnector;
import com.microej.example.wifi.setup.rest.endpoint.DiagnosticEndPoint;
import com.microej.example.wifi.setup.rest.endpoint.JoinEndPoint;
import com.microej.example.wifi.setup.rest.endpoint.ScanEndPoint;
import com.microej.example.wifi.setup.rest.internal.AccessPointJSON;
import com.microej.example.wifi.setup.rest.internal.ApplicationStrings;

import ej.ecom.wifi.AccessPoint;
import ej.ecom.wifi.SoftAPConfiguration;
import ej.ecom.wifi.WifiCapability;
import ej.net.util.wifi.AccessPointConfiguration;
import ej.net.util.wifi.WifiNetworkManager;
import ej.restserver.RestartableServer;
import ej.util.message.Level;

/**
 * A {@link SoftAPConfiguration} using rest endpoints. Three endpoints are available:
 * <ul>
 * <li>{@link ScanEndPoint} at url <code>/scan</code></li>
 * <li>{@link JoinEndPoint} at url <code>/join</code></li>
 * <li>{@link DiagnosticEndPoint} at url <code>/diagnostic</code></li>
 * </ul>
 */
public class RestSoftAPConnector extends SoftAPConnector {

	/**
	 * The server states.
	 */
	public enum STATE {
		/**
		 * The server is ready.
		 */
		READY,

		/**
		 * The server is busy, it will soon stop.
		 */
		BUSY
	}

	private static final int MAX_SOCKET = 10;
	private static final int MAX_JOB = 2;

	private static final int SERVER_PORT = 80;
	private static final long DELAY = 500;

	private RestartableServer restServer;
	private STATE state;
	private String accesses;
	private String joined;

	private int serverPort;

	/**
	 * Instantiates a {@link RestSoftAPConnector}.
	 *
	 * @throws IOException
	 *             When initialisation fails.
	 */
	public RestSoftAPConnector() throws IOException {
		this(SERVER_PORT);
	}

	/**
	 * Instantiates a {@link RestSoftAPConnector} with a given port.
	 *
	 * @param port
	 *            the server port.
	 * @throws IOException
	 *             When initialisation fails.
	 */
	public RestSoftAPConnector(int port) throws IOException {
		super();
		this.serverPort = port;
	}

	/**
	 * Instantiates a RestSoftAPConnector with a {@link ConfigurationManager}.
	 *
	 * @param configurationManager
	 *            the {@link ConfigurationManager} managing access points configuration..
	 * @throws IOException
	 *             When initialisation fails.
	 */
	public RestSoftAPConnector(ConfigurationManager configurationManager) throws IOException {
		this(configurationManager, SERVER_PORT);
	}

	/**
	 * Instantiates a RestSoftAPConnector with a {@link ConfigurationManager}.
	 *
	 * @param configurationManager
	 *            the {@link ConfigurationManager}.
	 * @param port
	 *            the server port.
	 * @throws IOException
	 *             When initialisation fails.
	 */
	public RestSoftAPConnector(ConfigurationManager configurationManager, int port) throws IOException {
		super(configurationManager);
		this.serverPort = port;
		this.state = STATE.READY;
		this.joined = ApplicationStrings.EMPTY_OBJECT;
		this.accesses = ApplicationStrings.EMPTY_ARRAY;
	}

	@Override
	public void start() throws IOException {
		updateAccesses();
		super.start();
		if (!getManager().isConnected()) {
			this.restServer = new RestartableServer(this.serverPort, MAX_SOCKET, MAX_JOB);
			this.state = STATE.READY;
			addEnpoints(this.restServer);
			this.restServer.start();
		}
	}

	@Override
	public void stop() {
		super.stop();
		RestartableServer server = this.restServer;
		if (server != null) {
			server.stop();
		}
		this.restServer = null;
	}

	/**
	 * Gets the server port.
	 *
	 * @return the server port.
	 */
	public int getServerPort() {
		return this.serverPort;
	}

	/**
	 * Sets the server port.
	 *
	 * @param serverPort
	 *            the server port
	 */
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}

	/**
	 * Gets the server state.
	 *
	 * @return the server state.
	 */
	public STATE getState() {
		return this.state;
	}

	/**
	 * Triggers an update of the access points. The function is synchronous if the NetworkManager supports a scan while
	 * being in a softAP State, asynchronous if not.
	 *
	 * @see WifiNetworkManager#supportScanWhileSoftAP()
	 */
	public void triggerUpdateAccess() {
		trigger(new Runnable() {

			@Override
			public void run() {
				updateAccesses();
				RestSoftAPConnector.this.state = STATE.READY;
			}
		}, supportScanWhileSoftAP());
	}

	/**
	 * Triggers a join. The function is synchronous if the NetworkManager supports a join while being in a softAP State,
	 * asynchronous if not.
	 *
	 * @param accessPointConfiguration
	 *            the configuration to join.
	 * @see WifiNetworkManager#getCapabilities()
	 */
	public void triggerJoin(final AccessPointConfiguration accessPointConfiguration) {
		final boolean pause = supportJoinWhileSoftAP();
		trigger(new Runnable() {

			@Override
			public void run() {
				RestartableServer server = RestSoftAPConnector.this.restServer;
				if (pause && server != null) {
					server.pause();
				}
				boolean joined = join(accessPointConfiguration);
				if (pause && server != null && !joined) {
					try {
						server.start();
					} catch (IOException e) {
						Messages.LOGGER.log(Level.SEVERE, Messages.CATEGORY, Messages.ERROR_START_SERVER, e);
					}
				}
			}
		}, pause);
	}

	/**
	 * Gets the access point in cache.
	 *
	 * @return the access point available.
	 */
	public String getAccesses() {
		return this.accesses;
	}

	/**
	 * Gets the cached joined.
	 *
	 * @return the cache joined.
	 */
	public String getJoined() {
		return this.joined;
	}

	@Override
	protected void onSuccessfulJoin(AccessPointConfiguration accessPointConfiguration) {
		if (accessPointConfiguration.getAccessPoint() != null) {
			this.joined = new AccessPointJSON(accessPointConfiguration.getAccessPoint()).toString();
		} else {
			this.joined = new AccessPointJSON(accessPointConfiguration.getSSID(), 0, null, 0, null).toString();
		}
		super.onSuccessfulJoin(accessPointConfiguration);
	}

	@Override
	protected void onMount(SoftAPConfiguration softAPConfiguration) {
		RestartableServer server = this.restServer;
		if (supportScanWhileSoftAP() && server != null) {
			try {
				server.start();
			} catch (IOException e) {
				Messages.LOGGER.log(Level.INFO, Messages.CATEGORY, Messages.ERROR_START_SERVER, e);
			}
		}
		super.onMount(softAPConfiguration);
		this.state = STATE.READY;
	}

	@Override
	protected void unmountSoftAP() throws IOException {
		RestartableServer server = this.restServer;
		if (supportScanWhileSoftAP() && server != null) {
			server.pause();
		}
		super.unmountSoftAP();
	}

	@Override
	protected void onUnmount() {
		super.onUnmount();
		this.state = STATE.BUSY;
	}

	/**
	 * Adds the server endpoints.
	 *
	 * @param server
	 *            the server.
	 * @throws IOException
	 *             if the endpoints could not be added.
	 */
	protected void addEnpoints(RestartableServer server) throws IOException {
		server.addEndpoint(new ScanEndPoint(this));
		server.addEndpoint(new JoinEndPoint(this));
		server.addEndpoint(new DiagnosticEndPoint(this));
	}

	private void updateAccesses() {
		try {
			StringBuilder access = new StringBuilder().append('[');
			AccessPoint[] accessPoints = scan();
			int length = accessPoints.length;
			for (int i = 0; i < length; i++) {
				AccessPoint accessPoint = accessPoints[i];
				access.append(new AccessPointJSON(accessPoint).toString());
				if (i < length - 1) {
					access.append(',');
				}
			}
			access.append(']');
			this.accesses = access.toString();
		} catch (IOException e) {
			Messages.LOGGER.log(Level.INFO, Messages.CATEGORY, Messages.ERROR_FAILED_TO_SCAN, e);
			this.accesses = ApplicationStrings.EMPTY_ARRAY;
		}
	}

	private boolean supportJoinWhileSoftAP() {
		boolean pauseOnJoin = true;
		final WifiNetworkManager manager = getManager();
		try {
			pauseOnJoin = manager == null || manager.getCapabilities() != WifiCapability.BOTH_SIMULTANEOUS;
		} catch (IOException e) {
			Messages.LOGGER.log(Level.INFO, Messages.CATEGORY, Messages.ERROR_CHECKING_VALUE, e);
		}
		return pauseOnJoin;
	}

	private boolean supportScanWhileSoftAP() {
		WifiNetworkManager manager = getManager();
		return manager == null || !manager.supportScanWhileSoftAP();
	}

	private synchronized void trigger(final Runnable runnable, boolean asynchronous) {
		if (asynchronous) {
			if (this.state == STATE.READY) {
				this.state = STATE.BUSY;
				new Thread(new Runnable() {

					@Override
					public void run() {
						try {
							Thread.sleep(DELAY);
							runnable.run();
						} catch (InterruptedException e) {
							Messages.LOGGER.log(Level.SEVERE, Messages.CATEGORY, Messages.ERROR_UNKNOWN, e);
						}

					}
				}).start();
			}
		} else {
			runnable.run();
		}
	}

}
