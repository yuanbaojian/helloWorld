package com.ybj.utils.exception;

/**
 * 自定义异常
 * @author caicai.gao
 */
public class CustomGenericException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private int errCode;

    private String message;

    public int getErrCode() {
        return errCode;
    }
    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }
    public CustomGenericException(int errCode, String errMsg) {
        this.errCode = errCode;
        this.setMessage(errMsg);
    }
	@Override
    public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}
