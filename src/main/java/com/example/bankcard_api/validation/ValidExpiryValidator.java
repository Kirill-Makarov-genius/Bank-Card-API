package com.example.bankcard_api.validation;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ValidExpiryValidator implements ConstraintValidator<ValidExpiry, String> {
    private static final DateTimeFormatter F = DateTimeFormatter.ofPattern("MM/yyyy");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) return true; // optional
        try {
            YearMonth ym = YearMonth.parse(value, F);
            YearMonth now = YearMonth.now();
            return !ym.isBefore(now);
        } catch (DateTimeParseException ex) {
            return false;
        }
    }
}
