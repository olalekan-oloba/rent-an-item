package com.omegalambdang.rentanitem.feature.payment.paymentmodule.paystack;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.internal.LinkedTreeMap;
import com.omegalambdang.rentanitem.exception.InvalidPaymentAccountDetailsException;
import com.omegalambdang.rentanitem.feature.integration.HttpClient;
import com.omegalambdang.rentanitem.feature.payment.PaymentAccountDetails;
import com.omegalambdang.rentanitem.feature.payment.PaymentAccountReference;
import com.omegalambdang.rentanitem.feature.payment.PaymentModule;
import com.omegalambdang.rentanitem.feature.payment.RemotePaymentAccountDetails;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.Collection;

@Service("paystack")
public class PayStack implements PaymentModule {

    private final HttpClient httpClient;
    private final PaystackPaymentModulePropertiesConfig  propertiesConfig;

    public PayStack(HttpClient httpClient, PaystackPaymentModulePropertiesConfig propertiesConfig) {
        this.httpClient = httpClient;
        this.propertiesConfig = propertiesConfig;
    }

    @Override
    public String verifyPaymentAccountDetails(PaymentAccountDetails paymentAccountDetails) {

        try {
            var url = propertiesConfig.getBaseUrl() + "/bank/resolve?account_number=" + paymentAccountDetails.accountNo() + "&bank_code=" + paymentAccountDetails.bankCode();
            var jsonString = httpClient.get(url, buildHeaders(), String.class);
            var jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
            var message=jsonObject.get("message").getAsString();
            return jsonObject.get("status").getAsBoolean() ?"":message;
        } catch (HttpClientErrorException e) {
            var jsonObject = JsonParser.parseString(e.getResponseBodyAsString()).getAsJsonObject();
            return jsonObject.get("message").getAsString();
        }
    }

    @Override
    public RemotePaymentAccountDetails createPaymentAccount(PaymentAccountReference paymentAccountReference) {

        //try {
            var transferRecipientRequest = new TransferRecipientRequest();
            transferRecipientRequest.setAccountNo(paymentAccountReference.getAccountNo());
            transferRecipientRequest.setCurrency("NGN"); //todo cannit be hardcoded
            transferRecipientRequest.setBankCode(paymentAccountReference.getBankCode());
            transferRecipientRequest.setName(paymentAccountReference.getAccountName());
            transferRecipientRequest.setType("nuban");

            var url = propertiesConfig.getBaseUrl() + "/transferrecipient";
            var jsonString = httpClient.post(url, buildHeaders(), transferRecipientRequest, String.class);
            var gson = new Gson();
            var payload = gson.fromJson(jsonString, Payload.class);
            LinkedTreeMap<String, Object> data;
            if (payload.data instanceof Collection<?>) {
                data = ((ArrayList<LinkedTreeMap<String, Object>>) payload.data).get(0);
            } else {
                data = (LinkedTreeMap<String, Object>) payload.data;
            }
            var transferRecipientCode = data.getOrDefault("recipient_code", "").toString();
            String bankName = null;
            String accountName = null;
            String bankCode = null;
            String accountNo = null;

            var detail = (LinkedTreeMap<String, Object>) data.get("details");
            if (detail != null) {
                bankName = detail.getOrDefault("bank_name", "").toString();
                accountName = detail.getOrDefault("account_name", "").toString();
                accountNo = detail.getOrDefault("account_number", "").toString();
                bankCode = detail.getOrDefault("bank_code", "").toString();
            }
            return new RemotePaymentAccountDetails(transferRecipientCode, accountName, bankName, bankCode, accountNo);

       // }catch (Exception e){

       // }
       // return null;
    }

    private HttpHeaders buildHeaders() {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Bearer " + propertiesConfig.getSecretKey());
        return headers;
    }
    @Data
    @NoArgsConstructor
    @SuppressWarnings("NullAway.Init")
    private static class Payload {
        private String status;
        private String message;
        private Object data;
    }

}
