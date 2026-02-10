package com.example.card_management_system.service;

import com.example.card_management_system.dto.CardResponseDTO;
import com.example.card_management_system.dto.CreateCardRequestDTO;
import com.example.card_management_system.entity.Card;
import com.example.card_management_system.mapper.CardMapper;
import com.example.card_management_system.repository.CardRepository;
import com.example.card_management_system.util.UUIDUtils;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.UUID;

@Service
public class CardService {

    private CardRepository cardRepository;
    private CardMapper cardMapper;

    public CardResponseDTO getCardById(String id) {
        UUID cardId = (UUIDUtils.toUUID(id));

        Card card = cardRepository.findById(cardId)
                                  .orElseThrow(() -> new EntityNotFoundException("Card not found with ID: " + id));

        return cardMapper.cardToResponseDto(card);
    }

    public CardResponseDTO createNewCard(CreateCardRequestDTO cardRequestDTO) {
        try {
            Card newCard = cardMapper.requestDtoToCard(cardRequestDTO);

            newCard.setAccountId(UUID.randomUUID());
            newCard.setActive(true);
            newCard.setSecurityCode(generateSecurityCode());

            Card savedCard = cardRepository.save(newCard);
            return cardMapper.cardToResponseDto(savedCard);

        } catch (RuntimeException e) {
            // TODO: Custom Exception?
            throw new RuntimeException("Failed to process card request: " + e.getMessage());
        }

    }

    private String generateSecurityCode() {
        SecureRandom secureRandom = new SecureRandom();

        int code = secureRandom.nextInt(1000);

        return String.format("%03d", code);
    }
}
