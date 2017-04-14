package com.edm.utils.file;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.edm.utils.except.Errors;
import com.edm.utils.web.R;
import com.edm.utils.web.Validator;
import com.google.common.collect.Maps;

public class Excels {

	private static final Logger logger = LoggerFactory.getLogger(Excels.class);
	
	public static final int D = 1; // date
	public static final int T = 2; // date time
	public static final int N = 3; // double
	public static final int S = 4; // string
	public static final int B = 5; // boolean

	private Workbook wb;
	private Map<Integer, CellStyle> styles;

	public Excels(String suff) {
		if (StringUtils.endsWithIgnoreCase(suff, Suffix.XLSX)) {
			wb = new SXSSFWorkbook(100);
		} else {
			wb = new HSSFWorkbook();
		}
	}

	public Excels(String suff, InputStream input) throws IOException {
		if (StringUtils.endsWithIgnoreCase(suff, Suffix.XLSX)) {
			wb = new XSSFWorkbook(input);
		} else {
			wb = new HSSFWorkbook(input);
		}
	}
	
	public String read(List<List<Data>> rowList, int sheetIndex, int line) throws IOException {
		Sheet sheet = wb.getSheetAt(sheetIndex);
		
		int count = 0;
		for (int i = sheet.getFirstRowNum(); i <= sheet.getLastRowNum(); i++) {
			Row row = sheet.getRow(i);
			if (row == null) {
				continue;
			}
			List<Data> cellList = new ArrayList<Data>();
			
			for (int j = 0; j < row.getLastCellNum(); j++) {
				cellList.add(getValue(row.getCell(j)));
			}
			rowList.add(cellList);
			if (line != -1 && count > line) {
				break;
			}
			count++;
		}
		
		return sheet.getSheetName();
	}

	public void write(OutputStream output, List<List<Data>> rowList, String sheetName) throws IOException {
		try {
			createStyles(wb);
			Sheet sheet = wb.createSheet(sheetName);
			for (int i = 0; i < rowList.size(); i++) {
				Row row = sheet.createRow(i);
				List<Data> cellList = rowList.get(i);
				for (int j = 0; j < cellList.size(); j++) {
					setValue(row.createCell(j), cellList.get(j));
					String value = value(cellList.get(j));
					Validator.validate(value, R.CLEAN, "文件数据");
				}
			}

			wb.write(output);
			output.flush();

		} catch (Errors e) {
			logger.error("(Excels:write) error: ", e);
			throw new Errors(e.getMessage(), e);
		} finally {
			output.close();
		}
	}

	private Data getValue(Cell cell) {
		Data data = null;
		if (cell != null) {
			int clazz = cell.getCellType();
			if (clazz == Cell.CELL_TYPE_NUMERIC) {
				short fmt = cell.getCellStyle().getDataFormat();
				if (DateUtil.isCellDateFormatted(cell)) {
					data = new Data((fmt == 14 ? D : T), cell.getDateCellValue());
				} else {
					double numeric = cell.getNumericCellValue();
					if (fmt == 14 || fmt == 22) {
						data = new Data((fmt == 14 ? D : T), DateUtil.getJavaDate(numeric));
					} else {
						data = new Data(N, numeric);
					}
				}
			} else if (clazz == Cell.CELL_TYPE_STRING) {
				RichTextString txt = cell.getRichStringCellValue();
				if (txt != null) {
					data = new Data(S, StringUtils.trim(txt.toString()));
				}
			} else if (clazz == Cell.CELL_TYPE_BOOLEAN) {
				data = new Data(B, cell.getBooleanCellValue());
			}
		} else {
			data = new Data(S, "");
		}

		return data;
	}

	private void setValue(Cell cell, Data data) {
		if (data != null) {
			if (data.getStyle() == D) {
				cell.setCellStyle(styles.get(D));
				cell.setCellValue((Date) data.getValue());
			} else if (data.getStyle() == T) {
				cell.setCellStyle(styles.get(T));
				cell.setCellValue((Date) data.getValue());
			} else if (data.getStyle() == N) {
				cell.setCellValue((Double) data.getValue());
			} else if (data.getStyle() == S) {
				cell.setCellValue(StringUtils.trim((String) data.getValue()));
			} else if (data.getStyle() == B) {
				cell.setCellValue((Boolean) data.getValue());
			}
		}
	}
	
	private Map<Integer, CellStyle> createStyles(Workbook wb) {
		styles = Maps.newHashMap();
		DataFormat df = wb.createDataFormat();

		CellStyle date = wb.createCellStyle();
		date.setDataFormat(df.getFormat("yyyy-MM-dd"));
		styles.put(D, date);

		CellStyle time = wb.createCellStyle();
		time.setDataFormat(df.getFormat("yyyy-MM-dd HH:mm:ss"));
		styles.put(T, time);

		return styles;
	}

	public static String value(Data data) {
		if (data == null || data.getValue() == null) {
			return "";
		}
		
		String value = null;
		if (data.getStyle() == D) {
			value = DateFormatUtils.format((Date) data.getValue(), "yyyy-MM-dd");
		} else if (data.getStyle() == T) {
			value = DateFormatUtils.format((Date) data.getValue(), "yyyy-MM-dd HH:mm:ss");
		} else if (data.getStyle() == N) {
			value = String.valueOf((Double) data.getValue());
		} else if (data.getStyle() == S) {
			value = (String) data.getValue();
		} else if (data.getStyle() == B) {
			value = String.valueOf((Boolean) data.getValue());
		} else {
			value = "";
		}
		
		return StringUtils.trim(value);
	}
}
