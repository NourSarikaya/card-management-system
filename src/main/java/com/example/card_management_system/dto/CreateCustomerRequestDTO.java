package com.example.card_management_system.dto;

import com.example.card_management_system.entity.Customer;
import com.example.card_management_system.util.inputValidationUtil.State;
import com.example.card_management_system.util.inputValidationUtil.ValueOfEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
    @Size(min = 2, max = 50)
    private String firstName;
    @Size(max = 1, message= "Middle Initial if present has to be of length 1")
    private String middleInitial;
    @NotBlank(message = "lastName is required")
    @Size(min = 2, max = 50)
    private String lastName;
    @NotBlank(message = "EmailAddress is required")
    @Email(message ="Please type in a valid email address of format example@email.com")
    private String emailAddress;
    @NotBlank(message = "Phone Number is required")
    @Size(max=15, message="Phone Number cannot be more than 15 digits")
    @Pattern(regexp = "\\d{6,15}", message="Phone Number cannot include letters or symbols")
    private String phoneNumber;
    @NotBlank(message = "Phone Type is required")
    @ValueOfEnum(enumClass = Customer.PhoneType.class, message="Phone type must be one of MOBILE, HOME, WORK")
    private String phoneType;
    @NotBlank(message = "addressLine1 is required")
    private String addressLine1;
    private String addressLine2;
    @NotBlank(message = "cityName is required")
    private String cityName;
    @NotBlank(message = "state is required")
    @Size(min = 2, max=2, message = "State abbreviation must be of length 2")
    @ValueOfEnum(enumClass = State.class, message= "Must be a valid state")
    private String state;
    @NotBlank(message = "zipcode is required")
    @Pattern(regexp = "\\d{5}", message = "zipcode must be 5 digits")
    private String zipCode;
}
