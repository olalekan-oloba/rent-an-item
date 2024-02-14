package com.omegalambdang.rentanitem.feature.payment;

import com.omegalambdang.rentanitem.exception.InvalidPaymentAccountDetailsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class PaymentAccountReferenceServiceImpl implements PaymentAccountReferenceService {

    private final PaymentModule paymentModule;
    private final PaymentAccountReferenceRepository repository;

    public PaymentAccountReferenceServiceImpl(PaymentModule paymentModule, PaymentAccountReferenceRepository repository) {
        this.paymentModule = paymentModule;
        this.repository = repository;
    }

    @Override
    public String verifyPaymentAccountDetails(String accountName, String accountNo, String bankCode, String countryCode) {
        return paymentModule.verifyPaymentAccountDetails(new PaymentAccountDetails(accountName, accountNo, bankCode, countryCode));
    }

    @Override
    public PaymentAccountReference createPaymentAccountReference(PaymentAccountReference paymentAccountReference) {
        return this.repository.save(paymentAccountReference);
    }

    @Override
    @Transactional
    public void createRemotePaymentAccount(PaymentAccountReference paymentAccountReference) {
        var remoteAccountDetails = this.paymentModule.createPaymentAccount(paymentAccountReference);
        this.repository.findById(paymentAccountReference.getId()).ifPresent(refreshedEntity -> {
                    refreshedEntity.setBankName(remoteAccountDetails.bankName());
                    refreshedEntity.setPaymentRecipientCode(remoteAccountDetails.paymentAccountRefCode());
                }
        );
    }
}
