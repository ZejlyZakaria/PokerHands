package com.pokerhands.model;

import java.util.List;

public class HandResult {

    private final HandCategory category;
    private final String description;
    private final List<Integer> tieBreakerRanks;

    public HandResult(HandCategory category, String description, List<Integer> tieBreakerRanks) {
        this.category = category;
        this.description = description;
        this.tieBreakerRanks = List.copyOf(tieBreakerRanks);
    }

    public HandCategory getCategory() { return category; }
    public String getDescription() { return description; }
    public List<Integer> getTieBreakerRanks() { return tieBreakerRanks; }

    public static int compare(HandResult a, HandResult b) {

        // Comparaison par catégorie
        if (a.getCategory().getStrength() > b.getCategory().getStrength()) return 1;
        if (a.getCategory().getStrength() < b.getCategory().getStrength()) return -1;


        // Catégories identiques : on compare les tiebreakers
        List<Integer> ranksA = a.getTieBreakerRanks();
        List<Integer> ranksB = b.getTieBreakerRanks();

        for (int i = 0; i < Math.min(ranksA.size(), ranksB.size()); i++) {
            if (ranksA.get(i) > ranksB.get(i)) return 1;
            if (ranksA.get(i) < ranksB.get(i)) return -1;
        }

        // Si tous les valeurs sont identiques
        return 0;
    }


    public static String whoWins(HandResult black, HandResult white) {
        int result = compare(black, white);

        if (result > 0) {
            return "Black wins - with " + black.getDescription();
        }
        else if (result < 0) {
            return "White wins - with " + white.getDescription();
        }
        else {
            return "Tie";
        }
    }
}