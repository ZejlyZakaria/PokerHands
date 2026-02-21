package com.pokerhands.model;


public enum Suit {
    CLUBS('C'),
    DIAMONDS('D'),
    HEARTS('H'),
    SPADES('S');

    private final char symbol;

    Suit(char symbol) {
        this.symbol = symbol;
    }

    public char getSymbol() {
        return symbol;
    }

    public static Suit from(char symbol) {
        for (Suit s : Suit.values()){
            if(s.getSymbol() == symbol){
                return s;
            }
        }
        throw new IllegalArgumentException("Invalid suit symbol: " + symbol);
    }


}
