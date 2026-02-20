package com.example.card_management_system.validation;

import com.example.card_management_system.dto.CreateCardRequestDTO;
import com.example.card_management_system.entity.Card;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;


public class CardLimitMatchValidator implements ConstraintValidator<CardLimitMatch, CreateCardRequestDTO> {

    @Override
    public boolean isValid(CreateCardRequestDTO requestDTO, ConstraintValidatorContext context) {
        if (requestDTO == null) {
            return true;
        }

        boolean isCreditCard = Card.CardType.CREDIT.name().equals(requestDTO.getCardType());
        boolean hasCreditLimit = requestDTO.getCreditLimit() != null && !requestDTO.getCreditLimit().isBlank();

        if (!isCreditCard && hasCreditLimit) {
            return false;
        } else {
            return true;
        }
    }
}