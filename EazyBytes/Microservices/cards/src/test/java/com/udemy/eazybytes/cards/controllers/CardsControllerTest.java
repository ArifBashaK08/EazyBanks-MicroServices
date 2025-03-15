package com.udemy.eazybytes.cards.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.udemy.eazybytes.cards.constants.CardsConstants;
import com.udemy.eazybytes.cards.dto.CardsDTO;
import com.udemy.eazybytes.cards.entities.Cards;
import com.udemy.eazybytes.cards.repository.CardsRepo;
import com.udemy.eazybytes.cards.services.ICardsServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CardsControllerTest {

    private MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();
    ObjectWriter objectWriter = objectMapper.writer();

    @Mock
    private CardsRepo cardsRepo;

    @Mock
    private ICardsServices iCardsServices;

    @InjectMocks
    private CardsController cardsController;

    CardsDTO cardsDTO1 = new CardsDTO(
            "9876543210", 
            "1234567890123456", 
            "Credit_Card",
            "VISA",  
            100000, 
            25000, 
            75000 
    );

    CardsDTO cardsDTO2 = new CardsDTO(
            "9123456789",
            "9876543210987654",
            "Debit_Card",
            "Master_Card",
            50000,
            10000,
            40000
    );

    CardsDTO cardsDTO3 = new CardsDTO(
            "9234567890",
            "5678901234567890",
            "Prepaid_Card",
            "RuPay",
            200000,
            5000,
            15000
    );

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(cardsController).build();
    }

    @Test
    public void createNewCard() throws Exception{
        Mockito.doNothing().when(iCardsServices).createCard("9345678901");

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .post("/api/cards/create")
                .param("mobileNumber", "9345678901")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(mockRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.statusCode", is("201")))
                .andExpect(jsonPath("$.statusMsg", is("Card created successfully")));
    }

    @Test
    public void fetchCardDetailsTest() throws Exception{
        Mockito.when(iCardsServices.fetchCardDetails(cardsDTO2.getMobileNumber())).thenReturn(cardsDTO2);

        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/cards/fetch")
                .param("mobileNumber", cardsDTO2.getMobileNumber())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.mobileNumber", is("9123456789")))
                .andExpect(jsonPath("$.cardNumber", is("9876543210987654" )))
                .andExpect(jsonPath("$.paymentNetworksType", is("Master_Card")));
    }

    @Test
    public void updateCardDetailsTest() throws Exception{
        cardsDTO1 = CardsDTO.builder()
                .cardNumber("1234567890123456")
                .cardType("Credit_Card")
                .paymentNetworksType("VISA")
                .mobileNumber("9876501234")
                .totalLimit(100000)
                .amountUsed(50000)
                .availableAmount(10000)
                .build();

        Mockito.when(iCardsServices.updateCard(cardsDTO1)).thenReturn(true);

        String content = objectWriter.writeValueAsString(cardsDTO1);

        MockHttpServletRequestBuilder mockRequest = MockMvcRequestBuilders
                .patch("/api/cards/update")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);

        mockMvc.perform(mockRequest)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.statusCode", is(CardsConstants.STATUS_200)))
                .andExpect(jsonPath("$.statusMsg", is(CardsConstants.MESSAGE_200)));
    }

    @Test
    public void deleteCardDetails() throws Exception{
        Mockito.when(iCardsServices.deleteCard(cardsDTO3.getMobileNumber())).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders
                .delete("/api/cards/delete")
                .param("mobileNumber", cardsDTO3.getMobileNumber())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", notNullValue()))
                .andExpect(jsonPath("$.statusCode", is(CardsConstants.STATUS_200)))
                .andExpect(jsonPath("$.statusMsg", is(CardsConstants.MESSAGE_200)));
    }
}