package com.pokerhands.model;

public class Card {
    private final Rank rank;
    private final Suit suit;

    public Card(Rank rank, Suit suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public static Card from(String representation) {
        if (representation == null) {
            throw new IllegalArgumentException("Card representation cannot be null");
        }
        if (representation.length() != 2) {
            throw new IllegalArgumentException("Card must be exactly 2 characters, got: " + representation);
        }

        char rankChar = representation.charAt(0);
        char suitChar = representation.charAt(1);

        Rank rank = Rank.from(rankChar);
        Suit suit = Suit.from(suitChar);

        return new Card(rank, suit);
    }


    public Rank getRank() {
        return rank;
    }

    public Suit getSuit() {
        return suit;
    }

    @Override
    public String toString() {
        return rank.getSymbol() + "" + suit.getSymbol();
    }
}
