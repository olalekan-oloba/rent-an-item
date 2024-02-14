package com.omegalambdang.rentanitem.feature.notification.impl.email;

public interface EmailService {

    void sendEmail(Email email);

    void sendAsyncEmail(Email email);

}
