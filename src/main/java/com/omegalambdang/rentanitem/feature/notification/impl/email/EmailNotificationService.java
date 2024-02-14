package com.omegalambdang.rentanitem.feature.notification.impl.email;

import com.omegalambdang.rentanitem.config.PropertiesConfig;
import com.omegalambdang.rentanitem.feature.account.rentor.Rentor;
import com.omegalambdang.rentanitem.feature.configuration.CoreConfigurationService;
import com.omegalambdang.rentanitem.feature.notification.NotificationService;
import com.omegalambdang.rentanitem.feature.notification.templateEngine.TemplateEngine;
import com.omegalambdang.rentanitem.util.FellowshipDateUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static com.omegalambdang.rentanitem.constants.AppConstants.*;
import static com.omegalambdang.rentanitem.constants.CoreConfigurationConstants.COMPANY_NAME_CONFG_KEY;
import static com.omegalambdang.rentanitem.constants.CoreConfigurationConstants.SUPPORT_EMAIL_CONFG_KEY;

@Service
@Slf4j
public class EmailNotificationService implements NotificationService {

    private static final String EMAIL_FOOTER_COPYRIGHT = "EMAIL_FOOTER_COPYRIGHT";
    private static final String EMAIL_DISCLAIMER = "EMAIL_DISCLAIMER";
    private static final String EMAIL_SPAM_DISCLAIMER = "EMAIL_SPAM_DISCLAIMER";
    private static final String EMAIL_FROM_EMAIL = "EMAIL_FROM_EMAIL";
    private static final String LOGOPATH = "LOGOPATH";
    private static final String EMAIL_USER_LASTNAME="EMAIL_USER_LASTNAME";
    private static final String EMAIL_FULLNAME="EMAIL_FULLNAME";
    private static final String EMAIL_SUBJ_CREATE_USER_EMAIL = "EMAIL_SUBJ_CREATE_USER_EMAIL";
    private static final String EMAIL_CREATE_ADMIN_USER_TMPL = "email_new_rentor_created";

    private final CoreConfigurationService coreConfigurationService;
    private final TemplateEngine templateEngine;
    private final EmailService emailService;
    private final PropertiesConfig propertiesConfig;

    public EmailNotificationService(CoreConfigurationService coreConfigurationService, TemplateEngine templateEngine, EmailService emailService, PropertiesConfig propertiesConfig) {
        this.coreConfigurationService = coreConfigurationService;
        this.templateEngine = templateEngine;
        this.emailService = emailService;
        this.propertiesConfig = propertiesConfig;
    }

    @Override
    public void sendRentorAccountCreatedNotification(Rentor rentor) {
        Map<String, String> templateTokens = this.createEmailObjectsMap();
        templateTokens.put(EMAIL_USER_LASTNAME, rentor.getLastName());
        templateTokens.put(EMAIL_FULLNAME,rentor.getLastName()+" "+rentor.getFirstName());
        templateTokens.put("APP_URL", propertiesConfig.getApi().getUrlDomain()  + "/login");

        String subject = this.coreConfigurationService.findConfigValueByKey(EMAIL_SUBJ_CREATE_USER_EMAIL);
        if (StringUtils.isEmpty(subject)) {
            subject = DEFAULT_CREATE_NEW_USER_EMAIL_SUBJ;
            log.warn("Found no Create password email subject defined in configuration. Using application defined subject:" + subject + " as a fallback");
        }

        Email email = this.createEmail(rentor.getEmail(), subject, EMAIL_CREATE_ADMIN_USER_TMPL, templateTokens);
        this.emailService.sendEmail(email);
    }

    private Email createEmail(String to, String subject, String template, Map<String, String> templateTokens) {

        Email email = new Email();

        String companyName = coreConfigurationService.findConfigValueByKey(COMPANY_NAME_CONFG_KEY);
        if (StringUtils.isEmpty(companyName)) {
            companyName = DEFAULT_COMPANY_NAME;
            log.warn("Found no Company Name defined in configuration. Using application defined name:" + companyName + " as a fallback");
        }
        email.setFrom(companyName);
        email.setFromEmail(templateTokens.get(EMAIL_FROM_EMAIL));
        email.setSubject(subject);
        email.setTo(to);
        email.setBody(this.templateEngine.processTemplateIntoString(template, templateTokens));
        return email;
    }


    private Map<String, String> createEmailObjectsMap() {

        String companyName =  coreConfigurationService.findConfigValueByKey(COMPANY_NAME_CONFG_KEY);
        if (StringUtils.isEmpty(companyName)) {
            log.warn("No Company name found in configuration,using default application defined value");
            companyName = DEFAULT_COMPANY_NAME;
        }
        String[] copyArg = {companyName, FellowshipDateUtils.getPresentYear()};

        String supportEmail = coreConfigurationService.findConfigValueByKey(SUPPORT_EMAIL_CONFG_KEY);
        if (StringUtils.isEmpty(supportEmail)) {
            log.warn("No Support email found in configuration,using default application defined value");
            supportEmail = DEFAULT_SUPPORT_EMAIL;
        }
        String[] supportEmailArg = {supportEmail};

        String emailDisclaimer = "This email address was given to us by you or by one of our customers. If you feel that you have received this email in error, please send an email to " + supportEmailArg[0] + " for de-activation";
        String emailSpamDisclaimer = "This email is sent in accordance with the US CAN-SPAM Law in effect 2004-01-01. Removal requests can be sent to this address and will be honored and respected";

        Map<String, String> templateTokens = new HashMap<>();

        templateTokens.put(LOGOPATH, "");
        templateTokens.put(EMAIL_FOOTER_COPYRIGHT, "Copyright @ " + copyArg[0] + " " + copyArg[1] + ", All Rights Reserved");
        templateTokens.put(EMAIL_DISCLAIMER, emailDisclaimer);
        templateTokens.put(EMAIL_SPAM_DISCLAIMER, emailSpamDisclaimer);
        templateTokens.put(EMAIL_FROM_EMAIL, supportEmail);
        templateTokens.put("EMAIL_APP_NAME", DEFAULT_APP_NAME);

        return templateTokens;
    }

}
