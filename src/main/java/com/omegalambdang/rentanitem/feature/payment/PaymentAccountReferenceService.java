package com.omegalambdang.rentanitem.feature.payment;

public interface PaymentAccountReferenceService {
    String verifyPaymentAccountDetails(String accountName, String accountNo, String bankCode, String countryCode);
    PaymentAccountReference createPaymentAccountReference(PaymentAccountReference paymentAccountReference);
    void createRemotePaymentAccount(PaymentAccountReference paymentAccountReference);
}
