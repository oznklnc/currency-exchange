package com.ozan.currency.exchange.validation;

import com.ozan.currency.exchange.validation.validator.ConversionHistoryFilterValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ConversionHistoryFilterValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConversionHistoryFilterValidation {

    String message() default "request.validation.conversion.history.filter";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
