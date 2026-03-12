package com.example.card_management_system.service;

import com.example.card_management_system.dto.CardResponseDTO;
import com.example.card_management_system.dto.CardUpdateDTO;
import com.example.card_management_system.entity.Card;
import com.example.card_management_system.entity.CardUpdate;
import com.example.card_management_system.mapper.CardMapper;
import com.example.card_management_system.repository.CardRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private CardMapper cardMapper;

    @InjectMocks
    private CardService cardService;

    @Test
    void givenValidUuidString_whenGetCardByIdCalled_thenReturnsCardResponseDto() {
        String cardIdString = "561eed14-b0e4-45ec-9e6f-dc5b4238ee57";
        UUID uuid = UUID.fromString("561eed14-b0e4-45ec-9e6f-dc5b4238ee57");

        Card card = Card.builder().accountId(uuid).build();
        CardResponseDTO cardResponseDTO = CardResponseDTO.builder().accountId(cardIdString).build();

        when(cardRepository.findById(uuid)).thenReturn(Optional.of(card));

        Card result = cardService.getCardById(cardIdString);

        assertThat(result).isNotNull();
        assertThat(result.getAccountId().toString()).isEqualTo(cardIdString);

        verify(cardRepository, times(1)).findById(uuid);
    }

    @Test
    void givenValidUuidString_whenGetCardByIdCalledAndCardDoesNotExist_thenThrowsException() {
        String cardIdString = "561eed14-b0e4-45ec-9e6f-dc5b4238ee57";
        UUID uuid = UUID.fromString("561eed14-b0e4-45ec-9e6f-dc5b4238ee57");

        when(cardRepository.findById(uuid)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            cardService.getCardById(cardIdString);
        });

        assertThat(exception.getMessage()).isEqualTo("Card not found with ID: " + cardIdString);

        verify(cardRepository, times(1)).findById(uuid);
        verifyNoInteractions(cardMapper);
    }

    @Test
    void givenValidCardUpdateDto_whenUpdateCardCalled_thenReturnsUpdatedResponse() {
        UUID accountId = UUID.randomUUID();
        Card existingCard = Card.builder()
                                .accountId(accountId)
                                .cardNumber("1234123412341234")
                                .build();


        CardUpdate cardUpdate = CardUpdate.builder()
                                          .active(false)
                                          .creditLimit(new BigDecimal(20000))
                                          .build();

        when(cardRepository.findById(accountId)).thenReturn(Optional.of(existingCard));
        when(cardRepository.save(any(Card.class))).thenReturn(existingCard);

        cardService.updateCard(accountId.toString(), cardUpdate);

        verify(cardRepository).save(existingCard);
        verify(cardMapper).updateCardFromCardUpdate(cardUpdate, existingCard);

    }

    @Test
    void givenNonExistentId_whenUpdateCardCalled_thenThrowsException() {
        UUID nonExistentId = UUID.randomUUID();
        CardUpdateDTO cardUpdateDTO = CardUpdateDTO.builder().active("false").build();

        CardUpdate cardUpdate = CardUpdate.builder()
                                          .active(false)
                                          .build();

        when(cardRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cardService.updateCard(nonExistentId.toString(), cardUpdate);
        });

        assertThat(exception.getMessage()).contains("Failed to update card");

        // verify no other operations were done
        verifyNoInteractions(cardMapper);
        verify(cardRepository, never()).save(any());
    }

    @Test
    void givenValidAccountId_whenDeleteCardCalled_thenDeleteCard() {
        UUID accountId = UUID.randomUUID();
        Card existingCard = Card.builder().accountId(accountId).build();

        when(cardRepository.findById(accountId)).thenReturn(Optional.of(existingCard));

        cardService.deleteCard(accountId.toString());

        verify(cardRepository).delete(existingCard);
        verify(cardRepository).findById(accountId);
    }

    @Test
    void givenInvalidAccountId_whenDeleteCardCalled_thenThrowsException() {
        UUID accountId = UUID.randomUUID();
        Card existingCard = Card.builder().accountId(accountId).build();

        when(cardRepository.findById(accountId)).thenThrow(new EntityNotFoundException("Could not delete card"));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            cardService.deleteCard(accountId.toString());
        });


        assertTrue(exception.getMessage().contains("Could not delete card"));
        verify(cardRepository, never()).delete(existingCard);
    }

    @Test
    void givenAValidCustomerId_whenGetAllCardsByCustomerIdCalled_thenReturnCardResponseList() {
        UUID customerId = UUID.randomUUID();
        Card card = Card.builder().accountId(customerId).build();
        Card card2 = Card.builder().accountId(customerId).build();

        List<Card> cardList = List.of(card, card2);
        CardResponseDTO cardResponseDTO = CardResponseDTO.builder().customerId(customerId.toString()).build();


        when(cardRepository.findByCustomer_CustomerId(customerId)).thenReturn(cardList);
        when(cardMapper.cardToResponseDto(any(Card.class))).thenReturn(cardResponseDTO);

        List<CardResponseDTO> results = cardService.getAllCardsByCustomerId(customerId.toString());

        assertThat(results).isNotNull().hasSize(2).allMatch(dto -> dto.getCustomerId().equals(customerId.toString()));

        verify(cardRepository).findByCustomer_CustomerId(customerId);
        verify(cardMapper, times(2)).cardToResponseDto(any());
    }

    @Test
    void givenACustomerWithNoCards_whenGetAllCardsByCustomerIdCalled_thenThrowException() {
        UUID customerId = UUID.randomUUID();

        when(cardRepository.findByCustomer_CustomerId(customerId)).thenReturn(List.of());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cardService.getAllCardsByCustomerId(customerId.toString());
        });

        assertTrue(exception.getMessage().contains("No cards found for customer"));
        verify(cardRepository, times(1)).findByCustomer_CustomerId(customerId);
        verifyNoInteractions(cardMapper);
    }
}