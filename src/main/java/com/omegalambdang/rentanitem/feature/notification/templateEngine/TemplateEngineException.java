package com.omegalambdang.rentanitem.feature.notification.templateEngine;

import com.omegalambdang.rentanitem.exception.GenericRuntimeException;

public class TemplateEngineException extends GenericRuntimeException {

    public TemplateEngineException(String message) {
        super(message);
    }

    public TemplateEngineException(String message, Throwable e) {
        super(message,e);
    }
}
