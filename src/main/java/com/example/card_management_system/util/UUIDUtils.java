package com.example.card_management_system.util;

import java.util.UUID;

public final class UUIDUtils {

    public static UUID toUUID(String stringId) {
        if (stringId == null || stringId.isBlank()) {
            return null;
        }
        try {
            return UUID.fromString(stringId);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid UUID format: " + stringId);
        }
    }
}
