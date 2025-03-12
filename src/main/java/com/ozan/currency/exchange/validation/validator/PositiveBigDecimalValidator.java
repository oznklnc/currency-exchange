package com.ozan.currency.exchange.validation.validator;

import com.ozan.currency.exchange.validation.PositiveBigDecimal;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.math.BigDecimal;
import java.util.Optional;

public class PositiveBigDecimalValidator implements ConstraintValidator<PositiveBigDecimal, BigDecimal> {

    @Override
    public boolean isValid(BigDecimal bigDecimal, ConstraintValidatorContext constraintValidatorContext) {
        return Optional.ofNullable(bigDecimal)
                .map(value -> value.compareTo(BigDecimal.ZERO) > 0)
                .orElse(false);
    }
}
