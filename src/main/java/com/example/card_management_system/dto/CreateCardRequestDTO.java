package com.example.card_management_system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateCardRequestDTO {
    @NotBlank(message = "customerId is required")
    private String customerId;
    @NotBlank(message = "cardNumber is required")
    @Pattern(regexp = "\\d{16}", message = "cardNumber must be 16 digits")
    private String cardNumber;
    @NotBlank(message = "cardType is required")
    private String cardType;
    @NotBlank(message = "expiryDate is required")
    @Size(min=6,max=6)
    private String expiryDate;//YYYYMM
    @NotBlank(message = "cardHolderName is required")
    private String cardHolderName;
    private String creditLimit;
}
