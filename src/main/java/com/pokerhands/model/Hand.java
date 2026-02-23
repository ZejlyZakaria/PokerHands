package com.pokerhands.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Hand {
    private final List<Card> cards;

    public Hand(List<Card> cards) {
        if (cards == null) {
            throw new IllegalArgumentException("Cards list cannot be null");
        }
        if (cards.size() != 5) {
            throw new IllegalArgumentException("A Hand must contain exactly 5 cards, got " + cards.size());
        }
        this.cards = List.copyOf(cards);
    }

    public List<Card> getCards() {
        return cards;
    }

    @Override
    public String toString() {
        return String.join(" ", cards.stream()
                .map(Card::toString)
                .toList());
    }

    public Hand sorted() {
        List<Card> sortedCards = new ArrayList<>(cards);

        sortedCards.sort((card1, card2) ->
                card2.getRank().getValue() - card1.getRank().getValue()
        );

        return new Hand(sortedCards);
    }

    // Factory statique
    public static Hand from(String line) {
        if(line == null)  throw new IllegalArgumentException("Hand line cannot be null");

        String[] parts = line.trim().split("\\s+");

        if (parts.length != 5) {
            throw new IllegalArgumentException(
                    "A hand must contain exactly 5 cards, got " + parts.length + ": " + line
            );
        }

        List<Card> cardList = new ArrayList<>();
        Set<String> seen = new HashSet<>();
        for (String part : parts){
            Card card = Card.from(part);
            if (!seen.add(part)) {
                throw new IllegalArgumentException("Duplicate card in hand: " + part);
            }
            cardList.add(card);
        }

        return new Hand(cardList);
    }
}
