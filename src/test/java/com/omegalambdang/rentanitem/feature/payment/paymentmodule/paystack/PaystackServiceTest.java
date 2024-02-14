package com.omegalambdang.rentanitem.feature.payment.paymentmodule.paystack;


import com.omegalambdang.rentanitem.feature.account.rentor.Rentor;
import com.omegalambdang.rentanitem.feature.integration.HttpClient;
import com.omegalambdang.rentanitem.feature.payment.PaymentAccountReference;
import com.omegalambdang.rentanitem.feature.payment.RemotePaymentAccountDetails;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaystackServiceTest {

    /**
     * System under test (SUT)
     */
    @InjectMocks
    PayStack payStack;
    @Mock
    HttpClient httpClient;
    @Mock
    PaystackPaymentModulePropertiesConfig propertiesConfig;

    @ParameterizedTest
    @MethodSource({"payloadProvider"})
    void shouldCreatePaymentAccountSuccessfully(String jsonStrPayload) throws Exception {

        //arrange
        var paymentAccountReference = new PaymentAccountReference();
        paymentAccountReference.setAccountNo("1234567890");
        paymentAccountReference.setAccountName("firstname lastname");
        paymentAccountReference.setPaymentInProgress(false);
        paymentAccountReference.setRentor(new Rentor(1L));
        paymentAccountReference.setPaymentRecipientCode(null); //to be retieved and populated asycrounously
        paymentAccountReference.setCurrency("NGN");
        paymentAccountReference.setBankCode("001");

        var expectedRemoteAcctDetails = new RemotePaymentAccountDetails(
                "RCP_q4372n3ve5jpfx2",
                "James Bond",
                "Bank One",
                "001",
                "0123456789"
        ) ;       //input
        when(this.propertiesConfig.getBaseUrl()).thenReturn("basesurl");
        when(this.propertiesConfig.getSecretKey()).thenReturn("secret");
        when(this.httpClient.post(anyString(),any(HttpHeaders.class),any(),any())).thenReturn(jsonStrPayload);
        //act
        var actualRemoteAcctDet=payStack.createPaymentAccount(mock(PaymentAccountReference.class));
        //assert
        assertThat(actualRemoteAcctDet).usingRecursiveComparison().isEqualTo(expectedRemoteAcctDetails);
    }

    static Stream<Arguments> payloadProvider() {

        var retrievedPayLoad=getRetrievedPayload();
        var createdPayLoad=getCreatedPayload();
        return Stream.of(
                arguments(retrievedPayLoad),
                arguments(createdPayLoad)

        );
    }

    @ParameterizedTest
    @MethodSource({"payloadProvider"})
    void testShouldHandleAndLogExeption_WhenCreatePaymentAccount_AndRemoteCallReturnError(String jsonStrPayload) throws Exception {
       //TODO
      /*  //arrange
        var paymentAccountReference = new PaymentAccountReference();
        paymentAccountReference.setAccountNo("1234567890");
        paymentAccountReference.setAccountName("firstname lastname");
        paymentAccountReference.setPaymentInProgress(false);
        paymentAccountReference.setRentor(new Rentor(1L));
        paymentAccountReference.setPaymentRecipientCode(null); //to be retieved and populated asycrounously
        paymentAccountReference.setCurrency("NGN");
        paymentAccountReference.setBankCode("001");

        var expectedRemoteAcctDetails = new RemotePaymentAccountDetails(
                "RCP_q4372n3ve5jpfx2",
                "James Bond",
                "Bank One",
                "001",
                "0123456789"
        ) ;       //input
        when(this.propertiesConfig.getBaseUrl()).thenReturn("basesurl");
        when(this.propertiesConfig.getSecretKey()).thenReturn("secret");
        when(this.httpClient.post(anyString(),any(HttpHeaders.class),any(),any())).thenReturn(jsonStrPayload);
        //act
        var actualRemoteAcctDet=payStack.createPaymentAccount(mock(PaymentAccountReference.class));
        //assert
        assertThat(actualRemoteAcctDet).usingRecursiveComparison().isEqualTo(expectedRemoteAcctDetails);*/
    }

    
    /**
     * returned transfer recipient payload for a new transfer recipient account
     * this payload retrieves recipient info as a single object
     * @return
     */
    private static String getCreatedPayload() {
        return "{\n" +
                "    \"status\": true,\n" +
                "    \"message\": \"Transfer recipient created successfully\",\n" +
                "    \"data\": {\n" +
                "        \"active\": true,\n" +
                "        \"createdAt\": \"2023-06-30T22:14:31.000Z\",\n" +
                "        \"currency\": \"NGN\",\n" +
                "        \"description\": null,\n" +
                "        \"domain\": \"test\",\n" +
                "        \"email\": null,\n" +
                "        \"id\": 56314336,\n" +
                "        \"integration\": 1019527,\n" +
                "        \"metadata\": null,\n" +
                "        \"name\": \"Olalekan Lekan\",\n" +
                "        \"recipient_code\": \"RCP_q4372n3ve5jpfx2\",\n" +
                "        \"type\": \"nuban\",\n" +
                "        \"updatedAt\": \"2023-07-12T11:32:50.000Z\",\n" +
                "        \"is_deleted\": false,\n" +
                "        \"isDeleted\": false,\n" +
                "        \"details\": {\n" +
                "            \"authorization_code\": null,\n" +
                "            \"account_number\": \"0123456789\",\n" +
                "            \"account_name\": \"James Bond\",\n" +
                "            \"bank_code\": \"001\",\n" +
                "            \"bank_name\": \"Bank One\"\n" +
                "        }\n" +
                "    }\n" +
                "}";
    }


    /**
     * returned transfer recipient payload for already existing transfer recipient
     * this payload retrieves recipient info in a collection
     * @return
     */
    private static String getRetrievedPayload() {
        return "{\n" +
                "  \"status\": true,\n" +
                "  \"message\": \"Recipients retrieved\",\n" +
                "  \"data\": [\n" +
                "    {\n" +
                "      \"active\": true,\n" +
                "      \"createdAt\": \"2023-06-30T22:14:31.000Z\",\n" +
                "      \"currency\": \"NGN\",\n" +
                "      \"description\": null,\n" +
                "      \"domain\": \"test\",\n" +
                "      \"email\": null,\n" +
                "      \"id\": 56314336,\n" +
                "      \"integration\": 1019527,\n" +
                "      \"metadata\": null,\n" +
                "      \"name\": \"Olalekan Lekan\",\n" +
                "      \"recipient_code\": \"RCP_q4372n3ve5jpfx2\",\n" +
                "      \"type\": \"nuban\",\n" +
                "      \"updatedAt\": \"2023-07-12T11:25:01.000Z\",\n" +
                "      \"is_deleted\": false,\n" +
                "      \"isDeleted\": false,\n" +
                "      \"details\": {\n" +
                "        \"authorization_code\": null,\n" +
                "        \"account_number\": \"0123456789\",\n" +
                "        \"account_name\": \"James Bond\",\n" +
                "        \"bank_code\": \"001\",\n" +
                "        \"bank_name\": \"Bank One\"\n" +
                "      }\n" +
                "    }\n" +
                "  ],\n" +
                "  \"meta\": {\n" +
                "    \"total\": 1,\n" +
                "    \"skipped\": 0,\n" +
                "    \"perPage\": 50,\n" +
                "    \"page\": 1,\n" +
                "    \"pageCount\": 1\n" +
                "  }\n" +
                "}";

    }

}
