package com.ozan.currency.exchange.model.request;

import lombok.*;

import java.util.Map;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WebClientRequest<Req, Res> {

    private String url;
    private Req requestBody;
    private Class<Res> responseClass;
    private Map<String, Object> queryParams;

}
