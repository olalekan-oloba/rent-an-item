package com.omegalambdang.rentanitem.feature.notification;

import com.omegalambdang.rentanitem.feature.account.rentor.Rentor;

public interface NotificationService {
    void sendRentorAccountCreatedNotification(Rentor rentor);
}
