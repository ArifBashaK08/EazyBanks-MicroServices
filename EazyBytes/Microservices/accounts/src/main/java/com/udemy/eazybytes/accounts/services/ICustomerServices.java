package com.udemy.eazybytes.accounts.services;

import com.udemy.eazybytes.accounts.dto.CustomersDetailsDTO;

public interface ICustomerServices {

    CustomersDetailsDTO fetchCustomerDetails(String mobileNumber, String correlationId);

}
