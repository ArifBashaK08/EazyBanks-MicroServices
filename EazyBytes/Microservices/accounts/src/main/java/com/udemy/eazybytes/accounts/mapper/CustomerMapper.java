package com.udemy.eazybytes.accounts.mapper;

import com.udemy.eazybytes.accounts.dto.CustomerDTO;
import com.udemy.eazybytes.accounts.dto.CustomersDetailsDTO;
import com.udemy.eazybytes.accounts.entities.Customer;

public class CustomerMapper {

    public static CustomerDTO mapToCustomerDTO(Customer customer, CustomerDTO customerDTO){

        customerDTO.setName(customer.getName());
        customerDTO.setEmail(customer.getEmail());
        customerDTO.setMobileNumber(customer.getMobileNumber());

        return  customerDTO;
    }

    public static Customer mapToCustomer(CustomerDTO customerDTO, Customer customer){

        customer.setName(customerDTO.getName());
        customer.setEmail(customerDTO.getEmail());
        customer.setMobileNumber(customerDTO.getMobileNumber());

        return  customer;
    }
    
    public static CustomersDetailsDTO mapToCustomerDetailsDTO(Customer customer, CustomersDetailsDTO customerDetailsDTO){
    	
    	customerDetailsDTO.setName(customer.getName());
    	customerDetailsDTO.setEmail(customer.getEmail());
    	customerDetailsDTO.setMobileNumber(customer.getMobileNumber());
    	
    	return  customerDetailsDTO;
    }
    
    public static Customer mapToCustomer(CustomersDetailsDTO customerDetailsDTO, Customer customer){

        customer.setName(customerDetailsDTO.getName());
        customer.setEmail(customerDetailsDTO.getEmail());
        customer.setMobileNumber(customerDetailsDTO.getMobileNumber());

        return  customer;
    }
}
