package com.example.card_management_system.validation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FutureExpiryValidatorTest {

    private final FutureExpiryValidator validator = new FutureExpiryValidator();

    @Test
    void shouldReturnFalseForPastDate() {
        assertFalse(validator.isValid("202001", null));
    }

    @Test
    void shouldReturnTrueForFutureDate() {
        assertTrue(validator.isValid("203001", null));
    }

    @Test
    void shouldReturnFalseForInvalidFormat() {
        assertFalse(validator.isValid("invalid", null));
    }
}
