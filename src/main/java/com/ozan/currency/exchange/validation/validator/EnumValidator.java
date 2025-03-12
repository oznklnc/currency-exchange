package com.ozan.currency.exchange.validation.validator;

import com.ozan.currency.exchange.validation.EnumValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.Optional;

public class EnumValidator implements ConstraintValidator<EnumValidation, String> {

    private Class<? extends Enum<?>> enumClass;


    @Override
    public void initialize(EnumValidation constraintAnnotation) {
        this.enumClass = constraintAnnotation.enumClazz();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return Optional.ofNullable(s)
                .map(this::isAnyMatch)
                .orElse(false);
    }

    private boolean isAnyMatch(String value) {
        return Arrays.stream(enumClass.getEnumConstants())
                .map(Enum::name)
                .anyMatch(enumValue -> enumValue.equals(value));
    }
}
