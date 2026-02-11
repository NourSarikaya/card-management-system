package com.example.card_management_system.service;

import com.example.card_management_system.dto.CardResponseDTO;
import com.example.card_management_system.dto.CardUpdateDTO;
import com.example.card_management_system.dto.CreateCardRequestDTO;
import com.example.card_management_system.entity.Card;
import com.example.card_management_system.mapper.CardMapper;
import com.example.card_management_system.repository.CardRepository;
import com.example.card_management_system.repository.CustomerRepository;
import com.example.card_management_system.util.UUIDUtils;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;
    private final CustomerRepository customerRepository;
    private final CardMapper cardMapper;


    public CardResponseDTO getCardById(String accountId) {
        UUID cardId = (UUIDUtils.toUUID(accountId));

        Card card = cardRepository.findById(cardId)
                                  .orElseThrow(() -> new EntityNotFoundException("Card not found with ID: " + accountId));

        return cardMapper.cardToResponseDto(card);
    }

    public CardResponseDTO createNewCard(CreateCardRequestDTO cardRequestDTO) {
        try {
//            UUID customerId = UUID.fromString(cardRequestDTO.getCustomerId());
//
//            if (!customerRepository.existsById(customerId)) {
//                throw new EntityNotFoundException("Customer not found: " + cardRequestDTO.getCustomerId());
//            }

            Card newCard = cardMapper.requestDtoToCard(cardRequestDTO);

            //newCard.setAccountId(null);
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

    public CardResponseDTO updateCard(String accountId, CardUpdateDTO cardUpdateDTO) {
        try {
            UUID cardId = (UUIDUtils.toUUID(accountId));
            Card existingCard = cardRepository.findById(cardId)
                                              .orElseThrow(() -> new RuntimeException("Card not found with ID: " + accountId));

            cardMapper.updateCardFromDto(cardUpdateDTO, existingCard);

            Card updatedCard = cardRepository.save(existingCard);

            return cardMapper.cardToResponseDto(updatedCard);
        } catch (RuntimeException e) {
            throw new RuntimeException("Failed to update card: " + e.getMessage());
        }
    }


    private String generateSecurityCode() {
        SecureRandom secureRandom = new SecureRandom();

        int code = secureRandom.nextInt(1000);

        return String.format("%03d", code);
    }
}
