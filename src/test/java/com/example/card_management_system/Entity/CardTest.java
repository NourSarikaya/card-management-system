package com.example.card_management_system.Entity;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {


    @Test
    void setCreditLimit() {

        Card card = new Card();
        card.setCreditLimit(20.0);
        assertEquals(20.0,card.getCreditLimit());

    }

    @Test
    void setIsActive() {

        Card card = new Card();
        card.setActive(true);
        assertTrue(card.isActive());

    }





}