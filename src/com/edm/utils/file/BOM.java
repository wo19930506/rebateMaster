package com.edm.utils.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

public class BOM {

	private static final int BOM_SIZE = 4;

	public static Streams charset(InputStream stream) throws IOException {
		PushbackInputStream pbis = new PushbackInputStream(stream, BOM_SIZE);
		
		String encoding = null;

		byte[] bom = new byte[BOM_SIZE];
		int len, b;
		len = pbis.read(bom, 0, bom.length);
		
		if ((bom[0] == (byte) 0x00) && (bom[1] == (byte) 0x00) && (bom[2] == (byte) 0xFE) && (bom[3] == (byte) 0xFF)) {
			encoding = "UTF-32BE";
			b = len - 4;
		} else if ((bom[0] == (byte) 0xFF) && (bom[1] == (byte) 0xFE) && (bom[2] == (byte) 0x00) && (bom[3] == (byte) 0x00)) {
			encoding = "UTF-32LE";
			b = len - 4;
		} else if ((bom[0] == (byte) 0xEF) && (bom[1] == (byte) 0xBB) && (bom[2] == (byte) 0xBF)) {
			encoding = "UTF-8";
			b = len - 3;
		} else if ((bom[0] == (byte) 0xFE) && (bom[1] == (byte) 0xFF)) {
			encoding = "UTF-16BE";
			b = len - 2;
		} else if ((bom[0] == (byte) 0xFF) && (bom[1] == (byte) 0xFE)) {
			encoding = "UTF-16LE";
			b = len - 2;
		} else {
			encoding = "GB2312";
			b = len;
		}
		
		if (b > 0) {
			pbis.unread(bom, (len - b), b);
		}

		return new Streams(encoding, pbis);
	}
}
