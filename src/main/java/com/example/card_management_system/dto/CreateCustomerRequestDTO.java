package com.example.card_management_system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateCustomerRequestDTO {
    @NotBlank(message = "firstName is required")
    private String firstName;
    @Size(max = 1)
    private String middleInitial;
    @NotBlank(message = "lastName is required")
    private String lastName;
    @NotBlank(message = "emailAddress is required")
    private String emailAddress;
    @NotBlank(message = "phoneNumber is required")
    private String phoneNumber;
    @NotBlank(message = "phoneType is required")
    private String phoneType;
    @NotBlank(message = "addressLine1 is required")
    private String addressLine1;
    private String addressLine2;
    @NotBlank(message = "cityName is required")
    private String cityName;
    @NotBlank(message = "state is required")
    @Size(min = 2, max=2)
    private String state;
    @NotBlank(message = "zipCode is required")
    @Size(min = 5, max=5)
    private String zipCode;
}
