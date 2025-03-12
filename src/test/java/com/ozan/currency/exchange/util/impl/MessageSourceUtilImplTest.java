package com.ozan.currency.exchange.util.impl;


import com.ozan.currency.exchange.base.UnitTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class MessageSourceUtilImplTest extends UnitTest {

    @Mock
    private MessageSource messageSource;

    @InjectMocks
    private MessageSourceUtilImpl messageSourceUtil;

    @Test
    void shouldReturnMessageWithKey() {
        //Given
        String key = "key";
        String message = "message";
        when(messageSource.getMessage(key, null, LocaleContextHolder.getLocale())).thenReturn(message);

        //When
        String result = messageSourceUtil.getMessage(key);

        //Then
        assertThat(result).isEqualTo(message);
    }

    @Test
    void shouldReturnMessageWithKeyAndArgs() {
        //Given
        String key = "key";
        String message = "message";
        Object[] args = new Object[]{"arg1", "arg2"};
        when(messageSource.getMessage(key, args, LocaleContextHolder.getLocale())).thenReturn(message);

        //When
        String result = messageSourceUtil.getMessage(key, args);

        //Then
        assertThat(result).isEqualTo(message);
    }
}