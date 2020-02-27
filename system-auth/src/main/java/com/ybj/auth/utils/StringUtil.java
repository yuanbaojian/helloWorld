package com.ybj.auth.utils;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 *
 * 字符串工具
 *
 * @author atoz-soft.com
 *
 */
public class StringUtil extends org.apache.commons.lang3.StringUtils{

	/** 短日期格式类型 */
	private static final String DATE_FORMAT_SHORT = "yyyy/MM/dd";

	/**
	 * 将一个字符串转化成非 null 形式,并且去掉前后空格
	 *
	 * @return 处理后的字符串
	 */
	public static String avoidNull(Object str) {
		if (str instanceof String) {
			return ((String) str).trim();
		} else {
			if (str == null) {
				return "";
			}

			return str.toString().trim();
		}
	}

	/**
	 * 将一个字符串转化成非 null 形式,并且去掉前后空格
	 *
	 * @return 处理后的字符串
	 */
	private static String avoidNull(String str) {
		if (str == null) {
			return "";
		}
		return str.trim();
	}

	/**
	 * 将字符串转化为int
	 *
	 * @param str string
	 * @return int
	 */
	public static int toInteger(String str) {
		if (null == str || "".equals(str)) {
			return -1;
		}

		return Integer.parseInt(str.trim());
	}

	/**
	 * java.util.Date转为yyyy/MM/dd格式的String
	 *
	 * @param date date
	 * @return yyyy/MM/dd格式的日期字符串
	 */
	public static String dateToString(Date date) {
		// Date转String
		String str;
		DateFormat format = new SimpleDateFormat(DATE_FORMAT_SHORT);
		str = format.format(date);
		return str;
	}

	/**
	 * java.util.Date转为特定格式的String
	 *
	 * @param date 日期
	 * @param formatStr 格式字符串
	 * @return 对应格式的日期字符串
	 */
	public static String dateToString(Date date, String formatStr) {
		// Date转String
		String str = null;
		DateFormat format = new SimpleDateFormat(formatStr);
		str = format.format(date);
		return str;
	}

	/**
	 * String 转Date,DateFormat"yyyy-MM-dd"
	 * @param dateStr dateString
	 * @return Date
	 */
	public static Date stringToDate(String dateStr) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date d = null;
		try {
			d = sdf.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return d;
	}
	/**
	 * 将字符串转化为Double
	 *
	 * @param str String
	 * @return Double
	 */
	public static Double toDouble(String str) {
		if (null == str || str.equals("")) {
			return (double) -1;
		}
		return Double.parseDouble(str.trim());
	}

	/**
	 * 判断字符串是空或者null
	 *
	 * @param str
	 *            字符串
	 * @param ignoreSpace
	 *            是否忽略空格
	 * @return true:是空字符串或者null；false：不是空字符串
	 */
	private static boolean isNullOrEmpty(String str, boolean ignoreSpace) {
		if (ignoreSpace) {
			return str == null || "".equals(str.trim());
		}

		return str == null || "".equals(str);
	}

	/**
	 * 判断字符串是空或者null(不忽略空格)
	 *
	 * @param str
	 *            字符串
	 * @return true:是空字符串或者null;false：不是空字符串
	 */
	private static boolean isNullOrEmpty(String str) {
		return isNullOrEmpty(str, true);
	}

	/**
	 * 判断Object是空或者null(忽略空格)
	 *
	 * @param obj
	 *            Object
	 * @return true:是空字符串或者null;false：不是空字符串
	 */
	public static boolean isNullOrEmpty(Object obj) {
		if (obj == null) {
			return true;
		} else {
			if (obj instanceof String) {
				return ((String) obj).trim().equals("");
			}
			return obj.toString().trim().equals("");
		}
	}

	/**
	 * 判断字符是否是0-9的数字
	 *
	 * @return boolean
	 */
	public static boolean isNumeric(String str) {
		if (!"".equals(avoidNull(str))) {
			return str.matches("^[0-9]*$");
		} else {
			return false;
		}
	}

	/**
	 * 取得字符的长度，汉字以两位算
	 *
	 * @param value String
	 * @return int
	 */
	public static int getStrLength(String value) {
		if (isNullOrEmpty(value)) {
			return 0;
		}

		int totalLength = 0;
		for (int i = 0; i < value.length(); i++) {
			int tempLength = String.valueOf((value.charAt(i))).getBytes().length;
			// 如果编码后大于等于两个字节，按两个字节算
			if (tempLength >= 2) {
				totalLength += 2;
			} else {
				totalLength++;
			}
		}

		return totalLength;
	}

	/**
	 * 字符串数组是否包含字符串
	 *
	 * @param arrString
	 * @param str
	 * @return
	 */
	public static boolean isHave(String[] arrString, String str) {
		if (arrString == null) {
			return false;
		}

		for (String string : arrString) {
			if (string.equals(str)) {
				return true;
			}
		}

		return false;
	}


	/**
	 * 根据字符分割字符串
	 * @param str
	 * @param regex
	 * @return
	 */
	public static String[] splitStr(String str, String regex) {
		if (!isNullOrEmpty(str)) {
			return str.split(regex);
		}
		return null;
	}

	/**
	 * 分割替换字符的字符串
	 * @param str
	 * @param regex
	 * 			要替换的字符
	 * @param replacement
	 * 			替换的 和 分割的 字符
	 * @return
	 */
	public static String[] splitReplaceStr(String str,String regex,String replacement) {
		if (!isNullOrEmpty(str)) {
			return str.replaceAll(regex, replacement).split(replacement);
		}
		return null;
	}
	/**
	 * 将字符串转化为int
	 *
	 * @param str
	 * @return
	 */
	public static int strToInt(String str) {
		if (null == str || str.equals("")) {
			return -1;
		}

		return Integer.parseInt(str.trim());
	}

	/**
	 * 判断字符串是否非空，true:是空字符或者null;false:不是空字符串
	 *
	 * @param str
	 * @return true:是空字符或者null;false:不是空字符串
	 */
	public static boolean isEmpty(String str) {
		if (str != null && !"".equals(str.trim())) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 根据文件路径获取文件名
	 * @param filePath
	 * @return
	 */
	public static String getFileName(String filePath) {
		String[] arr = splitStr(filePath, "\\\\");
		if (arr == null) {
			return null;
		}
		return arr.length < 2 ? arr[0] : arr[arr.length - 1];
	}

	/**
	*需要被调用的js方法
	*/
	public static native String strToMd5(String str)/*-{
		var dd = $wnd.hex_md5(str);
		return dd.toString();
	}-*/;

	/**
	 * 获取加前缀的字符串
	 * @return
	 */
	public static String getPrefixStr(String str) {
		return "a" + strToMd5(str);
	}

	/**
	 * 根据文件名获取文档名
	 * @param fileName
	 * @return
	 */
	public static String getFileNameByName(String fileName) {
		if (isNullOrEmpty(fileName)) {
			return null;
		}
		if (fileName.lastIndexOf(".")<0) {
			return fileName;
		}
		return fileName.substring(0, fileName.lastIndexOf("."));
	}




	public static boolean equals(Object a, Object b) {
        return (a == b) || (a != null && a.equals(b));
    }
}