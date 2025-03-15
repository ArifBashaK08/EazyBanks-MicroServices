package com.udemy.eazybytes.cards.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(
        name = "Cards",
        description = "Schema for card details"
)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardsDTO {

    @NotEmpty(message = "Mobile number is required")
    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Mobile number should have 10 digits"
    )
    @Schema(
            description = "Mobile number of the user",
            example = "9874563210"
    )
    private String mobileNumber;

    @Pattern(
            regexp = "^[0-9]{16}$",
            message = "Card number should have 16 digits"
    )
    @Schema(
            description = "Card number of the user",
            example = "9874 5632 1095 4789"
    )
    @NotEmpty(message = "Card number is required")
    private String cardNumber;

    @NotEmpty(message = "Card type is required")
    @Schema(
            description = "Type of the card",
            example = "Credit Card"
    )
    private String cardType;

    @NotEmpty(message = "Card payment networks type is required")
    @Schema(
            description = "Payment networks type of the card",
            example = "Visa"
    )
    private String  paymentNetworksType;

    @Positive(message = "Total card limit should be greater than zero")
     @Schema(
            description = "Total amount limit available against a card",
            example = "100000"
    )
    private int totalLimit;

    @PositiveOrZero(message = "Amount used should be greater than or equal to zero")
     @Schema(
            description = "Total amount debited from the card",
            example = "5000"
    )
    private int amountUsed;

    @PositiveOrZero(message = "Balance amount should be greater than or equal to zero")
     @Schema(
            description = "Available balance amount in account",
            example = "30000"
    )
    private int availableAmount;
}
