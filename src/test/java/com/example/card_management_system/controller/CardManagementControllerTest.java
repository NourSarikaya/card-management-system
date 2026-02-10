package com.example.card_management_system.controller;

import com.example.card_management_system.service.CardService;
import com.example.card_management_system.service.CustomerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
class CardManagementControllerTest {
    @Mock
    CardService cardService;

    @Mock
    CustomerService customerService;

    @InjectMocks
    CardManagementController cardManagementController;

    @Test
    public void shouldCreateCard(){
        //...
    }


}