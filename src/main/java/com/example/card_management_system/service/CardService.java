package com.example.card_management_system.service;

import com.example.card_management_system.repository.CardRepository;
import com.example.card_management_system.entity.Card;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;

    public Card createCard(Card card){
        return cardRepository.save(card);
    }
    //TO DO: instead of passing card entity pass DTO as argument


    public List<Card> getCardsByCustomerId(Card card){
        return cardRepository.findByCustomer_CustomerId(card.getCustomer().getCustomerId());
    }


}
