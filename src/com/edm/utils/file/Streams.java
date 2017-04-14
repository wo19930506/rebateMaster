package com.edm.utils.file;

import java.io.InputStream;

public class Streams {

	private String charset;
	private InputStream inputStream;

	public Streams(String charset, InputStream inputStream) {
		this.charset = charset;
		this.inputStream = inputStream;
	}

	public String getCharset() {
		return charset;
	}

	public InputStream getInputStream() {
		return inputStream;
	}
}