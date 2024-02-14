package com.omegalambdang.rentanitem.validator.sameFieldValidator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.BeanWrapperImpl;

import java.util.Objects;

@SuppressWarnings("NullAway.Init")
public class SameValidator implements ConstraintValidator<Same, Object> {

    private String field1FieldName;
    private String field2FieldName;

    @Override
    public void initialize(final Same constraintAnnotation) {
        field1FieldName = constraintAnnotation.field1();
        field2FieldName = constraintAnnotation.field2();
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {

        final String field1 = (String) new BeanWrapperImpl(value).getPropertyValue(field1FieldName);
        final String field2 = (String) new BeanWrapperImpl(value).getPropertyValue(field2FieldName);
        return Objects.equals(field1, field2);
    }

}
