package com.ozan.currency.exchange.validation;

import com.ozan.currency.exchange.validation.validator.PositiveBigDecimalValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PositiveBigDecimalValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface PositiveBigDecimal {

    String message() default "request.validation.positive.decimal";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
