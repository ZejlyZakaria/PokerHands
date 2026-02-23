package com.pokerhands.evaluator;

import com.pokerhands.model.*;

import java.util.*;

public class HandEvaluator {

    public static HandResult evaluate(Hand hand) {

        Map<Integer, Integer> rankCounts = getRankCounts(hand);
        boolean isFlush = isFlush(hand);

        List<Integer> ascendingValues = getSortedAscendingValues(hand);
        boolean isStraight = isStraightValues(ascendingValues);
        int straightHigh = isStraight ? computeStraightHigh(ascendingValues) : 0;

        if (isFlush && isStraight) {
            return createStraightFlushResult(straightHigh);
        }

        if (hasFourOfAKind(rankCounts)) {
            return createFourOfAKindResult(rankCounts);
        }

        if (hasFullHouse(rankCounts)) {
            return createFullHouseResult(rankCounts);
        }

        if (isFlush) {
            return createFlushResult(hand);
        }

        if (isStraight) {
            return createStraightResult(straightHigh);
        }

        if (hasThreeOfAKind(rankCounts)) {
            return createThreeOfAKindResult(rankCounts);
        }

        if (hasTwoPairs(rankCounts)) {
            return createTwoPairsResult(rankCounts);
        }

        if (hasPair(rankCounts)) {
            return createPairResult(rankCounts);
        }

        return createHighCardResult(hand);
    }

    // ====================== STRAIGHT FLUSH ======================

    private static HandResult createStraightFlushResult(int straightHigh) {
        String description = "Straight Flush : "
                + displayRank(straightHigh)
                + " high";

        return new HandResult(
                HandCategory.STRAIGHT_FLUSH,
                description,
                List.of(straightHigh)
        );
    }

    // ====================== FOUR OF A KIND ======================

    private static boolean hasFourOfAKind(Map<Integer, Integer> rankCounts) {
        return rankCounts.containsValue(4);
    }

    private static HandResult createFourOfAKindResult(Map<Integer, Integer> rankCounts) {

        int quadRank = 0;
        int kicker = 0;

        for (Map.Entry<Integer, Integer> entry : rankCounts.entrySet()) {
            if (entry.getValue() == 4) {
                quadRank = entry.getKey();
            } else if (entry.getValue() == 1) {
                kicker = entry.getKey();
            }
        }

        String description = "Four of a kind: " + displayRankPlural(quadRank);

        return new HandResult(
                HandCategory.FOUR_OF_A_KIND,
                description,
                List.of(quadRank, kicker)
        );
    }

    // ====================== FULL HOUSE ======================

    private static boolean hasFullHouse(Map<Integer, Integer> rankCounts) {
        return rankCounts.containsValue(3) && rankCounts.containsValue(2);
    }

    private static HandResult createFullHouseResult(Map<Integer, Integer> rankCounts) {

        int threeRank = 0;
        int pairRank = 0;

        for (Map.Entry<Integer, Integer> entry : rankCounts.entrySet()) {
            if (entry.getValue() == 3) {
                threeRank = entry.getKey();
            } else if (entry.getValue() == 2) {
                pairRank = entry.getKey();
            }
        }

        String description = "Full House: " + displayRank(threeRank) + " Over " + displayRank(pairRank);

        return new HandResult(
                HandCategory.FULL_HOUSE,
                description,
                List.of(threeRank, pairRank)
        );
    }

    // ====================== FLUSH ======================

    private static HandResult createFlushResult(Hand hand) {

        List<Integer> values = getSortedDescendingValues(hand);
        int highCard = values.get(0);

        String description = "Flush: "
                + displayRank(highCard)
                + " high";

        return new HandResult(
                HandCategory.FLUSH,
                description,
                values
        );
    }

    // ====================== STRAIGHT ======================

    private static HandResult createStraightResult(int straightHigh) {

        String description = "Straight: "
                + displayRank(straightHigh)
                + " high";

        return new HandResult(
                HandCategory.STRAIGHT,
                description,
                List.of(straightHigh)
        );
    }

    // ====================== THREE OF A KIND ======================

    private static boolean hasThreeOfAKind(Map<Integer, Integer> rankCounts) {
        // Excludes full house (handled earlier in evaluation order)
        return rankCounts.containsValue(3) && !rankCounts.containsValue(2);
    }

    private static HandResult createThreeOfAKindResult(Map<Integer, Integer> rankCounts) {

        int threeRank = 0;
        List<Integer> kickers = new ArrayList<>();

        for (Map.Entry<Integer, Integer> entry : rankCounts.entrySet()) {
            if (entry.getValue() == 3) {
                threeRank = entry.getKey();
            } else if (entry.getValue() == 1) {
                kickers.add(entry.getKey());
            }
        }

        kickers.sort(Collections.reverseOrder());

        List<Integer> tieBreakers = new ArrayList<>();
        tieBreakers.add(threeRank);
        tieBreakers.addAll(kickers);

        String description = "Three Of A Kind : " + displayRankPlural(threeRank);

        return new HandResult(
                HandCategory.THREE_OF_A_KIND,
                description,
                tieBreakers
        );
    }

    // ====================== TWO PAIRS ======================

    private static boolean hasTwoPairs(Map<Integer, Integer> rankCounts) {

        int pairCount = 0;

        for (int count : rankCounts.values()) {
            if (count == 2) {
                pairCount++;
            }
        }

        return pairCount == 2;
    }

    private static HandResult createTwoPairsResult(Map<Integer, Integer> rankCounts) {

        List<Integer> pairRanks = new ArrayList<>();
        int kicker = 0;

        for (Map.Entry<Integer, Integer> entry : rankCounts.entrySet()) {
            if (entry.getValue() == 2) {
                pairRanks.add(entry.getKey());
            } else if (entry.getValue() == 1) {
                kicker = entry.getKey();
            }
        }

        pairRanks.sort(Collections.reverseOrder());

        List<Integer> tieBreakers = new ArrayList<>(pairRanks);
        tieBreakers.add(kicker);

        String description = "Two Pair : " + displayRankPlural(pairRanks.get(0)) + " over " + displayRankPlural(pairRanks.get(1));

        return new HandResult(
                HandCategory.TWO_PAIRS,
                description,
                tieBreakers
        );
    }

    // ====================== ONE PAIR ======================

    private static boolean hasPair(Map<Integer, Integer> rankCounts) {

        int pairCount = 0;

        for (int count : rankCounts.values()) {
            if (count == 2) {
                pairCount++;
            }
        }

        return pairCount == 1;
    }

    private static HandResult createPairResult(Map<Integer, Integer> rankCounts) {

        int pairRank = 0;
        List<Integer> kickers = new ArrayList<>();

        for (Map.Entry<Integer, Integer> entry : rankCounts.entrySet()) {
            if (entry.getValue() == 2) {
                pairRank = entry.getKey();
            } else if (entry.getValue() == 1) {
                kickers.add(entry.getKey());
            }
        }

        kickers.sort(Collections.reverseOrder());

        List<Integer> tieBreakers = new ArrayList<>();
        tieBreakers.add(pairRank);
        tieBreakers.addAll(kickers);

        String description = "Pair Of " + displayRankPlural(pairRank);

        return new HandResult(
                HandCategory.PAIR,
                description,
                tieBreakers
        );
    }

    // ====================== HIGH CARD ======================

    private static HandResult createHighCardResult(Hand hand) {
        List<Integer> values = getSortedDescendingValues(hand);
        int highValue = values.get(0);

        return new HandResult(
            HandCategory.HIGH_CARD,
            "High Card: " + displayRank(highValue),
            values
        );
    }

    // ====================== HELPERS ======================

    private static Map<Integer, Integer> getRankCounts(Hand hand) {

        Map<Integer, Integer> rankCounts = new HashMap<>();

        for (Card card : hand.getCards()) {
            int value = card.getRank().getValue();
            rankCounts.put(value, rankCounts.getOrDefault(value, 0) + 1);
        }

        return rankCounts;
    }

    private static boolean isFlush(Hand hand) {

        Suit firstSuit = hand.getCards().get(0).getSuit();

        for (Card card : hand.getCards()) {
            if (card.getSuit() != firstSuit) {
                return false;
            }
        }

        return true;
    }

    private static List<Integer> getSortedDescendingValues(Hand hand) {

        List<Integer> values = new ArrayList<>();

        for (Card card : hand.getCards()) {
            values.add(card.getRank().getValue());
        }

        values.sort(Collections.reverseOrder());
        return values;
    }

    // ====================== STRAIGHT HELPERS ======================

    private static List<Integer> getSortedAscendingValues(Hand hand) {

        List<Integer> values = new ArrayList<>();

        for (Card card : hand.getCards()) {
            values.add(card.getRank().getValue());
        }

        Collections.sort(values);
        return values;
    }

    private static boolean isStraightValues(List<Integer> values) {
        if (new HashSet<>(values).size() != 5) {
            return false;
        }

        // Cas spécial : Roue straight (A-2-3-4-5)
        if (values.equals(List.of(2, 3, 4, 5, 14))) {
            return true;
        }

        for (int i = 0; i < values.size() - 1; i++) {
            if (values.get(i + 1) - values.get(i) != 1) {
                return false;
            }
        }

        return true;
    }

    private static int computeStraightHigh(List<Integer> values) {
        // Pour le cas de la roue straight, the high card used for comparison is 5
        if (values.equals(List.of(2, 3, 4, 5, 14))) {
            return 5;
        }

        return values.get(values.size() - 1);
    }

    private static String displayRank(int value) {
        return switch (value) {
            case 14 -> "Ace";
            case 13 -> "King";
            case 12 -> "Queen";
            case 11 -> "Jack";
            default -> String.valueOf(value);
        };
    }

    private static String displayRankPlural(int rank) {
        return switch (rank) {
            case 14 -> "Aces";
            case 13 -> "Kings";
            case 12 -> "Queens";
            case 11 -> "Jacks";
            default -> String.valueOf(rank); // 2-10 restent inchangés
        };
    }
}