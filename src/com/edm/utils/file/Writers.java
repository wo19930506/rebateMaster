package com.edm.utils.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

import com.edm.utils.Converts;
import com.edm.utils.except.Errors;
import com.edm.utils.web.R;
import com.edm.utils.web.Validator;
import com.google.common.collect.Lists;

public class Writers {

	private static final Logger logger = LoggerFactory.getLogger(Writers.class);
	
	public static void txt(String bom, String charset, InputStream input, OutputStream output) throws IOException {
		BufferedReader reader = null;
		PrintWriter writer = null;
		try {
			if (bom.equals("UTF-8")) {
				Streams streams = BOM.charset(input);
				reader = new BufferedReader(new InputStreamReader(streams.getInputStream(), "UTF-8"));
			} else {
				reader = new BufferedReader(new InputStreamReader(input, bom));
			}
			writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(output, charset)));
			String line = null;
			while ((line = reader.readLine()) != null) {
				Validator.validate(line, R.CLEAN, "文件数据");
				
				writer.write(line);
				writer.write("\r\n");
			}
			writer.close();
		} catch (Exception e) {
			logger.error("(Writers:txt) error: ", e);
			throw new Errors(e.getMessage(), e);
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
			}
		}
	}
	
	public static void csv(String bom, String charset, InputStream input, OutputStream output) throws IOException {
		CSVReader reader = null;
		CSVWriter writer = null;
		try {
			if (bom.equals("UTF-8")) {
				Streams streams = BOM.charset(input);
				reader = new CSVReader(new BufferedReader(new InputStreamReader(streams.getInputStream(), "UTF-8")));
			} else {
				reader = new CSVReader(new BufferedReader(new InputStreamReader(input, bom)));
			}
			
			writer = new CSVWriter(new BufferedWriter(new OutputStreamWriter(output, charset)), ',', CSVWriter.NO_QUOTE_CHARACTER);
			
			String[] nextLine = null;
			while ((nextLine = reader.readNext()) != null) {
				String line = Converts.toString(nextLine);
				Validator.validate(line, R.CLEAN, "文件数据");
				
				writer.writeNext(nextLine);
			}
			writer.close();
		} catch (Exception e) {
			logger.error("(Writers:csv) error: ", e);
			throw new Errors(e.getMessage(), e);
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
			}
		}
	}
	
	public static void excel(String suff, InputStream input, OutputStream output) throws IOException {
		try {
			List<List<Data>> rowList = Lists.newArrayList();
			Excels reader = new Excels(suff, input);
			Excels writer = new Excels(suff);
			String sheetName = reader.read(rowList, 0, -1);
			writer.write(output, rowList, sheetName);
		} catch (Exception e) {
			logger.error("(Writers:excel) error: ", e);
			throw new Errors(e.getMessage(), e);
		} 
	}
}
