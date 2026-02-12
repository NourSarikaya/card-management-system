package com.example.card_management_system.controller;
import com.example.card_management_system.dto.CardUpdateDTO;
import com.example.card_management_system.dto.CreateCardRequestDTO;
import com.example.card_management_system.dto.CreateCustomerRequestDTO;
import com.example.card_management_system.dto.CustomerUpdateDTO;
import com.example.card_management_system.entity.Card;
import com.example.card_management_system.entity.Customer;
import com.example.card_management_system.repository.CardRepository;
import com.example.card_management_system.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import static org.hamcrest.CoreMatchers.is;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Autowired
    private CustomerRepository customerRepository;


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
                .cardHolderName("card holder name").build();

        mockMvc.perform(post("/cards")
                .contentType(MediaType.APPLICATION_JSON)
                        //Jackson Object Mapper to Json String
                        .content(objectMapper.writeValueAsString(cardRequest)))
                .andDo(print()).andExpect(status().isCreated());

        assertThat(cardRepository.count()).isEqualTo(1);
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
    void shouldUpdateCreditLimit_OfCard_GivenValidAccountId() throws Exception {
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
        CardUpdateDTO cardUpdate = CardUpdateDTO.builder().creditLimit("6500.00").build();

        mockMvc.perform(put("/cards/{accountId}", testCard.getAccountId())
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

        mockMvc.perform(get("/cards/{accountId}", testCard.getAccountId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.cardNumber", is(testCard.getCardNumber())))
                .andExpect(jsonPath("$.cardType", is(String.valueOf(testCard.getCardType()))))
                .andExpect(jsonPath("$.cardHolderName", is(testCard.getCardHolderName())));

    }

    @Test
    void shouldCreateNewCustomer() throws Exception{
        //make sure Customer table is empty
        customerRepository.deleteAll();

        CreateCustomerRequestDTO customerRequest = CreateCustomerRequestDTO.builder()
                .firstName("firstname")
                .lastName("lastname")
                .middleInitial("E")
                .emailAddress("nnn1@gmail.com")
                .phoneNumber("123123123121342")
                .phoneType("MOBILE")
                .addressLine1("Street")
                .addressLine2("apt")
                .cityName("chi")
                .state("IL")
                .zipCode("60606")
                .build();

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        //Jackson Object Mapper to Json String
                        .content(objectMapper.writeValueAsString(customerRequest)))
                .andDo(print()).andExpect(status().isCreated());

        assertThat(customerRepository.count()).isEqualTo(1);
    }

    @Test
    void shouldThrowBadRequestException_WhenCreatingCustomerWithWrongStateField()  throws Exception{
        //make sure Customer table is empty
        customerRepository.deleteAll();

        CreateCustomerRequestDTO customerRequest = CreateCustomerRequestDTO.builder()
                .firstName("firstname")
                .lastName("lastname")
                .middleInitial("E")
                .emailAddress("nnn1@gmail.com")
                .phoneNumber("123123123121342")
                .phoneType("MOBILE")
                .addressLine1("Street")
                .addressLine2("apt")
                .cityName("chi")
                .state("ILL")//State has to be exactly 2 digits
                .zipCode("60606")
                .build();

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        //Jackson Object Mapper to Json String
                        .content(objectMapper.writeValueAsString(customerRequest)))
                .andDo(print()).andExpect(status().isBadRequest());

        assertThat(customerRepository.count()).isEqualTo(0);//Customer not created
    }

    @Test
    void shouldRetrieveCustomer_GivenValidCustomerId() throws Exception {
        //Retrieving testCustomer that is persisted at SetUp
        mockMvc.perform(get("/customers/{customerId}", testCustomer.getCustomerId()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.phoneNumber", is(testCustomer.getPhoneNumber())))
                .andExpect(jsonPath("$.firstName", is(testCustomer.getFirstName())))
                .andExpect(jsonPath("$.lastName", is(testCustomer.getLastName())));

    }

    @Test
    void shouldUpdateEmailAddress_OfCustomer_GivenValidCustomerId() throws Exception {
        //Update testCustomer
        CustomerUpdateDTO customerUpdate = CustomerUpdateDTO.builder().emailAddress("updated@email.com").build();

        mockMvc.perform(put("/customers/{customerId}", testCustomer.getCustomerId())
                        .contentType(MediaType.APPLICATION_JSON)
                        //Jackson Object Mapper to Json String
                        .content(objectMapper.writeValueAsString(customerUpdate)))
                .andDo(print()).andExpect(status().isOk());

        Customer updatedCustomer = customerRepository.findById(testCustomer.getCustomerId()).orElse(null);

        assertThat(updatedCustomer).isNotNull();
        assertThat(updatedCustomer.getEmailAddress()).isEqualTo(customerUpdate.getEmailAddress());
    }

}