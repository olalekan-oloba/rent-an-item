package com.omegalambdang.rentanitem.apiresponse;

import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Slf4j
public class ApiResponseUtil {

    private ApiResponseUtil() {
    }

    public static  <T> ResponseEntity<ApiDataResponse<T>> response(HttpStatus status, T data, String message ){
        return ApiResponseUtil.getResponse(status,data,message);
    }

    private static  <T> ResponseEntity<ApiDataResponse<T>> getResponse(HttpStatus status,@Nullable T data, @Nullable String message ){
        ApiDataResponse<T> ar = new ApiDataResponse<>(HttpStatus.OK);
        ar.setData(data);
        ar.setMessage(message);
        return new ResponseEntity<>(ar,status);
    }

    public static <T> ResponseEntity<ApiDataResponse<T>> errorResponse(HttpStatus status,@Nullable String errMsg,@Nullable String debugMsg,@Nullable String customErrCd ){
        return ApiResponseUtil.getErrResponse(status,errMsg,debugMsg,customErrCd);
    }

    private static <T> ResponseEntity<ApiDataResponse<T>> getErrResponse(HttpStatus status,@Nullable String errMsg,@Nullable String debugMsg,@Nullable String customErrCd  ){
        var ar = new ApiDataResponse<T>(status);
        ar.setMessage(errMsg);
        ar.setErrorCode(customErrCd);
        ar.setDebugMessage(debugMsg);
        return new ResponseEntity<>(ar,status);
    }



}
