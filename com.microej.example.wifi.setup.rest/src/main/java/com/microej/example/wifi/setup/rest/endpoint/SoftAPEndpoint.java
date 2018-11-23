/*
 * Java
 *
 * Copyright 2018 IS2T. All rights reserved.
 * This library is provided in source code for use, modification and test, subject to license terms.
 * Any modification of the source code will break IS2T warranties on the whole library.
 */
package com.microej.example.wifi.setup.rest.endpoint;

import java.util.Map;

import com.microej.example.wifi.setup.rest.RestSoftAPConnector;
import com.microej.example.wifi.setup.rest.RestSoftAPConnector.STATE;
import com.microej.example.wifi.setup.rest.internal.ApplicationStrings;

import ej.hoka.http.HTTPConstants;
import ej.hoka.http.HTTPRequest;
import ej.hoka.http.HTTPResponse;
import ej.restserver.RestEndpoint;

/**
 * The endpoint managing the connector status.
 *
 * /scan: 
 * 		post: 
 * 			description: 
 * 			produces: 
 * 				- application/json 
 * 			responses: 
 * 				200: 
 * 					description: Request was successful 
 * 				429:
 * 					description: Server is busy.
 */	
public abstract class SoftAPEndpoint extends RestEndpoint {

	/**
	 * 202 Accepted.
	 */
	public static final String ACCEPTED = "202 Accepted"; //$NON-NLS-1$

	/**
	 * 429 Too Many Requests.
	 */
	public static final String TOO_MANY = "429 Too Many Requests"; //$NON-NLS-1$

	/**
	 * The rest connector providing the endpoint.
	 */
	protected final RestSoftAPConnector connector;

	/**
	 * Instantiates a {@link SoftAPEndpoint}.
	 *
	 * @param uri
	 *            the URI.
	 * @param connector
	 *            the restConnector
	 * @throws IllegalArgumentException
	 *             tf the URI is false.
	 */
	public SoftAPEndpoint(String uri, RestSoftAPConnector connector) throws IllegalArgumentException {
		super(uri);
		this.connector = connector;
	}

	@Override
	public synchronized HTTPResponse post(HTTPRequest request, Map<String, String> headers,
			Map<String, String> parameters) {
		HTTPResponse response;
		synchronized (this.connector) {
			if (this.connector.getState() == STATE.READY) {
				response = doPost(request, headers, parameters);
				response.addHeaderField(HTTPConstants.FIELD_CONTENT_TYPE, ApplicationStrings.APPLICATION_JSON);
			} else {
				response = new HTTPResponse();
				response.setStatus(TOO_MANY);
			}
		}
		return response;
	}

	@Override
	public HTTPResponse get(HTTPRequest request, Map<String, String> headers, Map<String, String> parameters) {
		HTTPResponse response = super.get(request, headers, parameters);
		return response;
	}

	/**
	 * Do the post when the server is ready.
	 *
	 * @param request
	 *            the request.
	 * @param headers
	 *            the headers.
	 * @param parameters
	 *            the parameters.
	 * @return a {@link HTTPResponse}, not <code>null</code>.
	 */
	protected abstract HTTPResponse doPost(HTTPRequest request, Map<String, String> headers,
			Map<String, String> parameters);
}
