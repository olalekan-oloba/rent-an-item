package com.omegalambdang.rentanitem.feature.payment;


import com.omegalambdang.rentanitem.feature.account.rentor.Rentor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentAccountReferenceServiceTest {
    /**
     * System under test (SUT)
     */
    @InjectMocks
    PaymentAccountReferenceServiceImpl paymentAccountReferenceService;
    @Mock
    PaymentModule paymentModule;
    @Mock
    PaymentAccountReferenceRepository repository;

    @BeforeEach
    void setUp() throws Exception {

    }

    @Test
    void testShouldReturnNull_WhenVerifyPaymentAccountDetails_Successesfull() throws Exception {
        //arrange
        var accountNo = "1234567890";
        var accountName = "firstname lastname";
        var bankCode = "001";
        var country = "NGN";

        var paymentAccountDetails = new PaymentAccountDetails(accountName, accountNo, bankCode, country);

        String expectedResult = null;
        //dependencies
        when(this.paymentModule.verifyPaymentAccountDetails(any(PaymentAccountDetails.class))).thenReturn(expectedResult);
        //act
        var result = this.paymentAccountReferenceService.verifyPaymentAccountDetails(accountName, accountNo, bankCode, country);

        //assert
        verify(paymentModule, times(1)).verifyPaymentAccountDetails(assertArg(param -> assertThat(param)
                .usingRecursiveComparison()
                .isEqualTo(paymentAccountDetails)));

        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testShouldReturnErrorMsg_WhenVerifyPaymentAccountDetails_Fail() throws Exception {
        //arrange
        var accountNo = "1234567890";
        var accountName = "firstname lastname";
        var bankCode = "001";
        var country = "NGN";

        var paymentAccountDetails = new PaymentAccountDetails(accountName, accountNo, bankCode, country);

        var expectedResult = "errormsg";
        //dependencies
        when(this.paymentModule.verifyPaymentAccountDetails(any(PaymentAccountDetails.class))).thenReturn(expectedResult);
        //act
        var result = this.paymentAccountReferenceService.verifyPaymentAccountDetails(accountName, accountNo, bankCode, country);

        //assert
        verify(paymentModule, times(1)).verifyPaymentAccountDetails(assertArg(param -> assertThat(param)
                .usingRecursiveComparison()
                .isEqualTo(paymentAccountDetails)));

        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testShouldPersistPaymentAccountReferenceSuccessfully_WhenCreatePaymentAccountReference() throws Exception {
        //arrange
        var expectedPersistedPaymentAcctReference = new PaymentAccountReference();
        expectedPersistedPaymentAcctReference.setAccountNo("1234567890");
        expectedPersistedPaymentAcctReference.setAccountName("firstname lastname");
        expectedPersistedPaymentAcctReference.setPaymentInProgress(false);
        expectedPersistedPaymentAcctReference.setRentor(new Rentor(1L));
        expectedPersistedPaymentAcctReference.setPaymentRecipientCode(null); //to be retieved and populated asycrounously
        expectedPersistedPaymentAcctReference.setCurrency("NGN");
        expectedPersistedPaymentAcctReference.setBankCode("001");

        //dependencies
        when(this.repository.save(any(PaymentAccountReference.class))).thenAnswer(i -> i.getArgument(0));
        //act
        var result = this.paymentAccountReferenceService.createPaymentAccountReference(expectedPersistedPaymentAcctReference);
        //assert
        verify(repository, times(1)).save(assertArg(actualPersisted -> assertThat(actualPersisted)
                .usingRecursiveComparison()
                .isEqualTo(expectedPersistedPaymentAcctReference)));
    }

    @Test
    void testShouldCreateRemotePaymentAccountSuccessfully_WhenCreateRemotePaymentAccount() throws Exception {
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
                "refcode0001",
                paymentAccountReference.getAccountName(),
                paymentAccountReference.getBankName(),
                paymentAccountReference.getBankCode(),
                paymentAccountReference.getAccountNo()
        );
        //dependencies
        when(this.paymentModule.createPaymentAccount(any(PaymentAccountReference.class))).thenReturn(expectedRemoteAcctDetails);
        //act
        this.paymentAccountReferenceService.createRemotePaymentAccount(paymentAccountReference);
        //assert
        verify(this.paymentModule, times(1)).createPaymentAccount(assertArg(actualPersisted -> assertThat(actualPersisted)
                .usingRecursiveComparison()
                .isEqualTo(paymentAccountReference)));
    }

    @Test
    @DisplayName("test update payment account reference with remote reference code")
    void testShouldUpdatePaymentAccountReferenceWithRemoteAccountRefCodeSuccessfully_WhenCreateRemotePaymentAccount() throws Exception {
        //arrange
        var paymentAccountReference = new PaymentAccountReference(1L);
        paymentAccountReference.setAccountNo("1234567890");
        paymentAccountReference.setAccountName("firstname lastname");
        paymentAccountReference.setPaymentInProgress(false);
        paymentAccountReference.setRentor(new Rentor(1L));
        paymentAccountReference.setPaymentRecipientCode(null); //to be retieved and populated asycrounously
        paymentAccountReference.setCurrency("NGN");
        paymentAccountReference.setBankCode("001");

        var expectedRemoteAcctDetails = new RemotePaymentAccountDetails(
                "refcode0001",
                paymentAccountReference.getAccountName(),
                paymentAccountReference.getBankName(),
                paymentAccountReference.getBankCode(),
                paymentAccountReference.getAccountNo()
        );
        var expectedUpdatedAcctRef=paymentAccountReference;
        expectedUpdatedAcctRef.setPaymentRecipientCode(expectedRemoteAcctDetails.paymentAccountRefCode());
        expectedUpdatedAcctRef.setBankName(expectedRemoteAcctDetails.bankName());
        expectedUpdatedAcctRef.setAccountNo(expectedRemoteAcctDetails.paymentAccountRefCode());
        expectedUpdatedAcctRef.setAccountName(expectedRemoteAcctDetails.accountName());
        //dependencies
        when(this.paymentModule.createPaymentAccount(any(PaymentAccountReference.class))).thenReturn(expectedRemoteAcctDetails);
        when(this.repository.findById(anyLong())).thenReturn(Optional.of(paymentAccountReference));

        //act
        this.paymentAccountReferenceService.createRemotePaymentAccount(paymentAccountReference);
        //assert
        assertThat(paymentAccountReference).usingRecursiveComparison().isEqualTo(expectedUpdatedAcctRef);
    }


    @Test
    @DisplayName("test update payment account reference with remote reference code")
    void testShouldNotUpdatePaymentAccountReferenceWithRemoteAccountRefCodeSuccessfully_WhenCreateRemotePaymentAccount_AndPaymentReferenceNotFound() throws Exception {
        //arrange
        var paymentAccountReference = new PaymentAccountReference(1L);
        paymentAccountReference.setAccountNo("1234567890");
        paymentAccountReference.setAccountName("firstname lastname");
        paymentAccountReference.setPaymentInProgress(false);
        paymentAccountReference.setRentor(new Rentor(1L));
        paymentAccountReference.setPaymentRecipientCode(null); //to be retieved and populated asycrounously
        paymentAccountReference.setCurrency("NGN");
        paymentAccountReference.setBankCode("001");

        var expectedRemoteAcctDetails = new RemotePaymentAccountDetails(
                "refcode0001",
                paymentAccountReference.getAccountName(),
                paymentAccountReference.getBankName(),
                paymentAccountReference.getBankCode(),
                paymentAccountReference.getAccountNo()
        );
        var expectedUpdatedAcctRef=paymentAccountReference;
        //dependencies
        when(this.paymentModule.createPaymentAccount(any(PaymentAccountReference.class))).thenReturn(expectedRemoteAcctDetails);
        when(this.repository.findById(anyLong())).thenReturn(Optional.empty());

        //act
        this.paymentAccountReferenceService.createRemotePaymentAccount(paymentAccountReference);
        //assert
        assertThat(paymentAccountReference).usingRecursiveComparison().isEqualTo(expectedUpdatedAcctRef);
    }


}

