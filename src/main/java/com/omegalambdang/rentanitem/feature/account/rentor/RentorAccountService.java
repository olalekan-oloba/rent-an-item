package com.omegalambdang.rentanitem.feature.account.rentor;

import com.omegalambdang.rentanitem.common.dto.IdResponseDto;

public interface RentorAccountService {
    IdResponseDto createRentorAccount(SignUpRentorRequestDto signUpRentorRequestDto);
}
