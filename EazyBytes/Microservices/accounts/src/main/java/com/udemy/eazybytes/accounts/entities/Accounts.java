package com.udemy.eazybytes.accounts.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Accounts extends BaseEntity {
    private Long customerId;
    @Id
    @Column(insertable = false)
    private Long accountNumber;
    @NotNull
    private String accountType;
    @NotNull
    private String branchAddress;
}
