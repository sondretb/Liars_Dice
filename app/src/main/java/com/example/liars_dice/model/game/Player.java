package com.example.liars_dice.model.game;

public class Player {
    private final String name;
    private Bet lastBet;
    private int remainingDice;

    public Player(String name, int lives){
        this.name = name;
        this.lastBet = null;
        this.remainingDice = lives;
    }

    public void setBet(int diceValue, int diveAmount){
        if (this.lastBet == null){
            this.lastBet = new Bet(diceValue, diveAmount);
        }
        else{
            this.lastBet.setBetAmount(diveAmount);
            this.lastBet.setBetValue(diceValue);
        }
    }
    public void removeDice(){
        this.remainingDice--;
    }
    public Boolean isDead(){
        return (this.getRemainingDice() <= 0);
    }

    public String getName() {
        return name;
    }

    public Bet getLastBet() {
        return lastBet;
    }

    public int getRemainingDice() {
        return remainingDice;
    }
}
