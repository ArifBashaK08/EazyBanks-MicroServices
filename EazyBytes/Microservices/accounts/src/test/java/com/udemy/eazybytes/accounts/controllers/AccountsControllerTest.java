package com.udemy.eazybytes.accounts.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.udemy.eazybytes.accounts.dto.AccountsDTO;
import com.udemy.eazybytes.accounts.dto.CustomerDTO;
import com.udemy.eazybytes.accounts.entities.Accounts;
import com.udemy.eazybytes.accounts.repositories.AccountsRepo;
import com.udemy.eazybytes.accounts.services.IAccountsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class AccountsControllerTest {
    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    @Mock
    private AccountsRepo accountsRepo;

    @Mock
    private IAccountsService iAccountsService;

    @InjectMocks
    private AccountsController accountsController;
    AccountsDTO accountsDTO1 = new AccountsDTO(9786453120L, "SAVINGS", "123 Street, Walnut Road");
    CustomerDTO customerDTO1 = new CustomerDTO("John Deo", "john.deo@gmail.com", "9856321470", accountsDTO1);

    AccountsDTO accountsDTO2 = new AccountsDTO(8765432109L, "CURRENT", "456 Avenue, Maple Street");
    CustomerDTO customerDTO2 = new CustomerDTO("Jane Smith", "jane.smith@email.com", "9123456780", accountsDTO2);

    AccountsDTO accountsDTO3 = new AccountsDTO(7654321987L, "FIXED_DEPOSIT", "789 Boulevard, Oak Lane");
    CustomerDTO customerDTO3 = new CustomerDTO("Robert Brown", "robert.brown@email.com", "9876543210", accountsDTO3);

    AccountsDTO accountsDTO4 = new AccountsDTO(6543219876L, "SAVINGS", "321 Drive, Pine Avenue");
    CustomerDTO customerDTO4 = new CustomerDTO("Emily Davis", "emily.davis@email.com", "9786543210", accountsDTO4);

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(accountsController).build();
    }

    @Test
    public void createAccount_success() throws Exception {
        Accounts newAccount = Accounts.builder()
                .customerId(456L)
                .accountNumber(9786453120L)
                .accountType("SAVINGS")
                .branchAddress("Manikonda, Hyderabad")
                .build();

//        Mockito.when(accountsRepo.save(newAccount)).thenReturn(newAccount);
        Mockito.doNothing().when(iAccountsService).createAccount(Mockito.any(CustomerDTO.class));

        String content = objectWriter.writeValueAsString(newAccount);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .post("/api/accounts/create")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.statusCode", is("201")))
                .andExpect(jsonPath("$.statusMsg", is("Account created successfully")));
//                .andDo(print());
    }

    @Test
    public void fetchAccountDetails_success() throws Exception {
        Mockito.when(iAccountsService.fetchAccount(customerDTO3.getMobileNumber())).thenReturn(customerDTO3);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/accounts/fetch")
                        .param("mobileNumber", "9876543210")
                        .contentType(MediaType.APPLICATION_JSON))
//                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.name", is("Robert Brown")))
                .andExpect(jsonPath("$.accountsDTO.accountType", is("FIXED_DEPOSIT")));
    }

    @Test
    public void updateAccountDetails_success() throws Exception {
        AccountsDTO accountsDTO = AccountsDTO.builder()
                .accountNumber(7654321987L)
                .accountType("CURRENT")
                .branchAddress("700 Boulevard, Oak Lane").build();
        CustomerDTO updatedCustomerDTO = CustomerDTO.builder()
                .name("Robert Brown Red")
                .email("robert.brown.red@email.com")
                .mobileNumber("9876543222")
                .accountsDTO(accountsDTO).build();

        Mockito.when(iAccountsService.updateAccount(customerDTO3)).thenReturn(true);
        Mockito.when(iAccountsService.updateAccount(updatedCustomerDTO)).thenReturn(true);

        String content = objectWriter.writeValueAsString(updatedCustomerDTO);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .put("/api/accounts/update")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
//                .andDo(print())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.statusCode", is("200")))
                .andExpect(jsonPath("$.statusMsg", is("Request processed successfully")));
    }

    @Test
    public void deleteAccountDetails_success() throws Exception{
        Mockito.when(iAccountsService.deleteAccount(customerDTO2.getMobileNumber())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/accounts/delete")
                        .param("mobileNumber", "9123456780")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.statusCode", is("200")))
                .andExpect(jsonPath("$.statusMsg", is("Request processed successfully")));
    }
}
