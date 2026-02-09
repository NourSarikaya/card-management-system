package com.example.card_management_system.mapper;

import com.example.card_management_system.dto.CardResponseDTO;
import com.example.card_management_system.entity.Card;
import org.springframework.stereotype.Component;

@Component
public class CardMapper {

    public CardResponseDTO cardToResponseDto(Card card) {
        if (card == null) return null;

        return CardResponseDTO.builder()
                              .id(card.getId().toString())
                              .cardNumber(card.getCardNumber())
                              .cardType(card.getCardtype().toString())
                              .expiryDate(card.getExpiryDate().toString())
                              .cardHolderName(card.getCardHolderName())
                              .active(card.isActive())
                              .creditLimit(card.getCreditLimit() != null ? card.getCreditLimit().toString() : null)
                              .securityCode(card.getSecurityCode())
                              .customerId(card.getCustomer().getCustomerId().toString())
                              .build();

    }

}
