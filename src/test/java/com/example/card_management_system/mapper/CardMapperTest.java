package com.example.card_management_system.mapper;

import com.example.card_management_system.dto.CardResponseDTO;
import com.example.card_management_system.dto.CreateCardRequestDTO;
import com.example.card_management_system.entity.Card;
import com.example.card_management_system.entity.Customer;
import com.example.card_management_system.util.UUIDUtils;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class CardMapperTest {

    private final CardMapper cardMapper = new CardMapperImpl();

    @Test
    void givenCard_whenMapped_thenReturnsResponseDto() {
        UUID cardId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();

        Customer mockCustomer = new Customer();
        mockCustomer.setCustomerId(customerId);

        Card card = Card.builder()
                        .accountId(cardId)
                        .cardNumber("1234123412341234")
                        .cardType(Card.CardType.CREDIT)
                        .expiryDate(LocalDate.of(2029, 12, 31))
                        .cardHolderName("John")
                        .active(true)
                        .creditLimit(new BigDecimal("3000.50"))
                        .securityCode("123")
                        .customer(mockCustomer)
                        .build();

        CardResponseDTO result = cardMapper.cardToResponseDto(card);

        assertThat(result.getAccountId()).isEqualTo(card.getAccountId().toString());
        assertThat(result.getCardNumber()).isEqualTo(card.getCardNumber());
        assertThat(result.getCardType()).isEqualTo(card.getCardType().toString());
//        assertThat(result.getExpiryDate()).isEqualTo(card.getExpiryDate().toString()); LocalDate yyyy-MM-dd vs "yyyyMM"
        assertThat(result.getExpiryDate()).isEqualTo("202912");
        assertThat(result.getActive()).isEqualTo(String.valueOf(card.isActive()));
        assertThat(result.getCreditLimit()).isEqualTo(card.getCreditLimit().toString());
        assertThat(result.getSecurityCode()).isEqualTo(card.getSecurityCode());
        assertThat(result.getCustomerId()).isEqualTo(customerId.toString());
    }

    @Test
    void givenCardRequestDto_whenRequestDtoToCardCalled_thenReturnsCardEntity() {
        UUID uuid = UUID.randomUUID();
        String customerId = uuid.toString();

        CreateCardRequestDTO createCardRequestDTO = CreateCardRequestDTO.builder()
                                                                        .customerId(customerId)
                                                                        .cardNumber("1234123412341234")
                                                                        .cardType(Card.CardType.CREDIT.toString())
                                                                        .expiryDate("202912")
                                                                        .cardHolderName("John")
                                                                        .creditLimit("3000.50")
                                                                        .build();

        Card result = cardMapper.requestDtoToCard(createCardRequestDTO);

        assertThat(result.getCardNumber()).isEqualTo(createCardRequestDTO.getCardNumber());
        assertThat(result.getCardType()).isEqualTo(Card.CardType.valueOf(createCardRequestDTO.getCardType().toUpperCase()));
        assertThat(result.getCardNumber()).isEqualTo(createCardRequestDTO.getCardNumber());
        assertThat(result.getExpiryDate()).isEqualTo(LocalDate.of(2029,12,31));
        assertThat(result.getCardHolderName()).isEqualTo(createCardRequestDTO.getCardHolderName());
        assertThat(result.getCreditLimit()).isEqualTo(createCardRequestDTO.getCreditLimit());
        assertThat(result.getCustomer().getCustomerId()).isEqualTo(UUIDUtils.toUUID(createCardRequestDTO.getCustomerId()));
    }

}