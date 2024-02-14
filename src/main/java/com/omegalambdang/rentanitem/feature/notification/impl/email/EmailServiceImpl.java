package com.omegalambdang.rentanitem.feature.notification.impl.email;

import com.omegalambdang.rentanitem.feature.notification.impl.email.sender.EmailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service("emailService")
public class EmailServiceImpl implements EmailService {

    private final EmailSender emailSender;
    @Override
    public void sendEmail(Email email) {
        this.send(email);
    }

    @Override
    @Async
    public void sendAsyncEmail(Email email) {
        this.send(email);
    }

    private void send(Email email) {
        try {
            if (StringUtils.isEmpty(email.getFromEmail()) || StringUtils.isEmpty(email.getTo())) {
                log.error("Cannot send email: Invalid email address- source:" + email.getFromEmail() + " | destination:" + email.getTo());
                return;
            }

            EmailConfig emailConfig = null;// this.getEmailConfiguration();
            if (emailConfig != null) {
                this.emailSender.setEmailConfig(emailConfig);
            }
            this.emailSender.send(email);

        } catch (Exception e) {
            log.error("Cannot send email notification", e);
        }
    }


}
