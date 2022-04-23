package com.example.liars_dice.model;

import com.example.liars_dice.model.game.GameTable;
import com.example.liars_dice.model.game.Bet;
import com.example.liars_dice.model.game.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Observable;

public class GameModel extends Observable{          //might merge GameTable and GameModel, and make the GameTable functions private.
    private GameTable gameTable;
    private HashMap<String, Boolean> ruleMap;   //for the rules chosen in lobby creation


    public GameModel(int playerAmount, Collection<String> playersJoining, int playerLives){          // how players are added to the model could change depending on how the lobby is implemented
        gameTable = new GameTable(playerAmount);                                                    // Presuming Names can be an identifier of the players, subject to change.
        for (String playerName:
             playersJoining) {
            gameTable.addPlayer(new Player(playerName, playerLives));
        }
    }

    public void setBet(int diceValue, int diceAmount){
        gameTable.getPlayerInTurn().setBet(diceValue, diceAmount);
        gameTable.nextTurn();
    }

    public Boolean callLie(){       //might change to a wasLie() and handleResult(Boolean wasLie) function instead
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
            gameTable.removePlayer(loosingPlayer);
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

    public int getSmallestLegalBetAmount(int chosenDiceValue) {
        if (gameTable.getStandingBet().getBetValue() >= chosenDiceValue){
            return gameTable.getStandingBet().getBetAmount()+1;
        }
        else{
            return gameTable.getStandingBet().getBetAmount();
        }
    }
}
