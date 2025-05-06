package com.udemy.eazybytes.accounts.services;

import com.udemy.eazybytes.accounts.dto.CustomerDTO;

public interface IAccountsService {
    /*@param customerDTO - CustomerDTO Object*/
    void createAccount(CustomerDTO customerDTO);
    CustomerDTO fetchAccount(String mobileNumber);
    boolean updateAccount(CustomerDTO customerDTO);
    boolean deleteAccount(String mobileNumber);
    boolean updateCommunicationStatus(Long accountNumber);
}
