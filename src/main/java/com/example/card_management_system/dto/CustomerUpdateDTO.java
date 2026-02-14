package com.example.card_management_system.dto;

import com.example.card_management_system.util.inputValidationUtil.State;
import com.example.card_management_system.util.inputValidationUtil.ValueOfEnum;
import jakarta.validation.constraints.Email;
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
public class CustomerUpdateDTO {

    @Email(message ="Please type in a valid email address of format example@email.com")
    private String emailAddress;
    @Pattern(regexp = "\\d{6,15}", message="Phone Number cannot include letters or symbols")
    private String phoneNumber;
    private String addressLine1;
    private String addressLine2;
    private String cityName;
    @Size(min = 2, max=2, message = "State abbreviation must be of length 2")
    @ValueOfEnum(enumClass = State.class, message= "Must be a valid state")
    private String state;
    @Pattern(regexp = "\\d{5}", message = "zipcode must be 5 digits")
    private String zipCode;

}
