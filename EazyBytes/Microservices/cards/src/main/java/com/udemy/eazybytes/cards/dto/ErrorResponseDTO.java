package com.udemy.eazybytes.cards.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
@AllArgsConstructor
@Schema(
        name = "Error Response",
        description = "Schema for Error Response"
)
public class ErrorResponseDTO {

    @Schema(description = "API path invoked by user")
    private String apiPath;

    @Schema(description = "Error status code representing error")
    private HttpStatus errorCode;

    @Schema(description = "Error message describing about error")
    private String errorMsg;

    @Schema(description = "Time representing when error happened")
    private Date errorTime;
}
