package com.ozan.currency.exchange.model.dto;

import lombok.*;

import java.util.Map;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WebClientRequestDto<C, R> {

    private String url;
    private C requestBody;
    private Class<R> responseClass;
    private Map<String, Object> queryParams;

}
