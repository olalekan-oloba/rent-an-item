package com.omegalambdang.rentanitem.apiresponse;

import com.omegalambdang.rentanitem.validator.ValidationFieldError;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ErrorResult {
    private final List<ValidationFieldError> fieldErrors = new ArrayList<>();
}
