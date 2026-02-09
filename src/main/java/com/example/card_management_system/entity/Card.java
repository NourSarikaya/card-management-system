package com.example.card_management_system.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.validator.constraints.CreditCardNumber;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;




@Entity
@Table(name="card")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access= AccessLevel.PROTECTED, force = true)
@Builder
public class Card {
    /*
        1. Id (UUID) – Unique Identifier for each card
        2. CardNumber(String)– 16-digit credit/debit card number
        3. cardType(Enum): type of card (e.g. Credit, Debit)
        4. expiryDate(LocalDate): Expiration Date of card in YYYYMM format
        5. cardHolderName(String): Name of the card holder
        6. isActive(Boolean): Status of the card (active/inactive)
        7. CreditLimit(BigDecimal) – Credit Limit on the credit card
        8. SecurityCode (CVV)(String) - 3-digit code on the back of the card
    */

    @Id
    @GeneratedValue
    @Column (name="id", nullable = false, unique = true)
    private UUID id;

    @NotBlank
    @CreditCardNumber
    @Column(name = "card_number", nullable = false, unique = true, length = 16)
    private String cardNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name ="card_type", nullable = false)
    private CardType cardtype;

    @NotNull
    @Column(name = "expiry_date", nullable = false, length = 6)
    private LocalDate expiryDate; //YYYYMM

    @NotBlank
    @Column(name = "card_holder_name", nullable = false)
    private String cardHolderName;

    private boolean active;

    @NotNull
    @PositiveOrZero
    @Column(name = "credit_limit", nullable = false, precision = 15, scale = 2)
    private BigDecimal creditLimit;

    @NotBlank(message = "CVV must be 3 digits")
    @Column(name = "security_code", nullable = false, length = 3)
    private String securityCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public Card(String cardNumber, CardType cardtype, LocalDate expiryDate, String cardHolderName, boolean active, BigDecimal creditLimit, String securityCode, Customer customer) {
        this.cardNumber = cardNumber;
        this.cardtype = cardtype;
        this.expiryDate = expiryDate;
        this.cardHolderName = cardHolderName;
        this.active = active;
        this.creditLimit = creditLimit;
        this.securityCode = securityCode;
        this.customer = customer;
    }


    //CARD TYPE ENUM
    public enum CardType{
        CREDIT,
        DEBIT,
        LOYALTY,
        PREPAID
    }
}
