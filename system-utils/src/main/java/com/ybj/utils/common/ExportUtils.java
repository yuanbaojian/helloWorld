package com.ybj.utils.common;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * 导出数据
 * 
 * @author Administrator
 *
 */
public class ExportUtils {

	/**
	 * 
	 * @param fileName 文件名
	 * @param out 输出
	 * @throws UnsupportedEncodingException 
	 */
	public static ResponseEntity<byte[]> exportExcel(String fileName, ByteArrayOutputStream out) throws UnsupportedEncodingException {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
		httpHeaders.add("Content-Disposition", "attachment;filename="
				+ fileName);
		return new ResponseEntity<byte[]>(out.toByteArray(), httpHeaders, HttpStatus.OK);
	}
	
	/**
	 * 下载
	 * 
	 * @param os
	 * @param filePath
	 * @param fileName
	 */
	public static void downLoad(ByteArrayOutputStream os, String filePath,
			String fileName) {
		File file = new File(filePath, fileName);
		byte[] buffer = new byte[1024];
		BufferedInputStream bis = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(file));
			int i = bis.read(buffer);
			while (i != -1) {
				os.write(buffer, 0, i);
				i = bis.read(buffer);
			}
			os.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 创建文件，如果文件根据flg判断是否重新生成该文件
	 * @param filePath 文件路径
	 * @param FileName 文件名
	 * @param flg true:删除后重新生成文件，false:不删除文件
	 * @return
	 */
	public static boolean createFile(String filePath,String FileName,boolean flg) {
		String descFileName = filePath+File.separator+FileName;
		File file = new File(descFileName);
		if (file.exists()) {
			if (flg) {
				file.delete();
			}
		}
		if (descFileName.endsWith(File.separator)) {
			return false;
		}
		if (!file.getParentFile().exists()) {
			// 如果文件所在的目录不存在，则创建目录
			if (!file.getParentFile().mkdirs()) {
				return false;
			}
		}

		// 创建文件
		try {
			if (file.createNewFile()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
