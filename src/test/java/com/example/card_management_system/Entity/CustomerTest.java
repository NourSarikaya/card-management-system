package com.example.card_management_system.Entity;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;



class CustomerTest {

    /*
    private Customer customer;

    @BeforeAll
    static void setUp(){
        Customer customer = new Customer();
    }
    */

    @Test
    void getPhoneNumber() {
        Customer customer = new Customer();
        customer.setPhoneNumber("7730001111");
        assertEquals("7730001111",customer.getPhoneNumber());
    }

    @Test
    void getPhoneType() {
        Customer customer = new Customer();
        customer.setPhoneType(Customer.PhoneType.MOBILE);
        assertEquals(Customer.PhoneType.MOBILE,customer.getPhoneType());
    }
}