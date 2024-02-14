package com.omegalambdang.rentanitem.feature.notification.templateEngine;


import java.util.Map;

public interface TemplateEngine {

    public String processTemplateIntoString(String templateName, Map<String,String> templateTokens) throws TemplateEngineException;

}
