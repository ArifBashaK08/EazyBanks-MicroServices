package com.udemy.eazybytes.accounts.services.client;

import com.udemy.eazybytes.accounts.dto.CardsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient("cards")
public interface CardsFeignClient {
    @GetMapping(value = "/api/cards/fetch", consumes = "application/json")
    public ResponseEntity<CardsDTO> fetchCardDetails(@RequestParam  String mobileNumber);
}
