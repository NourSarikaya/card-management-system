package com.example.card_management_system.dto;

import com.example.card_management_system.entity.Card.CardType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateCardRequest(

                /*
        1. Id (UUID) – Unique Identifier for each card
        2. CardNumber(String)– 16-digit credit/debit card number
        3. cardType(Enum): type of card (e.g. Credit, Debit)
        4. expiryDate(LocalDate): Expiration Date of card in YYYYMM format
        5. cardHolderName(String): Name of the card holder
        6. isActive(Boolean): Status of the card (active/inactive)
        7. CreditLimit(BigDecimal) – Credit Limit on the credit card
        8. SecurityCode (CVV)(String) - 3-digit code on the back of the card
        9. customer(Customer)-Representing the customer this card belongs to
        */
//    String maskedCardNumber,
//    CardType cardType,
//    LocalDate expiryDate,
//    String cardHolderName,
//    boolean active,
//    BigDecimal creditLimit,
//    String securityCode,
//    Customer





){}
