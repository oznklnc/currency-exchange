package com.ozan.currency.exchange.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UrlPath {

    private static final String VERSION = "v1";

    public static final String BASE_PATH = "/api/" + VERSION;
}
