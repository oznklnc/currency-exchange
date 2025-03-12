package com.ozan.currency.exchange.validation.validator;

import com.ozan.currency.exchange.base.UnitTest;
import com.ozan.currency.exchange.model.enums.Currency;
import com.ozan.currency.exchange.validation.EnumValidation;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;


class EnumValidatorTest extends UnitTest {

    @Mock
    private EnumValidation enumValidation;

    @Mock
    private ConstraintValidatorContext context;

    @InjectMocks
    private EnumValidator enumValidator;

    @BeforeEach
    void setUp() {
        Mockito.when(enumValidation.enumClazz()).thenReturn((Class)Currency.class);
    }

    @Test
    void shouldReturnTrueWhenStringIsTRY() {
        //Given
        String value = "TRY";
        enumValidator.initialize(enumValidation);

        //When
        boolean result = enumValidator.isValid(value, context);

        //Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldReturnFalseWhenStringIsNotInCurrency() {
        //Given
        String value = "TRYD";
        enumValidator.initialize(enumValidation);

        //When
        boolean result = enumValidator.isValid(value, context);

        //Then
        assertThat(result).isFalse();
    }

    @Test
    void shouldReturnFalseWhenValueIsNull() {
        //Given
        enumValidator.initialize(enumValidation);

        //When
        boolean result = enumValidator.isValid(null, context);

        //Then
        assertThat(result).isFalse();
    }

}