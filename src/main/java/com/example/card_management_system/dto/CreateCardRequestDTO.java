package com.example.card_management_system.dto;

import com.example.card_management_system.entity.Card;
import com.example.card_management_system.util.inputValidationUtil.ValueOfEnum;
import com.example.card_management_system.validation.CardLimitMatch;
import com.example.card_management_system.validation.FutureExpiry;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.CreditCardNumber;
import org.hibernate.validator.constraints.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@CardLimitMatch
public class CreateCardRequestDTO {
    @NotBlank(message = "Customer ID is required")
    private String customerId;

    @NotBlank(message = "Card Number is required")
    @CreditCardNumber(message = "Must be a valid Luhn card number")
    @Pattern(regexp = "\\d{13,16}", message = "Card Number cannot be more than 16 digits")
    private String cardNumber;

    @NotBlank(message = "Card Type is required")
    @ValueOfEnum(enumClass = Card.CardType.class, message = "Card type should be one of DEBIT, CREDIT, LOYALTY, PREPAID")
    private String cardType;

    @NotBlank(message = "Expiry Date is required")
    @Size(min = 6, max = 6, message = "Expiry date must be of length 6 in YYYYMM format")
    @JsonFormat(pattern = "yyyyMM")
    @FutureExpiry
    private String expiryDate;//YYYYMM

    @NotBlank(message = "Card Holder Name is required")
    private String cardHolderName;

    private String creditLimit;
}
