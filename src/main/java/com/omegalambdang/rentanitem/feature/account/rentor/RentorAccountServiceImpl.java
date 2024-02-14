package com.omegalambdang.rentanitem.feature.account.rentor;

import com.omegalambdang.rentanitem.common.dto.IdResponseDto;
import com.omegalambdang.rentanitem.exception.ResourceConflictException;
import com.omegalambdang.rentanitem.feature.account.Gender;
import com.omegalambdang.rentanitem.feature.account.UserStatus;
import com.omegalambdang.rentanitem.feature.account.rentor.event.RentorAccountCreatedEvent;
import com.omegalambdang.rentanitem.feature.account.rentor.event.RentorAccountCreatedEventsPublisher;
import com.omegalambdang.rentanitem.feature.payment.PaymentAccountReference;
import com.omegalambdang.rentanitem.feature.payment.PaymentAccountReferenceService;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RentorAccountServiceImpl implements RentorAccountService{

    private final PasswordEncoder passwordEncoder;
    private final RentorRepository rentorRepository;
    private final PaymentAccountReferenceService paymentAccountReferenceService;
    private final RentorAccountCreatedEventsPublisher createRentorAccountEventsPublisher;

    public RentorAccountServiceImpl(PasswordEncoder passwordEncoder, RentorRepository rentorRepository, PaymentAccountReferenceService paymentAccountReferenceService, RentorAccountCreatedEventsPublisher createRentorAccountEventsPublisher) {
        this.passwordEncoder = passwordEncoder;
        this.rentorRepository = rentorRepository;
        this.paymentAccountReferenceService = paymentAccountReferenceService;
        this.createRentorAccountEventsPublisher = createRentorAccountEventsPublisher;
    }

    /**
     *
     * @param signUpRentorRequestDto user signup request
     * @return user id
     */
    @Override
    @Transactional
    public IdResponseDto createRentorAccount(SignUpRentorRequestDto signUpRentorRequestDto) {

        if (userEmailExists(signUpRentorRequestDto.getEmail())) {
            throw new ResourceConflictException("User with email [" + signUpRentorRequestDto.getEmail() + "] already exists");
        }

        var rentorModel=this.createRentorModelEntity(signUpRentorRequestDto);

        rentorModel=this.persistRentorModel(rentorModel);

        var paymentAccountReference=this.createPaymentAccountReference(rentorModel,signUpRentorRequestDto);

        this.publishEvent(rentorModel,paymentAccountReference);

        return new IdResponseDto(rentorModel.getId());
    }

    private boolean userEmailExists(String email) {
        return this.rentorRepository.existsByEmail(email);
    }

    private void publishEvent(Rentor rentorModel, PaymentAccountReference paymentAccountReference) {
        createRentorAccountEventsPublisher.publishCreateRentorEvent(new RentorAccountCreatedEvent(rentorModel,paymentAccountReference));
    }

    private PaymentAccountReference createPaymentAccountReference(Rentor rentorModel, SignUpRentorRequestDto signUpRentorRequestDto) {

        var paymentAcctReferenceModel=new PaymentAccountReference();
        paymentAcctReferenceModel.setBankCode(signUpRentorRequestDto.getBankCode());
        paymentAcctReferenceModel.setCurrency("NGN");
        paymentAcctReferenceModel.setAccountName(signUpRentorRequestDto.getAccountName());
        paymentAcctReferenceModel.setPaymentInProgress(false);
        paymentAcctReferenceModel.setAccountNo(signUpRentorRequestDto.getAccountNo());
        paymentAcctReferenceModel.setRentor(rentorModel);

        paymentAcctReferenceModel= this.paymentAccountReferenceService.createPaymentAccountReference(paymentAcctReferenceModel);

        return paymentAcctReferenceModel;
    }

    private Rentor persistRentorModel(Rentor rentorModel) {
        return this.rentorRepository.save(rentorModel);
    }

    private Rentor createRentorModelEntity(SignUpRentorRequestDto signUpRentorRequestDto) {
        var rentor=new Rentor();
        rentor.setContactAddress(signUpRentorRequestDto.getContactAddress());
        rentor.setPassword(passwordEncoder.encode(signUpRentorRequestDto.getPassword()));
        rentor.setStatus(UserStatus.ACTIVE);
        rentor.setPhone(signUpRentorRequestDto.getPhone());
        rentor.setEmail(signUpRentorRequestDto.getEmail());
        rentor.setIsRentor(true);
        rentor.setFirstName(signUpRentorRequestDto.getFirstName());
        rentor.setLastName(signUpRentorRequestDto.getLastName());
        rentor.setGender(EnumUtils.getEnum(Gender.class,signUpRentorRequestDto.getGender()));
        rentor.setCustomerAgreement(signUpRentorRequestDto.isCustomerAgreement());

        return rentor;
    }
}
