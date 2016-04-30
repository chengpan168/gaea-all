package com.gaea.common.exception;

/**
 *
 */
public class BusinessException extends ServiceException{
	private static final long serialVersionUID = -6974825435534947893L;

	public BusinessException() {
		super();
	}

	public BusinessException(String msg, Throwable throwable) {
		super(msg, throwable);
	}
	public BusinessException(String msg,Throwable throwable,String code) {
		super(msg, throwable);
		this.exceptionCode = code;
	}

	public BusinessException(String msg) {
		super(msg);
	}

	public BusinessException(Throwable arg0) {
		super(arg0);
	}



}
