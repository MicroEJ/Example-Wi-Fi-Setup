/*
 * Java
 *
 * Copyright 2018 IS2T. All rights reserved.
 * This library is provided in source code for use, modification and test, subject to license terms.
 * Any modification of the source code will break IS2T warranties on the whole library.
 */
package com.microej.example.wifi.setup.rest;

import java.io.IOException;
import java.net.InetAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.microej.example.wifi.setup.DefaultConfigurationManager;

import ej.ecom.network.IPConfiguration;
import ej.ecom.wifi.SecurityMode;
import ej.ecom.wifi.SoftAPConfiguration;
import ej.net.util.NetUtil;

/**
 * Example of usage of a {@link RestSoftAPConnector}.
 */
public class Main {

	private static final boolean USE_DHCP = false;
	private static final String NET_MASK = "255.255.255.0"; //$NON-NLS-1$
	private static final String IP_ADDR = "192.168.0.1"; //$NON-NLS-1$
	private static final String SOFT_AP_SSID = "MicroEJ_SoftAP"; //$NON-NLS-1$
	private static final String SOFT_AP_PASSPHRASE = "qwertyuiop"; //$NON-NLS-1$
	private static final SecurityMode SOFT_AP_SECURITY = SecurityMode.WPA2;
	private static final Logger LOGGER = Logger.getAnonymousLogger();

	/**
	 * Entry Point.
	 *
	 * @param args
	 *            not used.
	 */
	public static void main(String[] args) {
		try {
			start(new RestSoftAPConnector(), IP_ADDR, NET_MASK, USE_DHCP, SOFT_AP_SSID, SOFT_AP_PASSPHRASE,
					SOFT_AP_SECURITY);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
	}

	/**
	 * Configures and starts the connector.
	 *
	 * @param connector
	 *            the Connector
	 * @param ipAddr
	 *            the IP adress.
	 * @param netMask
	 *            the netmask.
	 * @param useDhcp
	 *            Whether to use DHCP.
	 * @param softApSsid
	 *            SSID to display.
	 * @param softApPassphrase
	 *            Passphrase to use, <code>null</code> for none.
	 * @param softApSecurity
	 *            SecurityMode to use.
	 */
	public static void start(RestSoftAPConnector connector, String ipAddr, String netMask, boolean useDhcp,
			String softApSsid, String softApPassphrase, SecurityMode softApSecurity) {
		SoftAPConfiguration softAPConfiguration = new SoftAPConfiguration();
		softAPConfiguration.setSSID(softApSsid);
		if (softApPassphrase != null) {
			softAPConfiguration.setPassphrase(softApPassphrase);
		}
		softAPConfiguration.setSecurityMode(softApSecurity);

		DefaultConfigurationManager configurationManager = new DefaultConfigurationManager(softAPConfiguration);
		connector.setConfigurationManager(configurationManager);
		try {
			IPConfiguration iPConfiguration = new IPConfiguration();
			iPConfiguration.setGateway(InetAddress.getByName(ipAddr));
			iPConfiguration.setNetmask(InetAddress.getByName(netMask));
			iPConfiguration.useDHCP(useDhcp);
			connector.setIPConfiguration(null, iPConfiguration);
			connector.start();
			InetAddress hostAddress = NetUtil.getFirstHostAddress();
			String ip = (hostAddress != null) ? hostAddress.getHostName() : "unknown"; //$NON-NLS-1$
			LOGGER.info("Server started on " + ip + ':' + connector.getServerPort()); //$NON-NLS-1$
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
	}

}
