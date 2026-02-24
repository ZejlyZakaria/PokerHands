package com.pokerhands.model;

import java.util.List;

import static com.pokerhands.model.Rank.displayRank;
import static com.pokerhands.model.Rank.displayRankPlural;

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
        int cmp = compare(black, white);

        if (cmp == 0) {
            return "Tie.";
        }

        HandResult winner = cmp > 0 ? black : white;
        HandResult loser = cmp > 0 ? white : black;

        // On récupère les tiebreakers du gagnant et du perdant
        List<Integer> wRanks = winner.getTieBreakerRanks();
        List<Integer> lRanks = loser.getTieBreakerRanks();

        // Recherche de la première différence (utilisée pour les cas dynamiques)
        int diffIndex = -1;
        for (int i = 0; i < Math.min(wRanks.size(), lRanks.size()); i++) {
            if (!wRanks.get(i).equals(lRanks.get(i))) {
                diffIndex = i;
                break;
            }
        }

        String message;

        // Cas spécial 1 : HIGH_CARD
        if (winner.getCategory() == HandCategory.HIGH_CARD) {
            if (diffIndex == -1) return "Tie."; // égalité parfaite
            int winningValue = wRanks.get(diffIndex);
            String diffDisplay = displayRank(winningValue);
            message = "high card: " + diffDisplay;
        }

        // Cas spécial 2 : PAIR
        else if (winner.getCategory() == HandCategory.PAIR) {
            int pairValue = wRanks.get(0); // la paire est toujours en premier
            String pairDisplay = displayRankPlural(pairValue);

            if (diffIndex == 0) {
                // Différence sur la paire elle-même (rare car catégorie même)
                message = "pair: " + pairDisplay;
            } else {
                // Différence sur un kicker
                int kicker = wRanks.get(diffIndex);
                String kickerDisplay = displayRank(kicker);
                message = "pair: " + pairDisplay + ", " + kickerDisplay + " kicker";
            }
        }

        // Cas spécial 3 : TWO_PAIRS
        else if (winner.getCategory() == HandCategory.TWO_PAIRS) {
            int highPair = wRanks.get(0);
            int lowPair = wRanks.get(1);
            String highDisplay = displayRankPlural(highPair);
            String lowDisplay = displayRankPlural(lowPair);

            if (diffIndex == 0) {
                // Différence sur la paire haute
                message = "two pairs: " + highDisplay + " and " + lowDisplay;
            } else if (diffIndex == 1) {
                // Différence sur la paire basse
                message = "two pairs: " + highDisplay + " and " + lowDisplay;
            } else {
                // Différence sur le kicker
                int kicker = wRanks.get(diffIndex);
                String kickerDisplay = displayRank(kicker);
                message = "two pairs: " + highDisplay + " and " + lowDisplay + ", " + kickerDisplay + " kicker";
            }
        }

        // Tous les autres cas → description pré-calculée
        else {
            message = winner.getDescription();
        }

        return (cmp > 0 ? "Black" : "White") + " wins. - with " + message;
    }

}