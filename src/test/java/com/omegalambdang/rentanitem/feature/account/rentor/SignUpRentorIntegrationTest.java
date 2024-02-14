package com.omegalambdang.rentanitem.feature.account.rentor;


import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.omegalambdang.rentanitem.AbstractSpringIntegrationTest;
import com.omegalambdang.rentanitem.feature.account.Gender;
import com.omegalambdang.rentanitem.feature.account.UserRepository;
import com.omegalambdang.rentanitem.feature.configuration.CoreConfiguration;
import com.omegalambdang.rentanitem.feature.configuration.CoreConfigurationRepository;
import com.omegalambdang.rentanitem.feature.payment.PaymentAccountReferenceRepository;
import com.omegalambdang.rentanitem.feature.reference.zone.Zone;
import com.omegalambdang.rentanitem.feature.reference.zone.ZoneRepository;
import com.omegalambdang.rentanitem.util.TestUtils;
import mockwebserver3.Dispatcher;
import mockwebserver3.MockResponse;
import mockwebserver3.MockWebServer;
import mockwebserver3.RecordedRequest;
import org.apache.commons.lang3.StringUtils;
import org.awaitility.Awaitility;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.ResultActions;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static com.omegalambdang.rentanitem.constants.CoreConfigurationConstants.PAYMENT_MODULE_CONFG_KEY;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SignUpRentorIntegrationTest extends AbstractSpringIntegrationTest {

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("emailuser", "emailpass"));
    private Zone zone;
    @Autowired
    ZoneRepository zoneRepository;
    @Autowired
    CoreConfigurationRepository coreConfigurationRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RentorRepository rentorRepository;
    @Autowired
    PaymentAccountReferenceRepository paymentAccountRepository;
    static MockWebServer mockWebServer = new MockWebServer();

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry r) throws IOException {
        r.add("payment-module.paystack.baseUrl", () -> "http://localhost:" + mockWebServer.getPort());
    }
    @BeforeAll
    static void beforeAll() throws IOException {
        mockWebServer.start();
    }
    @AfterAll
    static void afterAll() throws IOException {
        mockWebServer.shutdown();
    }
    @AfterAll
    public void tearDownAll() {
    }
    private static final int CREATE_PAYMENT_ACCT_DELAY_RESPONSE_TIME_IN_MILLISEC = 800;
    private static final int VERIFY_2XX = 1;
    private static final int VERIFY_5XX_ERR = 2;
    private static final int CREATE_2XX = 3;
    private static final int CREATE_5XX_ERR = 4;

    static Map<Integer, String> payloads = new HashMap<>();

    @BeforeEach
    void setUpBeforeEachMeth() {

        zone = new Zone();
        zone.setCode("ZC");
        zone.setName("xyz");
        zone.setSupported(true);
        zone.setCountryIsoCode("NG");

        this.zoneRepository.save(zone);

        payloads.put(VERIFY_2XX, """
                {
                  "status": true,
                  "message": "Account number resolved",
                  "data": {
                    "account_number": "1234567890",
                    "account_name": "James Bond",
                    "bank_id": 18
                  }
                }""");
        payloads.put(CREATE_2XX, """
                {
                    "status": true,
                    "message": "Transfer recipient created successfully",
                    "data": {
                        "active": true,
                        "createdAt": "2023-06-30T22:14:31.000Z",
                        "currency": "NGN",
                        "description": null,
                        "domain": "test",
                        "email": null,
                        "id": 56314336,
                        "integration": 1019527,
                        "metadata": null,
                        "name": "Olalekan Lekan",
                        "recipient_code": "RCP_q4372n3ve5jpfx2",
                        "type": "nuban",
                        "updatedAt": "2023-07-12T11:32:50.000Z",
                        "is_deleted": false,
                        "isDeleted": false,
                        "details": {
                            "authorization_code": null,
                            "account_number": "1234567890",
                            "account_name": "James Bond",
                            "bank_code": "033",
                            "bank_name": "BankOne"
                        }
                    }
                }""");
        payloads.put(VERIFY_5XX_ERR, "{}");
        payloads.put(CREATE_5XX_ERR, "{}");
    }

    @Test
    @DisplayName("signup rentor successfully")
    void givenRentorAccountNotExist_WhenSignUpRentor_ShouldCreateRentorAccountSuccessfully() throws Exception {
        //arrange
        mockWebServer.setDispatcher(new Dispatcher() {
            @Override
            public @NotNull MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                var path = request.getPath();
                var realPath = path.contains("?") ? path.substring(0, path.indexOf('?')) : path;
                return switch (realPath) {
                    case "/bank/resolve" -> new MockResponse()
                            .setResponseCode(200)
                            .setBody(payloads.get(VERIFY_2XX));
                    case "/transferrecipient" -> new MockResponse()
                            .setResponseCode(200)
                            .setBody(payloads.get(CREATE_2XX)).setBodyDelay(CREATE_PAYMENT_ACCT_DELAY_RESPONSE_TIME_IN_MILLISEC, MILLISECONDS);
                    default -> new MockResponse().setResponseCode(404);
                };
            }
        });

        //set up payment account module handler
        var coreConfig = new CoreConfiguration();
        coreConfig.setConfigurationKey(PAYMENT_MODULE_CONFG_KEY);
        coreConfig.setConfigurationName("Payment Module");
        coreConfig.setValue("paystack");
        this.coreConfigurationRepository.save(coreConfig);

        var request = SignUpRentorRequestDto.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("aa@gm.co")
                .state(zone.getId())
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

        //act
        var resultActions = this.performSignUpRentorRequest(request);

        //assert
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", Is.is("Success")))
                .andExpect(jsonPath("$.data.id").value(Matchers.greaterThan(0)))
                .andDo(print());

        transactionUtils.execute((status) -> {

            assertEquals(1, userRepository.findAll().stream().filter(it -> Boolean.TRUE == it.getIsRentor()).toList().size());
            var user = this.userRepository.findAll().stream().filter(it -> it.getEmail().equals(request.getEmail())).findFirst().get();
            //assert user created
            assertNotNull(user);
            assertNotNull(user.getPassword());
            //assert stylist acct created
            assertEquals(1, rentorRepository.count());
            var rentor = rentorRepository.findAll().getFirst();
            assertNotNull(rentor);

            //assert payment account created
            var paymentAccount = this.paymentAccountRepository.findById(rentor.getId()).get();
            assertNull(paymentAccount.getPaymentRecipientCode());
            assertNull(paymentAccount.getBankName());
            assertEquals(request.getAccountName(), paymentAccount.getAccountName());
        });

        Awaitility.await().atMost(2, SECONDS).untilAsserted(() -> {
            var rentor = rentorRepository.findAll().getFirst();
            //assert transfer account created
            var paymentAccount = this.paymentAccountRepository.findById(rentor.getId()).get();
            assertNotNull(paymentAccount.getPaymentRecipientCode());
            assertEquals("RCP_q4372n3ve5jpfx2", paymentAccount.getPaymentRecipientCode()); //web service response
            assertNotNull(paymentAccount.getBankName());
            assertEquals("BankOne", paymentAccount.getBankName()); //web service response
            assertEquals(request.getAccountName(), paymentAccount.getAccountName()); //web service response
        });

        Awaitility.await().atMost(2, SECONDS).untilAsserted(() -> {
            assertTrue(greenMail.getReceivedMessages().length > 0);
            var receivedMessage = greenMail.getReceivedMessages()[0];
            assertTrue(StringUtils.isNotEmpty(GreenMailUtil.getBody(receivedMessage)));
            assertEquals(1, receivedMessage.getAllRecipients().length);
            assertEquals(request.getEmail(), receivedMessage.getAllRecipients()[0].toString());
            assertEquals("Account Created", receivedMessage.getSubject());
        });
    }


    @Test
    @DisplayName("signup rentor successfully")
    void testing() throws Exception {
        //arrange
        mockWebServer.enqueue(new MockResponse().setResponseCode(500).setBody(payloads.get(VERIFY_5XX_ERR)));
        mockWebServer.enqueue(new MockResponse().setResponseCode(502).setBody(payloads.get(VERIFY_5XX_ERR)));
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(payloads.get(VERIFY_2XX)));
        mockWebServer.enqueue(new MockResponse().setResponseCode(502).setBody(payloads.get(CREATE_5XX_ERR)));
        mockWebServer.enqueue(new MockResponse().setResponseCode(502).setBody(payloads.get(CREATE_5XX_ERR)));
        mockWebServer.enqueue(new MockResponse().setResponseCode(200).setBody(payloads.get(CREATE_2XX)).setBodyDelay(CREATE_PAYMENT_ACCT_DELAY_RESPONSE_TIME_IN_MILLISEC, MILLISECONDS));

        //set up payment account module handler
        var coreConfig = new CoreConfiguration();
        coreConfig.setConfigurationKey(PAYMENT_MODULE_CONFG_KEY);
        coreConfig.setConfigurationName("Payment Module");
        coreConfig.setValue("paystack");
        this.coreConfigurationRepository.save(coreConfig);

        var request = SignUpRentorRequestDto.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("aa@gm.co")
                .state(zone.getId())
                .phone("08137640746")
                .gender(Gender.MALE.getCode())
                .bankCode("001")
                .accountNo("1234567890")
                .accountName("firstname lastname")
                .contactAddress("abc xyz")
                .password("pass1@PASS")
                .confirmPassword("pass1@PASS")
                .build();

        //act
        var resultActions = this.performSignUpRentorRequest(request);

        //assert
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", Is.is("Success")))
                .andExpect(jsonPath("$.data.id").value(Matchers.greaterThan(0)))
                .andDo(print());

        transactionUtils.execute((status) -> {

            assertEquals(1, userRepository.findAll().stream().filter(it -> Boolean.TRUE == it.getIsRentor()).toList().size());
            var user = this.userRepository.findAll().stream().filter(it -> it.getEmail().equals(request.getEmail())).findFirst().get();
            //assert user created
            assertNotNull(user);
            assertNotNull(user.getPassword());
            //assert stylist acct created
            assertEquals(1, rentorRepository.count());
            var rentor = rentorRepository.findAll().getFirst();
            assertNotNull(rentor);

            //assert payment account created
            var paymentAccount = this.paymentAccountRepository.findById(rentor.getId()).get();
            assertNull(paymentAccount.getPaymentRecipientCode());
            assertNull(paymentAccount.getBankName());
            assertEquals(request.getAccountName(), paymentAccount.getAccountName());
        });
        assertPaymentAcctRefUpdated(request);
        assertEmailSent(request.getEmail());
    }

    private void assertPaymentAcctRefUpdated(SignUpRentorRequestDto request) {
        Awaitility.await().atMost(4, SECONDS).untilAsserted(() -> {
            var rentor = rentorRepository.findAll().getFirst();
            //assert transfer account created
            var paymentAccount = this.paymentAccountRepository.findById(rentor.getId()).get();
            assertNotNull(paymentAccount.getPaymentRecipientCode());
            assertEquals("RCP_q4372n3ve5jpfx2", paymentAccount.getPaymentRecipientCode()); //web service response
            assertNotNull(paymentAccount.getBankName());
            assertEquals("BankOne", paymentAccount.getBankName()); //web service response
            assertEquals(request.getAccountName(), paymentAccount.getAccountName()); //web service response
        });
    }

    private void assertEmailSent(String emailAdd) {
        Awaitility.await().atMost(2, SECONDS).untilAsserted(() -> {
            assertTrue(greenMail.getReceivedMessages().length > 0);
            var receivedMessage = greenMail.getReceivedMessages()[0];
            assertTrue(StringUtils.isNotEmpty(GreenMailUtil.getBody(receivedMessage)));
            assertEquals(1, receivedMessage.getAllRecipients().length);
            assertEquals(emailAdd, receivedMessage.getAllRecipients()[0].toString());
            assertEquals("Account Created", receivedMessage.getSubject());
        });
    }


    @ParameterizedTest
    @MethodSource({"payloadProvider"})
    @DisplayName("signup rentor successfully")
    void givenClientError_WhenSignUpRentor_ShouldReturnRemoteErrorMsg(int expectedStatusCode, String expectedMessage, String body) throws Exception {
        //arrange
        mockWebServer.setDispatcher(new Dispatcher() {
            @Override
            public @NotNull MockResponse dispatch(RecordedRequest request) throws InterruptedException {
                var path = request.getPath();
                var realPath = path.contains("?") ? path.substring(0, path.indexOf('?')) : path;
                return switch (realPath) {
                    case "/bank/resolve" -> new MockResponse()
                            .setResponseCode(expectedStatusCode)
                            .setBody(body);
                    default -> new MockResponse().setResponseCode(404);
                };
            }
        });
        var coreConfig = new CoreConfiguration();
        coreConfig.setConfigurationKey(PAYMENT_MODULE_CONFG_KEY);
        coreConfig.setConfigurationName("Payment Module");
        coreConfig.setValue("paystack");
        this.coreConfigurationRepository.save(coreConfig);

        var request = SignUpRentorRequestDto.builder()
                .firstName("firstName")
                .lastName("lastName")
                .email("aa@gm.co")
                .state(zone.getId())
                .phone("08137640746")
                .gender(Gender.MALE.getCode())
                .bankCode("001")
                .accountNo("1234567890")
                .accountName("firstname lastname")
                .contactAddress("abc xyz")
                .password("pass1@PASS")
                .confirmPassword("pass1@PASS")
                .build();
        //act
        var resultActions = this.performSignUpRentorRequest(request);
        //assert
        resultActions.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", Is.is(expectedMessage)))
                .andDo(print());
    }

    Stream<Arguments> payloadProvider() {
        return Stream.of(
                arguments(400, "Unknown bank code: 059", """
                        {
                            "status": false,
                            "message": "Unknown bank code: 059",
                            "meta": {
                                "nextStep": "Ensure that the value(s) you're passing are valid."
                            },
                            "type": "validation_error",
                            "code": "invalid_params"
                        }
                        """),
                arguments(422, "Could not resolve account name. Check parameters or try again.", """
                         {
                          "status": false,
                               "message": "Could not resolve account name. Check parameters or try again.",
                               "meta": {
                                   "nextStep": "Ensure that you're passing the correct bank code. Use the List Banks Endpoint to get the list of all available banks and their corresponding bank codes"
                               },
                               "type": "validation_error",
                               "code": "invalid_bank_code"
                           }
                          
                        """)

        );
    }

    private ResultActions performSignUpRentorRequest(SignUpRentorRequestDto dto) throws Exception {
        return this.mockMvc
                .perform(post(this.baseApiPath + "/signUpRentor").content(TestUtils.asJsonString(dto))
                        .contentType(MediaType.APPLICATION_JSON).headers(headers).accept(MediaType.APPLICATION_JSON))
                .andDo(print());
    }


}
