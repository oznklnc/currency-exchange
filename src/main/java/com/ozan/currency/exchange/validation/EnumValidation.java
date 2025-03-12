package com.ozan.currency.exchange.validation;

import com.ozan.currency.exchange.validation.validator.EnumValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumValidator.class)
@Target({ElementType.FIELD, ElementType.TYPE_USE, ElementType.METHOD})
@ReportAsSingleViolation
public @interface EnumValidation {

    Class<? extends Enum<?>> enumClazz();

    String message() default "request.validation.invalid.enum";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
