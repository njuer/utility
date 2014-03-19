package org.minnie.utility.module.sohu;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.minnie.utility.util.StringUtil;

/**
 * @author neiplzer@gmail.com
 * @version 2014-3-17 类说明
 */

public class SummaryHSSF {

	public static void generateFullAnalysis(
			List<DoubleColorAnalyse> analyseList, String destPath) {

		// 创建Workbook对象（这一个对象代表着对应的一个Excel文件）
		// XSSFWorkbook表示以xlsx为后缀名的文件
		Workbook wb = new XSSFWorkbook();
		// 获得CreationHelper对象,这个应该是一个帮助类
		CreationHelper helper = wb.getCreationHelper();
		// 创建Sheet并给名字(表示Excel的一个Sheet)
		Sheet sheet = wb.createSheet("双色球分析");
		// Row表示一行Cell表示一列
		Row row = null;
		Cell cell = null;

		// 创建标题行
		createFullTitleRow(row, cell, wb, sheet);

		// 创建一个基本的样式
		CellStyle cellStyle = createStyleCell(wb);
		// 设置文字在单元格里面的位置
		cellStyle = setCellStyleAlignment(cellStyle,
				CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER);
		// 设置这个样式的格式(Format)
		cellStyle = setCellFormat(helper, cellStyle, "#######");
		// 先创建字体样式,并把这个样式加到单元格的字体里面
		cellStyle.setFont(createFonts(wb));
		int size = analyseList.size();
		for (int i = 0; i < size; i++) {

			DoubleColorAnalyse ssqAnalyse = analyseList.get(i);
			DoubleColor ssq = ssqAnalyse.getDoubleColor();

			// 获得这个sheet的第i行
			row = sheet.createRow(i + 1);
			// 设置行长度自动
			// row.setHeight((short)500);
			row.setHeightInPoints(20);
			// row.setZeroHeight(true);

			// 设置每个sheet每一行的宽度,自动,根据需求自行确定
			sheet.autoSizeColumn(0, true);
			// 获得这一行的每1列
			cell = row.createCell(0);
			// 把这个样式加到单元格里面
			cell.setCellStyle(cellStyle);
			// 给单元格设值
			cell.setCellValue(ssq.getPhase());

			List<String> redList = ssq.getRed();
			int redListSize = redList.size();
			for (int j = 0; j < redListSize; j++) {
				Integer red = Integer.valueOf(redList.get(j));
				
				// 设置每个sheet每一行的宽度,自动,根据需求自行确定
				sheet.autoSizeColumn(red, true);
				// 获得这一行的每1列
				cell = row.createCell(red);
				// 把这个样式加到单元格里面
				cell.setCellStyle(cellStyle);
				// 给单元格设值
				cell.setCellValue(red);
			}

			Integer blue =  Integer.valueOf(ssq.getBlue());
			// 设置每个sheet每一行的宽度,自动,根据需求自行确定
			sheet.autoSizeColumn(33 + blue, true);
			// 获得这一行的每1列
			cell = row.createCell(33 + blue);
			// 把这个样式加到单元格里面
			cell.setCellStyle(cellStyle);
			// 给单元格设值
			cell.setCellValue(blue);
			
			 /**
			 * 五期内选红
			 */
			 // 设置每个sheet每一行的宽度,自动,根据需求自行确定
			 sheet.autoSizeColumn(50, true);
			 // 获得这一行的每1列
			 cell = row.createCell(50);
			 // 把这个样式加到单元格里面
			 cell.setCellStyle(cellStyle);
			 // 给单元格设值
			 cell.setCellValue(ssqAnalyse.getLastFivePhaseForRed());
			
			 /**
			 * 下移号
			 */
			 // 设置每个sheet每一行的宽度,自动,根据需求自行确定
			 sheet.autoSizeColumn(51, true);
			 // 获得这一行的每1列
			 cell = row.createCell(51);
			 // 把这个样式加到单元格里面
			 cell.setCellStyle(cellStyle);
			 // 给单元格设值
			 cell.setCellValue(ssqAnalyse.getDownForRed());
			
			 /**
			 * 上期篮球杀红
			 */
			 // 设置每个sheet每一行的宽度,自动,根据需求自行确定
			 sheet.autoSizeColumn(52, true);
			 // 获得这一行的每1列
			 cell = row.createCell(52);
			 // 把这个样式加到单元格里面
			 cell.setCellStyle(cellStyle);
			 // 给单元格设值
			 cell.setCellValue(ssqAnalyse.getLastBlueKillRed());
			
			 /**
			 * 上期红球最大号码减去最小号码杀红
			 */
			 // 设置每个sheet每一行的宽度,自动,根据需求自行确定
			 sheet.autoSizeColumn(53, true);
			 // 获得这一行的每1列
			 cell = row.createCell(53);
			 // 把这个样式加到单元格里面
			 cell.setCellStyle(cellStyle);
			 // 给单元格设值
			 cell.setCellValue(ssqAnalyse.getLastMaxMinusMinKillRed());
			
			 /**
			 * 期号杀蓝
			 */
			 // 设置每个sheet每一行的宽度,自动,根据需求自行确定
			 sheet.autoSizeColumn(54, true);
			 // 获得这一行的每1列
			 cell = row.createCell(54);
			 // 把这个样式加到单元格里面
			 cell.setCellStyle(cellStyle);
			 // 给单元格设值
			 cell.setCellValue(ssqAnalyse.getPhaseNumberKillBlue());
			
			 /**
			 * 加减法杀蓝
			 */
			 // 设置每个sheet每一行的宽度,自动,根据需求自行确定
			 sheet.autoSizeColumn(55, true);
			 // 获得这一行的每1列
			 cell = row.createCell(55);
			 // 把这个样式加到单元格里面
			 cell.setCellStyle(cellStyle);
			 // 给单元格设值
			 cell.setCellValue(ssqAnalyse.getAddSubtractionKillBlue());

		}

		// 输出
		File file = new File(destPath);
		// 如果目录不存在，则创建目录
		File dir = new File(file.getParent());
		if (!dir.exists()) {
			dir.mkdirs();
		}
		OutputStream os;
		try {
			os = new FileOutputStream(file);
			wb.write(os);
			os.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void generateAnalysis(List<DoubleColorAnalyse> analyseList,
			String destPath) {

		int size = analyseList.size();
		// 创建Workbook对象（这一个对象代表着对应的一个Excel文件）
		// XSSFWorkbook表示以xlsx为后缀名的文件
		Workbook wb = new XSSFWorkbook();
		// 获得CreationHelper对象,这个应该是一个帮助类
		CreationHelper helper = wb.getCreationHelper();
		// 创建Sheet并给名字(表示Excel的一个Sheet)
		Sheet sheet = wb.createSheet("双色球分析");
		// Row表示一行Cell表示一列
		Row row = null;
		Cell cell = null;

		// 创建标题行
		createTitleRow(row, cell, wb, sheet);

		// 创建一个基本的样式
		CellStyle cellStyle = createStyleCell(wb);
		// 设置文字在单元格里面的位置
		cellStyle = setCellStyleAlignment(cellStyle,
				CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER);
		// 设置这个样式的格式(Format)
		cellStyle = setCellFormat(helper, cellStyle, "#######");
		// 先创建字体样式,并把这个样式加到单元格的字体里面
		cellStyle.setFont(createFonts(wb));

		for (int i = 0; i < size; i++) {

			DoubleColorAnalyse ssqAnalyse = analyseList.get(i);
			DoubleColor ssq = ssqAnalyse.getDoubleColor();

			// 获得这个sheet的第i行
			row = sheet.createRow(i + 1);
			// 设置行长度自动
			// row.setHeight((short)500);
			row.setHeightInPoints(20);
			// row.setZeroHeight(true);

			// 设置每个sheet每一行的宽度,自动,根据需求自行确定
			sheet.autoSizeColumn(0, true);
			// 获得这一行的每1列
			cell = row.createCell(0);
			// 把这个样式加到单元格里面
			cell.setCellStyle(cellStyle);
			// 给单元格设值
			cell.setCellValue(ssq.getPhase());

			List<String> redList = ssq.getRed();
			int redListSize = redList.size();
			for (int j = 0; j < redListSize; j++) {
				// 设置每个sheet每一行的宽度,自动,根据需求自行确定
				sheet.autoSizeColumn(j + 1, true);
				// 获得这一行的每1列
				cell = row.createCell(j + 1);
				// 把这个样式加到单元格里面
				cell.setCellStyle(cellStyle);
				// 给单元格设值
				cell.setCellValue(redList.get(j));
			}

			// 设置每个sheet每一行的宽度,自动,根据需求自行确定
			sheet.autoSizeColumn(7, true);
			// 获得这一行的每1列
			cell = row.createCell(7);
			// 把这个样式加到单元格里面
			cell.setCellStyle(cellStyle);
			// 给单元格设值
			cell.setCellValue(ssq.getBlue());

			/**
			 * 五期内选红
			 */
			// 设置每个sheet每一行的宽度,自动,根据需求自行确定
			sheet.autoSizeColumn(8, true);
			// 获得这一行的每1列
			cell = row.createCell(8);
			// 把这个样式加到单元格里面
			cell.setCellStyle(cellStyle);
			// 给单元格设值
			cell.setCellValue(ssqAnalyse.getLastFivePhaseForRed());

			/**
			 * 下移号
			 */
			// 设置每个sheet每一行的宽度,自动,根据需求自行确定
			sheet.autoSizeColumn(9, true);
			// 获得这一行的每1列
			cell = row.createCell(9);
			// 把这个样式加到单元格里面
			cell.setCellStyle(cellStyle);
			// 给单元格设值
			cell.setCellValue(ssqAnalyse.getDownForRed());

			/**
			 * 上期篮球杀红
			 */
			// 设置每个sheet每一行的宽度,自动,根据需求自行确定
			sheet.autoSizeColumn(10, true);
			// 获得这一行的每1列
			cell = row.createCell(10);
			// 把这个样式加到单元格里面
			cell.setCellStyle(cellStyle);
			// 给单元格设值
			cell.setCellValue(ssqAnalyse.getLastBlueKillRed());

			/**
			 * 上期红球最大号码减去最小号码杀红
			 */
			// 设置每个sheet每一行的宽度,自动,根据需求自行确定
			sheet.autoSizeColumn(11, true);
			// 获得这一行的每1列
			cell = row.createCell(11);
			// 把这个样式加到单元格里面
			cell.setCellStyle(cellStyle);
			// 给单元格设值
			cell.setCellValue(ssqAnalyse.getLastMaxMinusMinKillRed());

			/**
			 * 期号杀蓝
			 */
			// 设置每个sheet每一行的宽度,自动,根据需求自行确定
			sheet.autoSizeColumn(12, true);
			// 获得这一行的每1列
			cell = row.createCell(12);
			// 把这个样式加到单元格里面
			cell.setCellStyle(cellStyle);
			// 给单元格设值
			cell.setCellValue(ssqAnalyse.getPhaseNumberKillBlue());

			/**
			 * 加减法杀蓝
			 */
			// 设置每个sheet每一行的宽度,自动,根据需求自行确定
			sheet.autoSizeColumn(13, true);
			// 获得这一行的每1列
			cell = row.createCell(13);
			// 把这个样式加到单元格里面
			cell.setCellStyle(cellStyle);
			// 给单元格设值
			cell.setCellValue(ssqAnalyse.getAddSubtractionKillBlue());

		}

		// 输出
		File file = new File("C:/create.xlsx");
		OutputStream os;
		try {
			os = new FileOutputStream(file);
			wb.write(os);
			os.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void createTitleRow(Row row, Cell cell, Workbook wb,
			Sheet sheet) {

		// 获得这个sheet的第i行
		row = sheet.createRow(0);
		// 设置行长度自动
		// row.setHeight((short)500);
		row.setHeightInPoints(20);
		// row.setZeroHeight(true);

		// 设置每个sheet每一行的宽度,自动,根据需求自行确定
		sheet.autoSizeColumn(1, true);

		// 创建标题样式--期号
		CellStyle cellStyle = getCellStyleForTitle(wb);
		cellStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
		cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		// 获得这一行的每1列
		cell = row.createCell(0);
		// 把这个样式加到单元格里面
		cell.setCellStyle(cellStyle);
		// 给单元格设值
		cell.setCellValue("期号");

		cellStyle = getCellStyleForRed(wb);
		cellStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
		cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		// 获得这一行的每2列
		cell = row.createCell(1);
		// 把这个样式加到单元格里面
		cell.setCellStyle(cellStyle);
		// 给单元格设值
		cell.setCellValue("红1");

		// 获得这一行的每3列
		cell = row.createCell(2);
		// 把这个样式加到单元格里面
		cell.setCellStyle(cellStyle);
		// 给单元格设值
		cell.setCellValue("红2");

		// 获得这一行的每4列
		cell = row.createCell(3);
		// 把这个样式加到单元格里面
		cell.setCellStyle(cellStyle);
		// 给单元格设值
		cell.setCellValue("红3");

		// 获得这一行的每5列
		cell = row.createCell(4);
		// 把这个样式加到单元格里面
		cell.setCellStyle(cellStyle);
		// 给单元格设值
		cell.setCellValue("红4");

		// 获得这一行的每6列
		cell = row.createCell(5);
		// 把这个样式加到单元格里面
		cell.setCellStyle(cellStyle);
		// 给单元格设值
		cell.setCellValue("红5");

		// 获得这一行的每7列
		cell = row.createCell(6);
		// 把这个样式加到单元格里面
		cell.setCellStyle(cellStyle);
		// 给单元格设值
		cell.setCellValue("红6");

		cellStyle = getCellStyleForBlue(wb);
		cellStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
		cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		// 获得这一行的每8列
		cell = row.createCell(7);
		// 把这个样式加到单元格里面
		cell.setCellStyle(cellStyle);
		// 给单元格设值
		cell.setCellValue("蓝球");

		cellStyle = getCellStyleForRedAnalyse(wb);
		cellStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
		cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		// 获得这一行的每9列
		cell = row.createCell(8);
		// 把这个样式加到单元格里面
		cell.setCellStyle(cellStyle);
		// 给单元格设值
		cell.setCellValue("五期内选红");

		// 获得这一行的每10列
		cell = row.createCell(9);
		// 把这个样式加到单元格里面
		cell.setCellStyle(cellStyle);
		// 给单元格设值
		cell.setCellValue("下移号");

		// 获得这一行的每11列
		cell = row.createCell(10);
		// 把这个样式加到单元格里面
		cell.setCellStyle(cellStyle);
		// 给单元格设值
		cell.setCellValue("上期篮球杀红");

		// 获得这一行的每12列
		cell = row.createCell(11);
		// 把这个样式加到单元格里面
		cell.setCellStyle(cellStyle);
		// 给单元格设值
		cell.setCellValue("上期红球最大号码减去最小号码杀红");

		cellStyle = getCellStyleForBlueAnalyse(wb);
		cellStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
		cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		// 获得这一行的每13列
		cell = row.createCell(12);
		// 把这个样式加到单元格里面
		cell.setCellStyle(cellStyle);
		// 给单元格设值
		cell.setCellValue("期号杀蓝");

		// 获得这一行的每14列
		cell = row.createCell(13);
		// 把这个样式加到单元格里面
		cell.setCellStyle(cellStyle);
		// 给单元格设值
		cell.setCellValue("加减法杀蓝");
	}

	public static void createFullTitleRow(Row row, Cell cell, Workbook wb,
			Sheet sheet) {

		// 获得这个sheet的第i行
		row = sheet.createRow(0);
		// 设置行长度自动
		// row.setHeight((short)500);
		row.setHeightInPoints(20);
		// row.setZeroHeight(true);

		// 设置每个sheet每一行的宽度,自动,根据需求自行确定
		sheet.autoSizeColumn(1, true);

		// 创建标题样式--期号
		CellStyle cellStyle = getCellStyleForTitle(wb);
		cellStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
		cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);

		int index = 0;
		// 获得这一行的每1列
		cell = row.createCell(index++);
		// 把这个样式加到单元格里面
		cell.setCellStyle(cellStyle);
		// 给单元格设值
		cell.setCellValue("期号");

		cellStyle = getCellStyleForRed(wb);
		cellStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
		cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);

		/**
		 * 红球
		 */
		for (int i = index; i <= 33; i++) {
			// 获得这一行的每2列
			cell = row.createCell(i);
			// 把这个样式加到单元格里面
			cell.setCellStyle(cellStyle);
			// 给单元格设值
			cell.setCellValue(StringUtil.getBallValue(i));
			index = i;
		}

		cellStyle = getCellStyleForBlue(wb);
		cellStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
		cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);

		/**
		 * 蓝球
		 */
		// index = 34;
		int count = 1;
		while (count <= 16) {
			index++;
			cell = row.createCell(index);
			// 把这个样式加到单元格里面
			cell.setCellStyle(cellStyle);
			// 给单元格设值
			cell.setCellValue(StringUtil.getBallValue(count));
			count++;
		}
//		System.out.println("五期内选红="+index);
		cellStyle = getCellStyleForRedAnalyse(wb);
		cellStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
		cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		
		// 五期内选红列
		cell = row.createCell(++index);
		System.out.println("五期内选红="+index);
		// 把这个样式加到单元格里面
		cell.setCellStyle(cellStyle);
		// 给单元格设值
		cell.setCellValue("五期内选红");

		// 下移号列
		cell = row.createCell(++index);
		// 把这个样式加到单元格里面
		cell.setCellStyle(cellStyle);
		// 给单元格设值
		cell.setCellValue("下移号");

		// 上期篮球杀红列
		cell = row.createCell(++index);
		// 把这个样式加到单元格里面
		cell.setCellStyle(cellStyle);
		// 给单元格设值
		cell.setCellValue("上期篮球杀红");

		// 上期红球最大号码减去最小号码杀红列
		cell = row.createCell(++index);
		// 把这个样式加到单元格里面
		cell.setCellStyle(cellStyle);
		// 给单元格设值
		cell.setCellValue("上期红球最大号码减去最小号码杀红");

		cellStyle = getCellStyleForBlueAnalyse(wb);
		cellStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
		cellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
		
		// 期号杀蓝列
		cell = row.createCell(++index);
		// 把这个样式加到单元格里面
		cell.setCellStyle(cellStyle);
		// 给单元格设值
		cell.setCellValue("期号杀蓝");

		// 加减法杀蓝列
		cell = row.createCell(++index);
		// 把这个样式加到单元格里面
		cell.setCellStyle(cellStyle);
		// 给单元格设值
		cell.setCellValue("加减法杀蓝");
	}

	public static void main(String[] args) throws IOException {

		// Suffix 后缀

		// //创建Workbook对象（这一个对象代表着对应的一个Excel文件）
		// //HSSFWorkbook表示以xls为后缀名的文件
		// Workbook wb = new HSSFWorkbook();
		Workbook wb = new XSSFWorkbook();
		// 获得CreationHelper对象,这个应该是一个帮助类
		CreationHelper helper = wb.getCreationHelper();
		// 创建Sheet并给名字(表示Excel的一个Sheet)
		Sheet sheet1 = wb.createSheet("HSSF_Sheet_1");
		// Row表示一行Cell表示一列
		Row row = null;
		Cell cell = null;
		for (int i = 0; i < 60; i = i + 2) {
			// 获得这个sheet的第i行
			row = sheet1.createRow(i);
			// 设置行长度自动
			// row.setHeight((short)500);
			row.setHeightInPoints(20);
			// row.setZeroHeight(true);
			for (int j = 0; j < 25; j++) {
				// 设置每个sheet每一行的宽度,自动,根据需求自行确定
				sheet1.autoSizeColumn(j + 1, true);
				// 创建一个基本的样式
				CellStyle cellStyle = createStyleCell(wb);
				// 获得这一行的每j列
				cell = row.createCell(j);
				if (j == 0) {
					// 设置文字在单元格里面的位置
					cellStyle = setCellStyleAlignment(cellStyle,
							CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER);
					// 先创建字体样式,并把这个样式加到单元格的字体里面
					cellStyle.setFont(createFonts(wb));
					// 把这个样式加到单元格里面
					cell.setCellStyle(cellStyle);
					// 给单元格设值
					cell.setCellValue(true);
				} else if (j == 1) {
					// 设置文字在单元格里面的位置
					cellStyle = setCellStyleAlignment(cellStyle,
							CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER);
					// 设置这个样式的格式(Format)
					cellStyle = setCellFormat(helper, cellStyle,
							"#,##0.0000");
					// 先创建字体样式,并把这个样式加到单元格的字体里面
					cellStyle.setFont(createFonts(wb));
					// 把这个样式加到单元格里面
					cell.setCellStyle(cellStyle);
					// 给单元格设值
					cell.setCellValue(new Double(2008.2008));
				} else if (j == 2) {
					cellStyle = setCellStyleAlignment(cellStyle,
							CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER);
					cellStyle.setFont(createFonts(wb));
					cell.setCellStyle(cellStyle);
					cell.setCellValue(helper.createRichTextString("RichString"
							+ i + j));
				} else if (j == 3) {
					cellStyle = setCellStyleAlignment(cellStyle,
							CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER);
					cellStyle = setCellFormat(helper, cellStyle,
							"MM-yyyy-dd");
					cell.setCellStyle(cellStyle);
					cell.setCellValue(new Date());
				} else if (j == 24) {
					cellStyle = setCellStyleAlignment(cellStyle,
							CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER);
					cellStyle.setFont(createFonts(wb));
					// 设置公式
					cell.setCellFormula("SUM(E" + (i + 1) + ":X" + (i + 1)
							+ ")");
				} else {
					cellStyle = setCellStyleAlignment(cellStyle,
							CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER);
					cellStyle = setFillBackgroundColors(cellStyle,
							IndexedColors.ORANGE.getIndex(),
							IndexedColors.ORANGE.getIndex(),
							CellStyle.SOLID_FOREGROUND);
					cell.setCellStyle(cellStyle);
					cell.setCellValue(1);
				}
			}
		}
		// 输出
		File file = new File("C:/create.xlsx");
		OutputStream os = new FileOutputStream(file);
		wb.write(os);
		os.close();
	}

	/**
	 * 边框
	 * 
	 * @param wb
	 * @return
	 */
	public static CellStyle createStyleCell(Workbook wb) {
		CellStyle cellStyle = wb.createCellStyle();
		// 设置一个单元格边框粗细
		cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
		cellStyle.setBorderTop(CellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(CellStyle.BORDER_THIN);
		cellStyle.setBorderRight(CellStyle.BORDER_THIN);
		// 设置一个单元格边框颜色
		cellStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
		cellStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		cellStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		cellStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
		return cellStyle;
	}

	/**
	 * 设置文字在单元格里面的位置 CellStyle.ALIGN_CENTER CellStyle.VERTICAL_CENTER
	 * 
	 * @param cellStyle
	 * @param halign
	 * @param valign
	 * @return
	 */
	public static CellStyle setCellStyleAlignment(CellStyle cellStyle,
			short halign, short valign) {
		// 设置上下
		cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
		// 设置左右
		cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		return cellStyle;
	}

	/**
	 * 格式化单元格 如#,##0.00,m/d/yy去HSSFDataFormat或XSSFDataFormat里面找
	 * 
	 * @param cellStyle
	 * @param fmt
	 * @return
	 */
	public static CellStyle setCellFormat(CreationHelper helper,
			CellStyle cellStyle, String fmt) {
		// 还可以用其它方法创建format
		cellStyle.setDataFormat(helper.createDataFormat().getFormat(fmt));
		return cellStyle;
	}

	/**
	 * 前景和背景填充的着色
	 * 
	 * @param cellStyle
	 * @param bg
	 *            IndexedColors.ORANGE.getIndex();
	 * @param fg
	 *            IndexedColors.ORANGE.getIndex();
	 * @param fp
	 *            CellStyle.SOLID_FOREGROUND
	 * @return
	 */
	public static CellStyle setFillBackgroundColors(CellStyle cellStyle,
			short bg, short fg, short fp) {
		// cellStyle.setFillBackgroundColor(bg);
		cellStyle.setFillForegroundColor(fg);
		cellStyle.setFillPattern(fp);
		return cellStyle;
	}

	/**
	 * 设置字体
	 * 
	 * @param wb
	 * @return
	 */
	public static Font createFonts(Workbook wb) {
		return createFonts(wb, HSSFColor.BLACK.index, (short) 200, (short) 200);
	}

	public static Font createFonts(Workbook wb, short color) {
		return createFonts(wb, color, (short) 200, (short) 200);
	}

	public static Font createFonts(Workbook wb, short color, short boldweight,
			short height) {
		// 创建Font对象
		Font font = wb.createFont();
		// 设置字体
		font.setFontName("微软雅黑");
		// 着色
		font.setColor(color);
		// //斜体
		// font.setItalic(true);
		font.setBoldweight(boldweight);
		// 字体大小
		font.setFontHeight(height);
		return font;
	}

	public static CellStyle getCellStyle(Workbook wb, short color, short align,
			short verticalAlign, short boldweight, short height) {
		// 创建Font对象
		Font font = createFonts(wb, color, boldweight, height);
		// 创建一个基本的样式
		CellStyle cellStyle = createStyleCell(wb);
		// 先创建字体样式,并把这个样式加到单元格的字体里面
		cellStyle.setFont(font);
		// 设置上下
		cellStyle.setAlignment(align);
		// 设置左右
		cellStyle.setVerticalAlignment(verticalAlign);

		return cellStyle;
	}

	public static CellStyle getCellStyleForTitle(Workbook wb) {
		return getCellStyle(wb, HSSFColor.BLACK.index, CellStyle.ALIGN_CENTER,
				CellStyle.VERTICAL_CENTER, (short) 250, (short) 250);
	}

	public static CellStyle getCellStyleForRed(Workbook wb) {
		return getCellStyle(wb, HSSFColor.RED.index, CellStyle.ALIGN_CENTER,
				CellStyle.VERTICAL_CENTER, (short) 250, (short) 250);
	}

	public static CellStyle getCellStyleForBlue(Workbook wb) {
		return getCellStyle(wb, HSSFColor.BLUE.index, CellStyle.ALIGN_CENTER,
				CellStyle.VERTICAL_CENTER, (short) 250, (short) 250);
	}

	public static CellStyle getCellStyleForRedAnalyse(Workbook wb) {
		return getCellStyle(wb, HSSFColor.ROSE.index, CellStyle.ALIGN_CENTER,
				CellStyle.VERTICAL_CENTER, (short) 250, (short) 250);
	}

	public static CellStyle getCellStyleForBlueAnalyse(Workbook wb) {
		return getCellStyle(wb, HSSFColor.ROYAL_BLUE.index,
				CellStyle.ALIGN_CENTER, CellStyle.VERTICAL_CENTER, (short) 250,
				(short) 250);
	}

}
