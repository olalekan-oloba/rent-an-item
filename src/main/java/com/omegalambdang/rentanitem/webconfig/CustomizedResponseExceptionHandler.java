package com.omegalambdang.rentanitem.webconfig;


import com.omegalambdang.rentanitem.apiresponse.ApiDataResponse;
import com.omegalambdang.rentanitem.apiresponse.ApiResponseUtil;
import com.omegalambdang.rentanitem.exception.InvalidRequestException;
import com.omegalambdang.rentanitem.validator.ValidationErrors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;
import java.util.Objects;

@ControllerAdvice
@RestController
@Slf4j
public class CustomizedResponseExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ApiDataResponse<?> apiResponse = new ApiDataResponse<>(HttpStatus.BAD_REQUEST);
        apiResponse.addValidationErrors(ex.getBindingResult().getFieldErrors());
        ValidationErrors errors=new ValidationErrors();
        ex.getBindingResult().getFieldErrors().forEach(fieldError -> errors.addError(fieldError.getField(), fieldError.getRejectedValue(), fieldError.getDefaultMessage()));
        if(ex.getBindingResult().hasGlobalErrors()){
            ex.getBindingResult().getGlobalErrors().forEach(globalErr -> {
                Arrays.stream(Objects.requireNonNull(globalErr.getArguments())).forEach(it -> {
                    if (it.getClass().getName().equals("org.springframework.validation.beanvalidation.SpringValidatorAdapter$ResolvableAttribute")) {
                        errors.addError(String.valueOf(it), "", globalErr.getDefaultMessage());
                    }
                });
            });
        }
        apiResponse.setSubErrors(errors.getValidationFieldErrors());
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        ApiDataResponse<?> apiResponse = new ApiDataResponse<>(HttpStatus.UNPROCESSABLE_ENTITY);
        return new ResponseEntity<>(apiResponse, HttpStatus.UNPROCESSABLE_ENTITY);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiDataResponse<Object>> handleException(Exception e) {
        log.error(e.getMessage(), e);
        return ApiResponseUtil.errorResponse(HttpStatus.INTERNAL_SERVER_ERROR,"An unknown error has occurred","", e.getMessage());
    }

    @ExceptionHandler(InvalidRequestException.class)
    public  ResponseEntity<ApiDataResponse<Object>> handleInvalidRequestException(InvalidRequestException e) {
        return ApiResponseUtil.errorResponse(HttpStatus.BAD_REQUEST,e.getErrorMessage(),"",e.getErrorCode());
    }
}
