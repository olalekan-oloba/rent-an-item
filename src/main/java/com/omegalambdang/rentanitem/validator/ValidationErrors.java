package com.omegalambdang.rentanitem.validator;

import jakarta.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ValidationErrors {

    private Map<String,ValidationFieldError> validationFieldErrors;
    private @Nullable
    static ValidationErrors instance;

    public ValidationErrors(){
        validationFieldErrors=new HashMap<>();
    }

    static {
        instance=null;
    }

    public static ValidationErrors getInstance(){
        if(instance == null){
            instance = new ValidationErrors();
        }
        return instance;
    }

    public void addError(String field, Object rejectedValue, String defaultMessage){
        var validationFieldError=this.validationFieldErrors.computeIfAbsent(field,x-> new ValidationFieldError(x,rejectedValue,new ArrayList<>()));
        validationFieldError.addError(defaultMessage);
    }

    public boolean hasFieldErrors() {
        return CollectionUtils.isNotEmpty(this.validationFieldErrors.values());
    }
    public Collection<ValidationFieldError> getValidationFieldErrors(){
        return this.validationFieldErrors.values();
    }
    public boolean hasFieldError(String field) {
        return this.validationFieldErrors.containsKey(field);
    }
}


