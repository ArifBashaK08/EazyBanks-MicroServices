package com.udemy.eazybytes.accounts.services.impl;

import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.udemy.eazybytes.accounts.constants.AccountsConstants;
import com.udemy.eazybytes.accounts.dto.AccountsDTO;
import com.udemy.eazybytes.accounts.dto.CustomerDTO;
import com.udemy.eazybytes.accounts.entities.Accounts;
import com.udemy.eazybytes.accounts.entities.Customer;
import com.udemy.eazybytes.accounts.exceptions.CustomerAlreadyExistsException;
import com.udemy.eazybytes.accounts.exceptions.ResourceNotFoundException;
import com.udemy.eazybytes.accounts.mapper.AccountsMapper;
import com.udemy.eazybytes.accounts.mapper.CustomerMapper;
import com.udemy.eazybytes.accounts.repositories.AccountsRepo;
import com.udemy.eazybytes.accounts.repositories.CustomerRepo;
import com.udemy.eazybytes.accounts.services.IAccountsService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class AccountsServiceImpl implements IAccountsService {

    @Autowired
    private AccountsRepo accountsRepo;
    @Autowired
    private CustomerRepo customerRepo;

    @Override
    public void createAccount(CustomerDTO customerDTO) {
        Customer customer = CustomerMapper.mapToCustomer(customerDTO, new Customer());
        Optional<Customer> optionalCustomer = customerRepo.findByMobileNumber(customer.getMobileNumber());

        if (optionalCustomer.isPresent()) {
            throw new CustomerAlreadyExistsException("Customer already registered with given mobileNumber : " + customerDTO.getMobileNumber());
        }

        Customer savedCustomer = customerRepo.save(customer);
        accountsRepo.save(createNewAccount(savedCustomer));
    }

    private Accounts createNewAccount(Customer customer) {
        Accounts newAccount = new Accounts();
        newAccount.setCustomerId(customer.getCustomerId());
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);

        newAccount.setAccountNumber(randomAccNumber);
        newAccount.setAccountType(AccountsConstants.SAVINGS);
        newAccount.setBranchAddress(AccountsConstants.ADDRESS);

        return newAccount;
    }

    @Override
    public CustomerDTO fetchAccount(String mobileNumber) {

        Customer customer = customerRepo.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );

        Accounts accounts = accountsRepo.findByCustomerId(customer.getCustomerId()).orElseThrow(
                () -> new ResourceNotFoundException("Account", "customerId", customer.getCustomerId().toString())
        );

        CustomerDTO customerDTO = CustomerMapper.mapToCustomerDTO(customer, new CustomerDTO());
        customerDTO.setAccountsDTO(AccountsMapper.mapToAccountsDTO(accounts, new AccountsDTO()));
        return customerDTO;
    }

    @Override
    public boolean updateAccount(CustomerDTO customerDTO) {
        AccountsDTO accountsDTO = customerDTO.getAccountsDTO();

        if (accountsDTO == null) {
            return false;
        }
        Accounts accounts = accountsRepo.findById(accountsDTO.getAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Account", "AccountNumber",
                accountsDTO.getAccountNumber().toString()));
        AccountsMapper.mapToAccounts(accountsDTO, accounts);

        accounts = accountsRepo.save(accounts);

        Long customerId = accounts.getCustomerId();
        Customer customer = customerRepo.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer", "CustomerDTO",
                customerId.toString()));

        CustomerMapper.mapToCustomer(customerDTO, customer);
        customerRepo.save(customer);
        return true;
    }

    @Override
    public boolean deleteAccount(String mobileNumber) {
        Customer customer = customerRepo.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Customer", "mobileNumber", mobileNumber)
        );
        accountsRepo.deleteByCustomerId(customer.getCustomerId());
        customerRepo.deleteById(customer.getCustomerId());
        return true;
    }
}
