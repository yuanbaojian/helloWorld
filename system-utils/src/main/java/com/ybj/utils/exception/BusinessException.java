package com.ybj.utils.exception;

/**
 * 业务异常
 * @author caicai.gao
 */
public class BusinessException extends CustomGenericException {

	public BusinessException(int errCode, String errMsg) {
		super(errCode, errMsg);
	}

	private static final long serialVersionUID = 1L;  
}
