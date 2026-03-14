package com.example.card_management_system.controller;

import com.example.card_management_system.dto.CardResponseDTO;
import com.example.card_management_system.dto.CardUpdateDTO;
import com.example.card_management_system.dto.CreateCardRequestDTO;
import com.example.card_management_system.dto.v1.CardResponseV1DTO;
import com.example.card_management_system.dto.v1.CardUpdateV1DTO;
import com.example.card_management_system.dto.v1.CreateCardRequestV1DTO;
import com.example.card_management_system.entity.Card;
import com.example.card_management_system.entity.CardUpdate;
import com.example.card_management_system.mapper.CardMapper;
import com.example.card_management_system.service.CardService;
import com.example.card_management_system.util.UUIDUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardManagementControllerTest {

    @Mock
    CardService cardService;

    @Mock
    CardMapper cardMapper;

    @InjectMocks
    CardController cardManagementController;

    private CreateCardRequestDTO cardRequest;
    private CardResponseDTO expectedResponse;
    private CreateCardRequestV1DTO cardRequestV1;
    private CardResponseV1DTO expectedResponseV1;
    private Card card;
    private UUID customerId;


    @BeforeEach
    public void setUp() {
        customerId = UUID.fromString("561eed14-b0e4-45ec-9e6f-dc5b4238ee57");

        cardRequest = CreateCardRequestDTO.builder()
                                          .cardNumber("1234123412341234")
                                          .cardType("CREDIT")
                                          .customerId(customerId.toString())
                                          .creditLimit("3000")
                                          .cardHolderName("cardholdername").build();

        expectedResponse = CardResponseDTO.builder()
                                          .cardNumber("1234123412341234").cardType("CREDIT")
                                          .customerId("561eed14-b0e4-45ec-9e6f-dc5b4238ee57")
                                          .accountId("550e8400-e29b-41d4-a716-446655440000")
                                          .creditLimit("3000")
                                          .cardHolderName("cardholdername").build();

        cardRequestV1 = CreateCardRequestV1DTO.builder()
                                              .cardNumber("1234123412341234")
                                              .cardType("CREDIT")
                                              .customerId("561eed14-b0e4-45ec-9e6f-dc5b4238ee57")
                                              .creditLimit("3000")
                                              .dailyLimit("1000")
                                              .digitalCardOnly("false")
                                              .cardHolderName("cardholdername").build();

        expectedResponseV1 = CardResponseV1DTO.builder()
                                              .cardNumber("1234123412341234").cardType("CREDIT")
                                              .customerId("561eed14-b0e4-45ec-9e6f-dc5b4238ee57")
                                              .accountId("550e8400-e29b-41d4-a716-446655440000")
                                              .creditLimit("3000")
                                              .dailyLimit("1000")
                                              .digitalCardOnly("false")
                                              .cardHolderName("cardholdername").build();

        card = Card.builder().cardNumber("1234123412341234").cardType(Card.CardType.valueOf("CREDIT"))
                   .customerId(customerId)
                   .accountId(UUIDUtils.toUUID("550e8400-e29b-41d4-a716-446655440000"))
                   .creditLimit(BigDecimal.valueOf(3000))
                   .cardHolderName("cardholdername").build();
    }

    @Test
    public void givenCard_ExistingCustomer_CreatesCard() {

        when(cardMapper.requestDtoToCard(cardRequest)).thenReturn(card);
        when(cardService.createNewCard(card)).thenReturn(card);
        when(cardMapper.cardToResponseDto(card)).thenReturn(expectedResponse);

        ResponseEntity<CardResponseDTO> responseEntity = cardManagementController.createCard(cardRequest);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("cardholdername", responseEntity.getBody().getCardHolderName());
        verify(cardService).createNewCard(card);
    }


    @Test
    public void givenCardId_UpdatesTheProvidedCardFields() {

        CardUpdateDTO cardUpdateDTO = CardUpdateDTO.builder().creditLimit("6500").build();
        CardUpdate cardUpdate = CardUpdate.builder().creditLimit(BigDecimal.valueOf(6500)).build();
        CardResponseDTO expectedUpdatedResponse = CardResponseDTO.builder()
                                                                 .cardNumber("1234123412341234").cardType("CREDIT")
                                                                 .customerId("561eed14-b0e4-45ec-9e6f-dc5b4238ee57")
                                                                 .accountId("550e8400-e29b-41d4-a716-446655440000")
                                                                 .creditLimit("6500")
                                                                 .cardHolderName("cardholdername").build();

        Card updatedCard = Card.builder().cardNumber("1234123412341234").cardType(Card.CardType.valueOf("CREDIT"))
                               .customerId(customerId)
                               .accountId(UUIDUtils.toUUID("550e8400-e29b-41d4-a716-446655440000"))
                               .creditLimit(BigDecimal.valueOf(6500))
                               .cardHolderName("cardholdername").build();


        when(cardMapper.cardUpdateDTOtoCardUpdate(cardUpdateDTO)).thenReturn(cardUpdate);
        when(cardService.updateCard("550e8400-e29b-41d4-a716-446655440000", cardUpdate)).thenReturn(updatedCard);
        when(cardMapper.cardToResponseDto(updatedCard)).thenReturn(expectedUpdatedResponse);


        ResponseEntity<CardResponseDTO> responseEntity = cardManagementController
                .updateCardById("550e8400-e29b-41d4-a716-446655440000", cardUpdateDTO);


        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("cardholdername", responseEntity.getBody().getCardHolderName());
        assertEquals("6500", responseEntity.getBody().getCreditLimit());
        verify(cardService).updateCard("550e8400-e29b-41d4-a716-446655440000", cardUpdate);
    }

    @Test
    public void givenCardId_RetrievesCard() {

        when(cardService.getCardById(card.getAccountId().toString()))
                .thenReturn(card);
        when(cardMapper.cardToResponseDto(any())).thenReturn(expectedResponse);


        ResponseEntity<CardResponseDTO> responseEntity = cardManagementController
                .getCardById(String.valueOf(card.getAccountId().toString()));


        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("cardholdername", responseEntity.getBody().getCardHolderName());
        verify(cardService).getCardById("550e8400-e29b-41d4-a716-446655440000");
    }

    // V1 Controller tests
    @Test
    public void givenCard_ExistingCustomer_CreatesCardV1() {

        when(cardMapper.requestV1DtoToCard(cardRequestV1)).thenReturn(card);
        when(cardService.createNewCard(card)).thenReturn(card);
        when(cardMapper.cardToResponseV1Dto(card)).thenReturn(expectedResponseV1);

        ResponseEntity<CardResponseV1DTO> responseEntity = cardManagementController.createCardV1(cardRequestV1);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("cardholdername", responseEntity.getBody().getCardHolderName());
        assertEquals("1000", responseEntity.getBody().getDailyLimit());
        assertEquals("false", responseEntity.getBody().getDigitalCardOnly());
        verify(cardService).createNewCard(card);
    }


    @Test
    public void givenCardId_UpdatesTheProvidedCardFieldsV1() {

        CardUpdate cardUpdate = CardUpdate.builder().creditLimit(BigDecimal.valueOf(6500)).build();
        CardUpdateV1DTO cardUpdateV1DTO = CardUpdateV1DTO.builder().creditLimit("6500").build();
        CardResponseV1DTO expectedUpdatedResponse = CardResponseV1DTO.builder()
                                                                     .cardNumber("1234123412341234").cardType("CREDIT")
                                                                     .customerId("561eed14-b0e4-45ec-9e6f-dc5b4238ee57")
                                                                     .accountId("550e8400-e29b-41d4-a716-446655440000")
                                                                     .creditLimit("6500")
                                                                     .cardHolderName("cardholdername").build();

        Card updatedCard = Card.builder().cardNumber("1234123412341234").cardType(Card.CardType.valueOf("CREDIT"))
                               .customerId(customerId)
                               .accountId(UUIDUtils.toUUID("550e8400-e29b-41d4-a716-446655440000"))
                               .creditLimit(BigDecimal.valueOf(6500))
                               .cardHolderName("cardholdername").build();


        when(cardMapper.cardUpdateV1DTOtoCardUpdate(cardUpdateV1DTO)).thenReturn(cardUpdate);
        when(cardService.updateCard("550e8400-e29b-41d4-a716-446655440000", cardUpdate))
                .thenReturn(updatedCard);
        when(cardMapper.cardToResponseV1Dto(updatedCard)).thenReturn(expectedUpdatedResponse);


        ResponseEntity<CardResponseV1DTO> responseEntity = cardManagementController
                .updateCardByIdV1("550e8400-e29b-41d4-a716-446655440000", cardUpdateV1DTO);


        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("cardholdername", responseEntity.getBody().getCardHolderName());
        verify(cardService).updateCard("550e8400-e29b-41d4-a716-446655440000", cardUpdate);
    }

    @Test
    public void givenCardId_RetrievesCardV1() {

        when(cardService.getCardById(card.getAccountId().toString()))
                .thenReturn(card);
        when(cardMapper.cardToResponseV1Dto(any())).thenReturn(expectedResponseV1);


        ResponseEntity<CardResponseV1DTO> responseEntity = cardManagementController
                .getCardByIdV1(String.valueOf(card.getAccountId().toString()));


        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("cardholdername", responseEntity.getBody().getCardHolderName());
        verify(cardService).getCardById("550e8400-e29b-41d4-a716-446655440000");
    }

}