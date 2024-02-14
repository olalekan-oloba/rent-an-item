package com.omegalambdang.rentanitem.exception;


public class InvalidPaymentAccountDetailsException extends GenericRuntimeException {

	private static final String ERROR_CODE = "400";
	public InvalidPaymentAccountDetailsException() {
		super("Invalid Request");
	}

	public InvalidPaymentAccountDetailsException(String errorCode, String message) {
		super(errorCode,message);
	}

	public InvalidPaymentAccountDetailsException(String message) {
		super(ERROR_CODE,message);
	}

	public InvalidPaymentAccountDetailsException(String message, Throwable e) {
		super(message,e);
		this.setErrorCode(ERROR_CODE);
	}


}
