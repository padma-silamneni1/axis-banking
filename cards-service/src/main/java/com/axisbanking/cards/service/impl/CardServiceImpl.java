package com.axisbanking.cards.service.impl;

import com.axisbanking.cards.dto.CardDto;
import com.axisbanking.cards.exception.ResourceNotFoundException;
import com.axisbanking.cards.model.*;
import com.axisbanking.cards.repository.CardRepository;
import com.axisbanking.cards.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;

    @Override
    public CardDto issueCard(CardDto dto) {
        CardType type = CardType.valueOf(dto.getCardType());
        BigDecimal creditLimit = type == CardType.CREDIT_CARD
                ? (dto.getCreditLimit() != null ? dto.getCreditLimit() : new BigDecimal("200000"))
                : BigDecimal.ZERO;

        Card card = Card.builder()
                .cardNumber(generateCardNumber())
                .customerId(dto.getCustomerId())
                .cardHolderName(dto.getCardHolderName())
                .cardType(type)
                .cardNetwork(dto.getCardNetwork() != null ? dto.getCardNetwork() : "VISA")
                .creditLimit(creditLimit)
                .availableLimit(creditLimit)
                .outstandingAmount(BigDecimal.ZERO)
                .expiryDate(LocalDate.now().plusYears(5))
                .cvv(String.format("%03d", new Random().nextInt(1000)))
                .status(CardStatus.ACTIVE)
                .build();

        return mapToDto(cardRepository.save(card));
    }

    @Override
    public CardDto getCardById(Long id) {
        return mapToDto(cardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Card", "id", id.toString())));
    }

    @Override
    public CardDto getCardByNumber(String cardNumber) {
        return mapToDto(cardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Card", "cardNumber", cardNumber)));
    }

    @Override
    public List<CardDto> getCardsByCustomerId(Long customerId) {
        return cardRepository.findByCustomerId(customerId).stream()
                .map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<CardDto> getAllCards() {
        return cardRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public CardDto blockCard(Long id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Card", "id", id.toString()));
        card.setStatus(CardStatus.BLOCKED);
        return mapToDto(cardRepository.save(card));
    }

    @Override
    public CardDto activateCard(Long id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Card", "id", id.toString()));
        card.setStatus(CardStatus.ACTIVE);
        return mapToDto(cardRepository.save(card));
    }

    @Override
    public void deleteCard(Long id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Card", "id", id.toString()));
        cardRepository.delete(card);
    }

    private String generateCardNumber() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder("4532");
        for (int i = 0; i < 12; i++) sb.append(random.nextInt(10));
        return sb.toString();
    }

    private CardDto mapToDto(Card c) {
        return CardDto.builder()
                .id(c.getId()).cardNumber(c.getCardNumber())
                .customerId(c.getCustomerId()).cardHolderName(c.getCardHolderName())
                .cardType(c.getCardType().name()).cardNetwork(c.getCardNetwork())
                .creditLimit(c.getCreditLimit()).availableLimit(c.getAvailableLimit())
                .outstandingAmount(c.getOutstandingAmount()).expiryDate(c.getExpiryDate())
                .status(c.getStatus().name()).build();
    }
}
