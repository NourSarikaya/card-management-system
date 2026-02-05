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
        /*
        1. Id (UUID) – Unique Identifier for each card
        2. CardNumber(String)– 16-digit credit/debit card number
        3. cardType(Enum): type of card (e.g. Credit, Debit)
        4. expiryDate(String): Expiration Date of card in YYYYMM format
        5. cardHolderName(String): Name of the card holder
        6. isActive(Boolean): Status of the card (active/inactive)
        7. CreditLimit(BigDecimal) – Credit Limit on the credit card
        8. SecurityCode (CVV)(String) - 3-digit code on the back of the card
        9. customer(Customer)-Representing the customer this card belongs to
        */
        UUID cardId=UUID.randomUUID();


        Card request =new Card(cardId,"1234567891234567", CardType.CREDIT, LocalDate.of(2026, 2, 4), "card holder name",true, BigDecimal.valueOf(2000.00),"234", customer);



        when(cardRepository.save(any(Card.class))).thenAnswer(invocation -> {
            Card c = invocation.getArgument(0);
            return new Card(cardId,c.getCardNumber(), c.getCardtype(), c.getExpiryDate(), c.getCardHolderName(),c.isActive(), c.getCreditLimit(),c.getSecurityCode(), c.getCustomer());
        });

        ArgumentCaptor<Card> captor = ArgumentCaptor.forClass(Card.class);

        Card result = cardService.createCard(request);

        assertThat(result.getId()).isEqualTo(cardId);

        verify(cardRepository).save(captor.capture());



        Card sendToRepo =captor.getValue();

        assertThat(sendToRepo.getCardNumber()).isEqualTo("1234567891234567");
        assertThat(sendToRepo.getCardtype()).isEqualTo(CardType.CREDIT);
        assertThat(sendToRepo.getCardHolderName()).isEqualTo("card holder name");
        assertThat(sendToRepo.getExpiryDate()).isEqualTo(LocalDate.of(2026, 2, 4));
        assertThat(sendToRepo.getCustomer()).isEqualTo(customer);

    }

};