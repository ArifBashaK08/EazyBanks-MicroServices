package com.udemy.eazybytes.cards.mappers;

import com.udemy.eazybytes.cards.dto.CardsDTO;
import com.udemy.eazybytes.cards.entities.Cards;

public class CardsMapper {

    public static CardsDTO mapToCardsDTO(Cards cards, CardsDTO cardsDTO){

        cardsDTO.setCardNumber(cards.getCardNumber());
        cardsDTO.setCardType(cards.getCardType());
        cardsDTO.setMobileNumber(cards.getMobileNumber());
        cardsDTO.setPaymentNetworksType(cards.getPaymentNetworksType());
        cardsDTO.setAmountUsed(cards.getAmountUsed());
        cardsDTO.setTotalLimit(cards.getTotalLimit());
        cardsDTO.setAvailableAmount(cards.getAvailableAmount());

        return cardsDTO;
    }
    public static Cards mapToCards(CardsDTO cardsDTO, Cards cards){

//        cards.setCardNumber(cardsDTO.getCardNumber());
        cards.setMobileNumber(cardsDTO.getMobileNumber());
//        cards.setCardType(cardsDTO.getCardType());
//        cards.setPaymentNetworksType(cardsDTO.getPaymentNetworksType());
//        cards.setTotalLimit(cardsDTO.getTotalLimit());
        cards.setAmountUsed(cardsDTO.getAmountUsed());
        cards.setAvailableAmount(cardsDTO.getAvailableAmount());

        return cards;
    }

}
