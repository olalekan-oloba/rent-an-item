package com.omegalambdang.rentanitem.validator.sameFieldValidator;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy =  SameValidator.class)
public @interface Same {

    String message() default "first must be less than second!";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
    /**
     * @return The first field
     */
    String field1();

    /**
     * @return The second field
     */
    String field2();
}
