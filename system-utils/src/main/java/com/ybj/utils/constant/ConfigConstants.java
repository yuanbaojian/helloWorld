package com.ybj.utils.constant;

/**
 * 系统配置相关常量
 * @author caicai.gao
 *
 */
public class ConfigConstants {
    
    /****************************  系统常量      ************************************/

    /**登录请求的地址*/
    public static final String REQUEST_LOGIN = "/auth/login";
    /** 系统名称 */
    public static final String SYSTEMNAME = "CAPP";
    
    /** 管理员名称 */
    public static final String ADMIN_USER = "admin";
    
    /** 30分钟内密码最大错误次数 */
    public static final int ALLOW_FAIL_COUNT = 5;
    
    /** 密码过期时间 */
    public static final int LOCK_TIME = 30;
    
    /** 默认密码 */
    public static final String PASSWORD = "123456";
    
    /** 错误编码 */
    public static final int ERROR_CODE_NONE = 0;
    public static final int ERROR_CODE_SYSTEM = 10000;
    public static final int ERROR_CODE_BUSINESS = 10001;
    public static final int ERROR_CODE_USER = 10002;

    /** 电子仓库根目录 */
    public static final String STOREHOUSE_PATH = "storehouse_path";

    /** HttpResponse 的 contentType*/
    public static final String CONTENT_TYPE = "application/json";

    /** HttpResponse 的 characterEncoding*/
    public static final String CHARACTER_ENCODING = "utf-8";

    /** 日期格式 */
    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /** 错误信息标识 */
    public static final String ERROR_MSG = "errmsg";

    /** 添加失败 */
    public static final short FAIL = 0;
    
    /**  添加成功 */
    public static final short SUCCESS = 1;
    
    /** 添加信息已存在 */
    public static final short EXIST = 2;
    
    /** 字符串null */
    public static final String NULL_STRING = "null";
    
    /** 字符串true */
    public static final String TRUE_STRING = "true";
    
    /** 字符串false */
    public static final String FALSE_STRING = "false";
    
    /** 双引号 */
    public static final String QUOTATION_MARKS_STRING = "\"";
    
    /** 升序 */
    public static final String ASC_STRING = "asc";
    
    /** linux操作系统 */
    public static final String OS_LINUX = "linux";
    
}
