package com.ozan.currency.exchange.util;

public interface MessageSourceUtil {

    String getMessage(String key);
    String getMessage(String key, Object[] args);
}
