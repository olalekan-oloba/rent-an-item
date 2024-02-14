package com.omegalambdang.rentanitem.feature.account.rentor;


import com.omegalambdang.rentanitem.common.audit.AuditSection;
import com.omegalambdang.rentanitem.common.dto.IdResponseDto;
import com.omegalambdang.rentanitem.exception.ResourceConflictException;
import com.omegalambdang.rentanitem.feature.account.Gender;
import com.omegalambdang.rentanitem.feature.account.UserStatus;
import com.omegalambdang.rentanitem.feature.account.rentor.event.RentorAccountCreatedEventsPublisher;
import com.omegalambdang.rentanitem.feature.payment.PaymentAccountReference;
import com.omegalambdang.rentanitem.feature.payment.PaymentAccountReferenceService;
import com.omegalambdang.rentanitem.util.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentorAccountServiceTest {
    /**
     * System under test (SUT)
     */
    @InjectMocks
    RentorAccountServiceImpl rentorAccountService;
    @Mock
    RentorRepository rentorRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    PaymentAccountReferenceService paymentAccountReferenceService;
    @Mock
    RentorAccountCreatedEventsPublisher createRentorAccountEventsPublisher;

    @BeforeEach
    void setUp() throws Exception {

    }

    @Test
    void testShouldPersistRentorSuccessfully_WhenCreateRentorAccount() throws Exception {
        //arrange
        var request = SignUpRentorRequestDto.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("aa@gm.co")
                //.state(zone.getId())
                .phone("08137640746")
                .gender(Gender.MALE.getCode())
                .bankCode("001")
                .accountNo("1234567890")
                .accountName("firstname lastname")
                .contactAddress("abc xyz")
                .password("pass1@PASS")
                .confirmPassword("pass1@PASS")
                .customerAgreement(true)
                .build();

        var encrytedPass="encryptedpass";

        var expectedPersistedRentor = new Rentor();
        expectedPersistedRentor.setFirstName(request.getFirstName());
        expectedPersistedRentor.setLastName(request.getLastName());
        expectedPersistedRentor.setPhone(request.getPhone());
        expectedPersistedRentor.setEmail(request.getEmail());
        expectedPersistedRentor.setContactAddress(request.getContactAddress());
        expectedPersistedRentor.setIsRentor(true);
        expectedPersistedRentor.setGender(Gender.MALE);
        expectedPersistedRentor.setPasswordResetToken(null);
        expectedPersistedRentor.setPasswordResetVldtyTerm(null);
        expectedPersistedRentor.setLastLogin(null);
        expectedPersistedRentor.setId(1L);
        expectedPersistedRentor.setEmailVerified(false);
        expectedPersistedRentor.setStatus(UserStatus.ACTIVE);
        expectedPersistedRentor.setPassword(encrytedPass);
        expectedPersistedRentor.setCustomerAgreement(true);

        //dependencies
        when(this.passwordEncoder.encode(anyString())).thenReturn(encrytedPass);
        when(this.rentorRepository.save(any(Rentor.class))).thenAnswer(i -> {
            var x = i.getArgument(0, Rentor.class);
            x.setId(expectedPersistedRentor.getId());
            return x;
        });

        //act
        this.rentorAccountService.createRentorAccount(request);

        //assert
        verify(rentorRepository).save(assertArg(param -> assertThat(param)
                .usingRecursiveComparison()
                .ignoringFieldsOfTypes(AuditSection.class)
                .isEqualTo(expectedPersistedRentor)));
    }

    @Test
    void testAfterRentorCreatedSuccess_ShouldCreatePaymentAccountReferenceSuccessfully_WhenCreateRentorAccount() throws Exception {
        //arrange
        var request = SignUpRentorRequestDto.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("aa@gm.co")
                //.state(zone.getId())
                .phone("08137640746")
                .gender(Gender.MALE.getCode())
                .bankCode("001")
                .accountNo("1234567890")
                .accountName("firstname lastname")
                .contactAddress("abc xyz")
                .password("pass1@PASS")
                .confirmPassword("pass1@PASS")
                .build();

        var expectedPersistedRentor = new Rentor();
        expectedPersistedRentor.setFirstName(request.getFirstName());
        expectedPersistedRentor.setLastName(request.getLastName());
        expectedPersistedRentor.setPhone(request.getPhone());
        expectedPersistedRentor.setEmail(request.getEmail());
        expectedPersistedRentor.setContactAddress(request.getContactAddress());
        expectedPersistedRentor.setIsRentor(true);
        expectedPersistedRentor.setGender(Gender.MALE);
        expectedPersistedRentor.setPasswordResetToken(null);
        expectedPersistedRentor.setPasswordResetVldtyTerm(null);
        expectedPersistedRentor.setLastLogin(null);
        expectedPersistedRentor.setId(null);
        expectedPersistedRentor.setEmailVerified(false);
        expectedPersistedRentor.setStatus(UserStatus.ACTIVE);
        expectedPersistedRentor.setPassword("encryptedpass");
        expectedPersistedRentor.setId(1L);

        var expectedCreatedPaymentAcctReference = new PaymentAccountReference();
        expectedCreatedPaymentAcctReference.setAccountNo(request.getAccountNo());
        expectedCreatedPaymentAcctReference.setAccountName(request.getAccountName());
        expectedCreatedPaymentAcctReference.setPaymentInProgress(false);
        expectedCreatedPaymentAcctReference.setRentor(expectedPersistedRentor);
        expectedCreatedPaymentAcctReference.setPaymentRecipientCode(null); //to be retieved and populated asycrounously
        expectedCreatedPaymentAcctReference.setCurrency("NGN");
        expectedCreatedPaymentAcctReference.setBankCode(request.getBankCode());

        //dependencies
        when(this.passwordEncoder.encode(anyString())).thenReturn(expectedPersistedRentor.getPassword());
        when(this.rentorRepository.save(any(Rentor.class))).thenReturn(expectedPersistedRentor);
        //payment
        when(this.paymentAccountReferenceService.createPaymentAccountReference(any(PaymentAccountReference.class))).thenAnswer(i -> i.getArgument(0));

        //act
        this.rentorAccountService.createRentorAccount(request);

        //assert
        verify(paymentAccountReferenceService).createPaymentAccountReference(assertArg(param -> assertThat(param)
                .usingRecursiveComparison()
                .ignoringFieldsOfTypes(AuditSection.class)
                .isEqualTo(expectedCreatedPaymentAcctReference)));
    }

    @Test
    void testShouldPublishCreateRentorAccountEventSuccessfully_WhenCreateRentorAccount() throws Exception {
        //arrange
        var request = SignUpRentorRequestDto.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("aa@gm.co")
                //.state(zone.getId())
                .phone("08137640746")
                .gender(Gender.MALE.getCode())
                .bankCode("001")
                .accountNo("1234567890")
                .accountName("firstname lastname")
                .contactAddress("abc xyz")
                .password("pass1@PASS")
                .confirmPassword("pass1@PASS")
                .build();

        var expectedPersistedRentor = new Rentor();
        expectedPersistedRentor.setFirstName(request.getFirstName());
        expectedPersistedRentor.setLastName(request.getLastName());
        expectedPersistedRentor.setPhone(request.getPhone());
        expectedPersistedRentor.setEmail(request.getEmail());
        expectedPersistedRentor.setContactAddress(request.getContactAddress());
        expectedPersistedRentor.setIsRentor(true);
        expectedPersistedRentor.setGender(Gender.MALE);
        expectedPersistedRentor.setPasswordResetToken(null);
        expectedPersistedRentor.setPasswordResetVldtyTerm(null);
        expectedPersistedRentor.setLastLogin(null);
        expectedPersistedRentor.setId(null);
        expectedPersistedRentor.setEmailVerified(false);
        expectedPersistedRentor.setStatus(UserStatus.ACTIVE);
        expectedPersistedRentor.setPassword("encryptedpass");
        expectedPersistedRentor.setId(1L);

        var expectedCreatedPaymentAcctReference = new PaymentAccountReference();
        expectedCreatedPaymentAcctReference.setAccountNo(request.getAccountNo());
        expectedCreatedPaymentAcctReference.setAccountName(request.getAccountName());
        expectedCreatedPaymentAcctReference.setPaymentInProgress(false);
        expectedCreatedPaymentAcctReference.setRentor(expectedPersistedRentor);
        expectedCreatedPaymentAcctReference.setPaymentRecipientCode(null); //to be retieved and populated asycrounously
        expectedCreatedPaymentAcctReference.setCurrency("NGN");
        expectedCreatedPaymentAcctReference.setBankCode(request.getBankCode());
        expectedCreatedPaymentAcctReference.setId(1L);

        //dependencies
        when(this.passwordEncoder.encode(anyString())).thenReturn(expectedPersistedRentor.getPassword());
        when(this.rentorRepository.save(any(Rentor.class))).thenReturn(expectedPersistedRentor);
        when(this.paymentAccountReferenceService.createPaymentAccountReference(any(PaymentAccountReference.class))).thenReturn(expectedCreatedPaymentAcctReference);

        //act
        this.rentorAccountService.createRentorAccount(request);

        //assert
        verify(createRentorAccountEventsPublisher).publishCreateRentorEvent(assertArg(param -> {
            assertThat(param.rentor())
                    .usingRecursiveComparison()
                    .ignoringFieldsOfTypes(AuditSection.class)
                    .isEqualTo(expectedPersistedRentor);
            assertThat(param.paymentAccountReference())
                    .usingRecursiveComparison()
                    .ignoringFieldsOfTypes(AuditSection.class)
                    .isEqualTo(expectedCreatedPaymentAcctReference);
        }));
    }


    @Test
    void testShouldReturnRentorIdSuccessfully_WhenCreateRentorAccount() throws Exception {
        //arrange
        var request = SignUpRentorRequestDto.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("aa@gm.co")
                //.state(zone.getId())
                .phone("08137640746")
                .gender(Gender.MALE.getCode())
                .bankCode("001")
                .accountNo("1234567890")
                .accountName("firstname lastname")
                .contactAddress("abc xyz")
                .password("pass1@PASS")
                .confirmPassword("pass1@PASS")
                .build();

        var expectedPersistedRentor = new Rentor();
        expectedPersistedRentor.setFirstName(request.getFirstName());
        expectedPersistedRentor.setLastName(request.getLastName());
        expectedPersistedRentor.setPhone(request.getPhone());
        expectedPersistedRentor.setEmail(request.getEmail());
        expectedPersistedRentor.setContactAddress(request.getContactAddress());
        expectedPersistedRentor.setIsRentor(true);
        expectedPersistedRentor.setGender(Gender.MALE);
        expectedPersistedRentor.setPasswordResetToken(null);
        expectedPersistedRentor.setPasswordResetVldtyTerm(null);
        expectedPersistedRentor.setLastLogin(null);
        expectedPersistedRentor.setId(null);
        expectedPersistedRentor.setEmailVerified(false);
        expectedPersistedRentor.setStatus(UserStatus.ACTIVE);
        expectedPersistedRentor.setPassword("encryptedpass");
        expectedPersistedRentor.setId(1L);

        var expectedCreatedPaymentAcctReference = new PaymentAccountReference();
        expectedCreatedPaymentAcctReference.setAccountNo(request.getAccountNo());
        expectedCreatedPaymentAcctReference.setAccountName(request.getAccountName());
        expectedCreatedPaymentAcctReference.setPaymentInProgress(false);
        expectedCreatedPaymentAcctReference.setRentor(expectedPersistedRentor);
        expectedCreatedPaymentAcctReference.setPaymentRecipientCode(null); //to be retieved and populated asycrounously
        expectedCreatedPaymentAcctReference.setCurrency("NGN");
        expectedCreatedPaymentAcctReference.setBankCode(request.getBankCode());
        expectedCreatedPaymentAcctReference.setId(1L);

        var expectedRentorId = new IdResponseDto(expectedPersistedRentor.getId());

        //dependencies
        when(this.passwordEncoder.encode(anyString())).thenReturn(expectedPersistedRentor.getPassword());
        when(this.rentorRepository.save(any(Rentor.class))).thenReturn(expectedPersistedRentor);
        when(this.paymentAccountReferenceService.createPaymentAccountReference(any(PaymentAccountReference.class))).thenReturn(expectedCreatedPaymentAcctReference);

        //act
        var id = this.rentorAccountService.createRentorAccount(request);

        //assert
        assertThat(expectedRentorId).isEqualTo(id);
    }

    //
    @Test
    void testThrowConflict_WhenCreateRentorAccount_WithExistingEmail() {
        //arrange
        var request = SignUpRentorRequestDto.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("aa@gm.co")
                .phone("08137640746")
                .gender(Gender.MALE.getCode())
                .bankCode("001")
                .accountNo("1234567890")
                .accountName("firstname lastname")
                .contactAddress("abc xyz")
                .password("pass1@PASS")
                .confirmPassword("pass1@PASS")
                .build();

        var expectedException = ResourceConflictException.class;
        var expectedExceptionMsg = "User with email [aa@gm.co] already exists";

        when(this.rentorRepository.existsByEmail(anyString())).thenReturn(true);
        var ex = assertThrows(expectedException, () -> {
            this.rentorAccountService.createRentorAccount(request);
        });

        //assert
        assertThat(expectedExceptionMsg).isEqualTo(ex.getErrorMessage());

        verify(this.rentorRepository, never()).save(any(Rentor.class));
        verify(this.paymentAccountReferenceService, never()).createPaymentAccountReference(any(PaymentAccountReference.class));
    }

}


      /*  try (MockedStatic mocked = mockStatic(Foo.class)) {
            mocked.when(Foo::method).thenReturn("bar");
            assertEquals("bar", Foo.method());
            mocked.verify(Foo::method);
        }*/
