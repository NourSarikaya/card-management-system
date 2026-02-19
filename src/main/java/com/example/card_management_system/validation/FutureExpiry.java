package com.example.card_management_system.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = FutureExpiryValidator.class)
public @interface FutureExpiry {

    String message() default "Expiry date must be in the future";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default{};
}


