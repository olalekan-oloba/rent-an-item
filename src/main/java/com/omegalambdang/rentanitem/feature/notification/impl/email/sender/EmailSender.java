package com.omegalambdang.rentanitem.feature.notification.impl.email.sender;

import com.omegalambdang.rentanitem.feature.notification.impl.email.Email;
import com.omegalambdang.rentanitem.feature.notification.impl.email.EmailConfig;

public interface EmailSender {
  
  void send(final Email email);

  void setEmailConfig(EmailConfig emailConfig);

}
