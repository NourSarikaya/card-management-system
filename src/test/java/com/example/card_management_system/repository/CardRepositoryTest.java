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
    private Customer testCustomer;

    @BeforeEach
    public void setUp() {
        // Initialize test data before each test method
        UUID cardId = UUID.randomUUID();
        testCustomer = new Customer("firstname", "lastname",
                "E", "nnn@gmail.com", "123123123121332",
                Customer.PhoneType.MOBILE, "Street", "apt", "chi", "IL",
                "60606");

        testCard = new Card(//cardId,
                "6011111111111117",
                CardType.CREDIT,
                LocalDate.of(2026, 2, 7),
                "card holder name", true,
                BigDecimal.valueOf(2000.00), "234", testCustomer);

        customerRepository.save(testCustomer);
        cardRepository.save(testCard);

    }

    @AfterEach
    public void tearDown() {
        // Release test data after each test method
        cardRepository.delete(testCard);
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

        //Save another card for the same customer
        UUID cardId_2 = UUID.randomUUID();
        Card testCard_2 = new Card(//cardId_2,
                "4111111111111111", CardType.CREDIT,
                LocalDate.of(2026, 2, 7),
                "card holder name", true,
                BigDecimal.valueOf(2000.00), "234", testCustomer);
        cardRepository.save(testCard_2);


        List<Card> cards = cardRepository.findByCustomer_CustomerId(testCustomer.getCustomerId());


        assertThat(cards).isNotEmpty();
        assertThat(cards).hasSize(2);
        assertThat(cards).containsExactlyInAnyOrder(testCard, testCard_2);
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