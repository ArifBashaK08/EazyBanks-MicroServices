package com.udemy.eazybytes.accounts.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "Customer", description = "Schema holds Customer and Account details")
public class CustomerDTO {

    @NotEmpty(message = "Name cannot be null or empty")
    @Size(min = 5, max = 30, message = "Name should have 5 - 30 characters")
    @Schema(description = "Name of customer", example = "John Doe")
    private String name;

    @NotEmpty(message = "Email cannot be null or empty")
    @Email(message = "Email address should have valid value")
    @Schema(description = "Email of the customer", example = "john.doe@example.com")
    private String email;

    @NotEmpty(message = "Mobile number cannot be null or empty")
    @Pattern(regexp = "(^[0-9]{10}$)", message = "Mobile number must have 10 digits")
    @Schema(description = "Mobile number of the customer", example = "9874563210")
    private String mobileNumber;

    @Schema(description = "Account details of the customer")
    private AccountsDTO accountsDTO;
}
