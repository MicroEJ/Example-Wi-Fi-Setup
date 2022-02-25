/*
 * Java
 *
 * Copyright 2018-2022 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.example.wifi.setup.rest.endpoint;

import java.util.Map;

import com.microej.example.wifi.setup.rest.RestSoftAPConnector;
import com.microej.example.wifi.setup.rest.RestSoftAPConnector.STATE;
import com.microej.example.wifi.setup.rest.internal.ApplicationStrings;
import com.microej.example.wifi.setup.rest.internal.JSONHelper;

import ej.hoka.http.HTTPConstants;
import ej.hoka.http.HTTPRequest;
import ej.hoka.http.HTTPResponse;
import ej.restserver.RestEndpoint;

/**
 * The endpoint to receive a diagnostic.
 *
 * /diagnostic: get: description: Gets the states of the server. produces: - application/json responses: 200:
 * description: Request was successful schema: type: object properties: accesses: type: array description: list of the
 * available end point channel: type: integer description: the channel of the access point, rssi: type: float
 * description: the rssi of the access point, bssid: type: String description: the bssid of the access point in hex,
 * separated by ':', SSID: type: String description: the SSID of the access point, securityMode: type: String
 * description: the security mode of the access point, status: type: object description: the server status (ready or
 * busy) joined: type: object description: the current AP joined. channel: type: integer description: the channel of the
 * access point, rssi: type: float description: the rssi of the access point, bssid: type: String description: the bssid
 * of the access point in hex, separated by ':', SSID: type: String description: the SSID of the access point,
 * securityMode: type: String description: the security mode of the access point,
 */
public class DiagnosticEndPoint extends RestEndpoint {

	private static final String DIAGNOSTIC = "/diagnostic"; //$NON-NLS-1$
	private final RestSoftAPConnector connector;

	/**
	 * Instantiates a {@link DiagnosticEndPoint}.
	 *
	 * @param restSoftAPConnector
	 *            the connector.
	 * @throws IllegalArgumentException
	 *             if the URI is invalid.
	 */
	public DiagnosticEndPoint(RestSoftAPConnector restSoftAPConnector) throws IllegalArgumentException {
		super(DIAGNOSTIC);
		this.connector = restSoftAPConnector;
	}

	@Override
	public HTTPResponse get(HTTPRequest request, Map<String, String> headers, Map<String, String> parameters) {
		HTTPResponse response;
		String status = ApplicationStrings.READY;
		if (this.connector.getState() == STATE.BUSY) {
			status = ApplicationStrings.BUSY;
		}
		StringBuilder string = new StringBuilder().append('{');
		JSONHelper.append(string, ApplicationStrings.STATUS, status).append(',');
		JSONHelper.appendString(string, ApplicationStrings.ACCESSES).append(':').append(this.connector.getAccesses())
				.append(',');
		JSONHelper.appendString(string, ApplicationStrings.JOINED).append(':').append(this.connector.getJoined());
		string.append('}');

		response = new HTTPResponse(string.toString());
		response.addHeaderField(HTTPConstants.FIELD_CONTENT_TYPE, ApplicationStrings.APPLICATION_JSON);
		response.setStatus(HTTPConstants.HTTP_STATUS_OK);
		return response;
	}

}
