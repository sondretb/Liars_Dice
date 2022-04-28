package com.example.liars_dice.model.game;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Bet {
    private int value;
    private int amount;

    public Bet(
            @JsonProperty("value") int value,
            @JsonProperty("amount") int amount) {
        this.value = value;
        this.amount = amount;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
