package com.example.card_management_system.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;




@Entity
@Table(name="card")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access= AccessLevel.PROTECTED, force = true)
public class Card {
    /*
        1. Id (UUID) – Unique Identifier for each card
        2. CardNumber(String)– 16-digit credit/debit card number
        3. cardType(Enum): type of card (e.g. Credit, Debit)
        4. expiryDate(String): Expiration Date of card in YYYYMM format
        5. cardHolderName(String): Name of the card holder
        6. isActive(Boolean): Status of the card (active/inactive)
        7. CreditLimit(BigDecimal) – Credit Limit on the credit card
        8. SecurityCode (CVV)(String) - 3-digit code on the back of the card
    */


    @Id
    @GeneratedValue
    @Column (name="id")
    private UUID id;

    @Column(name = "card_number", nullable = false, unique = true, length = 16)
    private String cardNumber;

    @Enumerated(EnumType.STRING)
    @Column(name ="card", nullable = false)
    private CardType cardtype;

    @Column(name = "expiry_date", nullable = false, length = 6)
    private String expiryDate; //YYYYMM

    @Column(name = "card_holder_name", nullable = false)
    private String cardHolderName;

    private boolean isActive;

    @Column(name = "credit_limit", nullable = false, precision = 15, scale = 2)
    private BigDecimal creditLimit;

    @Column(name = "security_code", nullable = false, length = 3)
    private String securityCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;


    //CARD TYPE ENUM
    public enum CardType{
        CREDIT,
        DEBIT,
        LOYALTY,
        PREPAID
    }
}
