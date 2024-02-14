package com.omegalambdang.rentanitem.feature.payment;

public interface PaymentModule {
    String verifyPaymentAccountDetails(PaymentAccountDetails paymentAccountDetails);
    RemotePaymentAccountDetails createPaymentAccount(PaymentAccountReference paymentAccountReference);
}
