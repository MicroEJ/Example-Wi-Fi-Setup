/*
 * Java
 *
 * Copyright 2018-2022 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package ej.restserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A server that can be paused.
 */
public class RestartableServer {

	private RestServer server;
	private final int port;
	private final int maxSocket;
	private final int maxJob;
	private final List<RestEndpoint> endpoints;

	/**
	 * Instantiates a {@link RestartableServer}.
	 *
	 * @param port
	 *            the port to listen.
	 * @param maxSocket
	 *            the maximum socket.
	 * @param maxJob
	 *            the maxiumum of simultaneous jobs.
	 */
	public RestartableServer(int port, int maxSocket, int maxJob) {
		this.port = port;
		this.maxSocket = maxSocket;
		this.maxJob = maxJob;
		this.endpoints = new ArrayList<>();
	}

	/**
	 * Starts the server.
	 *
	 * @throws IOException
	 *             if it could not creat the server.
	 */
	public synchronized void start() throws IOException {
		if (this.server == null) {
			this.server = new RestServer(this.port, this.maxSocket, this.maxJob);
			for (RestEndpoint restEndpoint : this.endpoints) {
				this.server.addEndpoint(restEndpoint);
			}
			this.server.start();
		}
	}

	/**
	 * Pause the server.
	 */
	public synchronized void pause() {
		if (this.server != null) {
			this.server.stop();
			this.server = null;
		}
	}

	/**
	 * Add the endpoint.
	 *
	 * @param endPoint
	 *            the endpoint.
	 */
	public synchronized void addEndpoint(RestEndpoint endPoint) {
		this.endpoints.add(endPoint);
		if (this.server != null) {
			this.server.addEndpoint(endPoint);
		}
	}

	/**
	 * Stops the server.
	 */
	public synchronized void stop() {
		pause();
		this.endpoints.clear();
	}
}
