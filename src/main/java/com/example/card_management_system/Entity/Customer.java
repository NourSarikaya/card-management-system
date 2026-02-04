package com.example.card_management_system.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name="customer")
@Getter
@Setter
public class Customer {
    /*
        1.	customerID(UUID): Unique Identifier for each card
        2.	Name(String) – Name of the card holder
        3.	emailAddress(String): email address for Customer
        4.	phoneNumber(String): Phone number of the customer
        5.	phoneType(Enum): Type of phone (mobile, home, work)
        6.	addressLine1(String): US postal address line1
        7.	AddressLine2 (String): address line 2
        8.	cityName(String)– City Name
        9.	state(String) – State name
        10.	zipCode(String) – 5-digit zip code

    */

    @Column(name="customerId")
    @Id
    @GeneratedValue
    private UUID customerId;

    @NotBlank
    @Size(min = 2, max = 50)
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotBlank
    @Size(min = 2, max = 50)
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotBlank
    @Size(max = 1)
    @Column(name = "middle_initial")
    private String middleInitial;
    @NotBlank
    @Email
    @Column(name = "email_address", nullable = false, unique = true)
    private String emailAddress;

    @NotBlank
    @Column(name = "phone_number", nullable = false, unique = true,  length = 15)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "phone_type")
    private PhoneType phoneType;

    @NotBlank
    @Column(name = "address_line_1", nullable = false)
    private String addressLine1;

    @Column(name = "address_line_2")
    private String addressLine2;

    @NotBlank
    @Column(name = "city_name", nullable = false)
    private String cityName;

    @NotBlank
    @Size(min = 2, max = 2)
    @Column(name = "state", nullable = false, length = 2)
    private String state;

    @NotBlank
    @Column(name = "zip_code", nullable = false, length = 5)
    private String zipcode;


    //CARD TYPE ENUM
    public enum PhoneType{
        MOBILE,
        HOME,
        WORK
    }



}
