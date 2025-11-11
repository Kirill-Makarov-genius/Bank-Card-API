package com.example.bankcard_api.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = ValidExpiryValidator.class)
public @interface ValidExpiry {
    String message() default "Invalid expiry format or in the past (expected MM/yyyy)";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
