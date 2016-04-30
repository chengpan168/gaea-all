package com.gaea.common.exception;

/**
 *
 */
public class ParamException extends ServiceException{
	private static final long serialVersionUID = -4758495943331710032L;

	public ParamException() {
		super();
	}

	public ParamException(String message, Throwable throwable) {
		super(message, throwable);
	}

	public ParamException(String message) {
		super(message);
	}

	public ParamException(Throwable throwable) {
		super(throwable);
	}

}
