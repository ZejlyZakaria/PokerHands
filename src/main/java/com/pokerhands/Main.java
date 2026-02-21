package com.pokerhands;

import com.pokerhands.model.Card;
import com.pokerhands.model.Hand;
import com.pokerhands.model.Rank;
import com.pokerhands.model.Suit;

public class Main {
    public static void main(String[] args) {
        Hand hand = Hand.from("2H AS TD 5C KH");

        System.out.println("Main originale :");
        System.out.println(hand);

        Hand sortedHand = hand.sorted();

        System.out.println("\nMain triée (valeur décroissante) :");
        System.out.println(sortedHand);
    }
}