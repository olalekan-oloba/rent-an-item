package com.omegalambdang.rentanitem.exception;


import com.omegalambdang.rentanitem.validator.ValidationErrors;

public class InvalidRequestException extends GenericRuntimeException {

	private static final String ERROR_CODE = "400";
	private ValidationErrors validationErrors;
	public InvalidRequestException() {
		super("Invalid Request");
	}

	public InvalidRequestException(String errorCode, String message) {
		super(errorCode,message);
	}

	public InvalidRequestException(String message) {
		super(ERROR_CODE,message);
	}

	public InvalidRequestException(String message, Throwable e) {
		super(message,e);
		this.setErrorCode(ERROR_CODE);
	}

	public InvalidRequestException(ValidationErrors validationErrors) {
		super("Invalid Request");
		this.validationErrors = validationErrors;
	}
	public ValidationErrors getValidationErrors() {
		return validationErrors;
	}

}
