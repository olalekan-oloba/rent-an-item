package com.omegalambdang.rentanitem.apiresponse;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.omegalambdang.rentanitem.validator.ValidationFieldError;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * ApiDataResponse
 */
@JsonInclude(JsonInclude.Include.ALWAYS)
public class ApiDataResponse<T> {

  private HttpStatus status;
  private String message;
  private String errorCode;
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
  private LocalDateTime timestamp;
  private String debugMessage;
  @JsonIgnore
  private List<ApiSubError> subErrors;
  private Collection<ValidationFieldError> subErrors2;

  private T data;

  private ApiDataResponse() {
    timestamp = LocalDateTime.now();
  }

  public ApiDataResponse(HttpStatus status) {
    this();
    this.status = status;
  }

  private void addSubError(ApiSubError subError) {
    if (subErrors == null) {
      subErrors = new ArrayList<>();
    }
    subErrors.add(subError);
  }

  private void addValidationError(String field, Object rejectedValue, String message) {
    addSubError(new ApiValidationError(field, rejectedValue, message));
  }

  private void addValidationError(FieldError fieldError) {
    this.addValidationError(fieldError.getField(), fieldError.getRejectedValue(),
        fieldError.getDefaultMessage());
  }

  public void addValidationErrors(List<FieldError> fieldErrors) {
    fieldErrors.forEach(this::addValidationError);
  }


  public HttpStatus getStatus() {
    return status;
  }

  public void setStatus(HttpStatus status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getErrorCode() {
    return errorCode;
  }

  public void setErrorCode(String errorCode) {
    this.errorCode = errorCode;
  }

  public LocalDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(LocalDateTime timestamp) {
    this.timestamp = timestamp;
  }

  public String getDebugMessage() {
    return debugMessage;
  }

  public void setDebugMessage(String debugMessage) {
    this.debugMessage = debugMessage;
  }

  public List<ApiSubError> getSubErrors() {
    return subErrors;
  }

  public void setSubErrors(List<ApiSubError> subErrors) {
    this.subErrors = subErrors;
  }

  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

  public void setSubErrors(Collection<ValidationFieldError> validationFieldErrors) {
    this.subErrors2=validationFieldErrors;
  }

  public Collection<ValidationFieldError> getSubErrors2() {
    return subErrors2;
  }
}