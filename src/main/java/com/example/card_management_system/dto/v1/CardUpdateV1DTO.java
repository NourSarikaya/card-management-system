package com.example.card_management_system.dto.v1;

import com.example.card_management_system.validation.FutureExpiry;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardUpdateV1DTO {
    private String active;
    private String creditLimit;
    @Size(min=6,max=6, message= "Expiry date must be of length 6 in YYYYMM format")
    @FutureExpiry
    private String expiryDate;
    private String dailyLimit;
}
