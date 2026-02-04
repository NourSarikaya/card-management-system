package com.example.card_management_system.Entity;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {


    @Test
    void setCreditLimit() {

        Card c = new Card();
        c.setCreditLimit(20.0);
        assertEquals(20.0,c.getCreditLimit());

    }

    @Test
    void setIsActive() {

        Card c = new Card();
        c.setActive(true);
        assertTrue(c.isActive());

    }





}