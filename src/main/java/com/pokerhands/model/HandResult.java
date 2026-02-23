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
}