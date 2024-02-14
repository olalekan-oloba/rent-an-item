package com.omegalambdang.rentanitem.feature.notification.impl.email.sender.impl;

import com.omegalambdang.rentanitem.feature.notification.impl.email.Email;
import com.omegalambdang.rentanitem.feature.notification.impl.email.EmailConfig;
import com.omegalambdang.rentanitem.feature.notification.impl.email.sender.EmailSender;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.lang.Nullable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

@Slf4j
@RequiredArgsConstructor
@Component("defaultEmailSenderImpl")
@Profile("!prod")
public class DefaultEmailSenderImpl implements EmailSender {

    private final JavaMailSender emailSender;

    @Value("${spring.mail.host}")
    private @Nullable String SMTP_HOST;

    @Value("${spring.mail.username}")
    private @Nullable String SMTP_USERNAME;

    @Value("${spring.mail.password}")
    private @Nullable String SMTP_PASSWORD;

    @Value("${spring.mail.properties.mail.smtp.port}")
    private  int SMTP_PORT;
    @Value("${spring.mail.properties.mail.smtp.auth}")
    private  boolean SMTP_AUTH;
    @Value("${spring.mail.properties.mail.smtp.starttls.enable}")
    private  boolean SMTP_START_TLS_ENABLE;

    @Override
    public void send(Email email){

        log.info("Begin sending email ");
        final String eml = email.getFrom();
        final String from = email.getFromEmail();
        final String to = email.getTo();
        final String subject = email.getSubject();
        //final FileAttachement attachement = email.getAttachement();
        Objects.requireNonNull(to);

        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws MessagingException, IOException {

                JavaMailSenderImpl impl = (JavaMailSenderImpl) emailSender;
                // if email configuration is present in Database, use the same
                //if (emailConfig != null) {
                impl.setProtocol("smtp");
                impl.setHost(SMTP_HOST);
                impl.setPort(SMTP_PORT);
                impl.setUsername(SMTP_USERNAME);
                impl.setPassword(SMTP_PASSWORD);

                Properties prop = new Properties();
                prop.put("mail.smtp.auth", SMTP_AUTH);
                prop.put("mail.smtp.starttls.enable",SMTP_START_TLS_ENABLE);
                impl.setJavaMailProperties(prop);
                // }

                mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(to));

                InternetAddress inetAddress = new InternetAddress();

                inetAddress.setPersonal(eml);
                inetAddress.setAddress(from);

                mimeMessage.setFrom(inetAddress);
                mimeMessage.setSubject(subject);
                mimeMessage.setText(email.getBody(), "utf-8", "html");

                if(email.getAttachement()!=null){
                    MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
                    messageHelper.addAttachment(email.getAttachement().getFileName(), new ByteArrayResource(email.getAttachement().getInputStream().readAllBytes()));
                    if(email.getBody()!=null) {
                        messageHelper.setText(email.getBody(), true);
                    }
                }

            }
        };
        emailSender.send(preparator);
    }

    @Override
    public void setEmailConfig(@Nullable EmailConfig emailConfig) {
        // this.emailConfig=emailConfig;
    }
}
