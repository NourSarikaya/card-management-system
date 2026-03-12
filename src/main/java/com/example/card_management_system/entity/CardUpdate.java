package com.example.card_management_system.entity;

import com.example.card_management_system.validation.FutureExpiry;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CardUpdate {
    private Boolean active;
    private BigDecimal creditLimit;
    @FutureExpiry
    private LocalDate expiryDate;

    private BigDecimal dailyLimit;
}
