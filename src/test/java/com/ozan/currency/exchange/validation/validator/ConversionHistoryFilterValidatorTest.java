package com.ozan.currency.exchange.validation.validator;


import com.ozan.currency.exchange.base.UnitTest;
import com.ozan.currency.exchange.model.request.ExchangeConversionHistoryFilterRequest;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class ConversionHistoryFilterValidatorTest extends UnitTest {

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder constraintViolationBuilder;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext nodeBuilderCustomizableContext;

    @InjectMocks
    private ConversionHistoryFilterValidator validator;

    @Test
    void shouldReturnTrueWhenTransactionIdExist() {
        //Given
        ExchangeConversionHistoryFilterRequest request = ExchangeConversionHistoryFilterRequest.builder()
                .transactionId("123")
                .build();

        //When
        boolean result = validator.isValid(request, context);

        //Then
        assertThat(result).isTrue();
        verifyNoInteractions(context);
    }

    @Test
    void shouldReturnTrueWhenTransactionIdAndTransactionDateExist() {
        //Given
        ExchangeConversionHistoryFilterRequest request = ExchangeConversionHistoryFilterRequest.builder()
                .transactionId("123")
                .transactionDate(LocalDate.now())
                .build();

        //When
        boolean result = validator.isValid(request, context);

        //Then
        assertThat(result).isTrue();
        verifyNoInteractions(context);
    }

    @Test
    void shouldReturnTrueWhenTransactionDateExist() {
        //Given
        mockContext();
        ExchangeConversionHistoryFilterRequest request = ExchangeConversionHistoryFilterRequest.builder()
                .transactionDate(LocalDate.now())
                .build();

        //When
        boolean result = validator.isValid(request, context);

        //Then
        assertThat(result).isTrue();
        InOrder inOrder = inOrder(context, constraintViolationBuilder);
        inOrder.verify(context).disableDefaultConstraintViolation();
        inOrder.verify(context).buildConstraintViolationWithTemplate("request.validation.conversion.history.filter");
        inOrder.verify(constraintViolationBuilder).addPropertyNode("transactionId");
    }

    @Test
    void shouldReturnFalseWhenTransactionIdAndTransactionDateNotExist() {
        //Given
        mockContext();
        ExchangeConversionHistoryFilterRequest request = ExchangeConversionHistoryFilterRequest.builder()
                .build();

        //When
        boolean result = validator.isValid(request, context);

        //Then
        assertThat(result).isFalse();
        InOrder inOrder = inOrder(context, constraintViolationBuilder);
        inOrder.verify(context).disableDefaultConstraintViolation();
        inOrder.verify(context).buildConstraintViolationWithTemplate("request.validation.conversion.history.filter");
        inOrder.verify(constraintViolationBuilder).addPropertyNode("transactionId");
        inOrder.verify(context).buildConstraintViolationWithTemplate("request.validation.conversion.history.filter");
        inOrder.verify(constraintViolationBuilder).addPropertyNode("transactionDate");
    }


    private void mockContext() {
        when(context.buildConstraintViolationWithTemplate(anyString())).thenReturn(constraintViolationBuilder);
        when(constraintViolationBuilder.addPropertyNode(anyString())).thenReturn(nodeBuilderCustomizableContext);
    }
}