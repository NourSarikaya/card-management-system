package com.example.card_management_system.controller;

import com.example.card_management_system.dto.CardResponseDTO;
import com.example.card_management_system.dto.CardUpdateDTO;
import com.example.card_management_system.dto.CreateCardRequestDTO;
import com.example.card_management_system.service.CardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CardController {

    private final CardService cardService;

    @PostMapping("/cards")
    public ResponseEntity<CardResponseDTO> createCard(@Valid @RequestBody CreateCardRequestDTO request) {
        log.info("POST /cards - creating card for customer with customerId={}", request.getCustomerId());

        CardResponseDTO created = cardService.createNewCard(request);

        log.info("POST /cards - successfully created card for customer={}", created.getCardHolderName());
        return ResponseEntity.status(201).body(created);
    }

    @GetMapping("/cards/{accountId}")
    public ResponseEntity<CardResponseDTO> getCardById(@PathVariable String accountId) {
        log.info("GET /cards/{}- retrieving card", accountId);

        CardResponseDTO retrieved = cardService.getCardById(accountId);

        log.info("GET /cards/{}- successfully retrieved card", accountId);
        return ResponseEntity.ok(retrieved);
    }


    @PutMapping("/cards/{accountId}")
    public ResponseEntity<CardResponseDTO> updateCardById(@PathVariable String accountId, @Valid @RequestBody CardUpdateDTO update) {
        log.info("PUT /cards/{}- updating card", accountId);

        CardResponseDTO updated = cardService.updateCard(accountId, update);

        log.info("PUT /cards/{}- successfully updated card", accountId);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/cards/{accountId}")
    public ResponseEntity<Void> deleteCardById(@PathVariable String accountId) {
        log.info("DELETE /cards/{}- deleting card", accountId);

        cardService.deleteCard(accountId); //404

        log.info("DELETE /cards/{}- successfully deleted card   ", accountId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/cards/{customerId}/cards")
    public ResponseEntity<List<CardResponseDTO>> getAllCardsByCustomerId(@PathVariable String customerId) {
        log.info("GET /cards/{}/cards- retrieving all cards", customerId);

        List<CardResponseDTO> retrievedCards = cardService.getAllCardsByCustomerId(customerId);

        log.info("GET /cards/{}/cards- successfully retrieved all cards", customerId);
        return ResponseEntity.ok(retrievedCards);
    }

}