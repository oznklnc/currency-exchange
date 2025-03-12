package com.ozan.currency.exchange.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ozan.currency.exchange.model.Base;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Schema(
        name = "Error",
        description = "Schema to hold error information"
)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDto extends Base {

    @Schema(description = "Schema to hold error message")
    private String message;

    @Schema(description = "Schema to hold error field")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String field;
}
