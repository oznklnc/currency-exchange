package com.ozan.currency.exchange.mapper;

import com.ozan.currency.exchange.model.response.PagedResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;

@Component
public class ResponseMapper {

    public <E,R> PagedResponse<R> toPagedResponse(Page<E> page, Function<E, R> converter) {
        List<R> content = page.getContent().stream()
                .map(converter)
                .toList();
        return PagedResponse.<R>builder()
                .content(content)
                .currentPage(page.getNumber())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .contentCount(content.size())
                .pageSize(page.getSize())
                .build();
    }
}
