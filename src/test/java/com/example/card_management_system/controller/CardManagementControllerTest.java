package com.example.card_management_system.controller;

import com.example.card_management_system.dto.CardResponseDTO;
import com.example.card_management_system.dto.CreateCardRequestDTO;
import com.example.card_management_system.entity.Card;
import com.example.card_management_system.service.CardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardManagementControllerTest {

    @Mock
    CardService cardService;

    @InjectMocks
    CardManagementController cardManagementController;

    @Test
    public void givenCard_ExistingCustomer_CreatesCard(){
        CreateCardRequestDTO cardRequest = CreateCardRequestDTO.builder().cardNumber("1234123412341234").cardHolderName("ALICE WALLACE").build();
        CardResponseDTO expectedResponse = CardResponseDTO.builder().cardNumber("1234123412341234").cardHolderName("ALICE WALLACE").build();

        when(cardService.createNewCard(cardRequest)).thenReturn(expectedResponse);


        ResponseEntity<CardResponseDTO> responseEntity = cardManagementController.createCard(cardRequest);


        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("ALICE WALLACE", responseEntity.getBody().getCardHolderName());
    }

}