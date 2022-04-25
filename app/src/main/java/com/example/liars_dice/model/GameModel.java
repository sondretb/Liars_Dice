package com.example.liars_dice.model;

import com.example.liars_dice.model.game.GamePlayer;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;

public class GameModel extends Observable {
    private String id;
    private HashMap<String, GamePlayer> players;
    private ArrayList<String> turnList;
    private int currentTurn;

    public GameModel(String id) {
        this.id = id;
        this.players = new HashMap<String, GamePlayer>();
        this.turnList = new ArrayList<String>();
        currentTurn = 0;
    }

    public GameModel(
        @JsonProperty("id") String id,
        @JsonProperty("players") HashMap<String, GamePlayer> players,
        @JsonProperty("turnList") ArrayList<String> turnList,
        @JsonProperty("currentTurn") int currentTurn
    ) {
        this.id = id;
        this.players = players;
        this.turnList = turnList;
        this.currentTurn = currentTurn;
    }

    public String getId() {
        return id;
    }

    public HashMap<String, GamePlayer> getPlayers() {
        return players;
    }

    public ArrayList<String> getTurnList() {
        return turnList;
    }

    public int getCurrentTurn() {
        return currentTurn;
    }

    public void setGameModel(GameModel gameModel) {
        this.id = gameModel.getId();
        this.players = gameModel.getPlayers();
        this.turnList = gameModel.getTurnList();
        this.currentTurn = gameModel.getCurrentTurn();
        setChanged();
        notifyObservers();
    }

    public GamePlayer getCurrentPlayer() {
        if (turnList.size() > 0) {
            String currentPlayerID = this.turnList.get(currentTurn);
            GamePlayer currentPlayer = this.players.get(currentPlayerID);
            return currentPlayer;
        }
        return null;
    }

    static public GameModel fromJSON(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        GameModel gameModel = objectMapper.readValue(json, GameModel.class);
        return gameModel;
    }
}

/*
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
*/