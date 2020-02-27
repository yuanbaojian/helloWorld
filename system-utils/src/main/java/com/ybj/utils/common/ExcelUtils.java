package com.ybj.utils.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 
 * @author 张军 2012-05-29 excel2007使用包：poi-ooxml-3.8-20120326.jar;
 *         poi-ooxml-schemas-3.8-20120326.jar; 依赖包：dom4j-1.6.1.jar;
 *         stax-api-1.0.1.jar; xmlbeans-2.3.0.jar excel2003使用包:
 *         poi-3.8-20120326.jar
 */
public class ExcelUtils<T> {

	/**
	 * excel内容解析总入口
	 * 
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static List<String[]> readExcel(String path) throws Exception {
		String fix = getFilePostFix(path);
		if (fix != null && !"".equals(fix)) {
			if (fix.equals("." + ExcelType.XLS.name().toLowerCase())) {
				return new ExcelUtils().read2003(path);
			} else if (fix.equals("." + ExcelType.XLSX.name().toLowerCase())) {
				return new ExcelUtils().read2007(path);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * 
	 * 解析excel2003及以下版本(后缀名儿.xls)
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	private List<String[]> read2003(String path) throws IOException {
		List<String[]> list = new ArrayList<String[]>();
		HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(path));
		// 循环工作表Sheet
		for (int numSheet = 0; numSheet < workbook.getNumberOfSheets(); numSheet++) {
			HSSFSheet xssfSheet = workbook.getSheetAt(numSheet);
			if (xssfSheet == null) {
				continue;
			}
			// 循环行Row
			int maxRow = xssfSheet.getLastRowNum();
			for (int rowNum = 0; rowNum <= maxRow; rowNum++) {
				HSSFRow xssfRow = xssfSheet.getRow(rowNum);
				if (xssfRow == null) {
					continue;
				}
				// 循环列Cell
				int cellCount = xssfRow.getLastCellNum();
				if (cellCount < 1) {
					continue;
				}
				String[] obj = new String[cellCount];
				for (int cellNum = 0; cellNum < cellCount; cellNum++) {
					HSSFCell xssfCell = xssfRow.getCell(cellNum);
					if (xssfCell == null) {
						obj[cellNum] = "";
						continue;
					}
					obj[cellNum] = getValue(xssfCell);
				}
				if (isEfffective(obj)) {
					list.add(obj);
				}
			}
		}
		workbook.close();
		return list;
	}

	/**
	 * 
	 * 解析excel2007(后缀名为：xlsx)
	 * 
	 * @param path
	 * @return
	 * @throws IOException
	 */
	private List<String[]> read2007(String path) throws IOException {
		List<String[]> list = new ArrayList<String[]>();
		XSSFWorkbook xssfWorkbook = new XSSFWorkbook(new FileInputStream(path));
		// 循环工作表Sheet
		for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
			XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
			if (xssfSheet == null) {
				continue;
			}

			// 循环行Row
			int maxRow = xssfSheet.getLastRowNum();
			for (int rowNum = 0; rowNum <= maxRow; rowNum++) {
				XSSFRow xssfRow = xssfSheet.getRow(rowNum);
				if (xssfRow == null) {
					continue;
				}
				// 循环列Cell
				int cellCount = xssfRow.getLastCellNum();
				String[] obj = new String[cellCount];
				for (int cellNum = 0; cellNum < cellCount; cellNum++) {
					XSSFCell xssfCell = xssfRow.getCell(cellNum);
					if (xssfCell == null) {
						obj[cellNum] = "";
						continue;
					}
					obj[cellNum] = getValue(xssfCell);
				}
				if (isEfffective(obj)) {
					list.add(obj);
				}
			}
		}
		xssfWorkbook.close();
		return list;
	}

	/**
	 * 是否有效
	 * 
	 * @param obj
	 * @return
	 */
	private boolean isEfffective(String[] obj) {
		boolean isEfffective = false;
		for (String o : obj) {
			if (o != null && !"".equals(o.trim())) {
				isEfffective = true;
				break;
			}
		}
		return isEfffective;
	}

	/**
	 * 值转化
	 * 
	 * @param xssfCell
	 * @return
	 */
	private String getValue(XSSFCell xssfCell) {
		String back = "";
		try {
			if (xssfCell.getCellType() == CellType.BOOLEAN) {
				back = String.valueOf(xssfCell.getBooleanCellValue());
			} else if (xssfCell.getCellType() == CellType.NUMERIC) {
				back = String.valueOf((long) xssfCell.getNumericCellValue());
			} else {
				back = String.valueOf(xssfCell.getStringCellValue());
			}
		} catch (Exception e) {
			back = "";
		} finally {
			if (back == null || "null".equals(back)) {
				back = "";
			}
		}
		return back;
	}

	/**
	 * 值转化
	 * 
	 * @param hssfCell
	 * @return
	 */
	private String getValue(HSSFCell hssfCell) {
		String back = "";
		try {
			if (hssfCell.getCellType() == CellType.BLANK) {
				back = String.valueOf(hssfCell.getBooleanCellValue());
			} else if (hssfCell.getCellType() == CellType.NUMERIC) {
				back = String.valueOf((long) hssfCell.getNumericCellValue());
			} else {
				back = String.valueOf(hssfCell.getStringCellValue());
			}
		} catch (Exception e) {
			back = "";
		} finally {
			if (back == null || "null".equals(back) || "A".equals(back.trim())) {
				back = "";
			}
		}
		return back;
	}

	/**
	 * 取文件后
	 * 
	 * @param fileName 无后缀则返，有则返
	 * @return
	 */
	public static String getFilePostFix(String fileName) {
		return "."
				+ parseStr(fileName, "^.+(\\.[^\\?]+)(\\?.+)?", 1)
						.replaceFirst("\\.", "");
	}

	/**
	 * 捕获正则表达式中的指定字符串
	 * 
	 * @param sourceStr
	 * @param regex
	 * @param group
	 * @return
	 */
	public static String parseStr(String sourceStr, String regex, int group) {
		String ret = "";
		StringBuilder input = new StringBuilder(sourceStr);
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(input);

		if (matcher.find() && group <= matcher.groupCount()) {

			ret = matcher.group(group).trim();
		}

		return ret;
	}

	public static enum ExcelType {
		XLS, XLSX
	}

	/**
	 * 导出Excel方法
	 * 
	 * @param title sheet名
	 * @param headers 表头
	 * @param dataset 数据
	 * @param out 输出流
	 * @param indexName 需要生成行号的字段
	 * @param cellName 表头对应的名称
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public void exportExcels(String title, String[] headers,
			String[] cellName, List<T> dataset,String indexName,
			OutputStream out) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		// 声明一个工作薄
		XSSFWorkbook workbook = new XSSFWorkbook();
		// 生成一个表格
		XSSFSheet sheet = workbook.createSheet(title);
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth((short) 20);
		// 生成一个样式
		XSSFCellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setAlignment(HorizontalAlignment.CENTER);
		// 生成一个字体
		XSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 10);
		font.setBold(true);
		// 把字体应用到当前的样式
		style.setFont(font);
		// 生成并设置另一个样式
		XSSFCellStyle style2 = workbook.createCellStyle();
		style2.setBorderBottom(BorderStyle.THIN);
		style2.setBorderLeft(BorderStyle.THIN);
		style2.setBorderRight(BorderStyle.THIN);
		style2.setBorderTop(BorderStyle.THIN);
		style2.setAlignment(HorizontalAlignment.CENTER);
		// 生成另一个字体
		XSSFFont font2 = workbook.createFont();
		font.setFontHeightInPoints((short) 10);
		// 把字体应用到当前的样式
		style2.setFont(font2);
		// 产生表格标题行
		XSSFRow row = sheet.createRow(0);
		for (short i = 0; i < headers.length; i++) {
			XSSFCell cell = row.createCell(i);
			cell.setCellStyle(style);
			XSSFRichTextString text = new XSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}
		String value = null;
		Object object = null;
		for (int i = 0, j = dataset.size(); i < j; i++) {
			XSSFRow rowc = sheet.createRow(i + 1);
			Object r = dataset.get(i);
			Field[] fields = r.getClass().getDeclaredFields();
			for (Field f:fields) {
				f.setAccessible(true);
	        } 
			for (int m = 0, n = headers.length; m < n; m++) {
				XSSFCell cell = rowc.createCell(m);
				cell.setCellStyle(style2);
				if (!StringUtils.isBlank(indexName) && indexName.equals(cellName[m])){
					cell.setCellValue(i+1);
				} else {
					for (Field f:fields) {
						if (f.getName().equals(cellName[m])){
							object = f.get(r);
							if (object instanceof Date) {
								Date date = (Date) object;  
		                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		                        value = sdf.format(date);
							} else {
								value = toString(f.get(r));
							}
							break;
						}
					}
					cell.setCellValue(value);
				}
			}
		}
		try {
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				workbook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 导出Excel xls
	 * 
	 * @param title sheet名
	 * @param headers 表头
	 * @param dataset 数据
	 * @param out 输出流
	 * @param indexName 需要生成行号的字段
	 * @param cellName 表头对应的名称
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public void exportXLSByLM(String title, String[] headers,
			String[] cellName, List<Map<String,Object>> dataset,String indexName,
			OutputStream out) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		// 声明一个工作薄
		HSSFWorkbook workbook = new HSSFWorkbook();
		// 生成一个表格
		HSSFSheet sheet = workbook.createSheet(title);
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth((short) 20);
		// 生成一个样式
		HSSFCellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setAlignment(HorizontalAlignment.CENTER);
		// 生成一个字体
		HSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 10);
		font.setBold(true);
		// 把字体应用到当前的样式
		style.setFont(font);
		// 生成并设置另一个样式
		HSSFCellStyle style2 = workbook.createCellStyle();
		style2.setBorderBottom(BorderStyle.THIN);
		style2.setBorderLeft(BorderStyle.THIN);
		style2.setBorderRight(BorderStyle.THIN);
		style2.setBorderTop(BorderStyle.THIN);
		style2.setAlignment(HorizontalAlignment.CENTER);
		// 生成另一个字体
		HSSFFont font2 = workbook.createFont();
		font2.setFontHeightInPoints((short) 10);
		// 产生表格标题行
		HSSFRow row = sheet.createRow(0);
		for (short i = 0; i < headers.length; i++) {
			HSSFCell cell = row.createCell(i);
			cell.setCellStyle(style);
			HSSFRichTextString text = new HSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}
		Object object = null;
		for (int i = 0, j = dataset.size(); i < j; i++) {
			HSSFRow rowc = sheet.createRow(i + 1);
			Map<String,Object> data = dataset.get(i);
			for (int m = 0, n = headers.length; m < n; m++) {
				HSSFCell cell = rowc.createCell(m);
				cell.setCellStyle(style2);
				if (!StringUtils.isBlank(indexName) && indexName.equals(cellName[m])){
					cell.setCellValue(i+1);
				} else {
					object = data.get(cellName[m]);
					if (object instanceof Integer) {
					    cell.setCellValue(((Integer) object).intValue());
					} else if (object instanceof String) {
					    cell.setCellValue(toString(object));
					} else if (object instanceof Double) {
					    cell.setCellValue(((Double) object).doubleValue());
					} else if (object instanceof Float) {
					    cell.setCellValue(((Float) object).floatValue());
					} else if (object instanceof Long) {
					    cell.setCellValue(((Long) object).longValue());
					} else if (object instanceof Boolean) {
					    cell.setCellValue(toString(object));
					} else if (object instanceof Date) {
					    cell.setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date) object));
					} 
				}
			}
		}
		try {
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				workbook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 导出Excel方法
	 * 
	 * @param title 第一行定义的title名
	 * @param info 第二行定义的info信息
	 * @param sheetName sheet名
	 * @param headers 表头
	 * @param dataset 数据
	 * @param out 输出流
	 * @param indexName 需要生成行号的字段
	 * @param cellName 表头对应的名称
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public void exportByLM(String title,String info,String sheetName, String[] headers,
			String[] cellName, List<Map<String,Object>> dataset,String indexName,
			OutputStream out) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		// 声明一个工作薄
		XSSFWorkbook workbook = new XSSFWorkbook();
		// 生成一个表格
		XSSFSheet sheet = workbook.createSheet(sheetName);
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth((short) 15);
		// 生成一个样式
		XSSFCellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setAlignment(HorizontalAlignment.CENTER);
		// 生成一个字体
		XSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 10);
		font.setBold(true);
		// 把字体应用到当前的样式
		style.setFont(font);
		// 生成并设置另一个样式
		XSSFCellStyle style2 = workbook.createCellStyle();
		style2.setBorderBottom(BorderStyle.THIN);
		style2.setBorderLeft(BorderStyle.THIN);
		style2.setBorderRight(BorderStyle.THIN);
		style2.setBorderTop(BorderStyle.THIN);
		style2.setAlignment(HorizontalAlignment.CENTER);
		// 生成另一个字体
		XSSFFont font2 = workbook.createFont();
		font2.setFontHeightInPoints((short) 10);
		/*********生成第一行title start**********/
		// 生成一个样式
		XSSFCellStyle style3 = workbook.createCellStyle();
		// 设置这些样式
		style3.setAlignment(HorizontalAlignment.CENTER);
		style3.setVerticalAlignment(VerticalAlignment.CENTER);
		// 生成一个字体
		XSSFFont font3 = workbook.createFont();
		font3.setFontHeightInPoints((short) 18);
		font3.setFontName("宋体");
		font3.setBold(true);
		// 把字体应用到当前的样式
		style3.setFont(font3);
		XSSFRow row3 = sheet.createRow(0);
		row3.setHeight((short)(50 * 20));
		XSSFCell titleCell = row3.createCell(0);
		titleCell.setCellStyle(style3);
		sheet.addMergedRegion(new CellRangeAddress(0,(short)0,0,(short)9));
		XSSFRichTextString text3 = new XSSFRichTextString(title);
		titleCell.setCellValue(text3);
		/*********生成第一行title end**********/
		/*********生成第二行info start**********/
		// 生成一个样式
		XSSFCellStyle style4 = workbook.createCellStyle();
		// 设置这些样式
		style4.setAlignment(HorizontalAlignment.LEFT);
		style4.setVerticalAlignment(VerticalAlignment.CENTER);
		// 生成一个字体
		XSSFFont font4 = workbook.createFont();
		font4.setFontHeightInPoints((short) 14);
		font4.setFontName("宋体");
		// 把字体应用到当前的样式
		style4.setFont(font4);
		XSSFRow row4 = sheet.createRow(1);
		row4.setHeight((short)(25 * 20));
		XSSFCell infoCell = row4.createCell(0);
		infoCell.setCellStyle(style4);  
		sheet.addMergedRegion(new CellRangeAddress(1,(short)1,0,(short)9));
		XSSFRichTextString text4 = new XSSFRichTextString(info);
		infoCell.setCellValue(text4);
		/*********生成第二行info end**********/
		// 产生表格标题行
		XSSFRow row = sheet.createRow(2);
		for (short i = 0; i < headers.length; i++) {
			XSSFCell cell = row.createCell(i);
			cell.setCellStyle(style);
			XSSFRichTextString text = new XSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}
		Object object = null;
		for (int i = 0, j = dataset.size(); i < j; i++) {
			XSSFRow rowc = sheet.createRow(i + 3);
			Map<String,Object> data = dataset.get(i);
			for (int m = 0, n = headers.length; m < n; m++) {
				XSSFCell cell = rowc.createCell(m);
				cell.setCellStyle(style2);
				if (!StringUtils.isBlank(indexName) && indexName.equals(cellName[m])){
					cell.setCellValue(i+1);
				} else {
					object = data.get(cellName[m]);
					if (object instanceof Integer) {
					    cell.setCellValue(((Integer) object).intValue());
					} else if (object instanceof String) {
					    cell.setCellValue(toString(object));
					} else if (object instanceof Double) {
					    cell.setCellValue(((Double) object).doubleValue());
					} else if (object instanceof Float) {
					    cell.setCellValue(((Float) object).floatValue());
					} else if (object instanceof Long) {
					    cell.setCellValue(((Long) object).longValue());
					} else if (object instanceof Boolean) {
					    cell.setCellValue(toString(object));
					} else if (object instanceof Date) {
					    cell.setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date) object));
					} 
				}
			}
		}
		try {
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				workbook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 导出Excel xlsx
	 * 
	 * @param title sheet名
	 * @param headers 表头
	 * @param dataset 数据
	 * @param out 输出流
	 * @param indexName 需要生成行号的字段
	 * @param cellName 表头对应的名称
	 * @throws SecurityException 
	 * @throws NoSuchFieldException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	public void exportByLM(String title, String[] headers,
			String[] cellName, List<Map<String,Object>> dataset,String indexName,
			OutputStream out) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		// 声明一个工作薄
		XSSFWorkbook workbook = new XSSFWorkbook();
		// 生成一个表格
		XSSFSheet sheet = workbook.createSheet(title);
		// 设置表格默认列宽度为15个字节
		sheet.setDefaultColumnWidth((short) 20);
		// 生成一个样式
		XSSFCellStyle style = workbook.createCellStyle();
		// 设置这些样式
		style.setBorderBottom(BorderStyle.THIN);
		style.setBorderLeft(BorderStyle.THIN);
		style.setBorderRight(BorderStyle.THIN);
		style.setBorderTop(BorderStyle.THIN);
		style.setAlignment(HorizontalAlignment.CENTER);
		// 生成一个字体
		XSSFFont font = workbook.createFont();
		font.setFontHeightInPoints((short) 10);
		font.setBold(true);
		// 把字体应用到当前的样式
		style.setFont(font);
		// 生成并设置另一个样式
		XSSFCellStyle style2 = workbook.createCellStyle();
		style2.setBorderBottom(BorderStyle.THIN);
		style2.setBorderLeft(BorderStyle.THIN);
		style2.setBorderRight(BorderStyle.THIN);
		style2.setBorderTop(BorderStyle.THIN);
		style2.setAlignment(HorizontalAlignment.CENTER);
		// 生成另一个字体
		XSSFFont font2 = workbook.createFont();
		font2.setFontHeightInPoints((short) 10);
		// 产生表格标题行
		XSSFRow row = sheet.createRow(0);
		for (short i = 0; i < headers.length; i++) {
			XSSFCell cell = row.createCell(i);
			cell.setCellStyle(style);
			XSSFRichTextString text = new XSSFRichTextString(headers[i]);
			cell.setCellValue(text);
		}
		Object object = null;
		for (int i = 0, j = dataset.size(); i < j; i++) {
			XSSFRow rowc = sheet.createRow(i + 1);
			Map<String,Object> data = dataset.get(i);
			for (int m = 0, n = headers.length; m < n; m++) {
				XSSFCell cell = rowc.createCell(m);
				cell.setCellStyle(style2);
				if (!StringUtils.isBlank(indexName) && indexName.equals(cellName[m])){
					cell.setCellValue(i+1);
				} else {
					object = data.get(cellName[m]);
					if (object instanceof Integer) {
					    cell.setCellValue(((Integer) object).intValue());
					} else if (object instanceof String) {
					    cell.setCellValue(toString(object));
					} else if (object instanceof Double) {
					    cell.setCellValue(((Double) object).doubleValue());
					} else if (object instanceof Float) {
					    cell.setCellValue(((Float) object).floatValue());
					} else if (object instanceof Long) {
					    cell.setCellValue(((Long) object).longValue());
					} else if (object instanceof Boolean) {
					    cell.setCellValue(toString(object));
					} else if (object instanceof Date) {
					    cell.setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format((Date) object));
					} 
				}
			}
		}
		try {
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				workbook.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static String toString(Object o) {
		return o == null ? "" : o.toString();
	}
}
