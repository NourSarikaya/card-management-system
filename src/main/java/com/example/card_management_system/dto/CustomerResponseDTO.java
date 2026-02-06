package com.example.card_management_system.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponseDTO {

    private String customerID; //UUID??
    private String firstName;
    private String middleInitial;
    private String lastName;
    private String emailAddress;
    private String phoneNumber;
    private String phoneType;
    private String addressLine1;
    private String addressLine2;
    private String cityName;
    private String state;
    private String zipCode;

}
