package com.pokerhands.model;

public enum HandCategory {

    HIGH_CARD(1, "high card"),
    PAIR(2, "pair"),
    TWO_PAIRS(3, "two pairs"),
    THREE_OF_A_KIND(4, "three of a kind"),
    STRAIGHT(5, "straight"),
    FLUSH(6, "flush"),
    FULL_HOUSE(7, "full house"),
    FOUR_OF_A_KIND(8, "four of a kind"),
    STRAIGHT_FLUSH(9, "straight flush");

    private final int strength;
    private final String displayName;

    HandCategory(int strength, String displayName) {
        this.strength = strength;
        this.displayName = displayName;
    }

    public int getStrength() {
        return strength;
    }

    public String getDisplayName() {
        return displayName;
    }
}
