package com.example.card_management_system.controller;
import com.example.card_management_system.dto.CardUpdateDTO;
import com.example.card_management_system.dto.CreateCardRequestDTO;
import com.example.card_management_system.entity.Card;
import com.example.card_management_system.entity.Customer;
import com.example.card_management_system.repository.CardRepository;
import com.example.card_management_system.repository.CustomerRepository;
import com.example.card_management_system.service.CardService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//https://www.baeldung.com/spring-mockmvc-vs-webmvctest

@SpringBootTest
@AutoConfigureMockMvc
class CardManagementControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
        //existing customer the card will be created for
        customerRepository.save(testCustomer);
        //ensure repo empty
        cardRepository.deleteAll();
    }



    @Test
    void shouldCreateNewCard_CustomerExists() throws Exception{
        //need existing customer before creating card


        CreateCardRequestDTO cardRequest = CreateCardRequestDTO.builder()
                .cardNumber("4111111111111111")
                .cardType("CREDIT")
                .customerId(String.valueOf(testCustomer.getCustomerId()))
                .creditLimit("3000")
                .expiryDate("202605")
                .cardHolderName("firstname lastname").build();

        mockMvc.perform(post("/cards")
                .contentType(MediaType.APPLICATION_JSON)
                        //Jackson Object Mapper to Json String
                        .content(objectMapper.writeValueAsString(cardRequest)))
                .andDo(print()).andExpect(status().isCreated());

        assertThat(cardRepository.count()).isEqualTo(1);

        cardRepository.deleteAll();
    }

    @Test
    void shouldThrowBadRequestExceptionWhenCreatingCardWithWrongExpiryField() throws Exception{
        //need existing customer before creating card

        CreateCardRequestDTO invalidCardRequest = CreateCardRequestDTO.builder()
                .cardNumber("4111111111111111")
                .cardType("CREDIT")
                .customerId(String.valueOf(testCustomer.getCustomerId()))
                .creditLimit("3000")
                .expiryDate("2026") //missing the month should be 6 digits
                .cardHolderName("firstname lastname").build();

        mockMvc.perform(post("/cards")
                        .contentType(MediaType.APPLICATION_JSON)
                        //Jackson Object Mapper to Json String
                        .content(objectMapper.writeValueAsString(invalidCardRequest)))
                .andDo(print()).andExpect(status().isBadRequest());

        assertThat(cardRepository.count()).isEqualTo(0);
    }

    @Test
    void shouldUpdateNewCard_GivenValidAccountId() throws Exception{
        //Create the card to be updated in the repository
        UUID accountId = UUID.randomUUID();
        Card testCard = Card.builder()
                .accountId(accountId)
                .cardNumber("6011111111111117")
                .cardType(Card.CardType.CREDIT)
                .cardHolderName("card holder name")
                .expiryDate(LocalDate.of(2026, 2, 7))
                .creditLimit(BigDecimal.valueOf(2000.00))
                .securityCode("234")
                .active(true)
                .customer(testCustomer)
                .build();

        cardRepository.save(testCard);

        //Update Credit Limit of Card
        CardUpdateDTO cardUpdate = CardUpdateDTO.builder().creditLimit("6500.00").expiryDate("202606").build();

        mockMvc.perform(put("/cards/{accountId}",testCard.getAccountId())
                        .contentType(MediaType.APPLICATION_JSON)
                        //Jackson Object Mapper to Json String
                        .content(objectMapper.writeValueAsString(cardUpdate)))
                .andDo(print()).andExpect(status().isOk());

        Card updatedCard = cardRepository.findById(testCard.getAccountId()).orElse(null);

        assertThat(updatedCard).isNotNull();
        assertThat(updatedCard.getCreditLimit()).isEqualTo(cardUpdate.getCreditLimit());

        cardRepository.deleteAll();
    }



}