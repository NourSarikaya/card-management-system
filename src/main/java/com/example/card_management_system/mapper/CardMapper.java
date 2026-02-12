package com.example.card_management_system.mapper;

import com.example.card_management_system.dto.CardResponseDTO;
import com.example.card_management_system.dto.CardUpdateDTO;
import com.example.card_management_system.dto.CreateCardRequestDTO;
import com.example.card_management_system.entity.Card;
import org.mapstruct.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Mapper(componentModel = "spring")
public interface CardMapper {

    @Mappings({
            @Mapping(source = "customer.customerId", target = "customerId"),
            @Mapping(target = "expiryDate", dateFormat = "yyyyMM")

    })
    CardResponseDTO cardToResponseDto(Card card);

    @Mappings({
            @Mapping(target = "expiryDate", expression = "java(mapStringToLastDay(dto.getExpiryDate()))"),
            @Mapping(target = "customer.customerId", source = "customerId")
    })
    Card requestDtoToCard(CreateCardRequestDTO dto);

    void updateCardFromDto(CardUpdateDTO dto, @MappingTarget Card entity);

    default LocalDate mapStringToLastDay(String expiryDate) {
        if (expiryDate == null || expiryDate.isBlank()) {
            return null;
        }
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
            return YearMonth.parse(expiryDate, formatter).atEndOfMonth();
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Invalid expiry date format. Expected yyyyMM: " + expiryDate);
        }
    }
}