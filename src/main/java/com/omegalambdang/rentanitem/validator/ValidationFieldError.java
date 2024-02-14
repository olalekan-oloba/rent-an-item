package com.omegalambdang.rentanitem.validator;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ValidationFieldError {
    private String fieldName;
    private Object fieldValue;
    private List<String> validationErrMsgs;
    public void addError(String message){
        validationErrMsgs.add(message);
    }
}
