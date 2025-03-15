package com.udemy.eazybytes.cards.services.impl;

import com.udemy.eazybytes.cards.constants.CardsConstants;
import com.udemy.eazybytes.cards.dto.CardsDTO;
import com.udemy.eazybytes.cards.entities.Cards;
import com.udemy.eazybytes.cards.exceptions.CardAlreadyExistsException;
import com.udemy.eazybytes.cards.exceptions.ResourceNotFoundException;
import com.udemy.eazybytes.cards.mappers.CardsMapper;
import com.udemy.eazybytes.cards.repository.CardsRepo;
import com.udemy.eazybytes.cards.services.ICardsServices;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class CardsServicesImpl implements ICardsServices {

    private CardsRepo cardsRepo;

    @Override
    public void createCard(String mobileNumber) {
        Optional<Cards> card = cardsRepo.findByMobileNumber(mobileNumber);

        if (card.isPresent()) {
            throw new CardAlreadyExistsException("Card already registered with given mobile number " + mobileNumber);
        }

        cardsRepo.save(createNewCard(mobileNumber));
    }

    private Cards createNewCard(String mobileNumber) {
        Cards newCard = new Cards();
        long randomCardNumber = 1000000000000000L + new Random().nextLong(9000000000000000L);

        newCard.setCardNumber(String.valueOf(randomCardNumber));
        newCard.setMobileNumber(mobileNumber);
        newCard.setCardType(String.valueOf(CardsConstants.CARD_TYPES.Credit_Card));
        newCard.setPaymentNetworksType(String.valueOf(CardsConstants.PAYMENT_NETWORK_TYPE.VISA));
        newCard.setTotalLimit(1500000);
        newCard.setAmountUsed(0);
        newCard.setAvailableAmount(40000);

        return newCard;
    }

    @Override
    public CardsDTO fetchCardDetails(String mobileNumber) {

        Cards existingCard = cardsRepo.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new CardAlreadyExistsException("Card not found with given mobile number " + mobileNumber)
        );

        return CardsMapper.mapToCardsDTO(existingCard, new CardsDTO());
    }

    @Transactional
    @Override
    public boolean updateCard(CardsDTO cardsDTO) {
        Cards existingCard = cardsRepo.findByCardNumber(cardsDTO.getCardNumber()).orElseThrow(
                () -> new ResourceNotFoundException("Card", "cardNumber", cardsDTO.getCardNumber())
        );
        CardsMapper.mapToCards(cardsDTO, existingCard);
        System.out.println(existingCard);
        System.out.println("Card updated");
        cardsRepo.save(existingCard);
        return true;
    }

    @Override
    public boolean deleteCard(String mobileNumber) {
        Cards card = cardsRepo.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFoundException("Card", "mobileNumber", mobileNumber)
        );

        cardsRepo.deleteById(card.getCardId());
        return true;
    }
}
