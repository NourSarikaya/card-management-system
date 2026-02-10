package com.example.card_management_system.service;

import com.example.card_management_system.dto.CardResponseDTO;
import com.example.card_management_system.dto.CardUpdateDTO;
import com.example.card_management_system.dto.CreateCardRequestDTO;
import com.example.card_management_system.entity.Card;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class CardService {
    public CardResponseDTO createCard( @Valid CreateCardRequestDTO request) {
        return null;
    }

    public CardResponseDTO getCardById(UUID cardId) {
        return null;
    }

    public CardResponseDTO updateCard(UUID cardId, @Valid CardUpdateDTO update) {
        return null;
    }

    public void deleteCardById(UUID cardId) {
    }

    public List<CardResponseDTO> getAllCardsByCustomerId(UUID customerId) {
        return null;
    }
}
