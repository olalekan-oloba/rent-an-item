package com.omegalambdang.rentanitem.feature.payment.paymentmodule.paystack;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@SuppressWarnings("NullAway.Init")
public class TransferRecipientRequest {
    String type;
    String name;
    @JsonProperty("account_number")
    String accountNo;
    @JsonProperty("bank_code")
    String bankCode;
    String currency;
}
