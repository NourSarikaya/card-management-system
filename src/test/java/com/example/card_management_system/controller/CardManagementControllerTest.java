package com.example.card_management_system.controller;

import com.example.card_management_system.dto.CardResponseDTO;
import com.example.card_management_system.dto.CreateCardRequestDTO;
import com.example.card_management_system.service.CardService;
import com.example.card_management_system.service.CustomerService;
import jakarta.validation.Valid;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CardManagementControllerTest {
    @Mock
    CardService cardService;

    @Mock
    CustomerService customerService;

    @InjectMocks
    CardManagementController cardManagementController;

    @Valid CreateCardRequestDTO cardRequestDTO;

    @BeforeEach
    public void setUp() {
        String customerIdString="561eed14-b0e4-45ec-9e6f-dc5b4238ee57";
        cardRequestDTO =new CreateCardRequestDTO().builder().customerId(customerIdString).build();
    }

    @Test
    public void givenValidCardInfo_ExistingCustomer_thenReturnCardResponseDto(){
//        MockHttpServletRequest request = new MockHttpServletRequest();
//        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
//
//        when(cardService.createCard(any(CreateCardRequestDTO.class))).thenReturn(true);
//
//
//        ResponseEntity<CardResponseDTO> responseEntity = cardManagementController.createCard(cardRequestDTO);
//
//        assertThat(responseEntity.getStatusCode()).isEqualTo(201);
        //assertThat(responseEntity.getHeaders().getLocation().getPath()).isEqualTo("/1");

    }


}