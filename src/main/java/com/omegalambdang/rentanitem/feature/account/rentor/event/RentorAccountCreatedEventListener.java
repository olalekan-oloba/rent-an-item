package com.omegalambdang.rentanitem.feature.account.rentor.event;


import com.omegalambdang.rentanitem.feature.notification.NotificationService;
import com.omegalambdang.rentanitem.feature.payment.PaymentAccountReferenceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
public class RentorAccountCreatedEventListener {
    private final PaymentAccountReferenceService paymentAccountReferenceService;
    private final NotificationService notificationService;

    public RentorAccountCreatedEventListener(PaymentAccountReferenceService paymentAccountReferenceService, NotificationService notificationService) {
        this.paymentAccountReferenceService = paymentAccountReferenceService;
        this.notificationService = notificationService;
    }

    @TransactionalEventListener
    @Async
    public void createRemoteRentorPaymentAccount(RentorAccountCreatedEvent event) {
        log.info( "..begin creating remote rentor payment account ");
        try {
            paymentAccountReferenceService.createRemotePaymentAccount(event.paymentAccountReference());
        }catch (Exception e){
          log.error("unable to create remote payment account ["+event.paymentAccountReference()+"]",e);
        }
        this.notificationService.sendRentorAccountCreatedNotification(event.rentor());

    }

}

