package com.example.card_management_system.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class FutureExpiryValidator implements ConstraintValidator<FutureExpiry, String> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyyMM");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }

        try {
            YearMonth expiryDate = YearMonth.parse(value, FORMATTER);
            return expiryDate.isAfter(YearMonth.now());
        } catch (DateTimeParseException e) {
            return false;
        }

    }
}
