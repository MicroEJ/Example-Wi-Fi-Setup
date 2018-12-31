/*
 * Java
 *
 * Copyright 2018 IS2T. All rights reserved.
 * This library is provided in source code for use, modification and test, subject to license terms.
 * Any modification of the source code will break IS2T warranties on the whole library.
 */
package com.microej.example.wifi.setup.web;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Set;

import com.microej.example.wifi.setup.ConfigurationManager;
import com.microej.example.wifi.setup.SoftAPConnector;
import com.microej.example.wifi.setup.rest.RestSoftAPConnector;

import ej.restserver.RestartableServer;
import ej.restserver.endpoint.GzipResourceEndpoint;
import ej.restserver.endpoint.ResourceRestEndpoint;

/**
 * An HTTP server providing a web view to manage the {@link SoftAPConnector}.
 */
public class WebSoftAPConnector extends RestSoftAPConnector {

	private static final String SLASH = "/"; //$NON-NLS-1$
	private static final String HOME_RESOURCE = SLASH + "index.html"; //$NON-NLS-1$
	private static final String SERVER_RESOURCES_PATH = "/com/microej/example/wifi/setup/web/"; //$NON-NLS-1$
	private static final String SERVER_STATIC_ENDPOINT_FILENAME = "static.resources.list"; //$NON-NLS-1$

	/**
	 * Instantiates a {@link WebSoftAPConnector}.
	 *
	 * @throws IOException
	 *             When initialisation fails.
	 */
	public WebSoftAPConnector() throws IOException {
		super();
	}

	/**
	 * Instantiates a {@link WebSoftAPConnector}.
	 *
	 * @param port
	 *            the server port.
	 * @throws IOException
	 *             When initialisation fails.
	 */
	public WebSoftAPConnector(int port) throws IOException {
		super(port);
	}

	/**
	 * Instantiates a {@link WebSoftAPConnector} with a a {@link ConfigurationManager}..
	 *
	 * @param config
	 *            the configuration manager.
	 * @throws IOException
	 *             When initialisation fails.
	 */
	public WebSoftAPConnector(ConfigurationManager config) throws IOException {
		super(config);
	}

	/**
	 * Instantiates a {@link WebSoftAPConnector} with a a {@link ConfigurationManager}..
	 *
	 * @param port
	 *            the server port.
	 * @param config
	 *            the configuration manager.
	 * @throws IOException
	 *             When initialisation fails.
	 */
	public WebSoftAPConnector(ConfigurationManager config, int port) throws IOException {
		super(config, port);
	}

	@Override
	protected void addEnpoints(RestartableServer server) throws IOException {
		try (InputStream staticFiles = WebSoftAPConnector.class
				.getResourceAsStream(SERVER_RESOURCES_PATH + SERVER_STATIC_ENDPOINT_FILENAME)) {
			createStaticEndpoints(server, staticFiles, HOME_RESOURCE, SERVER_RESOURCES_PATH);
		}
		super.addEnpoints(server);
	}

	private void createStaticEndpoints(RestartableServer server, InputStream resourceFile, String homePage,
			String baseResourceDir) throws IOException {
		Properties filesProperties = new Properties();
		filesProperties.load(resourceFile);

		Set<String> files = filesProperties.stringPropertyNames();
		for (String filePath : files) {
			filePath = filePath.trim();
			String endpoint = SLASH + filePath;
			if (filePath.startsWith(baseResourceDir)) {
				endpoint = SLASH + filePath.substring(baseResourceDir.length());
			}
			if (filePath.endsWith(GzipResourceEndpoint.GZIP_FILE_EXTENSION)) {
				endpoint = endpoint.substring(0, endpoint.length() - GzipResourceEndpoint.GZIP_FILE_EXTENSION.length());
				if (endpoint.equals(homePage)) {
					server.addEndpoint(new GzipResourceEndpoint(SLASH, filePath));
				}
				server.addEndpoint(new GzipResourceEndpoint(endpoint, filePath));
			} else {
				if (endpoint.equals(homePage)) {
					server.addEndpoint(new ResourceRestEndpoint(SLASH, filePath));
				}
				server.addEndpoint(new ResourceRestEndpoint(endpoint, filePath));
			}
		}
	}
}
