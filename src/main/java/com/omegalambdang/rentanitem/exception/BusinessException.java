package com.omegalambdang.rentanitem.exception;


public class BusinessException extends GenericRuntimeException {

  public BusinessException(String message, Throwable e) {
    super(message,e);
  }
}
