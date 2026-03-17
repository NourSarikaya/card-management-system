package com.example.card_management_system.util.inputValidationUtil;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class StateTest {

    @Test
    void stateEnum_ShouldContainAllValues() {
        State[] states = State.values();

        assertNotNull(states);
        assertThat(states).hasSize(50);
    }

}