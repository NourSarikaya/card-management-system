package com.example.card_management_system.util;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class UUIDUtilsTest {

    @Test
    void givenValidStringUuid_whenToUuidCalled_thenReturnUuidObject() {
        String validUuidString = "561eed14-b0e4-45ec-9e6f-dc5b4238ee57";

        UUID result = UUIDUtils.toUUID(validUuidString);

        assertThat(result).isNotNull();
        assertThat(result.toString()).isEqualTo(validUuidString);
    }

    @Test
    void givenInvalidUuid_whenToUuidCalled_thenThrowsInvalidInputException() {
        String invalidUuid = "not-a-uuid";

        assertThrows(Exception.class, () -> {
            UUIDUtils.toUUID(invalidUuid);
        });
    }

    @Test
    void givenNullOrBlankString_whenToUuidCalled_thenReturnsNull() {
        assertThat(UUIDUtils.toUUID(null)).isNull();
        assertThat(UUIDUtils.toUUID(" ")).isNull();
    }
}
