package com.edm.utils.execute;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import com.edm.utils.Asserts;
import com.edm.utils.except.Errors;

public class Streams {

	public static void closeOutput(LinkedList<FileOutputStream> foss, LinkedList<BufferedWriter> bws) {
		try {
			if (!Asserts.empty(bws)) {
				for (Iterator<BufferedWriter> iter = bws.iterator(); iter.hasNext();) {
					BufferedWriter writer = iter.next();
					writer.close();
				}
			}
			if (!Asserts.empty(foss)) {
				for (Iterator<FileOutputStream> iter = foss.iterator(); iter.hasNext();) {
					FileOutputStream writer = iter.next();
					writer.close();
				}
			}
		} catch (IOException e) {
			throw new Errors("不可能抛出的异常", e);
		}
	}
	
	public static void closeInput(LinkedList<FileInputStream> fiss, LinkedList<BufferedReader> brs) {
		try {
			if (!Asserts.empty(brs)) {
				for (Iterator<BufferedReader> iter = brs.iterator(); iter.hasNext();) {
					BufferedReader writer = iter.next();
					writer.close();
				}
			}
			if (!Asserts.empty(fiss)) {
				for (Iterator<FileInputStream> iter = fiss.iterator(); iter.hasNext();) {
					FileInputStream writer = iter.next();
					writer.close();
				}
			}
		} catch (IOException e) {
			throw new Errors("不可能抛出的异常", e);
		}
	}
}
