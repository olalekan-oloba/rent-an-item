package com.omegalambdang.rentanitem.exception;


public class ServiceException extends GenericRuntimeException {

  public ServiceException(String message, Throwable e) {
    super(message,e);
  }
}
