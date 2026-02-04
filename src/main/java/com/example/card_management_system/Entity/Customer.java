package com.example.card_management_system.Entity;

import jakarta.persistence.*;
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

    @Column(name = "full_name", nullable = false)
    private String name;

    @Column(name = "email_address", nullable = false, unique = true)
    private String emailAddress;

    @Column(name = "phone_number", nullable = false, unique = true,  length = 15)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "phone_type")
    private PhoneType phoneType;

    @Column(name = "address_line_1", nullable = false)
    private String addressLine1;

    @Column(name = "address_line_2")
    private String addressLine2;

    @Column(name = "city_name", nullable = false)
    private String cityName;

    @Column(name = "state_name", nullable = false)
    private String stateName;

    @Column(name = "zip_code", nullable = false, length = 5)
    private String zipcode;


    //CARD TYPE ENUM
    public enum PhoneType{
        MOBILE,
        HOME,
        WORK
    }



}
