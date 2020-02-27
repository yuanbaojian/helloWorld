
package com.ybj.utils.common;


/**
 * 版本计算工具类
 * Created at 2017-08-03
 * @author caicai.gao
 */
public class VersionUtils {

	private static final char CHAR_A = 'A';
	private static final char CHAR_Z = 'Z';
	
	/**
	 *  计算新的版本
	 *  @param  previousVersion  前版本号
	 *  @return 新版本号
	 */
	public static String getNewVersion(String previousVersion) {
		// 版本生成规则：第一次发放为A版，原则上以后更改均采用换版的形式，按A—>B—>……—>Z—>AA—>AB—>……—>ZZ顺序更改
		String newMajorVersion;
        // 将前版本的字母转化成ASCⅡ中所代表的数字 ,并用preNum 来保存
		char[] c = previousVersion.toCharArray();
		// 第一位字母
		char preNum1;
		// 第二位字母
	    char preNum2 = ' ';
		if (c.length > 1) {
			 preNum1 = previousVersion.charAt(0);
		     preNum2 = previousVersion.charAt(1);
		} else {
			 preNum1 = previousVersion.charAt(0);
		}
        // 前版本只有一位
        if (preNum2==' ') {
        	// 第一位为Z，版本变为两位，从AA开始
            if (preNum1 == CHAR_Z) {
            	preNum1 = CHAR_A;
            	preNum2 = CHAR_A;
            	newMajorVersion = preNum1 + String.valueOf(preNum2);
            } else {
            	// 第一位不为Z，提升第一位
                int newInt = preNum1+1;
                preNum1 = (char)newInt;
                newMajorVersion = String.valueOf(preNum1);
            }
        	return newMajorVersion; 
        } else {
        	// 前版本有两位
            if (preNum1 == CHAR_Z && preNum2 == CHAR_Z) {
            	return "版本号已为ZZ,";
        	} else {
            	// 第二位为Z,提升第一位，第二位置为A
        		if (preNum2 == CHAR_Z) {
                    int newInt = preNum1+1;
                    preNum1 = (char)newInt;
                	newMajorVersion = preNum1 + String.valueOf(CHAR_A);
        		} else {
        			// 第二位不为Z，提升第二位
                    int newInt2 = preNum2+1;
                    preNum2 = (char)newInt2;
                    newMajorVersion = preNum1 + String.valueOf(preNum2);
        		}
        	}
        	return newMajorVersion; 
        }
	}

	public static void main(String[] args) {
		// 随机测试
		System.out.println("随机测试");
		String[] a = {"B","T","AT","AN","BF","BK","KD","JP"};
		for (String s : a) {
			System.out.println("input:" + s + ",output:" + getNewVersion(s));
		}
	}
}