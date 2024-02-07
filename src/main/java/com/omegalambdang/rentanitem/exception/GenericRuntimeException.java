package com.omegalambdang.rentanitem.exception;

import jakarta.annotation.Nullable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenericRuntimeException extends RuntimeException {
  @Nullable
  private String errorCode;
  @Nullable
  private String errorMessage;

  public GenericRuntimeException(String errorCode, String errorMessage) {
    this.setErrorCode(errorCode);
    this.setErrorMessage(errorMessage);
  }

  public GenericRuntimeException(String errorMessage) {
    this.setErrorMessage(errorMessage);
  }

  public GenericRuntimeException(Throwable exception) {
    super(exception);
    this.setErrorCode(null);
    this.setErrorMessage(null);
  }

  public GenericRuntimeException(String errorMessage, Throwable exception) {
    super(exception);
    this.setErrorCode(null);
    this.setErrorMessage(errorMessage);
  }

  public GenericRuntimeException(String errorCode, String errorMessage, Throwable exception) {
    super(exception);
    this.setErrorCode(errorCode);
    this.setErrorMessage(errorMessage);
  }
}
