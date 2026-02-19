package com.example.card_management_system.controller;

import com.example.card_management_system.dto.CardResponseDTO;
import com.example.card_management_system.dto.CardUpdateDTO;
import com.example.card_management_system.dto.CreateCardRequestDTO;
import com.example.card_management_system.service.CardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardManagementControllerTest {

    @Mock
    CardService cardService;

    @InjectMocks
    CardController cardManagementController;

    private CreateCardRequestDTO cardRequest;
    private CardResponseDTO expectedResponse;


    @BeforeEach
    public void setUp() {
        cardRequest = CreateCardRequestDTO.builder()
                                          .cardNumber("1234123412341234")
                                          .cardType("CREDIT")
                                          .customerId("561eed14-b0e4-45ec-9e6f-dc5b4238ee57")
                                          .creditLimit("3000")
                                          .cardHolderName("cardholdername").build();

        expectedResponse = CardResponseDTO.builder()
                                          .cardNumber("1234123412341234").cardType("CREDIT")
                                          .customerId("561eed14-b0e4-45ec-9e6f-dc5b4238ee57")
                                          .accountId("550e8400-e29b-41d4-a716-446655440000")
                                          .creditLimit("3000")
                                          .cardHolderName("cardholdername").build();
    }

    @Test
    public void givenCard_ExistingCustomer_CreatesCard() {


        when(cardService.createNewCard(cardRequest)).thenReturn(expectedResponse);


        ResponseEntity<CardResponseDTO> responseEntity = cardManagementController.createCard(cardRequest);


        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("cardholdername", responseEntity.getBody().getCardHolderName());
        verify(cardService).createNewCard(cardRequest);
    }


    @Test
    public void givenCardId_UpdatesTheProvidedCardFields() {

        CardUpdateDTO cardUpdate = CardUpdateDTO.builder().creditLimit("6500").build();
        CardResponseDTO expectedUpdatedResponse = CardResponseDTO.builder()
                                                                 .cardNumber("1234123412341234").cardType("CREDIT")
                                                                 .customerId("561eed14-b0e4-45ec-9e6f-dc5b4238ee57")
                                                                 .accountId("550e8400-e29b-41d4-a716-446655440000")
                                                                 .creditLimit("6500")
                                                                 .cardHolderName("cardholdername").build();

        when(cardService.updateCard("550e8400-e29b-41d4-a716-446655440000", cardUpdate))
                .thenReturn(expectedUpdatedResponse);


        ResponseEntity<CardResponseDTO> responseEntity = cardManagementController
                .updateCardById("550e8400-e29b-41d4-a716-446655440000", cardUpdate);


        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("cardholdername", responseEntity.getBody().getCardHolderName());
        verify(cardService).updateCard("550e8400-e29b-41d4-a716-446655440000", cardUpdate);
    }

    @Test
    public void givenCardId_RetrievesCard() {


        when(cardService.getCardById("550e8400-e29b-41d4-a716-446655440000"))
                .thenReturn(expectedResponse);


        ResponseEntity<CardResponseDTO> responseEntity = cardManagementController
                .getCardById("550e8400-e29b-41d4-a716-446655440000");


        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("cardholdername", responseEntity.getBody().getCardHolderName());
        verify(cardService).getCardById("550e8400-e29b-41d4-a716-446655440000");
    }

    //need to add more mockito tests for 80% coverage of controller

}