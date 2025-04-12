package com.udemy.eazybytes.accounts.controllers;

import com.udemy.eazybytes.accounts.dto.CustomersDetailsDTO;
import com.udemy.eazybytes.accounts.dto.ErrorResponseDTO;
import com.udemy.eazybytes.accounts.services.ICustomerServices;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "EazyBank Customers REST API",
        description = "Rest API to fetch Customer details @EazyBank"
)
@RestController
@RequestMapping(
        path = "/api/eazybanks",
        produces = {MediaType.APPLICATION_JSON_VALUE}
)
@RequiredArgsConstructor
@Validated
public class CustomerController {

    private final ICustomerServices iCustomerServices;
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Operation(
            summary = "Fetch Customer Details",
            description = "REST API to fetch user details using mobile number"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @GetMapping("/customer-details")
    public ResponseEntity<CustomersDetailsDTO> fetchCustomerDetails(@RequestHeader("eazyBanks-correlation-id") String correlationId
            , @RequestParam @Pattern(regexp = "^[0-9]{10}$",
                    message = "Mobile number must have 10 digits") String mobileNumber) {
        logger.debug("eazyBanks-correlation-id found : {}", correlationId);
        CustomersDetailsDTO customersDetailsDTO = iCustomerServices.fetchCustomerDetails(mobileNumber, correlationId);

        return ResponseEntity.status(HttpStatus.OK).body(customersDetailsDTO);
    }

    @RequestMapping("/**")
    public ResponseEntity<String> unauthorizedAccess(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("401 - Unauthorized Access");
    }

}
