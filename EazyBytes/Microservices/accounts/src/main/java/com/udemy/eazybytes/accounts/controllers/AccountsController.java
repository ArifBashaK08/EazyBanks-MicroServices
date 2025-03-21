package com.udemy.eazybytes.accounts.controllers;

import com.udemy.eazybytes.accounts.constants.AccountsConstants;
import com.udemy.eazybytes.accounts.dto.AccountsContactInfoDTO;
import com.udemy.eazybytes.accounts.dto.CustomerDTO;
import com.udemy.eazybytes.accounts.dto.ErrorResponseDTO;
import com.udemy.eazybytes.accounts.dto.ResponseDTO;
import com.udemy.eazybytes.accounts.services.IAccountsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "EazyBank Accounts CRUD REST API",
        description = "CRUD REST APIs for Accounts @ EazyBank"
)
@RestController
@RequestMapping(
        path = "/api/accounts",
        produces = {MediaType.APPLICATION_JSON_VALUE}
)
@RequiredArgsConstructor
@Validated
public class AccountsController {

    private final IAccountsService iAccountsService;

    @Value("${build.version}")
    private String buildVersion;

    @Autowired
    private Environment environment;

    @Autowired
    private AccountsContactInfoDTO accountsContactInfoDTO;

    @Operation(
            summary = "Create new account for customer",
            description = "REST API to create new account with  EazyBank"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "HTTP Status CREATED"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @PostMapping("/create")
    public ResponseEntity<ResponseDTO> createAccount(@Valid @RequestBody CustomerDTO customerDTO) {
        iAccountsService.createAccount(customerDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDTO(AccountsConstants.STATUS_201, AccountsConstants.MESSAGE_201));
    }

    @Operation(
            summary = "Fetch Account Details",
            description = "REST API to fetch user account details using mobile number"
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
    @GetMapping("/fetch")
    public ResponseEntity<CustomerDTO> fetchAccountDetails(@RequestParam @Pattern(regexp = "(^[0-9]{10}$)",
            message = "Mobile number must have 10 digits") String mobileNumber) {
        CustomerDTO customerDTO = iAccountsService.fetchAccount(mobileNumber);
        return ResponseEntity.status(HttpStatus.OK).body(customerDTO);
    }

    @Operation(
            summary = "Update Account Details",
            description = "REST API to update user details using mobile number"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Exception Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @PutMapping("/update")
    public ResponseEntity<ResponseDTO> updateAccountDetails(@Valid @RequestBody CustomerDTO customerDTO) {
        boolean isUpdated = iAccountsService.updateAccount(customerDTO);
        if (isUpdated) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDTO(
                            AccountsConstants.STATUS_200,
                            AccountsConstants.MESSAGE_200)
                    );
        } else {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDTO(
                            AccountsConstants.STATUS_417,
                            AccountsConstants.MESSAGE_417_UPDATE)
                    );
        }
    }

    @Operation(
            summary = "Delete existing account Details",
            description = "REST API to delete user details using mobile number from EazyBank"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "HTTP Status OK"
            ),
            @ApiResponse(
                    responseCode = "417",
                    description = "Exception Failed"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "HTTP Status INTERNAL_SERVER_ERROR",
                    content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))
            )
    })
    @DeleteMapping("/delete")
    public ResponseEntity<ResponseDTO> deleteAccountDetails(@RequestParam @Pattern(regexp = "(^[0-9]{10}$)",
            message = "Mobile number must have 10 digits") String mobileNumber) {
        boolean isDeleted = iAccountsService.deleteAccount(mobileNumber);
        if (isDeleted) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDTO(
                            AccountsConstants.STATUS_200,
                            AccountsConstants.MESSAGE_200)
                    );
        } else {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED)
                    .body(new ResponseDTO(
                            AccountsConstants.STATUS_417,
                            AccountsConstants.MESSAGE_417_DELETE)
                    );
        }
    }

    //======================= Profiling Demo =======================//
    @Operation(
            summary = "Gives build Details",
            description = "REST API to get build details"
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
    @GetMapping("/build-info")
    public ResponseEntity<String> getBuildInfo() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(buildVersion);
    }

    @Operation(
            summary = "Gives Java version",
            description = "REST API to get Java version"
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
    @GetMapping("/java-version")
    public ResponseEntity<String> getJavaVersion() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(environment.getProperty("JAVA_HOME"));
    }

    @Operation(
            summary = "Gives Java version",
            description = "REST API to get Java version"
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
    @GetMapping("/contact-info")
    public ResponseEntity<AccountsContactInfoDTO> getContactInfo() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(accountsContactInfoDTO);
    }

}
