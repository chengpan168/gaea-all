package com.gaea.common.exception;

/**
 *
 */
public class ServiceException extends RuntimeException{
	protected String exceptionCode;
	/**
	 * 
	 */
	private static final long serialVersionUID = -2357521295745486102L;

	public ServiceException() {
		super();
	}

	public ServiceException(String msg, Throwable throwable) {
		super(msg, throwable);
	}
	public ServiceException(String msg,Throwable throwable,String code) {
		super(msg, throwable);
		this.exceptionCode = code;
	}

	public ServiceException(String msg) {
		super(msg);
	}

	public ServiceException(Throwable arg0) {
		super(arg0);
	}

	public String getExceptionCode() {
		return exceptionCode;
	}

	public void setExceptionCode(String exceptionCode) {
		this.exceptionCode = exceptionCode;
	}
}
