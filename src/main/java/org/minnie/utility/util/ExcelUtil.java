package org.minnie.utility.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * @author neiplzer@gmail.com
 * @version 2014-3-17
 * 类说明
 */
public class ExcelUtil {
	
	private static Logger logger = Logger.getLogger(ExcelUtil.class.getName());

	static SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd");
	static Map<String, CellStyle> styleMap = new HashMap<String, CellStyle>(); // 存储单元格样式的Map

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	

	/**
	 * 写内容到excel中
	 * 
	 * @throws IOException
	 */
	public static void testWrite(String srcFilePath, String tarFilePath) {
		FileOutputStream out = null;
		try {
			Workbook book = getExcelWorkbook(srcFilePath);
			Sheet sheet = getSheetByNum(book, 1);

			Map<String, String> map = new HashMap<String, String>();
			List<Map<String, String>> list = new LinkedList<Map<String, String>>();
			map.put("A", "4,INT");
			map.put("B", "小红,GENERAL");
			map.put("C", "18,INT");
			map.put("D", "1990-03-10,DATE");
			map.put("E", "0.056,PERCENT");
			map.put("F", "4800,DOUBLE");
			list.add(map);

			int startRow = 6;
			boolean result = writeToExcel(list, sheet, startRow);
			if (result) {
				out = new FileOutputStream(tarFilePath);
				book.write(out);
				logger.info("文件写入完成！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 将传入的内容写入到excel中sheet里
	 * 
	 * @param list
	 */
	public static boolean writeToExcel(List<Map<String, String>> list,
			Sheet sheet, int startRow) {
		boolean result = false;
		try {
			Map<String, String> map = null;
			Row row = null;
			for (int i = 0; i < list.size(); i++) {
				map = list.get(i);
				row = sheet.getRow(startRow - 1);
				if (row == null) {
					row = sheet.createRow(startRow - 1);
				}
				startRow++;
				Cell cell = null;

				BigDecimal db = null;
				for (Map.Entry<String, String> entry : map.entrySet()) {
					String key = entry.getKey();
					int colNum = NumberUtils.toNum_new(key) - 1;

					String value_type = entry.getValue();
					String value = value_type.split(",")[0];
					String style = value_type.split(",")[1];

					cell = row.getCell(colNum);
					if (cell == null) {
						cell = row.createCell(colNum);
					}
					if (style.equals("GENERAL")) {
						cell.setCellValue(value);
					} else {
						if (style.equals("DOUBLE") || style.equals("INT")) {
							db = new BigDecimal(value,
									java.math.MathContext.UNLIMITED);
							cell.setCellValue(db.doubleValue());
						} else if (style.equals("PERCENT")) {
							db = new BigDecimal(value,
									java.math.MathContext.UNLIMITED);
							cell.setCellValue(db.doubleValue());
						} else if (style.equals("DATE")) {
							java.util.Date date = sFormat.parse(value);
							cell.setCellValue(date);
						}
						cell.setCellStyle(styleMap.get(style));
					}
				}
			}
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 获取excel的Workbook
	 * 
	 * @throws IOException
	 */
	public static Workbook getExcelWorkbook(String filePath) throws IOException {
		Workbook book = null;
		File file = null;
		FileInputStream fis = null;

		try {
			file = new File(filePath);
			if (!file.exists()) {
				throw new RuntimeException("文件不存在");
			} else {
				fis = new FileInputStream(file);
				book = WorkbookFactory.create(fis);
				initStyleMap(book);
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		} finally {
			if (fis != null) {
				fis.close();
			}
		}
		return book;
	}
	
	/**
	 * 根据索引 返回Sheet
	 * 
	 * @param number
	 */
	public static Sheet getSheetByNum(Workbook book, int number) {
		Sheet sheet = null;
		try {
			sheet = book.getSheetAt(number - 1);
			// if(sheet == null){
			// sheet = book.createSheet("Sheet"+number);
			// }
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return sheet;
	}
	
	/**
	 * 初始化格式Map
	 */

	public static void initStyleMap(Workbook book) {
		DataFormat hssfDF = book.createDataFormat();

		CellStyle doubleStyle = book.createCellStyle(); // 会计专用
		doubleStyle
				.setDataFormat(hssfDF
						.getFormat("_ * #,##0.00_ ;_ * \\-#,##0.00_ ;_ * \"-\"??_ ;_ @_ ")); // poi写入后为会计专用
		styleMap.put("DOUBLE", doubleStyle);

		CellStyle intStyle = book.createCellStyle(); // 会计专用
		intStyle.setDataFormat(hssfDF.getFormat("0")); // poi写入后为会计专用
		styleMap.put("INT", intStyle);

		CellStyle yyyyMMddStyle = book.createCellStyle();// 日期yyyyMMdd
		yyyyMMddStyle.setDataFormat(hssfDF.getFormat("yyyy-MM-dd"));
		styleMap.put("DATE", yyyyMMddStyle);

		CellStyle percentStyle = book.createCellStyle();// 百分比
		percentStyle.setDataFormat(hssfDF.getFormat("0.00%"));
		styleMap.put("PERCENT", percentStyle);
	}

}
