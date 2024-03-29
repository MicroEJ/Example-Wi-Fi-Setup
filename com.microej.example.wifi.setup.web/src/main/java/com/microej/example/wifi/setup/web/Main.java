/*
 * Java
 *
 * Copyright 2018-2022 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.example.wifi.setup.web;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import ej.ecom.wifi.SecurityMode;

/**
 * Entry point.
 */
public class Main {

	private static final String NET_MASK = "255.255.255.0"; //$NON-NLS-1$
	private static final String IP_ADDR = "192.168.0.1"; //$NON-NLS-1$
	private static final String SOFT_AP_SSID = "MicroEJ_SoftAP"; //$NON-NLS-1$
	private static final String SOFT_AP_PASSPHRASE = "qwertyuiop"; //$NON-NLS-1$
	private static final boolean USE_DHCP = true;
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
			com.microej.example.wifi.setup.rest.Main.start(new WebSoftAPConnector(), IP_ADDR, NET_MASK, USE_DHCP,
					SOFT_AP_SSID, SOFT_AP_PASSPHRASE, SOFT_AP_SECURITY);
		} catch (IOException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		}
	}
}
