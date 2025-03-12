package com.ozan.currency.exchange.model.dto;

import com.ozan.currency.exchange.model.Base;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Schema(
        name = "ApiErrorResponse",
        description = "Schema to hold error response information"
)
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorDto extends Base {

    @Schema(description = "API url invoked by client")
    private String url;

    @Schema(description = "User friendly error message list")
    private List<ErrorDto> errors;
}
