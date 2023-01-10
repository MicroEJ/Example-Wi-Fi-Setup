/*
 * Java
 *
 * Copyright 2018-2022 MicroEJ Corp. All rights reserved.
 * Use of this source code is governed by a BSD-style license that can be found with this software.
 */
package com.microej.example.wifi.setup;

import ej.util.message.MessageBuilder;
import ej.util.message.MessageLogger;
import ej.util.message.basic.BasicMessageBuilder;
import ej.util.message.basic.BasicMessageLogger;

/**
 * Gather the messages.
 */
public final class Messages {
	// ****************//
	// Error messages. //
	// ****************//
	/**
	 * Unknown error.
	 */
	public static final int ERROR_UNKNOWN = -255;

	/**
	 * Failed to join the network.
	 */
	public static final int ERROR_FAILED_TO_JOIN = -1;

	/**
	 * Failed to mount the network.
	 */
	public static final int ERROR_FAILED_TO_MOUNT = -2;
	/**
	 * Failed to unmount the network.
	 */
	public static final int ERROR_FAILED_TO_UNMOUNT = -3;

	/**
	 * Could not start the server.
	 */
	public static final int ERROR_START_SERVER = -4;

	/**
	 * Could not scan.
	 */
	public static final int ERROR_FAILED_TO_SCAN = -5;

	/**
	 * Could not access a value.
	 */
	public static final int ERROR_CHECKING_VALUE = -6;

	// ***************//
	// Info messages. //
	// ***************//

	/**
	 * Category message.
	 */
	public static final String CATEGORY = Messages.class.getPackage().getName();

	/**
	 * The message builder.
	 */
	public static final MessageBuilder BUILDER = new BasicMessageBuilder();

	/**
	 * The message logger.
	 */
	public static final MessageLogger LOGGER = new BasicMessageLogger(BUILDER);

	private Messages() {
		// Forbid instantiation
	}
}
