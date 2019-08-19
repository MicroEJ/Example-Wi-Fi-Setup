/*
 * Java
 *
 * Copyright 2018-2019 MicroEJ Corp. All rights reserved.
 * For demonstration purpose only.
 * MicroEJ Corp. PROPRIETARY. Use is subject to license terms.
 */
package com.microej.example.wifi.setup.rest.internal;

/**
 * Helper to create a JSON string.
 */
public class JSONHelper {

	private JSONHelper() {
		// Forbid instantiation.
	}

	/**
	 * Appends a float value ("key":value) to a StringBuilder.
	 *
	 * @param builder
	 *            the {@link StringBuilder}.
	 * @param key
	 *            the key.
	 * @param value
	 *            the value.
	 * @return the StringBuilder.
	 */
	public static StringBuilder append(StringBuilder builder, String key, float value) {
		return appendString(builder, key).append(':').append(value);

	}

	/**
	 * Appends an int value ("key":value) to a StringBuilder.
	 *
	 * @param builder
	 *            the {@link StringBuilder}.
	 * @param key
	 *            the key.
	 * @param value
	 *            the value.
	 * @return the StringBuilder.
	 */
	public static StringBuilder append(StringBuilder builder, String key, int value) {
		return appendString(builder, key).append(':').append(value);

	}

	/**
	 * Appends a String value ("key":"value") to a StringBuilder.
	 *
	 * @param builder
	 *            the {@link StringBuilder}.
	 * @param key
	 *            the key.
	 * @param value
	 *            the value.
	 * @return the StringBuilder.
	 */
	public static StringBuilder append(StringBuilder builder, String key, String value) {
		appendString(builder, key).append(':');
		return appendString(builder, value);
	}

	/**
	 * Appends a String in double quote to a StringBuilder.
	 *
	 * @param builder
	 *            the {@link StringBuilder}.
	 * @param key
	 *            the key.
	 * @return the StringBuilder.
	 */
	public static StringBuilder appendString(StringBuilder builder, String key) {
		return builder.append('"').append(key).append('"');
	}

	/**
	 * Prints an array of byte into a String.
	 *
	 * @param bytes
	 *            the bytes.
	 * @return the String.
	 */
	public static String toByteString(byte[] bytes) {
		StringBuilder value = new StringBuilder();
		if (bytes != null) {
			int length = bytes.length - 1;
			for (int i = 0; i < length; i++) {
				String hexString = Integer.toHexString(bytes[i] & 0xFF);
				if (hexString.length() < 2) {
					value.append('0');
				}
				value.append(hexString).append(':');
			}
			value.append(bytes[length] & 0xFF);
		}
		return value.toString();
	}
}
