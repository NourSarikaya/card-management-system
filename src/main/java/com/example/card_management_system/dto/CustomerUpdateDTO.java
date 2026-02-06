package com.example.card_management_system.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerUpdateDTO {

    private String emailAddress;
    private String phoneNumber;
    private String addressLine1;
    private String addressLine2;
    private String cityName;
    @Size(min = 2, max=2)
    private String state;
    @Size(min = 6, max=6)
    private String zipCode;

}
