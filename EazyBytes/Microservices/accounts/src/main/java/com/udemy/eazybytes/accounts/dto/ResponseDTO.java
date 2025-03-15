package com.udemy.eazybytes.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
@Schema(
        name = "Response",
        description = "Schema for Successful Response"
)
public class ResponseDTO {
    @Schema(
            description = "Status code in the response"
    )
    private String statusCode;
    @Schema(
            description = "Status message in the response"
    )
    private String statusMsg;
}
