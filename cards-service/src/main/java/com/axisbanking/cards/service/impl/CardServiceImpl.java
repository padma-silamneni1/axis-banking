package com.axisbanking.cards.service.impl;

import com.axisbanking.cards.dto.CardDto;
import com.axisbanking.cards.exception.ResourceNotFoundException;
import com.axisbanking.cards.model.*;
import com.axisbanking.cards.repository.CardRepository;
import com.axisbanking.cards.service.CardService;
import com.axisbanking.common.event.EventPublisher;
import com.axisbanking.common.event.EventType;
import com.axisbanking.common.event.KafkaTopics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardRepository cardRepository;
    private final EventPublisher eventPublisher;

    @Override
    @CacheEvict(value = "cardsByCustomer", allEntries = true)
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

        Card saved = cardRepository.save(card);

        Map<String, Object> payload = new HashMap<>();
        payload.put("cardNumber", maskCardNumber(saved.getCardNumber()));
        payload.put("customerId", saved.getCustomerId());
        payload.put("cardType", type.name());
        eventPublisher.publish(KafkaTopics.CARD_EVENTS, EventType.CARD_ISSUED,
                saved.getId().toString(), "Card", "ISSUE", payload);

        log.info("Card issued: {} type: {}", maskCardNumber(saved.getCardNumber()), type);
        return mapToDto(saved);
    }

    @Override
    @Cacheable(value = "cards", key = "#id")
    public CardDto getCardById(Long id) {
        return mapToDto(cardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Card", "id", id.toString())));
    }

    @Override
    @Cacheable(value = "cards", key = "'num:' + #cardNumber")
    public CardDto getCardByNumber(String cardNumber) {
        return mapToDto(cardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Card", "cardNumber", cardNumber)));
    }

    @Override
    @Cacheable(value = "cardsByCustomer", key = "#customerId")
    public List<CardDto> getCardsByCustomerId(Long customerId) {
        return cardRepository.findByCustomerId(customerId).stream()
                .map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    public List<CardDto> getAllCards() {
        return cardRepository.findAll().stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    @CachePut(value = "cards", key = "#id")
    @CacheEvict(value = "cardsByCustomer", allEntries = true)
    public CardDto blockCard(Long id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Card", "id", id.toString()));
        card.setStatus(CardStatus.BLOCKED);
        Card saved = cardRepository.save(card);

        Map<String, Object> payload = new HashMap<>();
        payload.put("cardNumber", maskCardNumber(saved.getCardNumber()));
        payload.put("status", "BLOCKED");
        eventPublisher.publish(KafkaTopics.CARD_EVENTS, EventType.CARD_BLOCKED,
                id.toString(), "Card", "BLOCK", payload);

        return mapToDto(saved);
    }

    @Override
    @CachePut(value = "cards", key = "#id")
    @CacheEvict(value = "cardsByCustomer", allEntries = true)
    public CardDto activateCard(Long id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Card", "id", id.toString()));
        card.setStatus(CardStatus.ACTIVE);
        Card saved = cardRepository.save(card);

        Map<String, Object> payload = new HashMap<>();
        payload.put("cardNumber", maskCardNumber(saved.getCardNumber()));
        payload.put("status", "ACTIVE");
        eventPublisher.publish(KafkaTopics.CARD_EVENTS, EventType.CARD_ACTIVATED,
                id.toString(), "Card", "ACTIVATE", payload);

        return mapToDto(saved);
    }

    @Override
    @CacheEvict(value = {"cards", "cardsByCustomer"}, allEntries = true)
    public void deleteCard(Long id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Card", "id", id.toString()));
        cardRepository.delete(card);
    }

    private String maskCardNumber(String cardNumber) {
        if (cardNumber.length() > 4) {
            return "****" + cardNumber.substring(cardNumber.length() - 4);
        }
        return cardNumber;
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
