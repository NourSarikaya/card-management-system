package com.example.card_management_system.service;

import com.example.card_management_system.entity.Card;
import com.example.card_management_system.entity.Card.CardType;
import com.example.card_management_system.entity.Customer;
import com.example.card_management_system.repository.CardRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock
    Customer customer;

    @Mock
    CardRepository cardRepository;

    @InjectMocks
    CardService cardService;

    @Test
    void shouldCreateCardWithCorrectFields(){

        UUID cardId=UUID.randomUUID();


        Card card =new Card(cardId,"1234567891234567", CardType.CREDIT, LocalDate.of(2026, 2, 4), "card holder name",true, BigDecimal.valueOf(2000.00),"234", customer);



        when(cardRepository.save(any(Card.class))).thenReturn(card);

        Card result = cardService.createCard(card);



        // check that exact  arguments are passed to the repository
        ArgumentCaptor<Card> captor = ArgumentCaptor.forClass(Card.class);
        verify(cardRepository).save(captor.capture());
        Card sendToRepo =captor.getValue();

        assertThat(sendToRepo.getCardNumber()).isEqualTo("1234567891234567");
        assertThat(sendToRepo.getCardtype()).isEqualTo(CardType.CREDIT);
        assertThat(sendToRepo.getCardHolderName()).isEqualTo("card holder name");
        assertThat(sendToRepo.getExpiryDate()).isEqualTo(LocalDate.of(2026, 2, 4));
        assertThat(sendToRepo.getCustomer()).isEqualTo(customer);

    }

};