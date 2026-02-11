package com.example.card_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardUpdateDTO {
    private String active;
    private String creditLimit; //BigDecimal??
    private String expiryDate;

}
