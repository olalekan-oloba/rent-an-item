package com.omegalambdang.rentanitem.feature.account.rentor.event;

import com.omegalambdang.rentanitem.feature.account.rentor.Rentor;
import com.omegalambdang.rentanitem.feature.payment.PaymentAccountReference;

public record RentorAccountCreatedEvent(Rentor rentor, PaymentAccountReference paymentAccountReference) {

}
