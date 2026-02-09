package com.example.card_management_system.mapper;

import com.example.card_management_system.dto.CardResponseDTO;
import com.example.card_management_system.entity.Card;
import com.example.card_management_system.entity.Customer;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class CardMapperTest {

    private final CardMapper cardMapper = new CardMapper();

    @Test
    void shouldMapCardEntityToCardResponseDto() {
        UUID cardId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();

        Customer mockCustomer = new Customer();
        mockCustomer.setCustomerId(customerId);

        Card card = Card.builder()
                        .id(cardId)
                        .cardNumber("1234123412341234")
                        .cardtype(Card.CardType.CREDIT)
                        .expiryDate(LocalDate.of(2029, 12, 31))
                        .cardHolderName("John")
                        .active(true)
                        .creditLimit(new BigDecimal("3000.50"))
                        .securityCode("123")
                        .customer(mockCustomer)
                        .build();

        CardResponseDTO result = cardMapper.cardToResponseDto(card);

        assertThat(result.getId().equals(card.getId().toString()));
        assertThat(result.getCardNumber().equals(card.getId().toString()));
        assertThat(result.getCardType().equals(card.getCardtype().toString()));
        assertThat(result.getExpiryDate().equals(card.getExpiryDate().toString()));
        assertThat(result.isActive()).isTrue();
        assertThat(result.getCreditLimit().equals(card.getCreditLimit().toString()));
        assertThat(result.getSecurityCode().equals(card.getSecurityCode()));
        assertThat(result.getCustomerId().equals(card.getCustomer().getCustomerId().toString()));
    }

}