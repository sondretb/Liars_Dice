package com.example.liars_dice.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LobbyPlayerModel {
    private String id;
    private Boolean ready;

    public LobbyPlayerModel(String id) {
        this.id = id;
        this.ready = false;
    }

    public LobbyPlayerModel(@JsonProperty("id") String id, @JsonProperty("ready") boolean ready) {
        this.id = id;
        this.ready = ready;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getReady() {
        return ready;
    }

    public void setReady(Boolean ready) {
        this.ready = ready;
    }
}
