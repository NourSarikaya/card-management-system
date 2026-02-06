package com.example.card_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardResponseDTO {

    private String id; //UUID??
    private String cardNumber;
    private String cardType;
    private String expiryDate;
    private String cardHolderName;
    private boolean active;
    private String creditLimit; //BigDecimal??
    private String securityCode;
}
