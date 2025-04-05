package com.eazybytes.loans.controller;

import com.eazybytes.loans.dto.LoansDTO;
import com.eazybytes.loans.entity.Loans;
import com.eazybytes.loans.repository.LoansRepository;
import com.eazybytes.loans.service.ILoansService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
class LoansControllerTest {
    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    @Mock
    private LoansRepository loansRepo;

    @Mock
    private ILoansService iLoansService;

    @InjectMocks
    private LoansController loansController;

    LoansDTO loansDTO1 = new LoansDTO("9876543210", "100762831592", "Personal Loan", 100000, 50000, 50000);
    LoansDTO loansDTO2 = new LoansDTO("8765432109", "200763945673", "Home Loan", 500000, 200000, 300000);
    LoansDTO loansDTO3 = new LoansDTO("9654321098", "300764123987", "Car Loan", 300000, 100000, 200000);
    LoansDTO loansDTO4 = new LoansDTO("8543210987", "400765789654", "Education Loan", 200000, 50000, 150000);
    LoansDTO loansDTO5 = new LoansDTO("9432109876", "500766231478", "Gold Loan", 150000, 100000, 50000);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(loansController).build();
    }

    @Test
    public void createLoan() throws Exception {
        Loans newLoan = Loans.builder()
                .mobileNumber("9321098765")
                .loanNumber("600767345892")
                .loanType("Business Loan")
                .totalLoan(750000)
                .amountPaid(300000)
                .outstandingAmount(450000)
                .build();

        Mockito.when(loansRepo.save(newLoan)).thenReturn(newLoan);
        Mockito.doNothing().when(iLoansService).createLoan(String.valueOf(Mockito.any(LoansDTO.class)));

        String content = objectWriter.writeValueAsString(newLoan);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .post("/api/loans/create")
                .param("mobileNumber", "9321098765")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.statusCode", is("201")))
                .andExpect(jsonPath("$.statusMsg", is("Loan created successfully")));

    }

    @Test
    public void fetchLoanDetails() throws Exception {
        Mockito.when(iLoansService.fetchLoan(loansDTO4.getMobileNumber())).thenReturn(loansDTO4);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/loans/fetch")
                .param("mobileNumber", "6543210987")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.loanNumber", is("400765789654")))
                .andExpect(jsonPath("$.amountPaid", is(50000)));
    }

    @Test
    public void updateLoanDetails() throws Exception {
        loansDTO3 = LoansDTO.builder()
                .loanNumber("300764123987")
                .mobileNumber("9654321098")
                .loanType("Education Loan")
                .totalLoan(1500000)
                .amountPaid(500000)
                .outstandingAmount(2000000)
                .build();

        Mockito.when(iLoansService.updateLoan(loansDTO3)).thenReturn(true);

        String content = objectWriter.writeValueAsString(loansDTO3);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .put("/api/loans/update")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON).content(content);

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.statusCode", is("200")))
                .andExpect(jsonPath("$.statusMsg", is("Request processed successfully")));
    }

    @Test
    public void deleteLoanDetails() throws Exception {
        String mobileNumber = loansDTO2.getMobileNumber();
        Mockito.when(iLoansService.deleteLoan(mobileNumber)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/loans/delete")
                .param("mobileNumber", mobileNumber)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.statusCode", is("200")))
                .andExpect(jsonPath("$.statusMsg", is("Request processed successfully")));
    }
}