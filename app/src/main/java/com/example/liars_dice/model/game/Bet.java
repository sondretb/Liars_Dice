package com.example.liars_dice.model.game;

public class Bet {
    private int betValue;
    private int betAmount;

    public Bet(int betValue, int betAmount) {
        this.betValue = betValue;
        this.betAmount = betAmount;
    }

    public int getBetValue() {
        return betValue;
    }

    public void setBetValue(int betValue) {
        this.betValue = betValue;
    }

    public int getBetAmount() {
        return betAmount;
    }

    public void setBetAmount(int betAmount) {
        this.betAmount = betAmount;
    }
}
