package com.ozan.currency.exchange.model.response;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Schema(
        name = "PagedResponse",
        description = "Schema to hold paged response information. This is used for pagination."
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PagedResponse<T> {

    @Schema(description = "Content of the page")
    private List<T> content;
    @Schema(description = "Current page number")
    private Integer currentPage;
    @Schema(description = "Total number of pages")
    private Integer totalPages;
    @Schema(description = "Total number of elements")
    private Long totalElements;
    @Schema(description = "Total number of content")
    private Integer contentCount;
    @Schema(description = "Page size")
    private Integer pageSize;
}
