package com.omegalambdang.rentanitem.feature.account.rentor;

import com.omegalambdang.rentanitem.apiresponse.ApiDataResponse;
import com.omegalambdang.rentanitem.apiresponse.ApiResponseUtil;
import com.omegalambdang.rentanitem.common.dto.IdResponseDto;
import com.omegalambdang.rentanitem.exception.InvalidRequestException;
import com.omegalambdang.rentanitem.feature.payment.PaymentAccountReferenceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;



@Tag(name = "Rentor Account Controller ")
@RequiredArgsConstructor
@RestController
public class SignUpRentorController {

    private final RentorAccountService accountService;
    private final PaymentAccountReferenceService paymentAccountService;
    @Operation(summary = "Rentor sign up", description = "")
    @PostMapping("${api.basepath-api}/signUpRentor")
    @ResponseStatus(code = HttpStatus.CREATED)
    public ResponseEntity<ApiDataResponse<IdResponseDto>> signUp(@Valid @RequestBody SignUpRentorRequestDto requestDto) {

        var errMsg=paymentAccountService.verifyPaymentAccountDetails(requestDto.getAccountName(),requestDto.getAccountNo(),requestDto.getBankCode(),"NG");

        if(StringUtils.isNotEmpty(errMsg)){
           throw new InvalidRequestException(errMsg);
        }
        var idResponseDto=this.accountService.createRentorAccount(requestDto);
        return ApiResponseUtil.response(HttpStatus.CREATED, idResponseDto, "Success");
    }

}
