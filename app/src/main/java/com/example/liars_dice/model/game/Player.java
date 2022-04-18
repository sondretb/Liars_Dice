package com.example.liars_dice.model.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;


public class Player {
    private final String name;
    private Bet lastBet;
    private int remainingAmountOfDice;
    private ArrayList<Integer> diceOnHand;

    public Player(String name, int lives){
        this.name = name;
        this.lastBet = null;
        this.remainingAmountOfDice = lives;
        this.diceOnHand = new ArrayList<>();
    }

    public void setBet(int diceValue, int diceAmount){
        if (this.lastBet == null){
            this.lastBet = new Bet(diceValue, diceAmount);
        }
        else{
            this.lastBet.setBetAmount(diceAmount);
            this.lastBet.setBetValue(diceValue);
        }
    }
    public void removeDice(){
        this.remainingAmountOfDice--;
    }
    public Boolean isDead(){
        return (this.getRemainingAmountOfDice() <= 0);
    }

    public String getName() {
        return name;
    }

    public void clearBet(){
        lastBet = null;
    }

    public Bet getLastBet() {
        return lastBet;
    }
    public ArrayList<Integer> getDiceOnHand(){
        return diceOnHand;
    }

    public void rollDice(){
        diceOnHand.clear();
        for (int i = 0; i < remainingAmountOfDice; i++) {
            diceOnHand.add(ThreadLocalRandom.current().nextInt(1, 7));
        }
    }

    public void rollDice(int diceMax){              //in case we add optional larger dice
        diceOnHand.clear();
        for (int i = 0; i < remainingAmountOfDice; i++) {
            diceOnHand.add(ThreadLocalRandom.current().nextInt(1, diceMax+1));
        }
    }

    public int getRemainingAmountOfDice() {
        return remainingAmountOfDice;
    }
    public int countDiceOfValue(int diceInQuestion){
        return Collections.frequency(diceOnHand, diceInQuestion);
    }
}
