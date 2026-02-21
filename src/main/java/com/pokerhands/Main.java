package com.pokerhands;

import com.pokerhands.model.Card;
import com.pokerhands.model.Rank;
import com.pokerhands.model.Suit;

public class Main {
    public static void main(String[] args) {

        Card C1 = new Card(Rank.ACE, Suit.HEARTS);
        System.out.println(C1);
    }
}