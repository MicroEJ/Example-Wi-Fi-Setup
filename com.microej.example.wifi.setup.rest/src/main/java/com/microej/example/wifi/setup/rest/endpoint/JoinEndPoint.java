/*
 * Java
 *
 * Copyright 2017-2022 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.example.wifi.setup.rest.endpoint;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Map;

import com.microej.example.wifi.setup.Messages;
import com.microej.example.wifi.setup.rest.RestSoftAPConnector;
import com.microej.example.wifi.setup.rest.internal.ApplicationStrings;

import ej.ecom.wifi.SecurityMode;
import ej.hoka.http.HTTPConstants;
import ej.hoka.http.HTTPRequest;
import ej.hoka.http.HTTPResponse;
import ej.net.util.wifi.AccessPointConfiguration;
import ej.net.util.wifi.SecurityUtil;
import ej.util.message.Level;

/**
 * A join endpoint, expecting a POST request.
 *
 * /join: post: description: join an accesspoint produces: - application/string urlencoded parameters: - SSID: the ssid
 * to connect to - securityMode (optional): the securitymode to use - passphrase (optional): The passphrase to use
 * responses: 202: description: Request was successful, will try to join the AP after 1s, this will close the server.
 * type: object description: the current AP joined. channel: type: integer description: the channel of the access point,
 * rssi: type: float description: the rssi of the access point, bssid: type: String description: the bssid of the access
 * point in hex, separated by ':', SSID: type: String description: the SSID of the access point, securityMode: type:
 * String description: the security mode of the access point, 400: description: SSID was not found. 429: description:
 * Server is busy.
 *
 */
public class JoinEndPoint extends SoftAPEndpoint {

	private static final String JOIN = "/join"; //$NON-NLS-1$

	/**
	 * Instantiates a {@link JoinEndPoint}.
	 *
	 * @param connector
	 *            the connector to use.
	 */
	public JoinEndPoint(RestSoftAPConnector connector) {
		super(JOIN, connector);
	}

	/**
	 * Posts request, expecting parameters - {@value ApplicationStrings#SSID} describing the ssid. - optionnal:
	 * {@value ApplicationStrings#PASSPHRASE}.
	 */
	@Override
	public HTTPResponse doPost(HTTPRequest request, Map<String, String> headers, Map<String, String> parameters) {
		HTTPResponse response;
		String ssid = parameters.get(ApplicationStrings.SSID);
		if (ssid != null) {
			String passphrase = parameters.get(ApplicationStrings.PASSPHRASE);
			SecurityMode security;
			try {
				security = SecurityUtil.getSecurityMode(
						URLDecoder.decode(parameters.get(ApplicationStrings.SECURITY), ApplicationStrings.UTF_8));
				final AccessPointConfiguration configuration = new AccessPointConfiguration(
						URLDecoder.decode(ssid, ApplicationStrings.UTF_8));
				if (passphrase != null) {
					configuration.setPassphrase(URLDecoder.decode(passphrase, ApplicationStrings.UTF_8));
				}
				configuration.setSecurityMode(security);
				this.connector.triggerJoin(configuration);
			} catch (UnsupportedEncodingException e) {
				Messages.LOGGER.log(Level.SEVERE, Messages.CATEGORY, Messages.ERROR_FAILED_TO_JOIN, e);
			}
			response = new HTTPResponse(this.connector.getJoined());
			response.setStatus(ACCEPTED);
		} else {
			response = new HTTPResponse(
					ApplicationStrings.ERROR_PARAMETER + ApplicationStrings.SSID + ApplicationStrings.IS_REQUIRED);
			response.setStatus(HTTPConstants.HTTP_STATUS_BADREQUEST);
		}
		response.addHeaderField(HTTPConstants.FIELD_CONNECTION, HTTPConstants.CONNECTION_FIELD_VALUE_KEEP_ALIVE);
		return response;
	}
}
