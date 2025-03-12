package com.ozan.currency.exchange.validation.validator;

import com.ozan.currency.exchange.model.request.ExchangeConversionHistoryFilterRequest;
import com.ozan.currency.exchange.validation.ConversionHistoryFilterValidation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.Optional;

public class ConversionHistoryFilterValidator
        implements ConstraintValidator<ConversionHistoryFilterValidation, ExchangeConversionHistoryFilterRequest> {


    @Override
    public boolean isValid(ExchangeConversionHistoryFilterRequest request, ConstraintValidatorContext constraintValidatorContext) {
        return isValidTransactionId(request.getTransactionId(), constraintValidatorContext) || isValidTransactionDatePresent(request.getTransactionDate(), constraintValidatorContext);
    }

    private boolean isValidTransactionId(String transactionId, ConstraintValidatorContext constraintValidatorContext) {
        return Optional.ofNullable(transactionId)
                .map(StringUtils::hasText)
                .orElseGet(() -> buildConstraintViolation("transactionId", constraintValidatorContext));
    }

    private boolean isValidTransactionDatePresent(LocalDate transactionDate, ConstraintValidatorContext constraintValidatorContext) {
        return Optional.ofNullable(transactionDate)
                .map(t -> true)
                .orElseGet(() -> buildConstraintViolation("transactionDate", constraintValidatorContext));
    }

    private boolean buildConstraintViolation(String fieldName, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate("request.validation.conversion.history.filter")
                .addPropertyNode(fieldName)
                .addConstraintViolation();
        return false;

    }
}
