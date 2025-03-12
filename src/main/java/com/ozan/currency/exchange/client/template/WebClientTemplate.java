package com.ozan.currency.exchange.client.template;

import com.ozan.currency.exchange.model.dto.WebClientRequestDto;

public interface WebClientTemplate {

    <C, R> R get(WebClientRequestDto<C, R> webClientRequestDto);
}
