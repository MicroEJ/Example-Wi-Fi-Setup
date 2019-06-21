/*
 * Java
 *
 * Copyright 2017-2018 IS2T. All rights reserved.
 * This library is provided in source code for use, modification and test, subject to license terms.
 * Any modification of the source code will break IS2T warranties on the whole library.
 */
package com.microej.example.wifi.setup.rest.internal;

import ej.ecom.wifi.AccessPoint;
import ej.ecom.wifi.SecurityMode;
import ej.net.util.wifi.SecurityUtil;

/**
 * A JSON view of an {@link AccessPoint}.
 */
public class AccessPointJSON {

	private final String json;

	/**
	 * Instantiates an {@link AccessPointJSON}.
	 *
	 * @param accessPoint
	 *            the access point to use.
	 */
	public AccessPointJSON(AccessPoint accessPoint) {
		this(accessPoint.getSSID(), accessPoint.getChannel(), accessPoint.getSecurityMode(), accessPoint.getRSSI(),
				accessPoint.getBSSID());
	}

	/**
	 * Instantiates an {@link AccessPointJSON}.
	 *
	 * @param ssid
	 *            the SSID
	 * @param channel
	 *            the channel
	 * @param mode
	 *            the mode
	 * @param rssi
	 *            the rssi
	 * @param bssid
	 *            the bssid
	 *
	 *
	 */
	public AccessPointJSON(String ssid, int channel, SecurityMode mode, float rssi, byte[] bssid) {
		StringBuilder builder = new StringBuilder();
		builder.append('{');
		JSONHelper.append(builder, ApplicationStrings.SSID, ssid).append(',');
		JSONHelper.append(builder, ApplicationStrings.SECURITY, SecurityUtil.getSecurityModeText(mode)).append(',');
		JSONHelper.append(builder, ApplicationStrings.CHANNEL, channel).append(',');
		JSONHelper.append(builder, ApplicationStrings.RSSI, rssi).append(',');
		JSONHelper.append(builder, ApplicationStrings.BSSID, JSONHelper.toByteString(bssid)).append('}');
		this.json = builder.toString();
	}

	@Override
	public String toString() {
		return this.json;
	}
}
