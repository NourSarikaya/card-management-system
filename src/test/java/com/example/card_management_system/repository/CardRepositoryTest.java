package com.example.card_management_system.repository;

import com.example.card_management_system.entity.Card;
import com.example.card_management_system.entity.Card.CardType;
import com.example.card_management_system.entity.Customer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CardRepositoryTest {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CustomerRepository customerRepository;


    private Card testCard;
    private Card testCard_2;
    private Customer testCustomer;

    @BeforeEach
    public void setUp() {
        // Initialize test data before each test method
        UUID cardId = UUID.randomUUID();
        UUID cardId_2 = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();

        testCustomer = Customer.builder()
                .customerId(customerId)
                .firstName("firstname")
                .lastName("lastname")
                .middleInitial("E")
                .emailAddress("nnn@gmail.com")
                .phoneNumber("123123123121332")
                .phoneType(Customer.PhoneType.MOBILE)
                .addressLine1("Street")
                .addressLine2("apt")
                .cityName("chi")
                .state("IL")
                .zipcode("60606")
                .build();

        testCard = Card.builder()
                .accountId(cardId)
                .cardNumber("6011111111111117")
                .cardType(CardType.CREDIT)
                .cardHolderName("card holder name")
                .expiryDate(LocalDate.of(2026, 2, 7))
                .creditLimit(BigDecimal.valueOf(2000.00))
                .securityCode("234")
                .active(true)
                .customer(testCustomer)
                .build();

        //Save another card for the same customer
        testCard_2 = Card.builder()
                .accountId(cardId_2)
                .cardNumber("4111111111111111")
                .cardType(CardType.CREDIT)
                .cardHolderName("card holder name")
                .expiryDate(LocalDate.of(2026, 2, 7))
                .creditLimit(BigDecimal.valueOf(2000.00))
                .securityCode("234")
                .active(true)
                .customer(testCustomer)
                .build();

        customerRepository.save(testCustomer);
        cardRepository.save(testCard);
        cardRepository.save(testCard_2);
    }

    @AfterEach
    public void tearDown() {
        // Release test data after each test method
        customerRepository.delete(testCustomer);
        cardRepository.delete(testCard);
        cardRepository.delete(testCard_2);
    }


    @Test
    void givenCard_whenSaved_thenCanBeFoundByCardId() {
        Card savedCard = cardRepository.findById(testCard.getAccountId()).orElse(null);
        assertNotNull(savedCard);
        assertEquals(testCard.getCardHolderName(), savedCard.getCardHolderName());
        assertEquals(testCard.getCreditLimit(), savedCard.getCreditLimit());
    }

    @Test
    void givenCard_whenFindByCustomerIdCalled_thenCardIsFound() {

        List<Card> cards = cardRepository.findByCustomer_CustomerId(testCustomer.getCustomerId());


        assertThat(cards).isNotEmpty();
        assertThat(cards).hasSize(2);
        assertThat(cards)
                .allSatisfy(c -> {
                    assertThat(c.getCustomer().getCustomerId()).isEqualTo(testCustomer.getCustomerId());
                    assertThat(c.getCardHolderName()).isEqualTo("card holder name");
                    assertThat(c.getExpiryDate()).isEqualTo(LocalDate.of(2026, 2, 7));
                });

    }

    @Test
    void givenCard_whenDeleted_thenCanNotBeFoundByCardId() {
        assertThat(testCard.getAccountId()).isNotNull();

        //Deleting Card
        cardRepository.deleteById(testCard.getAccountId());
        assertThat(cardRepository.findById(testCard.getAccountId())).isEmpty();
    }

    @Test
    void givenCard_whenUpdated_thenCanBeFoundByCardIdWithUpdatedData() {
        //Updating Credit Limit of Card
        testCard.setCreditLimit(BigDecimal.valueOf(4000.00));
        cardRepository.save(testCard);

        Card updatedCard = cardRepository.findById(testCard.getAccountId()).orElse(null);
        assertNotNull(updatedCard);
        assertEquals(testCard.getCreditLimit(), updatedCard.getCreditLimit());
    }


}