package com.gaea.common.exception;

/**
 *
 */
public class SystemException extends ServiceException{
	private static final long serialVersionUID = -2597835592314198271L;

	public SystemException() {
		super();
	}

	public SystemException(String msg, Throwable throwable) {
		super(msg, throwable);
	}
	public SystemException(String msg,Throwable throwable,String code) {
		super(msg, throwable);
		this.exceptionCode = code;
	}

	public SystemException(String msg) {
		super(msg);
	}

	public SystemException(Throwable arg0) {
		super(arg0);
	}

}
