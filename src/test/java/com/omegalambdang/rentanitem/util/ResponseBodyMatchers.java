package com.omegalambdang.rentanitem.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.omegalambdang.rentanitem.validator.ValidationFieldError;
import org.springframework.test.web.servlet.ResultMatcher;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ResponseBodyMatchers {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ResultMatcher containsError(String expectedFieldName, String expectedMessage) {
        return mvcResult -> {
            String json = mvcResult.getResponse().getContentAsString();
            //get the field errors from the api response object
            String fieldErrorsResp = TestUtils.objectFromResponseStr(json, "$.subErrors2").toString();
            TypeReference<List<ValidationFieldError>> ref = new TypeReference<>() {};
            var errorResults = objectMapper.readValue(fieldErrorsResp,ref);
            var fieldErrors = errorResults.stream()
                    .filter(fieldError -> fieldError.getFieldName().equals(expectedFieldName))
                    .filter(fieldError -> fieldError.getValidationErrMsgs().contains(expectedMessage))
                    .toList();
            assertEquals(1, fieldErrors.size());
            assertEquals(expectedFieldName, fieldErrors.getFirst().getFieldName());
            assertTrue(fieldErrors.getFirst().getValidationErrMsgs().contains(expectedMessage));
        };
    }
}