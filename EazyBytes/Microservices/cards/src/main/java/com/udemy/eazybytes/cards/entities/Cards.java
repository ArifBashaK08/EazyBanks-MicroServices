package com.udemy.eazybytes.cards.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cards  extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cardId;

    @NotNull
    @Pattern(regexp = "^[0-9]{10}$")
    private String mobileNumber;

    @Column(updatable = false)
    private String cardNumber;

    @Column(updatable = false)
    private String cardType;

    @Column(updatable = false)
    private String paymentNetworksType;

    @Column(updatable = false)
    private int totalLimit;

    private int amountUsed;

    private int availableAmount;
}
