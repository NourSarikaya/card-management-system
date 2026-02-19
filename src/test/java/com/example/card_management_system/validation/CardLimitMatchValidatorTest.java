package com.example.card_management_system.validation;

import com.example.card_management_system.dto.CreateCardRequestDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CardLimitMatchValidatorTest {

    CardLimitMatchValidator validator = new CardLimitMatchValidator();

    @Test
    public void shouldReturnTrueWhenDebitCardHasCreditLimit() {
        CreateCardRequestDTO requestDTO = CreateCardRequestDTO.builder().cardType("DEBIT").creditLimit("1000").build();

        assertFalse(validator.isValid(requestDTO, null));
    }

    @Test
    public void shouldReturnFalseWhenLoyaltyCardHasCreditLimit() {
        CreateCardRequestDTO requestDTO = CreateCardRequestDTO.builder()
                                                              .cardType("LOYALTY")
                                                              .creditLimit("1000")
                                                              .build();

        assertFalse(validator.isValid(requestDTO, null));
    }

    @Test
    public void shouldReturnFalseWhenNonCreditCardHasCreditLimit() {
        CreateCardRequestDTO requestDTO = CreateCardRequestDTO.builder().cardType("CREDIT").creditLimit("1000").build();

        assertTrue(validator.isValid(requestDTO, null));
    }

    @Test
    public void shouldReturnTrueWhenRequestDtoIsNull() {
        assertTrue(validator.isValid(null, null));
    }
}