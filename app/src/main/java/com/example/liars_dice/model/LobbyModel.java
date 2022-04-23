package com.example.liars_dice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Observable;

public class LobbyModel extends Observable {
    private String id;
    private HashMap<String, LobbyPlayerModel> players;

    public LobbyModel(String id) {
        this.id = id;
        this.players = new HashMap<String, LobbyPlayerModel>();
    }

    public LobbyModel(@JsonProperty("id") String id, @JsonProperty("players") HashMap<String, LobbyPlayerModel> players) {
        this.id = id;
        this.players = players;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        setChanged();
        notifyObservers();
    }

    public HashMap<String, LobbyPlayerModel> getPlayers() {
        return players;
    }

    public void setPlayers(HashMap<String, LobbyPlayerModel> players) {
        this.players = players;
        setChanged();
        notifyObservers();
    }

    public void setLobbyModel(LobbyModel lobbyModel) {
        System.out.println("SETTING LOBBY MODEL: "+lobbyModel.getId() + " " + lobbyModel.getPlayers().toString());
        this.id = lobbyModel.getId();
        this.players = lobbyModel.getPlayers();
        setChanged();
        notifyObservers();
    }

    static public LobbyModel fromJSON(String json) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        LobbyModel lobbyModel = objectMapper.readValue(json, LobbyModel.class);
        return lobbyModel;
    }
}
