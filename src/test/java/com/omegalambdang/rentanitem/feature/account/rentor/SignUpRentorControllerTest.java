package com.omegalambdang.rentanitem.feature.account.rentor;

import com.omegalambdang.rentanitem.AbstractControllerTest;
import com.omegalambdang.rentanitem.common.audit.AuditSection;
import com.omegalambdang.rentanitem.feature.account.Gender;
import com.omegalambdang.rentanitem.util.annotation.UnsecuredWebMvcTest;
import com.omegalambdang.rentanitem.common.dto.IdResponseDto;
import com.omegalambdang.rentanitem.feature.payment.PaymentAccountReferenceService;
import com.omegalambdang.rentanitem.util.TestUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.ResultActions;

import java.time.*;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@UnsecuredWebMvcTest(SignUpRentorController.class)
class SignUpRentorControllerTest extends AbstractControllerTest {

    @MockBean
    PaymentAccountReferenceService paymentAccountService;
    @MockBean
    RentorAccountService accountService;

    //FIRSTNAME
    @ParameterizedTest
    @ValueSource(strings = {"", "  ", "   "})
    @DisplayName("test empty first name")
    void testFailWith400_WhenSignUpRentor_WithEmptyFirstName(String firstName) throws Exception {
        //arrange
        var request = SignUpRentorRequestDto.builder()
                .firstName(firstName).build();
        var expectedStatus = status().isBadRequest();
        var expectedFieldName = "firstName";
        var expectedMsg = "First name must not be empty";
        //act
        var resultActions = this.performSignUpRentorRequest(request);
        //assert
        resultActions.andExpect(expectedStatus)
                .andExpect(TestUtils.responseBody().containsError(expectedFieldName, expectedMsg));
    }

    @Test
    @DisplayName("test null first name")
    void testFailWith400_WhenSignUpRentor_WithNullFirstName() throws Exception {
        //arrange
        var request = SignUpRentorRequestDto.builder()
                .firstName(null).build();
        var expectedStatus = status().isBadRequest();
        var expectedFieldName = "firstName";
        var expectedMsg = "First name must not be empty";
        //act
        var resultActions = this.performSignUpRentorRequest(request);
        //assert
        resultActions.andExpect(expectedStatus)
                .andExpect(TestUtils.responseBody().containsError(expectedFieldName, expectedMsg));
    }

    @Test
    @DisplayName("test max size first name")
    void testFailWith400_WhenSignUpRentor_WithFirstNameGreaterThanAllowableMaxSize() throws Exception {
        //arrange
        int maxSize = 100;
        String firstName = RandomStringUtils.randomAlphabetic(maxSize) + 1;
        var request = SignUpRentorRequestDto.builder()
                .firstName(firstName).build();
        var expectedStatus = status().isBadRequest();
        var expectedFieldName = "firstName";
        var expectedMsg = "First name must not be more than " + maxSize + " xters";
        //act
        var resultActions = this.performSignUpRentorRequest(request);
        //assert
        resultActions.andExpect(expectedStatus)
                .andExpect(TestUtils.responseBody().containsError(expectedFieldName, expectedMsg));
    }

    //LASTNAME
    @ParameterizedTest
    @ValueSource(strings = {"", "  ", "   "})
    @DisplayName("test empty last name")
    void testFailWith400_WhenSignUpRentor_WithEmptyLastName(String lastName) throws Exception {
        //arrange
        var request = SignUpRentorRequestDto.builder()
                .lastName(lastName).build();
        var expectedStatus = status().isBadRequest();
        var expectedFieldName = "lastName";
        var expectedMsg = "Last name must not be empty";
        //act
        var resultActions = this.performSignUpRentorRequest(request);
        //assert
        resultActions.andExpect(expectedStatus)
                .andExpect(TestUtils.responseBody().containsError(expectedFieldName, expectedMsg));
    }

    @Test
    @DisplayName("test validate null last name")
    void testFailWith400_WhenSignUpRentor_WithNullLastName() throws Exception {
        //arrange
        var request = SignUpRentorRequestDto.builder()
                .lastName(null).build();
        var expectedStatus = status().isBadRequest();
        var expectedFieldName = "lastName";
        var expectedMsg = "Last name must not be empty";
        //act
        var resultActions = this.performSignUpRentorRequest(request);
        //assert
        resultActions.andExpect(expectedStatus)
                .andExpect(TestUtils.responseBody().containsError(expectedFieldName, expectedMsg));
    }

    @Test
    @DisplayName("test max size last name")
    void testFailWith400_WhenSignUpRentor_WithLastNameGreaterThanAllowableMaxSize() throws Exception {
        //arrange
        int maxSize = 100;
        String lastName = RandomStringUtils.randomAlphabetic(maxSize) + 1;
        var request = SignUpRentorRequestDto.builder()
                .lastName(lastName).build();
        var expectedStatus = status().isBadRequest();
        var expectedFieldName = "lastName";
        var expectedMsg = "Last name must not be more than " + maxSize + " xters";
        //act
        var resultActions = this.performSignUpRentorRequest(request);
        //assert
        resultActions.andExpect(expectedStatus)
                .andExpect(TestUtils.responseBody().containsError(expectedFieldName, expectedMsg));
    }

    //EMAIL
    @ParameterizedTest
    @ValueSource(strings = {"", "  ", "   ","   "})
    @DisplayName("test not empty email")
    void testFailWith400_WhenSignUpRentor_WithEmptyEmail(String email) throws Exception {
        //arrange
        var request = SignUpRentorRequestDto.builder()
                .build();
        var expectedStatus = status().isBadRequest();
        var expectedFieldName = "email";
        var expectedMsg = "Email must not be empty";
        PropertyUtils.setSimpleProperty(request, expectedFieldName, email);
        //act
        var resultActions = this.performSignUpRentorRequest(request);
        //assert
        resultActions.andExpect(expectedStatus)
                .andExpect(TestUtils.responseBody().containsError(expectedFieldName, expectedMsg));
    }
    @Test
    @DisplayName("test validate not null email")
    void testFailWith400_WhenSignUpRentor_WithNullEmail() throws Exception {
        //arrange
        var request = SignUpRentorRequestDto.builder().build();
        var expectedStatus = status().isBadRequest();
        var expectedFieldName = "email";
        var expectedMsg = "Email must not be empty";
        PropertyUtils.setSimpleProperty(request, expectedFieldName, null);

        //act
        var resultActions = this.performSignUpRentorRequest(request);
        //assert
        resultActions.andExpect(expectedStatus)
                .andExpect(TestUtils.responseBody().containsError(expectedFieldName, expectedMsg));
    }

    @Test
    @DisplayName("test max size email")
    void testFailWith400_WhenSignUpRentor_WithEmailGreaterThanAllowableMaxSize() throws Exception {
        //arrange
        int maxSize = 255;
        String email = RandomStringUtils.randomAlphabetic(maxSize) + 1;
        var request = SignUpRentorRequestDto.builder().build();
        var expectedStatus = status().isBadRequest();
        var expectedFieldName = "email";
        var expectedMsg = "Email must not be more than " + maxSize + " xters";
        PropertyUtils.setSimpleProperty(request, expectedFieldName, email);

        //act
        var resultActions = this.performSignUpRentorRequest(request);
        //assert
        resultActions.andExpect(expectedStatus)
                .andExpect(TestUtils.responseBody().containsError(expectedFieldName, expectedMsg));
    }

    @Test
    @DisplayName("test max size email")
    void testFailWith400_WhenSignUpRentor_WithEmailUserNamePartGreaterThanAllowableMaxSize() throws Exception {
        //arrange
        int maxSize = 64;
        String email=RandomStringUtils.randomAlphabetic(maxSize+1) + "@abc.com";
        var request = SignUpRentorRequestDto.builder().build();
        var expectedStatus = status().isBadRequest();
        var expectedFieldName = "email";
        var expectedMsg = "Email not a valid email format";
        PropertyUtils.setSimpleProperty(request, expectedFieldName, email);

        //act
        var resultActions = this.performSignUpRentorRequest(request);
        //assert
        resultActions.andExpect(expectedStatus)
                .andExpect(TestUtils.responseBody().containsError(expectedFieldName, expectedMsg));
    }


    @ParameterizedTest
    @ValueSource(strings = {
            "aa", //no @ symbol,no domain and subdomain
            "aa.bc.cc", //no @ symbol
            "aa@",//no domain and subdomain
            "aa@bx",//no subdomain
            "aa@bx.",//no subdomain
            "@bx.cc",//no username
    })
    @DisplayName("test validate invalid email")
    void testFailWith400_WhenSignUpRentor_WithValidEmail(String invalidEmail) throws Exception {
        //arrange
        var request = SignUpRentorRequestDto.builder()
                .build();
        var expectedStatus = status().isBadRequest();
        var expectedFieldName = "email";
        var expectedMsg = "Email not a valid email format";
        PropertyUtils.setSimpleProperty(request, expectedFieldName, invalidEmail);
        //act
        var resultActions = this.performSignUpRentorRequest(request);
        //assert
        resultActions.andExpect(expectedStatus)
                .andExpect(TestUtils.responseBody().containsError(expectedFieldName, expectedMsg));
    }

    //PHONE
    @ParameterizedTest
    @ValueSource(strings = {"", "  ", "   ","   "})
    @DisplayName("test not empty phone")
    void testFailWith400_WhenSignUpRentor_WithEmptyPhone(String phone) throws Exception {
        //arrange
        var request = SignUpRentorRequestDto.builder()
                .build();
        var expectedStatus = status().isBadRequest();
        var expectedFieldName = "phone";
        var expectedMsg = "Phone must not be empty";
        PropertyUtils.setSimpleProperty(request, expectedFieldName, phone);
        //act
        var resultActions = this.performSignUpRentorRequest(request);
        //assert
        resultActions.andExpect(expectedStatus)
                .andExpect(TestUtils.responseBody().containsError(expectedFieldName, expectedMsg));
    }

    @Test
    @DisplayName("test validate not null phone")
    void testFailWith400_WhenSignUpRentor_WithNullPhone() throws Exception {
        //arrange
        var request = SignUpRentorRequestDto.builder().build();
        var expectedStatus = status().isBadRequest();
        var expectedFieldName = "phone";
        var expectedMsg = "Phone must not be empty";
        PropertyUtils.setSimpleProperty(request, expectedFieldName, null);

        //act
        var resultActions = this.performSignUpRentorRequest(request);
        //assert
        resultActions.andExpect(expectedStatus)
                .andExpect(TestUtils.responseBody().containsError(expectedFieldName, expectedMsg));
    }

    @Test
    @DisplayName("test max size phone")
    void testFailWith400_WhenSignUpRentor_WithPhoneGreaterThanAllowableMaxSize() throws Exception {
        //arrange
        int maxSize = 100;
        String phone = RandomStringUtils.randomAlphabetic(maxSize) + 1;
        var request = SignUpRentorRequestDto.builder().build();
        var expectedStatus = status().isBadRequest();
        var expectedFieldName = "phone";
        var expectedMsg = "Phone must not be more than " + maxSize + " xters";
        PropertyUtils.setSimpleProperty(request, expectedFieldName, phone);

        //act
        var resultActions = this.performSignUpRentorRequest(request);
        //assert
        resultActions.andExpect(expectedStatus)
                .andExpect(TestUtils.responseBody().containsError(expectedFieldName, expectedMsg));
    }

    //GENDER
    @ParameterizedTest
    @ValueSource(strings = {"", "  ", "   ","   "})
    @DisplayName("test not empty gender")
    void testFailWith400_WhenSignUpRentor_WithEmptyGender(String gender) throws Exception {
        //arrange
        var request = SignUpRentorRequestDto.builder()
                .build();
        var expectedStatus = status().isBadRequest();
        var expectedFieldName = "gender";
        var expectedMsg = "Gender must not be empty";
        PropertyUtils.setSimpleProperty(request, expectedFieldName, gender);
        //act
        var resultActions = this.performSignUpRentorRequest(request);
        //assert
        resultActions.andExpect(expectedStatus)
                .andExpect(TestUtils.responseBody().containsError(expectedFieldName, expectedMsg));
    }

    @Test
    @DisplayName("test validate not null gender")
    void testFailWith400_WhenSignUpRentor_WithNullGender() throws Exception {
        //arrange
        var request = SignUpRentorRequestDto.builder().build();
        var expectedStatus = status().isBadRequest();
        var expectedFieldName = "gender";
        var expectedMsg = "Gender must not be empty";
        PropertyUtils.setSimpleProperty(request, expectedFieldName, null);

        //act
        var resultActions = this.performSignUpRentorRequest(request);
        //assert
        resultActions.andExpect(expectedStatus)
                .andExpect(TestUtils.responseBody().containsError(expectedFieldName, expectedMsg));
    }
    //todo test case for valid gender enum

    //CONTACT ADDRESS
    @ParameterizedTest
    @ValueSource(strings = {"", "  ", "   ","   "})
    @DisplayName("test not empty contactAddress")
    void testFailWith400_WhenSignUpRentor_WithEmptyContactAddress(String contactAddress) throws Exception {
        //arrange
        var request = SignUpRentorRequestDto.builder()
                .build();
        var expectedStatus = status().isBadRequest();
        var expectedFieldName = "contactAddress";
        var expectedMsg = "Contact Address must not be empty";
        PropertyUtils.setSimpleProperty(request, expectedFieldName, contactAddress);
        //act
        var resultActions = this.performSignUpRentorRequest(request);
        //assert
        resultActions.andExpect(expectedStatus)
                .andExpect(TestUtils.responseBody().containsError(expectedFieldName, expectedMsg));
    }

    @Test
    @DisplayName("test validate not null contactAddress")
    void testFailWith400_WhenSignUpRentor_WithNullContactAddress() throws Exception {
        //arrange
        var request = SignUpRentorRequestDto.builder().build();
        var expectedStatus = status().isBadRequest();
        var expectedFieldName = "contactAddress";
        var expectedMsg = "Contact Address must not be empty";
        PropertyUtils.setSimpleProperty(request, expectedFieldName, null);

        //act
        var resultActions = this.performSignUpRentorRequest(request);
        //assert
        resultActions.andExpect(expectedStatus)
                .andExpect(TestUtils.responseBody().containsError(expectedFieldName, expectedMsg));
    }

    @Test
    @DisplayName("test max size contactAddress")
    void testFailWith400_WhenSignUpRentor_WithContactAddressGreaterThanAllowableMaxSize() throws Exception {
        //arrange
        int maxSize = 255;
        String contactAddress = RandomStringUtils.randomAlphabetic(maxSize) + 1;
        var request = SignUpRentorRequestDto.builder().build();
        var expectedStatus = status().isBadRequest();
        var expectedFieldName = "contactAddress";
        var expectedMsg = "Contact Address must not be more than " + maxSize + " xters";
        PropertyUtils.setSimpleProperty(request, expectedFieldName, contactAddress);

        //act
        var resultActions = this.performSignUpRentorRequest(request);
        //assert
        resultActions.andExpect(expectedStatus)
                .andExpect(TestUtils.responseBody().containsError(expectedFieldName, expectedMsg));
    }

    //ACCOUNT NAME
    @ParameterizedTest
    @ValueSource(strings = {"", "  ", "   ","   "})
    @DisplayName("test not empty account name")
    void testFailWith400_WhenSignUpRentor_WithEmptyAccountName(String accountName) throws Exception {
        //arrange
        var request = SignUpRentorRequestDto.builder()
                .build();
        var expectedStatus = status().isBadRequest();
        var expectedFieldName = "accountName";
        var expectedMsg = "Account Name must not be empty";
        PropertyUtils.setSimpleProperty(request, expectedFieldName, accountName);
        //act
        var resultActions = this.performSignUpRentorRequest(request);
        //assert
        resultActions.andExpect(expectedStatus)
                .andExpect(TestUtils.responseBody().containsError(expectedFieldName, expectedMsg));
    }
    @Test
    @DisplayName("test validate not null account Name")
    void testFailWith400_WhenSignUpRentor_WithNullAccountName() throws Exception {
        //arrange
        var request = SignUpRentorRequestDto.builder().build();
        var expectedStatus = status().isBadRequest();
        var expectedFieldName = "accountName";
        var expectedMsg = "Account Name must not be empty";
        PropertyUtils.setSimpleProperty(request, expectedFieldName, null);

        //act
        var resultActions = this.performSignUpRentorRequest(request);
        //assert
        resultActions.andExpect(expectedStatus)
                .andExpect(TestUtils.responseBody().containsError(expectedFieldName, expectedMsg));
    }

    @Test
    @DisplayName("test max size accountName")
    void testFailWith400_WhenSignUpRentor_WithAccountNameGreaterThanAllowableMaxSize() throws Exception {
        //arrange
        int maxSize = 255;
        String accountName = RandomStringUtils.randomAlphabetic(maxSize) + 1;
        var request = SignUpRentorRequestDto.builder().build();
        var expectedStatus = status().isBadRequest();
        var expectedFieldName = "accountName";
        var expectedMsg = "Account Name must not be more than " + maxSize + " xters";
        PropertyUtils.setSimpleProperty(request, expectedFieldName, accountName);

        //act
        var resultActions = this.performSignUpRentorRequest(request);
        //assert
        resultActions.andExpect(expectedStatus)
                .andExpect(TestUtils.responseBody().containsError(expectedFieldName, expectedMsg));
    }

    //ACCOUNT NO
    @ParameterizedTest
    @ValueSource(strings = {"", "  ", "   ","   "})
    @DisplayName("test not empty account no")
    void testFailWith400_WhenSignUpRentor_WithEmptyAccountNo(String accountNo) throws Exception {
        //arrange
        var request = SignUpRentorRequestDto.builder()
                .build();
        var expectedStatus = status().isBadRequest();
        var expectedFieldName = "accountNo";
        var expectedMsg = "Account Number must not be empty";
        PropertyUtils.setSimpleProperty(request, expectedFieldName, accountNo);
        //act
        var resultActions = this.performSignUpRentorRequest(request);
        //assert
        resultActions.andExpect(expectedStatus)
                .andExpect(TestUtils.responseBody().containsError(expectedFieldName, expectedMsg));
    }
    @Test
    @DisplayName("test validate not null accountNo")
    void testFailWith400_WhenSignUpRentor_WithNullAccountNo() throws Exception {
        //arrange
        var request = SignUpRentorRequestDto.builder().build();
        var expectedStatus = status().isBadRequest();
        var expectedFieldName = "accountNo";
        var expectedMsg = "Account Number must not be empty";
        PropertyUtils.setSimpleProperty(request, expectedFieldName, null);

        //act
        var resultActions = this.performSignUpRentorRequest(request);
        //assert
        resultActions.andExpect(expectedStatus)
                .andExpect(TestUtils.responseBody().containsError(expectedFieldName, expectedMsg));
    }

    @ParameterizedTest
    @ValueSource(strings = {"xxxx","yyyy","1045xxx"})
    @DisplayName("test account no is integer")
    void testFailWith400_WhenSignUpRentor_WithInvalidAccountNoNotNumber(String accountNo) throws Exception {
        //arrange
        var request = SignUpRentorRequestDto.builder()
                .build();
        var expectedStatus = status().isBadRequest();
        var expectedFieldName = "accountNo";
        var expectedMsg = "Account Number must be a number";
        PropertyUtils.setSimpleProperty(request, expectedFieldName, accountNo);
        //act
        var resultActions = this.performSignUpRentorRequest(request);
        //assert
        resultActions.andExpect(expectedStatus)
                .andExpect(TestUtils.responseBody().containsError(expectedFieldName, expectedMsg));
    }

    @ParameterizedTest
    @ValueSource(ints = {8,9,11,12})
    @DisplayName("test account no length")
    void testFailWith400_WhenSignUpRentor_WithInvalidAccountNoSize(int size) throws Exception {
        //arrange
        int validLength=10;
        String accountNo=RandomStringUtils.randomNumeric(size);
        var request = SignUpRentorRequestDto.builder()
                .build();
        var expectedStatus = status().isBadRequest();
        var expectedFieldName = "accountNo";
        var expectedMsg = "Account Number must be "+validLength+" xters long";
        PropertyUtils.setSimpleProperty(request, expectedFieldName, accountNo);
        //act
        var resultActions = this.performSignUpRentorRequest(request);
        //assert
        resultActions.andExpect(expectedStatus)
                .andExpect(TestUtils.responseBody().containsError(expectedFieldName, expectedMsg));
    }
    
    //BANK CODE
    @ParameterizedTest
    @ValueSource(strings = {"", "  ", "   ","   "})
    @DisplayName("test not empty bankCode")
    void testFailWith400_WhenSignUpRentor_WithEmptyBankCode(String bankCode) throws Exception {
        //arrange
        var request = SignUpRentorRequestDto.builder()
                .build();
        var expectedStatus = status().isBadRequest();
        var expectedFieldName = "bankCode";
        var expectedMsg = "Bank must not be empty";
        PropertyUtils.setSimpleProperty(request, expectedFieldName, bankCode);
        //act
        var resultActions = this.performSignUpRentorRequest(request);
        //assert
        resultActions.andExpect(expectedStatus)
                .andExpect(TestUtils.responseBody().containsError(expectedFieldName, expectedMsg));
    }

    @Test
    @DisplayName("test validate not null bank code")
    void testFailWith400_WhenSignUpRentor_WithNullBankCode() throws Exception {
        //arrange
        var request = SignUpRentorRequestDto.builder().build();
        var expectedStatus = status().isBadRequest();
        var expectedFieldName = "bankCode";
        var expectedMsg = "Bank must not be empty";
        PropertyUtils.setSimpleProperty(request, expectedFieldName, null);

        //act
        var resultActions = this.performSignUpRentorRequest(request);
        //assert
        resultActions.andExpect(expectedStatus)
                .andExpect(TestUtils.responseBody().containsError(expectedFieldName, expectedMsg));
    }

    @ParameterizedTest
    @ValueSource(strings = {"xxxx","yyyy","1045xxx"})
    @DisplayName("test bank code is number")
    void testFailWith400_WhenSignUpRentor_WithInvalidBankCodeNotNumber(String bankCode) throws Exception {
        //arrange
        var request = SignUpRentorRequestDto.builder()
                .build();
        var expectedStatus = status().isBadRequest();
        var expectedFieldName = "bankCode";
        var expectedMsg = "Bank code must be a number";
        PropertyUtils.setSimpleProperty(request, expectedFieldName, bankCode);
        //act
        var resultActions = this.performSignUpRentorRequest(request);
        //assert
        resultActions.andExpect(expectedStatus)
                .andExpect(TestUtils.responseBody().containsError(expectedFieldName, expectedMsg));
    }

    @ParameterizedTest
    @ValueSource(ints = {1,2,4,5})
    @DisplayName("test account no length")
    void testFailWith400_WhenSignUpRentor_WithInvalidBankCodeSize(int size) throws Exception {
        //arrange
        int validLength=3;
        String bankCode=RandomStringUtils.randomNumeric(size);
        var request = SignUpRentorRequestDto.builder()
                .build();
        var expectedStatus = status().isBadRequest();
        var expectedFieldName = "bankCode";
        var expectedMsg = "Bank Code must be "+validLength+" xters long";
        PropertyUtils.setSimpleProperty(request, expectedFieldName, bankCode);
        //act
        var resultActions = this.performSignUpRentorRequest(request);
        //assert
        resultActions.andExpect(expectedStatus)
                .andExpect(TestUtils.responseBody().containsError(expectedFieldName, expectedMsg));
    }

    //STATE
    @Test
    @DisplayName("test validate state is integer greater than zero")
    void testFailWith400_WhenSignUpRentor_WithNullState() throws Exception {
        //arrange
        var request = SignUpRentorRequestDto.builder().build();
        var expectedStatus = status().isBadRequest();
        var expectedFieldName = "state";
        var expectedMsg = "State must be number greater than zero";
        PropertyUtils.setSimpleProperty(request, expectedFieldName, 0);

        //act
        var resultActions = this.performSignUpRentorRequest(request);
        //assert
        resultActions.andExpect(expectedStatus)
                .andExpect(TestUtils.responseBody().containsError(expectedFieldName, expectedMsg));
    }

    //PASSWORD
    @ParameterizedTest
    @ValueSource(strings = {"", "  ", "   ","   "})
    @DisplayName("test not empty password")
    void testFailWith400_WhenSignUpRentor_WithEmptyPassword(String password) throws Exception {

        //arrange
        var request = SignUpRentorRequestDto.builder()
                .build();
        var expectedStatus = status().isBadRequest();
        var expectedFieldName = "password";
        var expectedMsg = "Password must not be empty";
        PropertyUtils.setSimpleProperty(request, expectedFieldName, password);
        //act
        var resultActions = this.performSignUpRentorRequest(request);
        //assert
        resultActions.andExpect(expectedStatus)
                .andExpect(TestUtils.responseBody().containsError(expectedFieldName, expectedMsg));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "  ", "   ","   ","xxxx","ccc"})
    @DisplayName("test same password and confirmation password")
    void testFailWith400_WhenSignUpRentor_AndPasswordAndConfimationPasswordNotSame(String confirmationPassword) throws Exception {
        //arrange
        var request = SignUpRentorRequestDto.builder()
                .password("pass")
                .confirmPassword(confirmationPassword)
                .build();
        var expectedStatus = status().isBadRequest();
        var expectedFieldName = "password";
        var expectedFieldName2 = "confirmPassword";
        var expectedMsg = "Password and Confirm Password do not match";
        //act
        var resultActions = this.performSignUpRentorRequest(request);
        //assert
        resultActions.andExpect(expectedStatus)
                .andExpect(TestUtils.responseBody().containsError(expectedFieldName, expectedMsg))
                .andExpect(TestUtils.responseBody().containsError(expectedFieldName2, expectedMsg));
    }

    @ParameterizedTest
    @ValueSource(strings = {"ABCDE","FDFD"})
    @DisplayName("test fail password no lower")
    void testFailWith400_WhenSignUpRentor_AndPasswordNotContainsAtLeastALowerCaseXter(String invalidPassword) throws Exception {
        //arrange
        var request = SignUpRentorRequestDto.builder()
                .build();
        var expectedStatus = status().isBadRequest();
        var expectedFieldName = "password";
        var expectedMsg = "Password must contain at least one lowercase letter";
        PropertyUtils.setSimpleProperty(request, expectedFieldName, invalidPassword);
        //act
        var resultActions = this.performSignUpRentorRequest(request);
        //assert
        resultActions.andExpect(expectedStatus)
                .andExpect(TestUtils.responseBody().containsError(expectedFieldName, expectedMsg));
    }

    @ParameterizedTest
    @ValueSource(strings = {"absc","fdfd"})
    @DisplayName("test fail password no uppercase")
    void testFailWith400_WhenSignUpRentor_AndPasswordNotContainsAtLeastAUpperCaseXter(String invalidPassword) throws Exception {
        //arrange
        var request = SignUpRentorRequestDto.builder()
                .build();
        var expectedStatus = status().isBadRequest();
        var expectedFieldName = "password";
        var expectedMsg = "Password must contain at least one lowercase letter";
        PropertyUtils.setSimpleProperty(request, expectedFieldName, invalidPassword);
        //act
        var resultActions = this.performSignUpRentorRequest(request);
        //assert
        resultActions.andExpect(expectedStatus)
                .andExpect(TestUtils.responseBody().containsError(expectedFieldName, expectedMsg));
    }

    @ParameterizedTest
    @ValueSource(strings = {"aBsc","fdYd"})
    @DisplayName("test fail password no special xter")
    void testFailWith400_WhenSignUpRentor_AndPasswordNotContainsAtLeastASpecialXter(String invalidPassword) throws Exception {
        //arrange
        var request = SignUpRentorRequestDto.builder()
                .build();
        var expectedStatus = status().isBadRequest();
        var expectedFieldName = "password";
        var expectedMsg = "Password must contain at least one lowercase letter";
        PropertyUtils.setSimpleProperty(request, expectedFieldName, invalidPassword);
        //act
        var resultActions = this.performSignUpRentorRequest(request);
        //assert
        resultActions.andExpect(expectedStatus)
                .andExpect(TestUtils.responseBody().containsError(expectedFieldName, expectedMsg));
    }

    @ParameterizedTest
    @ValueSource(strings = {"aBsc@","fdYd%"})
    @DisplayName("test fail password no digit")
    void testFailWith400_WhenSignUpRentor_AndPasswordNotContainsAtLeastADigit(String invalidPassword) throws Exception {
        //arrange
        var request = SignUpRentorRequestDto.builder()
                .build();
        var expectedStatus = status().isBadRequest();
        var expectedFieldName = "password";
        var expectedMsg = "Password must contain at least one lowercase letter";
        PropertyUtils.setSimpleProperty(request, expectedFieldName, invalidPassword);
        //act
        var resultActions = this.performSignUpRentorRequest(request);
        //assert
        resultActions.andExpect(expectedStatus)
                .andExpect(TestUtils.responseBody().containsError(expectedFieldName, expectedMsg));
    }

    @ParameterizedTest
    @ValueSource(strings = {"aBsc@1","fdYd%3"})
    @DisplayName("test fail password length less than")
    void testFailWith400_WhenSignUpRentor_AndPasswordLenghtLessThanRequired(String invalidPassword) throws Exception {
        //arrange
        var request = SignUpRentorRequestDto.builder()
                .build();
        var expectedStatus = status().isBadRequest();
        var expectedFieldName = "password";
        var expectedMsg = "Password must contain at least one lowercase letter";
        PropertyUtils.setSimpleProperty(request, expectedFieldName, invalidPassword);
        //act
        var resultActions = this.performSignUpRentorRequest(request);
        //assert
        resultActions.andExpect(expectedStatus)
                .andExpect(TestUtils.responseBody().containsError(expectedFieldName, expectedMsg));
    }

    @Test
    @DisplayName("test validate not null password")
    void testFailWith400_WhenSignUpRentor_WithNullPassword() throws Exception {
        //arrange
        var request = SignUpRentorRequestDto.builder().build();
        var expectedStatus = status().isBadRequest();
        var expectedFieldName = "password";
        var expectedMsg = "Password must not be empty";
        PropertyUtils.setSimpleProperty(request, expectedFieldName, null);

        //act
        var resultActions = this.performSignUpRentorRequest(request);
        //assert
        resultActions.andExpect(expectedStatus)
                .andExpect(TestUtils.responseBody().containsError(expectedFieldName, expectedMsg));
    }

    //TRMS AND CONDITIONS
    @Test
    @DisplayName("test validate terms and condition")
    void testFailWith400_WhenSignUpRentor_WithFalseTermsAndCondition() throws Exception {
        //arrange
        var request = SignUpRentorRequestDto.builder().build();
        var expectedStatus = status().isBadRequest();
        var expectedFieldName = "customerAgreement";
        var expectedMsg = "You must agree to the terms and conditions";
        PropertyUtils.setSimpleProperty(request, expectedFieldName, false);

        //act
        var resultActions = this.performSignUpRentorRequest(request);
        //assert
        resultActions.andExpect(expectedStatus)
                .andExpect(TestUtils.responseBody().containsError(expectedFieldName, expectedMsg));
    }


    @Test
    void testSignUpRentorSuccessfully() throws Exception {
        //arrange
        var idResponseDto=new IdResponseDto(1L);
        var request= SignUpRentorRequestDto.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("aa@gm.co")
                .state(1)
                .phone("08137640746")
                .gender(Gender.MALE.getCode())
                .bankCode("001")
                .accountNo("1234567890")
                .accountName("firstname lastname")
                .contactAddress("abc xyz")
                .password("pass1@PASS")
                .confirmPassword("pass1@PASS")
                .build();

        var expectedStatus = status().isCreated();
        var expectedMsg = "Success";

        when(this.paymentAccountService.verifyPaymentAccountDetails(anyString(),anyString(),anyString(),anyString())).thenReturn(null);
        when(this.accountService.createRentorAccount(any(SignUpRentorRequestDto.class))).thenReturn(idResponseDto);

        //act
        var resultActions = this.performSignUpRentorRequest(request);

        //assert
        resultActions.andExpect(expectedStatus)
                .andExpect(jsonPath("$.message").value(expectedMsg))
                .andExpect(jsonPath("$.data.id").value(idResponseDto.getId()));
        var signUpRequestCaptor = ArgumentCaptor.forClass(SignUpRentorRequestDto.class);

        verify(accountService).createRentorAccount(signUpRequestCaptor.capture());
        assertTrue(new ReflectionEquals(request).matches(signUpRequestCaptor.getValue()));

    }

    @Test
    @DisplayName("test invalid transfer account ")
    void testReturnInvalidRequest_ForIncorrectAccountDetails() throws Exception {
        //arrange
        var request= SignUpRentorRequestDto.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("aa@gm.co")
                .state(1)
                .phone("08137640746")
                .gender(Gender.MALE.getCode())
                .bankCode("001")
                .accountNo("1234567890")
                .accountName("firstname lastname")
                .contactAddress("abc xyz")
                .password("pass1@PASS")
                .confirmPassword("pass1@PASS")
                .build();

        var expectedStatus = status().isBadRequest();
        var expectedMsg = "Incorrect Account details";

        when(this.paymentAccountService.verifyPaymentAccountDetails(anyString(),anyString(),anyString(),anyString())).thenReturn(expectedMsg);

        //act
        var resultActions = this.performSignUpRentorRequest(request);

        //assert
        resultActions.andExpect(expectedStatus)
                .andExpect(jsonPath("$.message").value(expectedMsg));

        var bankCodeCaptor = ArgumentCaptor.forClass(String.class);
        var acctNoCaptor = ArgumentCaptor.forClass(String.class);
        var acctNameCaptor = ArgumentCaptor.forClass(String.class);
        var countryCodeCaptor = ArgumentCaptor.forClass(String.class);


        verify(paymentAccountService).verifyPaymentAccountDetails(acctNameCaptor.capture(),acctNoCaptor.capture(),bankCodeCaptor.capture(),countryCodeCaptor.capture());

        assertThat(request.getAccountNo(), is(equalTo(acctNoCaptor.getValue())));
        assertThat(request.getAccountName(), is(equalTo(acctNameCaptor.getValue())));
        assertThat(request.getBankCode(), is(equalTo(bankCodeCaptor.getValue())));
        assertThat("NG", is(equalTo(countryCodeCaptor.getValue())));

        verify(accountService,never()).createRentorAccount(any(SignUpRentorRequestDto.class));
    }
    
    ResultActions performSignUpRentorRequest(SignUpRentorRequestDto dto) throws Exception {
        return mockMvc.perform(post(baseApiPath + "/signUpRentor").contentType("application/json").content(TestUtils.asJsonString(dto)))
                .andDo(print());
    }

    @Test
    void test() throws Exception {

       /* System.out.println(LocalDateTime.now(ZoneId.systemDefault()));
        System.out.println(ZonedDateTime.now());
        System.out.println(OffsetDateTime.now());
        System.out.println(ZoneId.getAvailableZoneIds());*/
/*[Asia/Aden, America/Cuiaba, Etc/GMT+9, Etc/GMT+8, Africa/Nairobi, America/Marigot,
                Asia/Aqtau, Pacific/Kwajalein, America/El_Salvador, Asia/Pontianak, Africa/Cairo,
                Pacific/Pago_Pago, Africa/Mbabane, Asia/Kuching, Pacific/Honolulu,
                Pacific/Rarotonga, America/Guatemala, Australia/Hobart,
                Europe/London, America/Belize, America/Panama, Asia/Chungking,
                America/Managua, America/Indiana/Petersburg, Asia/Yerevan, Europe/Brussels,
                GMT, Europe/Warsaw, America/Chicago, Asia/Kashgar, Chile/Continental, Pacific/Yap,
                CET, Etc/GMT-1, Etc/GMT-0, Europe/Jersey, America/Tegucigalpa, Etc/GMT-5, Europe/Istanbul,
                America/Eirunepe, Etc/GMT-4, America/Miquelon, Etc/GMT-3, Europe/Luxembourg, Etc/GMT-2,
                Etc/GMT-9, America/Argentina/Catamarca, Etc/GMT-8, Etc/GMT-7, Etc/GMT-6, Europe/Zaporozhye,
                Canada/Yukon, Canada/Atlantic, Atlantic/St_Helena, Australia/Tasmania, Libya,
                Europe/Guernsey, America/Grand_Turk, Asia/Samarkand, America/Argentina/Cordoba,
                Asia/Phnom_Penh, Africa/Kigali, Asia/Almaty, US/Alaska, Asia/Dubai, Europe/Isle_of_Man,
                America/Araguaina, Cuba, Asia/Novosibirsk, America/Argentina/Salta, Etc/GMT+3,
                Africa/Tunis, Etc/GMT+2, Etc/GMT+1, Pacific/Fakaofo, Africa/Tripoli, Etc/GMT+0,
                Israel, Africa/Banjul, Etc/GMT+7, Indian/Comoro, Etc/GMT+6, Etc/GMT+5, Etc/GMT+4,
                Pacific/Port_Moresby, US/Arizona, Antarctica/Syowa, Indian/Reunion, Pacific/Palau,
                Europe/Kaliningrad, America/Montevideo, Africa/Windhoek, Asia/Karachi, Africa/Mogadishu,
                Australia/Perth, Brazil/East, Etc/GMT, Asia/Chita, Pacific/Easter, Antarctica/Davis,
                Antarctica/McMurdo, Asia/Macao, America/Manaus, Africa/Freetown, Europe/Bucharest,
                Asia/Tomsk, America/Argentina/Mendoza, Asia/Macau, Europe/Malta, Mexico/BajaSur,
                Pacific/Tahiti, Africa/Asmera, Europe/Busingen, America/Argentina/Rio_Gallegos,
                Africa/Malabo, Europe/Skopje, America/Catamarca, America/Godthab, Europe/Sarajevo,
                Australia/ACT, GB-Eire, Africa/Lagos, America/Cordoba, Europe/Rome, Asia/Dacca,
                Indian/Mauritius, Pacific/Samoa, America/Regina, America/Fort_Wayne, America/Dawson_Creek,
                Africa/Algiers, Europe/Mariehamn, America/St_Johns, America/St_Thomas, Europe/Zurich,
                America/Anguilla, Asia/Dili, America/Denver, Africa/Bamako, Europe/Saratov, GB,
                Mexico/General, Pacific/Wallis, Europe/Gibraltar, Africa/Conakry, Africa/Lubumbashi,
                Asia/Istanbul, America/Havana, NZ-CHAT, Asia/Choibalsan, America/Porto_Acre, Asia/Omsk,
                Europe/Vaduz, US/Michigan, Asia/Dhaka, America/Barbados, Europe/Tiraspol, Atlantic/Cape_Verde,
                Asia/Yekaterinburg, America/Louisville, Pacific/Johnston, Pacific/Chatham, Europe/Ljubljana,
                America/Sao_Paulo, Asia/Jayapura, America/Curacao, Asia/Dushanbe, America/Guyana, America/Guayaquil,
                America/Martinique, Portugal, Europe/Berlin, Europe/Moscow, Europe/Chisinau, America/Puerto_Rico,
                America/Rankin_Inlet, Pacific/Ponape, Europe/Stockholm, Europe/Budapest, America/Argentina/Jujuy,
                Australia/Eucla, Asia/Shanghai, Universal, Europe/Zagreb, America/Port_of_Spain, Europe/Helsinki,
                Asia/Beirut, Asia/Tel_Aviv, Pacific/Bougainville, US/Central, Africa/Sao_Tome, Indian/Chagos,
                America/Cayenne, Asia/Yakutsk, Pacific/Galapagos, Australia/North, Europe/Paris, Africa/Ndjamena,
                Pacific/Fiji, America/Rainy_River, Indian/Maldives, Australia/Yancowinna, SystemV/AST4, Asia/Oral,
                America/Yellowknife, Pacific/Enderbury, America/Juneau, Australia/Victoria, America/Indiana/Vevay,
                Asia/Tashkent, Asia/Jakarta, Africa/Ceuta, Asia/Barnaul, America/Recife, America/Buenos_Aires,
                America/Noronha, America/Swift_Current, Australia/Adelaide, America/Metlakatla, Africa/Djibouti,
                America/Paramaribo, Asia/Qostanay, Europe/Simferopol, Europe/Sofia, Africa/Nouakchott, Europe/Prague,
                America/Indiana/Vincennes, Antarctica/Mawson, America/Kralendijk, Antarctica/Troll, Europe/Samara,
                Indian/Christmas, America/Antigua, Pacific/Gambier, America/Indianapolis, America/Inuvik,
                America/Iqaluit, Pacific/Funafuti, UTC, Antarctica/Macquarie, Canada/Pacific, America/Moncton,
                Africa/Gaborone, Pacific/Chuuk, Asia/Pyongyang, America/St_Vincent, Asia/Gaza, Etc/Universal, PST8PDT,
                Atlantic/Faeroe, Asia/Qyzylorda, Canada/Newfoundland, America/Kentucky/Louisville, America/Yakutat,
                America/Ciudad_Juarez, Asia/Ho_Chi_Minh, Antarctica/Casey, Europe/Copenhagen, Africa/Asmara,
                Atlantic/Azores, Europe/Vienna, ROK, Pacific/Pitcairn, America/Mazatlan, Australia/Queensland,
                Pacific/Nauru, Europe/Tirane, Asia/Kolkata, SystemV/MST7, Australia/Canberra, MET, Australia/Broken_Hill, Europe/Riga, America/Dominica, Africa/Abidjan, America/Mendoza, America/Santarem, Kwajalein, America/Asuncion, Asia/Ulan_Bator, NZ, America/Boise, Australia/Currie, EST5EDT, Pacific/Guam, Pacific/Wake, Atlantic/Bermuda, America/Costa_Rica, America/Dawson, Asia/Chongqing, Eire, Europe/Amsterdam, America/Indiana/Knox, America/North_Dakota/Beulah, Africa/Accra, Atlantic/Faroe, Mexico/BajaNorte, America/Maceio, Etc/UCT, Pacific/Apia, GMT0, America/Atka, Pacific/Niue, Australia/Lord_Howe, Europe/Dublin, Pacific/Truk, MST7MDT, America/Monterrey, America/Nassau, America/Jamaica, Asia/Bishkek, America/Atikokan, Atlantic/Stanley, Australia/NSW, US/Hawaii, SystemV/CST6, Indian/Mahe, Asia/Aqtobe, America/Sitka, Asia/Vladivostok, Africa/Libreville, Africa/Maputo, Zulu, America/Kentucky/Monticello, Africa/El_Aaiun, Africa/Ouagadougou, America/Coral_Harbour, Pacific/Marquesas, Brazil/West, America/Aruba, America/North_Dakota/Center, America/Cayman, Asia/Ulaanbaatar, Asia/Baghdad, Europe/San_Marino, America/Indiana/Tell_City, America/Tijuana, Pacific/Saipan, SystemV/YST9, Africa/Douala, America/Chihuahua, America/Ojinaga, Asia/Hovd, America/Anchorage, Chile/EasterIsland, America/Halifax, Antarctica/Rothera, America/Indiana/Indianapolis, US/Mountain, Asia/Damascus, America/Argentina/San_Luis, America/Santiago, Asia/Baku, America/Argentina/Ushuaia, Atlantic/Reykjavik, Africa/Brazzaville, Africa/Porto-Novo, America/La_Paz, Antarctica/DumontDUrville, Asia/Taipei, Antarctica/South_Pole, Asia/Manila, Asia/Bangkok, Africa/Dar_es_Salaam, Poland, Atlantic/Madeira, Antarctica/Palmer, America/Thunder_Bay, Africa/Addis_Ababa, Asia/Yangon, Europe/Uzhgorod, Brazil/DeNoronha, Asia/Ashkhabad, Etc/Zulu, America/Indiana/Marengo, America/Creston, America/Punta_Arenas, America/Mexico_City, Antarctica/Vostok, Asia/Jerusalem, Europe/Andorra, US/Samoa, PRC, Asia/Vientiane, Pacific/Kiritimati, America/Matamoros, America/Blanc-Sablon, Asia/Riyadh, Iceland, Pacific/Pohnpei, Asia/Ujung_Pandang, Atlantic/South_Georgia, Europe/Lisbon, Asia/Harbin, Europe/Oslo, Asia/Novokuznetsk, CST6CDT, Atlantic/Canary, America/Knox_IN, Asia/Kuwait, SystemV/HST10, Pacific/Efate, Africa/Lome, America/Bogota, America/Menominee, America/Adak, Pacific/Norfolk, Europe/Kirov, America/Resolute, Pacific/Kanton, Pacific/Tarawa, Africa/Kampala, Asia/Krasnoyarsk, Greenwich, SystemV/EST5, America/Edmonton, Europe/Podgorica, Australia/South, Canada/Central, Africa/Bujumbura, America/Santo_Domingo, US/Eastern, Europe/Minsk, Pacific/Auckland, Africa/Casablanca, America/Glace_Bay, Canada/Eastern, Asia/Qatar, Europe/Kiev, Singapore, Asia/Magadan, SystemV/PST8, America/Port-au-Prince, Europe/Belfast, America/St_Barthelemy, Asia/Ashgabat, Africa/Luanda, America/Nipigon, Atlantic/Jan_Mayen, Brazil/Acre, Asia/Muscat, Asia/Bahrain, Europe/Vilnius, America/Fortaleza, Etc/GMT0, US/East-Indiana, America/Hermosillo, America/Cancun, Africa/Maseru, Pacific/Kosrae, Africa/Kinshasa, Asia/Kathmandu, Asia/Seoul, Australia/Sydney, America/Lima, Australia/LHI, America/St_Lucia, Europe/Madrid, America/Bahia_Banderas, America/Montserrat, Asia/Brunei, America/Santa_Isabel, Canada/Mountain, America/Cambridge_Bay, Asia/Colombo, Australia/West, Indian/Antananarivo, Australia/Brisbane, Indian/Mayotte, US/Indiana-Starke, Asia/Urumqi, US/Aleutian, Europe/Volgograd, America/Lower_Princes, America/Vancouver, Africa/Blantyre, America/Rio_Branco, America/Danmarkshavn, America/Detroit, America/Thule, Africa/Lusaka, Asia/Hong_Kong, Iran, America/Argentina/La_Rioja, Africa/Dakar, SystemV/CST6CDT, America/Tortola, America/Porto_Velho, Asia/Sakhalin, Etc/GMT+10, America/Scoresbysund, Asia/Kamchatka, Asia/Thimbu, Africa/Harare, Etc/GMT+12, Etc/GMT+11, Navajo, America/Nome, Europe/Tallinn, Turkey, Africa/Khartoum, Africa/Johannesburg, Africa/Bangui, Europe/Belgrade, Jamaica, Africa/Bissau, Asia/Tehran, WET, Europe/Astrakhan, Africa/Juba, America/Campo_Grande, America/Belem, Etc/Greenwich, Asia/Saigon, America/Ensenada, Pacific/Midway, America/Jujuy, Africa/Timbuktu, America/Bahia, America/Goose_Bay, America/Virgin, America/Pangnirtung, Asia/Katmandu, America/Phoenix, Africa/Niamey, America/Whitehorse, Pacific/Noumea, Asia/Tbilisi, Europe/Kyiv, America/Montreal, Asia/Makassar, America/Argentina/San_Juan, Hongkong, UCT, Asia/Nicosia, America/Indiana/Winamac, SystemV/MST7MDT, America/Argentina/ComodRivadavia, America/Boa_Vista, America/Grenada, Asia/Atyrau, Australia/Darwin, Asia/Khandyga, Asia/Kuala_Lumpur, Asia/Famagusta, Asia/Thimphu, Asia/Rangoon, Europe/Bratislava, Asia/Calcutta, America/Argentina/Tucuman, Asia/Kabul, Indian/Cocos, Japan, Pacific/Tongatapu, America/New_York, Etc/GMT-12, Etc/GMT-11, America/Nuuk, Etc/GMT-10, SystemV/YST9YDT, Europe/Ulyanovsk, Etc/GMT-14, Etc/GMT-13, W-SU, America/Merida, EET, America/Rosario, Canada/Saskatchewan, America/St_Kitts, Arctic/Longyearbyen, America/Fort_Nelson, America/Caracas, America/Guadeloupe, Asia/Hebron, Indian/Kerguelen, SystemV/PST8PDT, Africa/Monrovia, Asia/Ust-Nera, Egypt, Asia/Srednekolymsk, America/North_Dakota/New_Salem, Asia/Anadyr, Australia/Melbourne, Asia/Irkutsk, America/Shiprock, America/Winnipeg, Europe/Vatican, Asia/Amman, Etc/UTC, SystemV/AST4ADT, Asia/Tokyo, America/Toronto, Asia/Singapore, Australia/Lindeman, America/Los_Angeles, SystemV/EST5EDT, Pacific/Majuro, America/Argentina/Buenos_Aires, Europe/Nicosia, Pacific/Guadalcanal, Europe/Athens, US/Pacific, Europe/Monaco]*/

        /// Alice (in time zone Europe/London) is entering a datetime
        String aliceInput = "2024-02-10 15:28:55";
        ZoneId aliceZone = ZoneId.of("Africa/Nairobi");
        ZonedDateTime parsed = LocalDateTime.parse(aliceInput, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                .atZone(aliceZone);
        Instant instant = parsed.toInstant();
        System.out.println("ccc/"+instant);
// instant is stored in the DB

// Bob (in time zone Australia/Sydney) is viewing the datetime Alice has entered
        ZoneId bobZone = ZoneId.of("Australia/Sydney");
        ZonedDateTime atZone = ZonedDateTime.ofInstant(instant, bobZone);
        System.out.println(atZone.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));


        //arrange
        }

}
