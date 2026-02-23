package com.pokerhands.parser;

import com.pokerhands.evaluator.HandEvaluator;
import com.pokerhands.model.Hand;
import com.pokerhands.model.HandResult;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class InputParser {
    public static String processLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return "Invalid input";
        }

        String trimmed = line.trim();
        String[] parts = trimmed.split("\\s+");

        if (parts.length != 12 || !parts[0].equals("Black:") || !parts[6].equals("White:")) {
            return "Invalid format";
        }

        // Vérification des doublons dans les deux mains
        Set<String> allCards = new HashSet<>();
        for (int i = 1; i <= 5; i++) {
            String card = parts[i];
            if (!allCards.add(card)) {
                return "Invalid input: duplicate card " + card;
            }
        }
        for (int i = 7; i <= 11; i++) {
            String card = parts[i];
            if (!allCards.add(card)) {
                return "Invalid input: duplicate card " + card;
            }
        }

        // Extraction
        String blackCards = String.join(" ", Arrays.copyOfRange(parts, 1, 6));
        String whiteCards = String.join(" ", Arrays.copyOfRange(parts, 7, 12));

        try {
            Hand blackHand = Hand.from(blackCards);
            Hand whiteHand = Hand.from(whiteCards);

            HandResult blackResult = HandEvaluator.evaluate(blackHand);
            HandResult whiteResult = HandEvaluator.evaluate(whiteHand);

            return HandResult.whoWins(blackResult, whiteResult);
        } catch (IllegalArgumentException e) {
            return "Invalid card: " + e.getMessage();
        }
    }
}
