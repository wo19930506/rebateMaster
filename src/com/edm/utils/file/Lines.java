package com.edm.utils.file;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import au.com.bytecode.opencsv.CSVReader;

import com.edm.utils.MailBox;
import com.edm.utils.except.Errors;
import com.google.common.collect.Lists;

public class Lines {

	private static final Logger logger = LoggerFactory.getLogger(Lines.class);

	public static int count(MultipartFile upload, String encoding) {
		try {
			String suff = Files.suffix(upload.getOriginalFilename());
			if (suff.equals(Suffix.TXT)) {
				return txt(upload.getInputStream(), encoding);
			} else if (suff.equals(Suffix.CSV)) {
				return csv(upload.getInputStream(), encoding);
			} else if (suff.equals(Suffix.XLS) || suff.equals(Suffix.XLSX)) {
				return excel(upload.getInputStream(), suff);
			} else {
				throw new Errors("文件后缀不是合法值");
			}
		} catch (IOException e) {
			logger.error("(Lines:count) error: ", e);
			throw new Errors("(Lines:count) error: ", e);
		} catch (Errors e) {
			throw new Errors("(Lines:count) error: ", e);
		}
	}
	
	public static int count(String filePath, String encoding)  {
		try {
			String suff = Files.suffix(filePath);
			if (suff.equals(Suffix.TXT)) {
				return txt(new FileInputStream(filePath), encoding);
			} else if (suff.equals(Suffix.CSV)) {
				return csv(new FileInputStream(filePath), encoding);
			} else if (suff.equals(Suffix.XLS) || suff.equals(Suffix.XLSX)) {
				return excel(new FileInputStream(filePath), suff);
			} else {
				throw new Errors("文件后缀不是合法值");
			}
		} catch (IOException e) {
			logger.error("(Lines:count) error: ", e);
			throw new Errors("(Lines:count) error: ", e);
		} catch (Errors e) {
			throw new Errors("(Lines:count) error: ", e);
		}
	}

	private static int txt(InputStream input, String encoding) {
		int row = 0;
		int count = 0;
		
		boolean isMail = false;
		
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(input, encoding));
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (count == 0) {
					for (String value : StringUtils.splitPreserveAllTokens(line, ",")) {
						if (MailBox.validate(value)) {
							isMail = true;
							break;
						}
					}
				}
				if (StringUtils.isNotBlank(line)) {
					count++;
				}
			}
		} catch (Exception e) {
			logger.error("(Lines:txt) error: ", e);
			throw new Errors("文件格式有误", e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) { }
			}
		}
		
		row = (isMail ? count : count - 1);
		return (row < 0 ? 0 : row);
	}
	
	private static int csv(InputStream input, String encoding) {
		int row = 0;
		int count = 0;
		
		boolean isMail = false;
		
		CSVReader reader = null;
		try {
			reader = new CSVReader(new BufferedReader(new InputStreamReader(input, encoding)));
			String[] nextLine = null;
			while ((nextLine = reader.readNext()) != null) {
				boolean blank = true;
				for (String value : nextLine) {
					if (StringUtils.isNotBlank(value)) {
						if (count == 0) {
							if (MailBox.validate(value)) {
								isMail = true;
								blank = false;
								break;
							}
						}
						blank = false;
						break;
					}
				}
				if (!blank) {
					count++;
				}
			}
		} catch (Exception e) {
			logger.error("(Lines:csv) error: ", e);
			throw new Errors("文件格式有误", e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) { }
			}
		}
		
		row = (isMail ? count : count - 1);
		return (row < 0 ? 0 : row);
	}
	
	private static int excel(InputStream input, String suff) {
		int row = 0;
		int count = 0;
		
		boolean isMail = false;
		
		try {
			List<List<Data>> rowList = Lists.newArrayList();
			Excels reader = new Excels(suff, input);
			reader.read(rowList, 0, -1);
			
			for (List<Data> cellList : rowList) {
				boolean blank = true;
				for (Data data : cellList) {
					String value = Excels.value(data);
					if (StringUtils.isNotBlank(value)) {
						if (count == 0) {
							if (MailBox.validate(value)) {
								isMail = true;
								blank = false;
								break;
							}
						}
						blank = false;
						break;
					}
				}
				if (!blank) {
					count++;
				}
			}
		} catch (Exception e) {
			logger.error("(Lines:excel) error: ", e);
			throw new Errors("文件格式有误", e);
		}
		
		row = (isMail ? count : count - 1);
		return (row < 0 ? 0 : row);
	}
}
