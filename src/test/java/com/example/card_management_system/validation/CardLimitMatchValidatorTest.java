package com.example.card_management_system.validation;

import com.example.card_management_system.dto.CreateCardRequestDTO;
import com.example.card_management_system.dto.v1.CreateCardRequestV1DTO;
import com.example.card_management_system.validation.v1.CardLimitMatchValidatorV1;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CardLimitMatchValidatorTest {

    CardLimitMatchValidator validator = new CardLimitMatchValidator();
    CardLimitMatchValidatorV1 validatorV1 = new CardLimitMatchValidatorV1();

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

    @Test
    public void shouldReturnTrueWhenDebitCardHasCreditLimitV1() {
        CreateCardRequestV1DTO requestDTO = CreateCardRequestV1DTO.builder().cardType("DEBIT").creditLimit("1000").build();

        assertFalse(validatorV1.isValid(requestDTO, null));
    }

    @Test
    public void shouldReturnFalseWhenLoyaltyCardHasCreditLimitV1() {
        CreateCardRequestV1DTO requestDTO = CreateCardRequestV1DTO.builder()
                                                                    .cardType("LOYALTY")
                                                                    .creditLimit("1000")
                                                                    .build();

        assertFalse(validatorV1.isValid(requestDTO, null));
    }

    @Test
    public void shouldReturnFalseWhenNonCreditCardHasCreditLimitV1() {
        CreateCardRequestV1DTO requestDTO = CreateCardRequestV1DTO.builder().cardType("CREDIT").creditLimit("1000").build();

        assertTrue(validatorV1.isValid(requestDTO, null));
    }

    @Test
    public void shouldReturnTrueWhenRequestDtoIsNullV1() {
        assertTrue(validatorV1.isValid(null, null));
    }
}