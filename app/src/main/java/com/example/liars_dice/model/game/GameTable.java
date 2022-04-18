package com.example.liars_dice.model.game;

import java.util.ArrayList;

public class GameTable {
    private ArrayList<Player> players;
    private int turnIndex;

    public GameTable(int playerAmount) {
        this.players = new ArrayList<>(playerAmount);
        turnIndex = 0;
    }

    public Player getPlayerInTurn() {
        return players.get(turnIndex);
    }

    public void nextTurn() {
        this.turnIndex = (this.turnIndex + 1) % players.size();
    }

    public void setTurn(Player player){
        turnIndex = players.indexOf(player);
    }
    public void addPlayer(Player player){
        this.players.add(player);
    }
    public Bet getStandingBet(){
        return players.get((turnIndex-1)%players.size()).getLastBet();
    }
}
