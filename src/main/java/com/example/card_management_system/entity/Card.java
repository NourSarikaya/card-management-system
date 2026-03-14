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
@Table(name = "card")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
@Builder
public class Card {

    @Id
    @Column(name = "account_id", nullable = false, unique = true)
    private UUID accountId;

    @Column(name = "account_number", nullable = false, unique = true)
    private UUID accountNumber;

    @NotBlank
    @CreditCardNumber
    @Column(name = "card_number", nullable = false, unique = true, length = 16)
    private String cardNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "card_type", nullable = false)
    private CardType cardType;

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

    @Column(name = "digital_card_only")
    private Boolean digitalCardOnly;

    @Column(name = "daily_limit")
    private BigDecimal dailyLimit;

    @Column(name = "customer_id", nullable = false)
    private UUID customerId;


    //CARD TYPE ENUM
    public enum CardType {
        CREDIT,
        DEBIT,
        LOYALTY,
        PREPAID
    }
}
