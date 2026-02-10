package com.example.card_management_system.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CardUpdateDTO {
    private String active;
    private String creditLimit; //BigDecimal??
    private String expiryDate;

}
