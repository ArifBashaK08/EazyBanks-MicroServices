package com.udemy.eazybytes.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Account", description = "Schema holds Account details of the customer")
public class AccountsDTO {

    @NotEmpty(message = "Account number cannot be null or empty")
    @Pattern(regexp = "(^[0-9]{10}$)", message = "Account number must have 10 digits")
    @Schema(description = "Account Number of the customer")
    private Long accountNumber;

    @NotEmpty(message = "Account Type is mandatory")
    @Schema(description = "Type of Account of the customer", example = "SAVINGS")
    private String accountType;

    @NotEmpty(message = "Branch address cannot be null or empty")
    @Schema(description = "Eazy Bank Branch Address", example = "123 street, New York, US")
    private String branchAddress;
}
