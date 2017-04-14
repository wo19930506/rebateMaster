package com.edm.modules.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

/**
 * 封装各种格式的编码解码工具类.
 * 1. Commons-Codec的hex/base64 编码
 * 2. JDK提供的URLEncoder
 * 
 * @author yjli
 */
public abstract class Encodes {

	private static final String DEFAULT_ENCODING = "UTF-8";
	
	/**
	 * Hex编码, byte[] -> String.
	 */
	public static String encodeHex(byte[] input) {
		return Hex.encodeHexString(input);
	}
	
	/**
	 * Hex解码, String -> byte[].
	 */
	public static byte[] decodeHex(String input) {
		try {
			return Hex.decodeHex(input.toCharArray());
		} catch (DecoderException e) {
			throw new IllegalStateException("Hex Decoder exception", e);
		}
	}

	/**
	 * Base64编码, byte[] -> String.
	 */
	public static String encodeBase64(byte[] input) {
		return Base64.encodeBase64String(input);
	}
	
	/**
	 * Base64编码, URL安全.
	 */
	public static String encodeBase64URLSafe(byte[] input) {
		return Base64.encodeBase64URLSafeString(input);
	}
	
	/**
	 * Base64解码, String -> byte[].
	 */
	public static byte[] decodeBase64(String input) {
		return Base64.decodeBase64(input);
	}
	
	/**
	 * URL编码, Encode默认为UTF-8.
	 */
	public static String urlEncode(String part) {
		try {
			return URLEncoder.encode(part, DEFAULT_ENCODING);
		} catch (UnsupportedEncodingException e) {
			throw Exceptions.unchecked(e);
		}
	}

	/**
	 * URL解码, Encode默认为UTF-8.
	 */
	public static String urlDecode(String part) {
		try {
			return URLDecoder.decode(part, DEFAULT_ENCODING);
		} catch (UnsupportedEncodingException e) {
			throw Exceptions.unchecked(e);
		}
	}
}
