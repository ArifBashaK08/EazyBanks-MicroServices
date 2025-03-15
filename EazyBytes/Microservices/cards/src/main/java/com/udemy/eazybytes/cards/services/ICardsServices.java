package com.udemy.eazybytes.cards.services;

import com.udemy.eazybytes.cards.dto.CardsDTO;

public interface ICardsServices {
    /**
     *
     * @param mobileNumber - Mobile Number of the Customer
     */
    void createCard(String mobileNumber);

    /**
     *
     * @param mobileNumber - Mobile Number of the Customer
     * @return Card Details based on a given mobileNumber
     */
    CardsDTO fetchCardDetails(String mobileNumber);

    /**
     *
     * @param CardsDTO - CardsDto Object
     * @return boolean indicating if the update of card details is successful or not
     */
    boolean updateCard(CardsDTO cardsDTO);

    /**
     *
     * @param mobileNumber - Mobile Number of the Customer
     * @return boolean indicating if the deletion of card details is successful or not
     */
    boolean deleteCard(String mobileNumber);
}
