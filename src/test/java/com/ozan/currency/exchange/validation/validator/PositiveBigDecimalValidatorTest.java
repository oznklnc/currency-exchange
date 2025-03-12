package com.ozan.currency.exchange.validation.validator;

import com.ozan.currency.exchange.base.UnitTest;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class PositiveBigDecimalValidatorTest extends UnitTest {

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @InjectMocks
    private PositiveBigDecimalValidator positiveBigDecimalValidator;

    @Test
    void shouldReturnTrueWhenBigDecimalIsTrue() {
        //given
        var bigDecimal = BigDecimal.TEN;

        //when
        boolean result = positiveBigDecimalValidator.isValid(bigDecimal, constraintValidatorContext);

        //then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnFalseWhenBigDecimalNullValue() {
        //when
        boolean result = positiveBigDecimalValidator.isValid(null, constraintValidatorContext);

        //then
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnFalseWhenBigDecimalNegativeValue() {
        //given
        BigDecimal bigDecimal = BigDecimal.valueOf(-1);

        //when
        boolean result = positiveBigDecimalValidator.isValid(bigDecimal, constraintValidatorContext);

        //then
        assertThat(result).isFalse();
    }
}