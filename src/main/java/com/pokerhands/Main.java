package com.pokerhands;

import com.pokerhands.model.*;
import com.pokerhands.evaluator.HandEvaluator;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Exemple 1 : Four of a Kind (quatre Rois + As)
        Hand hand1 = Hand.from("KH KS KC KD AH");
        HandResult result1 = HandEvaluator.evaluate(hand1);
        System.out.println("Hand 1: " + hand1);
        System.out.println("Category: " + result1.getCategory().getDisplayName());
        System.out.println("Description: " + result1.getDescription());
        System.out.println("TieBreakers: " + result1.getTieBreakerRanks());
        System.out.println();

        // Exemple 2 : Full House (Brelan de 4 sur paire de 2)
        Hand hand2 = Hand.from("TH TS AS AC TD");
        HandResult result2 = HandEvaluator.evaluate(hand2);
        System.out.println("Hand 2: " + hand2);
        System.out.println("Category: " + result2.getCategory().getDisplayName());
        System.out.println("Description: " + result2.getDescription());
        System.out.println("TieBreakers: " + result2.getTieBreakerRanks());
        System.out.println();

        // Exemple 3 : Straight (A-2-3-4-5)
        Hand hand3 = Hand.from("AH 2D 3S 4C 5H");
        HandResult result3 = HandEvaluator.evaluate(hand3);
        System.out.println("Hand 3: " + hand3);
        System.out.println("Category: " + result3.getCategory().getDisplayName());
        System.out.println("Description: " + result3.getDescription());
        System.out.println("TieBreakers: " + result3.getTieBreakerRanks());
        System.out.println();

        // Exemple 4 : High Card
        Hand hand4 = Hand.from("2H 5D 7S 8C AH");
        HandResult result4 = HandEvaluator.evaluate(hand4);
        System.out.println("Hand 4: " + hand4);
        System.out.println("Category: " + result4.getCategory().getDisplayName());
        System.out.println("Description: " + result4.getDescription());
        System.out.println("TieBreakers: " + result4.getTieBreakerRanks());

        // Exemple 5 : Two Pair
        Hand hand5 = Hand.from("9H TH JH QH KH");
        HandResult result5 = HandEvaluator.evaluate(hand5);
        System.out.println("Hand 5: " + hand5);
        System.out.println("Category: " + result5.getCategory().getDisplayName());
        System.out.println("Description: " + result5.getDescription());
        System.out.println("TieBreakers: " + result5.getTieBreakerRanks());
    }
}