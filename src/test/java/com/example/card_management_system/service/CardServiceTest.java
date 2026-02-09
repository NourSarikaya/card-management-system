package com.example.card_management_system.service;

import com.example.card_management_system.dto.CardResponseDTO;
import com.example.card_management_system.entity.Card;
import com.example.card_management_system.mapper.CardMapper;
import com.example.card_management_system.repository.CardRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

        Card card = Card.builder().id(uuid).build();
        CardResponseDTO cardResponseDTO = CardResponseDTO.builder().id(cardIdString).build();

        when(cardRepository.findById(uuid)).thenReturn(Optional.of(card));
        when(cardMapper.cardToResponseDto(card)).thenReturn(cardResponseDTO);

        CardResponseDTO result = cardService.getCardById(cardIdString);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(cardIdString);

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
}
