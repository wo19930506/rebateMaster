package com.edm.utils.file;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.regex.Matcher;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import au.com.bytecode.opencsv.CSVReader;

import com.edm.utils.Asserts;
import com.edm.utils.Converts;
import com.edm.utils.except.Errors;
import com.edm.utils.web.R;
import com.edm.utils.web.Validates;
import com.edm.utils.web.Validator;
import com.google.common.collect.Lists;

public class Readers {

	private static final Logger logger = LoggerFactory.getLogger(Readers.class);

	public static String[] lines(String path, int lineCount) {
		String suff = Files.suffix(path);
		String[] lines = null;

		try {
			if (suff.equals(Suffix.TXT)) {
				lines = txt(new FileInputStream(path), "UTF-8", lineCount);
			} else if (suff.equals(Suffix.CSV)) {
				lines = csv(new FileInputStream(path), "UTF-8", lineCount);
			} else if (suff.equals(Suffix.XLS) || suff.equals(Suffix.XLSX)) {
				lines = excel(Files.suffix(path), new FileInputStream(path), lineCount);
			} else {
				throw new Errors("文件后缀不是合法值");
			}

			return lines;
		} catch (Exception e) {
			logger.error("(Readers:lines) error: ", e);
			throw new Errors(e.getMessage(), e);
		}
	}
	
	public static String[] lines(MultipartFile file, String encoding, int lineCount) {
		String[] lines = null;
		String suff = Files.suffix(file.getOriginalFilename());
		try {
			if (suff.equals(Suffix.TXT)) {
				lines = txt(file.getInputStream(), encoding, lineCount);
			} else if (suff.equals(Suffix.CSV)) {
				lines = csv(file.getInputStream(), encoding, lineCount);
			} else if (suff.equals(Suffix.XLS) || suff.equals(Suffix.XLSX)) {
				lines = excel(suff, file.getInputStream(), lineCount);
			} else {
				throw new Errors("文件后缀不是合法值");
			}
			return lines;
		} catch (Exception e) {
			logger.error("(Readers:lines) error: ", e);
			throw new Errors(e.getMessage(), e);
		}
	}
	
	public static String html(InputStream input, String encoding) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(input, encoding));

			StringBuffer sbff = new StringBuffer();
			String line = null;
			
			String regex = "url(\\s*)\\((\\s*)\\)"; // 匹配url(   ) 或  url   () 或  url (   )
			while ((line = reader.readLine()) != null) {
				Matcher m = Validates.pattern(line, regex);
				while(m.find()) {
					String urlMethod = m.group();
					if(line.contains(urlMethod)) {
						line = StringUtils.replace(line, urlMethod, "");
					}
				}
				sbff.append(line);
			}

			return sbff.toString();
		} catch (Exception e) {
			logger.error("(Readers:html) error: ", e);
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
	
	private static String[] txt(InputStream input, String encoding, int lineCount) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(input, encoding));

			String[] lines = new String[lineCount];
			String line = null;
			
			int count = 0;
			while ((line = reader.readLine()) != null) {
				if (Asserts.empty(StringUtils.splitPreserveAllTokens(line, ","))) {
					continue;
				}
				if ((count + 1) > lineCount) {
					break;
				}
				
				Validator.validate(line, R.CLEAN, "文件数据");
				
				lines[count] = line;
				count++;
			}

			if (count < lineCount) {
				throw new Errors("文件数据不能少于" + lineCount + "行");
			}
			
			return lines;
		} catch (Exception e) {
			logger.error("(Readers:txt) error: ", e);
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

	private static String[] csv(InputStream input, String encoding, int lineCount) {
		CSVReader reader = null;
		try {
			reader = new CSVReader(new BufferedReader(new InputStreamReader(input, encoding)));

			String[] lines = new String[lineCount];
			String[] nextLine = null;

			int count = 0;
			while ((nextLine = reader.readNext()) != null) {
				if (Asserts.empty(nextLine)) {
					continue;
				}
				if ((count + 1) > lineCount) {
					break;
				}

				String line = Converts.toString(nextLine);
				Validator.validate(line, R.CLEAN, "文件数据");

				lines[count] = line;
				count++;
			}

			if (count < lineCount) {
				throw new Errors("文件数据不能少于" + lineCount + "行");
			}
			
			return lines;
		} catch (Exception e) {
			logger.error("(Readers:csv) error: ", e);
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

	private static String[] excel(String suff, InputStream input, int lineCount) {
		try {
			String[] lines = new String[lineCount];
			List<String> nextLines = Lists.newArrayList();

			List<List<Data>> rowList = Lists.newArrayList();
			Excels reader = new Excels(suff, input);
			reader.read(rowList, 0, 20);
			
			int count = 0;
			for (List<Data> cellList : rowList) {
				if (Asserts.empty(cellList)) {
					continue;
				}
				if ((count + 1) > lineCount) {
					break;
				}
				for (int i = 0; i < cellList.size(); i++) {
					String value = Excels.value(cellList.get(i));
					nextLines.add(value);
				}
				
				String line = Converts.toString(nextLines);
				Validator.validate(line, R.CLEAN, "文件数据");

				lines[count] = line;
				nextLines.clear();
				count++;
			}
			
			if (count < lineCount) {
				throw new Errors("文件数据不能少于" + lineCount + "行");
			}
			
			return lines;
		} catch (Exception e) {
			logger.error("(Readers:excel) error: ", e);
			throw new Errors(e.getMessage(), e);
		}
	}
}
