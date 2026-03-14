package com.example.card_management_system.controller;

import com.example.card_management_system.dto.CardUpdateDTO;
import com.example.card_management_system.dto.CreateCardRequestDTO;
import com.example.card_management_system.entity.Card;
import com.example.card_management_system.repository.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.CoreMatchers.is;

import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//https://www.baeldung.com/spring-mockmvc-vs-webmvctest
//https://www.makariev.com/blog/testing-spring-boot-crud-rest-api-mock-mvc/
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CardManagementControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CardRepository cardRepository;

    @BeforeEach
    public void setUp() {
        // Initialize test data before each test method
        UUID customerId = UUID.randomUUID();
    }


    @Test
    void shouldCreateNewCard_CustomerExists() throws Exception {
        //need existing customer before creating card

        CreateCardRequestDTO cardRequest = CreateCardRequestDTO.builder()
                                                               .cardNumber("4111111111111111")
                                                               .cardType("CREDIT")
                                                               .customerId(String.valueOf(UUID.randomUUID()))
                                                               .creditLimit("3000")
                                                               .expiryDate("202605")
                                                               .cardHolderName("card holder name").build();

        mockMvc.perform(post("/api/cards/create")
                       .with(jwt().jwt(builder -> builder.subject("test-user"))
                                  .authorities(new SimpleGrantedAuthority("SCOPE_ROLE_CLIENT")))
                       .contentType(MediaType.APPLICATION_JSON)
                       //Jackson Object Mapper to Json String
                       .content(objectMapper.writeValueAsString(cardRequest)))
               .andDo(print()).andExpect(status().isCreated());

        assertThat(cardRepository.count()).isEqualTo(1);
    }

    @Test
    void shouldThrowBadRequestExceptionWhenCreatingCardWithWrongExpiryField() throws Exception {
        //need existing customer before creating card

        CreateCardRequestDTO invalidCardRequest = CreateCardRequestDTO.builder()
                                                                      .cardNumber("4111111111111111")
                                                                      .cardType("CREDIT")
                                                                      .customerId(String.valueOf(UUID.randomUUID()))
                                                                      .creditLimit("3000")
                                                                      .expiryDate("2026") //missing the month should be 6 digits
                                                                      .cardHolderName("firstname lastname").build();

        mockMvc.perform(post("/api/cards/create")
                       .with(jwt().jwt(builder -> builder.subject("test-user"))
                                  .authorities(new SimpleGrantedAuthority("SCOPE_ROLE_CLIENT")))
                       .contentType(MediaType.APPLICATION_JSON)
                       //Jackson Object Mapper to Json String
                       .content(objectMapper.writeValueAsString(invalidCardRequest)))
               .andDo(print()).andExpect(status().isBadRequest());

        assertThat(cardRepository.count()).isEqualTo(0);
    }

    @Test
    void shouldUpdateCreditLimit_OfCard_GivenValidAccountId() throws Exception {
        //Create the card to be updated in the repository
        UUID accountId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();

        Card testCard = Card.builder()
                            .accountId(accountId)
                            .cardNumber("6011111111111117")
                            .cardType(Card.CardType.CREDIT)
                            .cardHolderName("card holder name")
                            .expiryDate(LocalDate.of(2026, 2, 7))
                            .creditLimit(BigDecimal.valueOf(2000.00))
                            .securityCode("234")
                            .active(true)
                            .customerId(customerId)
                            .build();

        cardRepository.save(testCard);

        //Update Credit Limit of Card
        CardUpdateDTO cardUpdate = CardUpdateDTO.builder().creditLimit("6500.00").build();

        mockMvc.perform(put("/api/cards/update/{accountId}", testCard.getAccountId())
                       .with(jwt().jwt(builder -> builder.subject("test-user"))
                                  .authorities(new SimpleGrantedAuthority("SCOPE_ROLE_CLIENT")))
                       .contentType(MediaType.APPLICATION_JSON)
                       //Jackson Object Mapper to Json String
                       .content(objectMapper.writeValueAsString(cardUpdate)))
               .andDo(print()).andExpect(status().isOk());

        Card updatedCard = cardRepository.findById(testCard.getAccountId()).orElse(null);

        assertThat(updatedCard).isNotNull();
        assertThat(updatedCard.getCreditLimit()).isEqualTo(cardUpdate.getCreditLimit());
    }

    @Test
    void shouldRetrieveCard_GivenValidAccountId() throws Exception {
        //Create the card to be retrieved from the repository
        UUID accountId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();

        Card testCard = Card.builder()
                            .accountId(accountId)
                            .cardNumber("6011111111111117")
                            .cardType(Card.CardType.CREDIT)
                            .cardHolderName("card holder name")
                            .expiryDate(LocalDate.of(2026, 2, 7))
                            .creditLimit(BigDecimal.valueOf(2000.00))
                            .securityCode("234")
                            .active(true)
                            .customerId(customerId)
                            .build();

        cardRepository.save(testCard);

        mockMvc.perform(get("/api/cards/find/{accountId}", testCard.getAccountId())
                       .with(jwt().jwt(builder -> builder.subject("test-user"))
                                  .authorities(new SimpleGrantedAuthority("SCOPE_ROLE_CLIENT"))))
               .andExpect(status().isOk())
               .andDo(print())
               .andExpect(jsonPath("$.cardNumber", is(testCard.getCardNumber())))
               .andExpect(jsonPath("$.cardType", is(String.valueOf(testCard.getCardType()))))
               .andExpect(jsonPath("$.cardHolderName", is(testCard.getCardHolderName())));

    }

    @Test
    void shouldDeleteCard_GivenValidAccountId() throws Exception {
        //Create the card to be retrieved from the repository
        UUID accountId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();

        Card testCard = Card.builder()
                            .accountId(accountId)
                            .cardNumber("6011111111111117")
                            .cardType(Card.CardType.CREDIT)
                            .cardHolderName("card holder name")
                            .expiryDate(LocalDate.of(2026, 2, 7))
                            .creditLimit(BigDecimal.valueOf(2000.00))
                            .securityCode("234")
                            .active(true)
                            .customerId(customerId)
                            .build();

        cardRepository.save(testCard);

        mockMvc.perform(delete("/api/cards/remove/{accountId}", testCard.getAccountId())
                       .with(jwt().jwt(builder -> builder.subject("test-user"))
                                  .authorities(new SimpleGrantedAuthority("SCOPE_ROLE_CLIENT"))))
               .andDo(print())
               .andExpect(status().isNoContent());

    }

    @Test
    void shouldRetrieveAllCards_GivenValidCustomerId() throws Exception {
        //Create cards to be retrieved from the repository
        UUID accountId = UUID.randomUUID();
        UUID accountId_2 = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();

        Card testCard = Card.builder()
                            .accountId(accountId)
                            .accountNumber(UUID.randomUUID())
                            .cardNumber("6011111111111117")
                            .cardType(Card.CardType.CREDIT)
                            .cardHolderName("card holder name")
                            .expiryDate(LocalDate.of(2026, 2, 7))
                            .creditLimit(BigDecimal.valueOf(2000.00))
                            .securityCode("234")
                            .active(true)
                            .customerId(customerId)
                            .build();

        Card testCard_2 = Card.builder()
                              .accountId(accountId_2)
                              .accountNumber(UUID.randomUUID())
                              .cardNumber("4111111111111111")
                              .cardType(Card.CardType.CREDIT)
                              .cardHolderName("card holder name")
                              .expiryDate(LocalDate.of(2026, 2, 7))
                              .creditLimit(BigDecimal.valueOf(2000.00))
                              .securityCode("234")
                              .active(true)
                              .customerId(customerId)
                              .build();

        cardRepository.save(testCard);
        cardRepository.save(testCard_2);

        mockMvc.perform(get("/api/cards/{customerId}/all", customerId)
                       .with(jwt().jwt(builder -> builder.subject("test-user"))
                                  .authorities(new SimpleGrantedAuthority("SCOPE_ROLE_CLIENT"))))
               .andExpect(status().isOk())
               .andDo(print());


    }

}