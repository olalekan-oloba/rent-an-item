package com.omegalambdang.rentanitem.validator.enumValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {EnumValidator.class})
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface Enum
{
    public abstract String message() default "Invalid value. This is not permitted.";
     
    public abstract Class<?>[] groups() default {};
  
    public abstract Class<? extends Payload>[] payload() default {};
     
    public abstract Class<? extends java.lang.Enum<?>> enumClass();
     
    public abstract boolean ignoreCase() default false;
}
