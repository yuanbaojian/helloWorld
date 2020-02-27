package com.ybj.utils.exception;

import java.util.HashMap;
import java.util.Map;

/**
 * 服务异常
 * @author caicai.gao
 */
public class ServiceException extends RuntimeException {
	private static final long serialVersionUID = 2731411059449846965L;

	private Map<String, String> errors;

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public ServiceException(String message) {
		super(message);
	}

	public ServiceException(String message, String field, String error) {
		super(message);
		setDataError(field, error);
	}

	public void setDataError(String field, String error) {
		if (errors == null) {
			errors = new HashMap<String, String>();
		}
		errors.put(field, error);
	}

	public Map<String, String> getDataError() {
		return errors;
	}
}
