package com.example.card_management_system.mapper;

import com.example.card_management_system.dto.CardResponseDTO;
import com.example.card_management_system.dto.CardUpdateDTO;
import com.example.card_management_system.dto.CreateCardRequestDTO;
import com.example.card_management_system.dto.v1.CardResponseV1DTO;
import com.example.card_management_system.dto.v1.CardUpdateV1DTO;
import com.example.card_management_system.dto.v1.CreateCardRequestV1DTO;
import com.example.card_management_system.entity.Card;
import com.example.card_management_system.entity.CardUpdate;
import org.mapstruct.*;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Mapper(componentModel = "spring")
public interface CardMapper {

    @Mappings({
//            @Mapping(source = "customer.customerId", target = "customerId"),
            @Mapping(target = "expiryDate", dateFormat = "yyyyMM")

    })
    CardResponseDTO cardToResponseDto(Card card);


    @Mappings({
            @Mapping(target = "expiryDate", expression = "java(mapStringToLastDay(dto.getExpiryDate()))"),
//            @Mapping(target = "customer.customerId", source = "customerId")
    })
    Card requestDtoToCard(CreateCardRequestDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CardUpdate cardUpdateDTOtoCardUpdate(CardUpdateDTO cardUpdateDTO);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCardFromDto(CardUpdateDTO dto, @MappingTarget Card entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCardFromCardUpdate(CardUpdate cardUpdate, @MappingTarget Card entity);


    //V1 DTOs
    @Mappings({
//            @Mapping(source = "customer.customerId", target = "customerId"),
            @Mapping(target = "expiryDate", dateFormat = "yyyyMM")

    })
    CardResponseV1DTO cardToResponseV1Dto(Card card);

    @Mappings({
            @Mapping(target = "expiryDate", expression = "java(mapStringToLastDay(dto.getExpiryDate()))"),
//            @Mapping(target = "customer.customerId", source = "customerId")
    })
    Card requestV1DtoToCard(CreateCardRequestV1DTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    CardUpdate cardUpdateV1DTOtoCardUpdate(CardUpdateV1DTO cardUpdateV1DTO);

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