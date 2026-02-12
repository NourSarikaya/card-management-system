package com.example.card_management_system.service;

import com.example.card_management_system.dto.CardResponseDTO;
import com.example.card_management_system.dto.CardUpdateDTO;
import com.example.card_management_system.dto.CreateCardRequestDTO;
import com.example.card_management_system.entity.Card;
import com.example.card_management_system.mapper.CardMapper;
import com.example.card_management_system.repository.CardRepository;
import com.example.card_management_system.util.UUIDUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final CardMapper cardMapper;

    public CardResponseDTO getCardById(String accountId) {
        UUID cardId = (UUIDUtils.toUUID(accountId));

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new EntityNotFoundException("Card not found with ID: " + accountId));

        return cardMapper.cardToResponseDto(card);
    }

    public CardResponseDTO createNewCard(CreateCardRequestDTO cardRequestDTO) {
        try {
            Card newCard = cardMapper.requestDtoToCard(cardRequestDTO);

            newCard.setAccountId(UUID.randomUUID());
            newCard.setActive(true);
            newCard.setSecurityCode(generateSecurityCode());

            Card savedCard = cardRepository.save(newCard);
            log.info("Successfully created and saved new card for account: {}", savedCard.getAccountId());

            return cardMapper.cardToResponseDto(savedCard);

        } catch (RuntimeException e) {
            // TODO: Custom Exception?
            log.error("Error while creating card: {}:", e.getMessage());
            throw new RuntimeException("Failed to process card request: " + e.getMessage());
        }

    }

    public CardResponseDTO updateCard(String accountId, CardUpdateDTO cardUpdateDTO) {
        try {
            UUID accountUuid = UUIDUtils.toUUID(accountId);
            Card existingCard = cardRepository.findById(accountUuid)
                    .orElseThrow(() -> new RuntimeException("Card not found with ID: " + accountId));

            cardMapper.updateCardFromDto(cardUpdateDTO, existingCard);

            Card updatedCard = cardRepository.save(existingCard);
            log.info("Successfully updated and saved card for account: {}", accountId);

            return cardMapper.cardToResponseDto(updatedCard);
        } catch (RuntimeException e) {
            log.error("Error while updating card: {}", e.getMessage());
            throw new RuntimeException("Failed to update card");
        }
    }

    public List<CardResponseDTO> getAllCardsByCustomerId(String customerId) {
        UUID customerUuid = UUIDUtils.toUUID(customerId);

        List<Card> cardList = cardRepository.findByCustomer_CustomerId(customerUuid);

        if (cardList.isEmpty()) {
            throw new RuntimeException("No cards found for customer: " + customerId);
        }

        return cardList.stream()
                .map(cardMapper::cardToResponseDto)
                .toList();
    }

    public void deleteCard(String accountId) {
        UUID accountUuid = UUIDUtils.toUUID(accountId);

        Card existingCard = cardRepository.findById(accountUuid)
                .orElseThrow(() -> new EntityNotFoundException("Card not found with ID: " + accountId));
        try {
            cardRepository.delete(existingCard);
            log.info("Successfully delete card for account: {}", accountId);
        } catch (RuntimeException e) {
            log.error("Database error while deleting card {}: {}", accountId, e.getMessage());
            throw new RuntimeException("Could not delete card");
        }
    }


    private String generateSecurityCode() {
        SecureRandom secureRandom = new SecureRandom();

        int code = secureRandom.nextInt(1000);

        return String.format("%03d", code);
    }
}