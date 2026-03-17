package com.example.card_management_system.controller;

import com.example.card_management_system.dto.CardResponseDTO;
import com.example.card_management_system.dto.CardUpdateDTO;
import com.example.card_management_system.dto.CreateCardRequestDTO;
import com.example.card_management_system.dto.v1.CardResponseV1DTO;
import com.example.card_management_system.dto.v1.CardUpdateV1DTO;
import com.example.card_management_system.dto.v1.CreateCardRequestV1DTO;
import com.example.card_management_system.entity.Card;
import com.example.card_management_system.entity.CardUpdate;
import com.example.card_management_system.mapper.CardMapper;
import com.example.card_management_system.service.CardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;
    private final CardMapper cardMapper;

    @PostMapping("/create")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<CardResponseDTO> createCard(@Valid @RequestBody CreateCardRequestDTO request) {
        log.info("POST /cards - creating card for customer with customerId={}", request.getCustomerId());

        Card newCard = cardMapper.requestDtoToCard(request);

        Card created = cardService.createNewCard(newCard);

        CardResponseDTO responseDTO = cardMapper.cardToResponseDto(created);

        log.info("POST /cards - successfully created card for customer={}", created.getCardHolderName());
        return ResponseEntity.status(201).body(responseDTO);
    }

    @GetMapping("/find/{accountId}")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<CardResponseDTO> getCardById(@PathVariable String accountId) {
        log.info("GET /cards/{}- retrieving card", accountId);

        Card card = cardService.getCardById(accountId);

        CardResponseDTO retrieved = cardMapper.cardToResponseDto(card);

        log.info("GET /cards/{}- successfully retrieved card", accountId);
        return ResponseEntity.ok(retrieved);
    }


    @PutMapping("/update/{accountId}")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<CardResponseDTO> updateCardById(@PathVariable String accountId, @Valid @RequestBody CardUpdateDTO update) {
        log.info("PUT /cards/{}- updating card", accountId);

        CardUpdate cardUpdate = cardMapper.cardUpdateDTOtoCardUpdate(update);

        Card updated = cardService.updateCard(accountId, cardUpdate);

        CardResponseDTO cardResponseDTO = cardMapper.cardToResponseDto(updated);

        log.info("PUT /cards/{}- successfully updated card", accountId);
        return ResponseEntity.ok(cardResponseDTO);
    }

    @DeleteMapping("/remove/{accountId}")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<Void> deleteCardById(@PathVariable String accountId) {
        log.info("DELETE /cards/{}- deleting card", accountId);

        cardService.deleteCard(accountId); //404

        log.info("DELETE /cards/{}- successfully deleted card   ", accountId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{customerId}/all")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<CardResponseDTO>> getAllCardsByCustomerId(@PathVariable String customerId) {
        log.info("GET /cards/{}/cards- retrieving all cards", customerId);

        List<Card> retrievedCards = cardService.getAllCardsByCustomerId(customerId);

        List<CardResponseDTO> cardReponseDTOS = retrievedCards.stream().map(cardMapper::cardToResponseDto).toList();

        log.info("GET /cards/{}/cards- successfully retrieved all cards", customerId);
        return ResponseEntity.ok(cardReponseDTOS);
    }

    // V1 ENDPOINTS
    @PostMapping("/v1/create")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"), tags = {"card-controller-V1"})
    public ResponseEntity<CardResponseV1DTO> createCardV1(@Valid @RequestBody CreateCardRequestV1DTO request) {
        log.info("POST /v1/cards - creating card for customer with customerId={}", request.getCustomerId());

        Card newCard = cardMapper.requestV1DtoToCard(request);

        Card created = cardService.createNewCard(newCard);

        CardResponseV1DTO cardResponseV1DTO = cardMapper.cardToResponseV1Dto(created);

        log.info("POST /cards - successfully created card for customer={}", created.getCardHolderName());
        return ResponseEntity.status(201).body(cardResponseV1DTO);
    }

    @GetMapping("/v1/find/{accountId}")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"), tags = {"card-controller-V1"})
    public ResponseEntity<CardResponseV1DTO> getCardByIdV1(@PathVariable String accountId) {
        log.info("GET /v1/find/{}- retrieving card", accountId);

        Card card = cardService.getCardById(accountId);

        CardResponseV1DTO retrieved = cardMapper.cardToResponseV1Dto(card);

        log.info("GET /v1/find/{}- successfully retrieved card", accountId);
        return ResponseEntity.ok(retrieved);
    }

    @PutMapping("/v1/update/{accountId}")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"), tags = {"card-controller-V1"})
    public ResponseEntity<CardResponseV1DTO> updateCardByIdV1(@PathVariable String accountId, @Valid @RequestBody CardUpdateV1DTO cardUpdateV1DTO) {
        log.info("PUT /v1/update/{}- updating card", accountId);

        CardUpdate cardUpdate = cardMapper.cardUpdateV1DTOtoCardUpdate(cardUpdateV1DTO);

        Card updatedCard = cardService.updateCard(accountId, cardUpdate);

        CardResponseV1DTO updatedResponse = cardMapper.cardToResponseV1Dto(updatedCard);

        log.info("PUT /v1/update/{}- successfully updated card", accountId);
        return ResponseEntity.ok(updatedResponse);
    }

    @DeleteMapping("/v1/remove/{accountId}")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"), tags = {"card-controller-V1"})
    public ResponseEntity<Void> deleteCardByIdV1(@PathVariable String accountId) {
        log.info("DELETE /v1/remove/{}- deleting card", accountId);

        cardService.deleteCard(accountId); //404

        log.info("DELETE /v1/remove/{}- successfully deleted card   ", accountId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/v1/{customerId}/all")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"), tags = {"card-controller-V1"})
    public ResponseEntity<List<CardResponseV1DTO>> getAllCardsByCustomerIdV1(@PathVariable String customerId) {
        log.info("GET /v1/{}/all- retrieving all cards", customerId);

        List<Card> retrievedCards = cardService.getAllCardsByCustomerId(customerId);

        List<CardResponseV1DTO> cardResponseV1DTOS = retrievedCards.stream()
                                                                   .map(cardMapper::cardToResponseV1Dto)
                                                                   .toList();

        log.info("GET /v1/{}/all- successfully retrieved all cards", customerId);
        return ResponseEntity.ok(cardResponseV1DTOS);
    }
}