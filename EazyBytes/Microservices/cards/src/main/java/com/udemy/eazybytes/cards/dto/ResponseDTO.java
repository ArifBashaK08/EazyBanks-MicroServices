package com.udemy.eazybytes.cards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(
        name = "Response",
        description = "Schema for Successful Response"
)
public class ResponseDTO {
    @Schema(description = "Status code in the response")
    private String statusCode;
    @Schema(description = "Status message in the response")
    private String statusMsg;
}
