package com.example.card_management_system.controller;
import com.example.card_management_system.dto.*;
import com.example.card_management_system.entity.Card;
import com.example.card_management_system.service.CardService;
//import com.example.card_management_system.service.CustomerService;
import com.example.card_management_system.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class CardManagementController {

    //Logging with Slf4j
    private static final Logger logger = LoggerFactory.getLogger(CardManagementController.class);

    private final CardService cardService;
    private final CustomerService customerService;

    /**
     * CREATE CARD
     * createCard(@RequestBody CreateCardRequestDTO request)
     * Creates a new card for an existing customer using card details provided.
     * @param request the request that contains the customer id and other card info
     * @return status code & created Card in the response body
     */
    @PostMapping("/cards")
    public ResponseEntity<CardResponseDTO> createCard(@Valid @RequestBody CreateCardRequestDTO request){
        logger.info("POST /cards - creating card for customer with customerId={}",request.getCustomerId());

        CardResponseDTO created= cardService.createNewCard(request);

        logger.info("POST /cards - successfully created card for customer={}",created.getCardHolderName());
        return ResponseEntity.status(201).body(created);
    }

    /**
     * GET CARD BY ID
     * getCardById(@PathVariable UUID accountId)
     * Retrieves card given cardId
     * @param accountId
     * @return status code & fetched Card in the response body
     */
    @GetMapping("/cards/{accountId}")
    public ResponseEntity<CardResponseDTO> getCardById(@PathVariable String accountId){
        logger.info("GET /cards/{}- retrieving card",accountId);

        CardResponseDTO retrieved = cardService.getCardById(accountId);

        logger.info("GET /cards/{}- successfully retrieved card",accountId);
        return ResponseEntity.ok(retrieved);
    }

    /**
     * UPDATE CARD WITH GIVEN ID
     * updateCardById(@PathVariable UUID accountId, @RequestBody CardUpdateDTO update)
     * Updates card given cardId
     * @param accountId, update
     * @return status code & updated Card in the response body
     */
    @PutMapping("/cards/{accountId}")
    public ResponseEntity<CardResponseDTO> updateCardById(@PathVariable String accountId, @Valid @RequestBody CardUpdateDTO update){
        logger.info("PUT /cards/{}- updating card",accountId);

        CardResponseDTO updated = cardService.updateCard(accountId, update);

        logger.info("PUT /cards/{}- successfully updated card",accountId);
        return ResponseEntity.ok(updated);
    }

    /**
     * CREATE CUSTOMER
     * createCustomer(@RequestBody CreateCustomerRequestDTO request)
     * Creates a new customer using customer details provided.
     * @param request the request that contains the customer info
     * @return status code & created Customer in the response body
     */
    @PostMapping("/customers")
    public ResponseEntity<CustomerResponseDTO> createCustomer(@Valid @RequestBody CreateCustomerRequestDTO request){
        logger.info("POST /customers - creating customer ");

        CustomerResponseDTO created= customerService.createNewCustomer(request);

        logger.info("POST /customers - successfully created customer with id = {}", created.getCustomerID());
        return ResponseEntity.status(201).body(created);
    }

    /**
     * GET CUSTOMER BY ID
     * getCustomerById(@PathVariable UUID customerId)
     * Retrieves customer given customerId
     * @param customerId
     * @return status code & fetched Customer in the response body
     */
    @GetMapping("/customers/{customerId}")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable String customerId){
        logger.info("GET /customers/{}- retrieving customer",customerId);

        CustomerResponseDTO updated = customerService.getCustomerById(customerId);

        logger.info("GET /customers/{}- successfully retrieved customer",customerId);
        return ResponseEntity.ok(updated);
    }

    /**
     * UPDATE CUSTOMER WITH GIVEN ID
     * updateCustomerById(@PathVariable UUID customerId, @RequestBody CustomerUpdateDTO update)
     * Updates customer given customerId
     * @param customerId, update
     * @return status code & updated Customer in the response body
     */
    @PutMapping("/customers/{customerId}")
    public ResponseEntity<CustomerResponseDTO> updateCustomerById(@PathVariable String customerId, @Valid @RequestBody CustomerUpdateDTO update){
        logger.info("PUT /customers/{}- updating customer",customerId);

        CustomerResponseDTO updated = customerService.updateCustomer(customerId, update);

        logger.info("PUT /customers/{}- successfully updated customer",customerId);
        return ResponseEntity.ok(updated);
    }

    /**
     * DELETE CARD BY ID
     * deleteCardById(@PathVariable UUID cardId)
     * Deletes card given cardId
     * @param accountId
     * @return
     */
    @DeleteMapping("/cards/{accountId}")
    public ResponseEntity<Void> deleteCardById(@PathVariable String accountId){
        logger.info("DELETE /cards/{}- deleting card",accountId);

        cardService.deleteCard(accountId); //404

        logger.info("DELETE /cards/{}- successfully deleted card",accountId);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET ALL CARDS BY CUSTOMER ID
     * getAllCardsByCustomerId(@PathVariable UUID customerId)
     * Retrieves all cards given customerId
     * @param customerId
     * @return status code & fetched Cards in the response body
     */
    @GetMapping("/customers/{customerId}/cards")
    public ResponseEntity<List<CardResponseDTO>> getAllCardsByCustomerId(@PathVariable String customerId){
        logger.info("GET /customers/{}/cards- retrieving all cards",customerId);

        List<CardResponseDTO> retrievedCards = cardService.getAllCardsByCustomerId(customerId);

        logger.info("GET /customers/{}/cards- successfully retrieved all cards",customerId);
        return ResponseEntity.ok(retrievedCards);
    }




}