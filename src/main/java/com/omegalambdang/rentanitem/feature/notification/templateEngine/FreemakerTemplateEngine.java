package com.omegalambdang.rentanitem.feature.notification.templateEngine;

import freemarker.template.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.util.Map;

@Service("freemakerTemplateEngine")
public class FreemakerTemplateEngine implements TemplateEngine {

    private final Configuration freemarkerConfig;
    @Autowired
    public FreemakerTemplateEngine(Configuration freemarkerConfig) {
        this.freemarkerConfig = freemarkerConfig;
    }

    private final static String TEMPLATE_FILE_EXT=".ftl";
    private final static String TEMPLATE_PATH = "/templates/mail/";

    @Override
    public String processTemplateIntoString(String templateName, Map<String, String> templateTokens) {
        try {
            freemarkerConfig.setClassForTemplateLoading(this.getClass(), TEMPLATE_PATH);
            return FreeMarkerTemplateUtils
                    .processTemplateIntoString(freemarkerConfig.getTemplate(templateName+TEMPLATE_FILE_EXT), templateTokens);
        } catch (Exception e) {
            throw new TemplateEngineException("unable to process template into string",e);
        }
    }

}
