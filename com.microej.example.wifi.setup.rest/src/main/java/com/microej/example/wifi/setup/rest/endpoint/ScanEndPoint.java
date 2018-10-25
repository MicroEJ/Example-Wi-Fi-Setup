/*
 * Java
 *
 * Copyright 2017-2018 IS2T. All rights reserved.
 * This library is provided in source code for use, modification and test, subject to license terms.
 * Any modification of the source code will break IS2T warranties on the whole library.
 */
package com.microej.example.wifi.setup.rest.endpoint;

import java.util.Hashtable;

import com.microej.example.wifi.setup.SoftAPConnector;
import com.microej.example.wifi.setup.rest.RestSoftAPConnector;

import ej.hoka.http.HTTPConstants;
import ej.hoka.http.HTTPRequest;
import ej.hoka.http.HTTPResponse;

/**
 * The endpoint to receive a scan of the available wifi.
 * 
 *  /scan:
 *      post:
 *          description: Return the list of available wifi
 *          produces:
 *              - application/json
 *          responses:
 *              200:
 *                  description: Request was successful
 *              429:
 *                  description: Server is busy.
 */
public class ScanEndPoint extends SoftAPEndpoint {

	private static final String SCAN = "/scan"; //$NON-NLS-1$
	
	/**
	 * Instantiates a {@link ScanEndPoint}.
	 *
	 * @param connector
	 *            the {@link SoftAPConnector} to use.
	 * @throws IllegalArgumentException
	 *             if uri is <code>null</code>.
	 */
	public ScanEndPoint(RestSoftAPConnector connector) throws IllegalArgumentException {
		super(SCAN, connector);
	}

	@Override
	public HTTPResponse doPost(HTTPRequest request, Hashtable<String, String> headers,
			Hashtable<String, String> parameters) {
		HTTPResponse response;
		this.connector.triggerUpdateAccess();
		response = new HTTPResponse(this.connector.getAccesses());
		response.addHeaderField(HTTPConstants.FIELD_CONNECTION, HTTPConstants.CONNECTION_FIELD_VALUE_KEEP_ALIVE);
		response.setStatus(ACCEPTED);
		return response;
	}
}
