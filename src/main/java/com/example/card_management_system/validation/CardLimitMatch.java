package com.example.card_management_system.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CardLimitMatchValidator.class)
public @interface CardLimitMatch {

    String message() default "Credit limit is only allowed for CREDIT cards";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
