package com.ybj.utils.common;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * 针对浏览器对文件名称的编码进行转换
 * @author caicai.gao
 */
public class BrowserUtils {
	public static String browserEncode(String fileName,String userAgent) {
		String name="";
		try {
			if (userAgent != null && userAgent.contains("Firefox") || userAgent.contains("Chrome")
			        || userAgent.contains("Safari")) {
				if (userAgent.contains("Firefox")) {
					fileName = fileName.replace(" ", "_");
				}
				name= new String((fileName).getBytes(), "ISO8859-1");  
			} else {
				// 其他浏览器
				name=URLEncoder.encode(fileName,"UTF8");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} 
		return name;
	}
}
