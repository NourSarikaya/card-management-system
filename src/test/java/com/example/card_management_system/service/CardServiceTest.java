package com.example.card_management_system.service;

import com.example.card_management_system.dto.CardResponseDTO;
import com.example.card_management_system.dto.CardUpdateDTO;
import com.example.card_management_system.dto.CreateCardRequestDTO;
import com.example.card_management_system.entity.Card;
import com.example.card_management_system.mapper.CardMapper;
import com.example.card_management_system.repository.CardRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
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
        when(cardMapper.cardToResponseDto(card)).thenReturn(cardResponseDTO);

        CardResponseDTO result = cardService.getCardById(cardIdString);

        assertThat(result).isNotNull();
        assertThat(result.getAccountId()).isEqualTo(cardIdString);

        verify(cardRepository, times(1)).findById(uuid);
        verify(cardMapper, times(1)).cardToResponseDto(card);
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
    void givenValidCardRequestDto_whenCreateNewCard_thenReturnCardResponseDto() {
        CreateCardRequestDTO request = CreateCardRequestDTO.builder().cardNumber("1234123412341234").build();

        Card mappedCard = new Card();
        Card savedCard = new Card();
        CardResponseDTO expectedResponse = new CardResponseDTO();

        when(cardMapper.requestDtoToCard(any())).thenReturn(mappedCard);
        when(cardRepository.save(any())).thenReturn(savedCard);
        when(cardMapper.cardToResponseDto(any())).thenReturn(expectedResponse);

        CardResponseDTO result = cardService.createNewCard(request);

        assertThat(result).isNotNull();

        ArgumentCaptor<Card> cardCaptor = ArgumentCaptor.forClass(Card.class);
        verify(cardRepository).save(cardCaptor.capture());

        Card capturedCard = cardCaptor.getValue();

        assertThat(capturedCard.getAccountId()).isNotNull();
        assertThat(capturedCard.getSecurityCode()).hasSize(3);
        assertThat(capturedCard.isActive()).isTrue();
    }

    @Test
    void givenInvalidCardRequestDto_whenCreateNewCardCalled_thenThrowsException() {
        String invalidDate = "2026-02";
        String mapperErrorMessage = "Invalid expiry date format. Expected yyyyMM: " + invalidDate;

        CreateCardRequestDTO request = CreateCardRequestDTO.builder().expiryDate(invalidDate).build();

        when(cardMapper.requestDtoToCard(any())).thenThrow(new RuntimeException(mapperErrorMessage));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cardService.createNewCard(request);
        });

        assertThat(exception.getMessage()).contains("Failed to process card request");
        assertThat(exception.getMessage()).contains(mapperErrorMessage);

        verify(cardRepository, never()).save(any());
        verifyNoInteractions(cardRepository);

    }

    @Test
    void givenValidCardUpdateDto_whenUpdateCardCalled_thenReturnsUpdatedResponse() {
        UUID accountId = UUID.randomUUID();
        Card existingCard = Card.builder()
                                .accountId(accountId)
                                .cardNumber("1234123412341234")
                                .build();

        CardUpdateDTO cardUpdateDTO = CardUpdateDTO.builder()
                                                   .active("false")
                                                   .creditLimit(BigDecimal.valueOf(2000.00).toString())
                                                   .build();

        when(cardRepository.findById(accountId)).thenReturn(Optional.of(existingCard));

        when(cardRepository.save(any(Card.class))).thenReturn(existingCard);
        when(cardMapper.cardToResponseDto(any(Card.class))).thenReturn(new CardResponseDTO());

        cardService.updateCard(String.valueOf(accountId), cardUpdateDTO);

        verify(cardMapper).updateCardFromDto(cardUpdateDTO, existingCard);

        verify(cardRepository).save(existingCard);

    }

    @Test
    void givenNonExistentId_whenUpdateCardCalled_thenThrowsException() {
        UUID nonExistentId = UUID.randomUUID();
        CardUpdateDTO cardUpdateDTO = CardUpdateDTO.builder().active("false").build();

        when(cardRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            cardService.updateCard(String.valueOf(nonExistentId), cardUpdateDTO);
        });

        assertThat(exception.getMessage()).contains("Failed to update card");
        assertThat(exception.getMessage()).contains("Card not found with ID");

        // verify no other operations were done
        verifyNoInteractions(cardMapper);
        verify(cardRepository, never()).save(any());
    }
}
