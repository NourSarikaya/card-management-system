package com.example.card_management_system.mapper;

import com.example.card_management_system.dto.CardResponseDTO;
import com.example.card_management_system.dto.CardUpdateDTO;
import com.example.card_management_system.dto.CreateCardRequestDTO;
import com.example.card_management_system.dto.v1.CardUpdateV1DTO;
import com.example.card_management_system.dto.v1.CreateCardRequestV1DTO;
import com.example.card_management_system.entity.Card;
import com.example.card_management_system.entity.CardUpdate;
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

        Card card = Card.builder()
                        .accountId(cardId)
                        .cardNumber("1234123412341234")
                        .cardType(Card.CardType.CREDIT)
                        .expiryDate(LocalDate.of(2029, 12, 31))
                        .cardHolderName("John")
                        .active(true)
                        .creditLimit(new BigDecimal("3000.50"))
                        .securityCode("123")
                        .customerId(customerId)
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
        assertThat(result.getCardType()).isEqualTo(Card.CardType.valueOf(createCardRequestDTO.getCardType()
                                                                                             .toUpperCase()));
        assertThat(result.getCardNumber()).isEqualTo(createCardRequestDTO.getCardNumber());
        assertThat(result.getExpiryDate()).isEqualTo(LocalDate.of(2029, 12, 31));
        assertThat(result.getCardHolderName()).isEqualTo(createCardRequestDTO.getCardHolderName());
        assertThat(result.getCreditLimit()).isEqualTo(createCardRequestDTO.getCreditLimit());
        assertThat(result.getCustomerId()).isEqualTo(UUIDUtils.toUUID(createCardRequestDTO.getCustomerId()));
    }

    @Test
    void givenDebitCardRequestDto_whenRequestDtoToCardCalled_thenReturnsCardEntity() {
        UUID uuid = UUID.randomUUID();
        String customerId = uuid.toString();

        CreateCardRequestDTO createCardRequestDTO = CreateCardRequestDTO.builder()
                                                                        .customerId(customerId)
                                                                        .cardNumber("1234123412341234")
                                                                        .cardType(Card.CardType.DEBIT.toString())
                                                                        .expiryDate(null)
                                                                        .cardHolderName("John")
                                                                        .build();

        Card result = cardMapper.requestDtoToCard(createCardRequestDTO);

        assertThat(result.getCardNumber()).isEqualTo(createCardRequestDTO.getCardNumber());
        assertThat(result.getCardType()).isEqualTo(Card.CardType.valueOf(createCardRequestDTO.getCardType()
                                                                                             .toUpperCase()));
        assertThat(result.getCardNumber()).isEqualTo(createCardRequestDTO.getCardNumber());
        assertThat(result.getExpiryDate()).isNull();
        assertThat(result.getCardHolderName()).isEqualTo(createCardRequestDTO.getCardHolderName());
        assertThat(result.getCustomerId()).isEqualTo(UUIDUtils.toUUID(createCardRequestDTO.getCustomerId()));
    }

    @Test
    void givenValidCardUpdateRequest_whenUpdateCardCalled_thenOnlySpecificFieldsAreUpdated() {
        Card existingCard = Card.builder()
                                .cardNumber("1234123412341234")
                                .securityCode("123")
                                .creditLimit(BigDecimal.valueOf(1000.00))
                                .active(true)
                                .expiryDate(LocalDate.of(2027, 1, 1))
                                .build();

        CardUpdateDTO cardUpdateDTO = CardUpdateDTO.builder()
                                                   .creditLimit(BigDecimal.valueOf(2000.00).toString())
                                                   .active("false")
                                                   .expiryDate("202812")
                                                   .build();

        cardMapper.updateCardFromDto(cardUpdateDTO, existingCard);

        assertThat(BigDecimal.valueOf(2000.00)).isEqualTo(existingCard.getCreditLimit());
        assertThat(existingCard.isActive()).isFalse();
        assertThat(LocalDate.of(2028, 12, 31)).isEqualTo(existingCard.getExpiryDate());

        // Assert immutable fields that are not updated
        assertThat("1234123412341234").isEqualTo(existingCard.getCardNumber());
        assertThat("123").isEqualTo(existingCard.getSecurityCode());
    }

    @Test
    void givenValidCardUpdateRequest_whenUpdateCardCalled_thenOnlySpecificCardFieldsAreUpdated() {
        Card existingCard = Card.builder()
                                .cardNumber("1234123412341234")
                                .securityCode("123")
                                .creditLimit(BigDecimal.valueOf(1000.00))
                                .active(true)
                                .expiryDate(LocalDate.of(2027, 1, 1))
                                .build();

        CardUpdate cardUpdate = CardUpdate.builder()
                                          .creditLimit(BigDecimal.valueOf(2000.00))
                                          .active(false)
                                          .expiryDate(LocalDate.of(2028, 12, 31))
                                          .build();

        cardMapper.updateCardFromCardUpdate(cardUpdate, existingCard);

        assertThat(BigDecimal.valueOf(2000.00)).isEqualTo(existingCard.getCreditLimit());
        assertThat(existingCard.isActive()).isFalse();
        assertThat(LocalDate.of(2028, 12, 31)).isEqualTo(existingCard.getExpiryDate());

        // Assert immutable fields that are not updated
        assertThat("1234123412341234").isEqualTo(existingCard.getCardNumber());
        assertThat("123").isEqualTo(existingCard.getSecurityCode());
    }

    @Test
    void shouldMapCardUpdateDTOtoCardUpdate() {
        CardUpdateDTO cardUpdateDTO = CardUpdateDTO.builder()
                                                   .creditLimit(BigDecimal.valueOf(2000.00).toString())
                                                   .active("false")
                                                   .expiryDate("202812")
                                                   .build();

        CardUpdate result = cardMapper.cardUpdateDTOtoCardUpdate(cardUpdateDTO);

        assertThat(BigDecimal.valueOf(2000.00)).isEqualTo(result.getCreditLimit());
        assertThat(result.getActive()).isFalse();
        assertThat(LocalDate.of(2028, 12, 31)).isEqualTo(result.getExpiryDate());
    }

    @Test
    void givenNullCardUpdateDTO_shouldReturnNullCardUpdate() {
        CardUpdateDTO cardUpdateDTO = null;

        CardUpdate result = cardMapper.cardUpdateDTOtoCardUpdate(cardUpdateDTO);

        assertThat(result).isNull();
    }

    @Test
    void givenCardRequestDto_whenRequestV1DtoToCardCalled_thenReturnsCardEntity() {
        UUID uuid = UUID.randomUUID();
        String customerId = uuid.toString();

        CreateCardRequestV1DTO createCardRequestV1DTO = CreateCardRequestV1DTO.builder()
                                                                              .customerId(customerId)
                                                                              .cardNumber("1234123412341234")
                                                                              .cardType(Card.CardType.CREDIT.toString())
                                                                              .expiryDate("202912")
                                                                              .cardHolderName("John")
                                                                              .creditLimit("3000.50")
                                                                              .digitalCardOnly("false")
                                                                              .dailyLimit("20000")
                                                                              .build();

        Card result = cardMapper.requestV1DtoToCard(createCardRequestV1DTO);

        assertThat(result.getCardNumber()).isEqualTo(createCardRequestV1DTO.getCardNumber());
        assertThat(result.getCardType()).isEqualTo(Card.CardType.valueOf(createCardRequestV1DTO.getCardType()
                                                                                               .toUpperCase()));
        assertThat(result.getCardNumber()).isEqualTo(createCardRequestV1DTO.getCardNumber());
        assertThat(result.getExpiryDate()).isEqualTo(LocalDate.of(2029, 12, 31));
        assertThat(result.getCardHolderName()).isEqualTo(createCardRequestV1DTO.getCardHolderName());
        assertThat(result.getCreditLimit()).isEqualTo(createCardRequestV1DTO.getCreditLimit());
        assertThat(result.getCustomerId()).isEqualTo(UUIDUtils.toUUID(createCardRequestV1DTO.getCustomerId()));
        assertThat(result.getDigitalCardOnly()).isEqualTo(false);
        assertThat(result.getDailyLimit()).isEqualTo(BigDecimal.valueOf(20000));
    }

    @Test
    void shouldMapCardUpdateV1DTOtoCardUpdate() {
        CardUpdateV1DTO cardUpdateV1DTO = CardUpdateV1DTO.builder()
                                                         .creditLimit(BigDecimal.valueOf(2000.00).toString())
                                                         .active("false")
                                                         .expiryDate("202812")
                                                         .dailyLimit(BigDecimal.valueOf(5000.00).toString())
                                                         .build();

        CardUpdate result = cardMapper.cardUpdateV1DTOtoCardUpdate(cardUpdateV1DTO);

        assertThat(BigDecimal.valueOf(2000.00)).isEqualTo(result.getCreditLimit());
        assertThat(BigDecimal.valueOf(5000.00)).isEqualTo(result.getDailyLimit());
        assertThat(result.getActive()).isFalse();
        assertThat(LocalDate.of(2028, 12, 31)).isEqualTo(result.getExpiryDate());
    }

}