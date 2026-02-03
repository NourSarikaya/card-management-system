package com.example.card_management_system.Entity;


import org.junit.jupiter.api.Test;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CardTest {


    @Test
    void setCreditLimit() {

        Card c = new Card(UUID.randomUUID());
        c.setCreditLimit(20.0);
        assertEquals(20.0,c.getCreditLimit());

    }

    @Test
    void getId() {
    }

    @Test
    void getCreditLimit() {
    }



}