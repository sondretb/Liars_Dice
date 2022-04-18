package com.example.liars_dice.model;

import com.example.liars_dice.model.game.GameTable;
import com.example.liars_dice.model.game.Bet;
import com.example.liars_dice.model.game.Player;

import java.util.Collection;
import java.util.Observable;

public class GameModel extends Observable{
    public GameModel(int playerAmount, Collection<Player> playersJoining){
        gameTable = new GameTable(playerAmount);
        for (Player player:
             playersJoining) {
            gameTable.addPlayer(player);
        }
    }
    private GameTable gameTable;

    public void setBet(int diceValue, int diceAmont){
        gameTable.getPlayerInTurn().setBet(diceValue, diceAmont);
        gameTable.nextTurn();
    }

    public Boolean callLie(){
        Bet lastBet = gameTable.getStandingBet();
        Player loosingPlayer;
        Boolean wasLie = lastBet.getBetAmount() < gameTable.countDice(lastBet.getBetValue());

        if (wasLie){
            loosingPlayer = gameTable.lastPlayer();
        }
        else{
            loosingPlayer = gameTable.getPlayerInTurn();
        }
        loosingPlayer.removeDice();
        if (loosingPlayer.isDead()){
            gameTable.nextTurn();
        }
        else{
            gameTable.setTurn(loosingPlayer);
        }
        return wasLie;
    }

    public void startNewRound(){
        gameTable.clearBets();
        gameTable.rerollAll();
    }

}
