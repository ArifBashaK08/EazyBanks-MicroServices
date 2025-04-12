package com.udemy.eazybytes.accounts.services.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.udemy.eazybytes.accounts.dto.AccountsDTO;
import com.udemy.eazybytes.accounts.dto.CardsDTO;
import com.udemy.eazybytes.accounts.dto.CustomersDetailsDTO;
import com.udemy.eazybytes.accounts.dto.LoansDTO;
import com.udemy.eazybytes.accounts.entities.Accounts;
import com.udemy.eazybytes.accounts.entities.Customer;
import com.udemy.eazybytes.accounts.exceptions.ResourceNotFoundException;
import com.udemy.eazybytes.accounts.mapper.AccountsMapper;
import com.udemy.eazybytes.accounts.mapper.CustomerMapper;
import com.udemy.eazybytes.accounts.repositories.AccountsRepo;
import com.udemy.eazybytes.accounts.repositories.CustomerRepo;
import com.udemy.eazybytes.accounts.services.ICustomerServices;
import com.udemy.eazybytes.accounts.services.client.CardsFeignClient;
import com.udemy.eazybytes.accounts.services.client.LoansFeignClient;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomerServiceImpl implements ICustomerServices {

    private AccountsRepo accountsRepo;
    private CustomerRepo customerRepo;
    private CardsFeignClient cardsFeignClient;
    private LoansFeignClient loansFeignClient;

    @Override
    public CustomersDetailsDTO fetchCustomerDetails(String mobileNumber, String correlationId) {
        Customer customer = customerRepo.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber));

        Accounts account = accountsRepo.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString()));

        CustomersDetailsDTO customersDetailsDTO = CustomerMapper.mapToCustomerDetailsDTO(customer, new CustomersDetailsDTO());

        customersDetailsDTO.setAccountsDTO(AccountsMapper.mapToAccountsDTO(account, new AccountsDTO()));

        ResponseEntity<LoansDTO> loansDTOResponseEntity = loansFeignClient.fetchLoanDetails(correlationId, mobileNumber);
        if (null != loansDTOResponseEntity) {
            customersDetailsDTO.setLoansDTO(loansDTOResponseEntity.getBody());
        }

        ResponseEntity<CardsDTO> cardsDTOResponseEntity = cardsFeignClient.fetchCardDetails(correlationId, mobileNumber);
        if (null != loansDTOResponseEntity) {
            customersDetailsDTO.setCardsDTO(cardsDTOResponseEntity.getBody());
        }

        return customersDetailsDTO;
    }
}
