package com.example.card_management_system.service;

import com.example.card_management_system.dto.CardResponseDTO;
import com.example.card_management_system.entity.Card;
import com.example.card_management_system.entity.CardUpdate;
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

    public Card getCardById(String accountId) {
        UUID cardId = (UUIDUtils.toUUID(accountId));

        return cardRepository.findById(cardId)
                             .orElseThrow(() -> new EntityNotFoundException("Card not found with ID: " + accountId));
    }

    public Card createNewCard(Card newCard) {
        try {

            newCard.setAccountId(UUID.randomUUID());
            newCard.setAccountNumber(UUID.randomUUID());
            newCard.setActive(true);
            newCard.setSecurityCode(generateSecurityCode());

            Card savedCard = cardRepository.save(newCard);
            log.info("Successfully created and saved new card for account: {}", savedCard.getAccountId());

            return savedCard;

        } catch (RuntimeException e) {
            log.error("Error while creating card: {}:", e.getMessage());
            throw new RuntimeException("Failed to process card request: " + e.getMessage());
        }

    }

    public Card updateCard(String accountId, CardUpdate CardUpdate) {
        try {
            UUID accountUuid = UUIDUtils.toUUID(accountId);
            Card existingCard = cardRepository.findById(accountUuid)
                                              .orElseThrow(() -> new RuntimeException("Card not found with ID: " + accountId));

            cardMapper.updateCardFromCardUpdate(CardUpdate, existingCard);

            Card updatedCard = cardRepository.save(existingCard);
            log.info("Successfully updated and saved card for account: {}", accountId);

            return updatedCard;
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