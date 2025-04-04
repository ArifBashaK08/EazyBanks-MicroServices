package com.udemy.eazybytes.accounts.services.client;

import com.udemy.eazybytes.accounts.dto.LoansDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient("loans")
public interface LoansFeignClient {
    @GetMapping(value = "/api/loans/fetch", consumes = "application/json")
    public ResponseEntity<LoansDTO> fetchLoanDetails(@RequestParam  String mobileNumber);
}
