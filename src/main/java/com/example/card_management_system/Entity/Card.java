package com.example.card_management_system.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;




@Entity
@Table(name="card")
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED, force = true)
public class Card {
    /*
        1. Id (UUID) – Unique Identifier for each card
        2. CardNumber(String)– 16-digit credit/debit card number
        3. cardType(Enum): type of card (e.g. Credit, Debit)
        4. expiryDate(String): Expiration Date of card in YYYYMM format
        5. cardHolderName(String): Name of the card holder
        6. isActive(Boolean): Status of the card (active/inactive)
        7. CreditLimit(double) – Credit Limit on the credit card
        8. SecurityCode (CVV)(String) - 3-digit code on the back of the card
    */




    @Column (name="id")
    @Id @GeneratedValue
    private UUID id;


    private String cardNumber;

    @Enumerated(EnumType.STRING)
    private CardType cardtype;


    private String expiryDate; //YYYYMM


    private String cardHolderName;

    @Setter
    private boolean isActive;

    @Setter
    private double creditLimit;

    private String securityCode;

    //@ManyToOne
    //private Customer customer;


    //CARD TYPE ENUM
    public enum CardType{
        CREDIT,
        DEBIT,
        LOYALTY,
        PREPAID
    }
}
