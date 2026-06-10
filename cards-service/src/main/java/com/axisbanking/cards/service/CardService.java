package com.axisbanking.cards.service;

import com.axisbanking.cards.dto.CardDto;
import java.util.List;

public interface CardService {

    CardDto issueCard(CardDto cardDto);

    CardDto getCardById(Long id);

    CardDto getCardByNumber(String cardNumber);

    List<CardDto> getCardsByCustomerId(Long customerId);

    List<CardDto> getAllCards();

    CardDto blockCard(Long id);

    CardDto activateCard(Long id);

    void deleteCard(Long id);
}
