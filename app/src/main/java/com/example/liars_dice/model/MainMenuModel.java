package com.example.liars_dice.model;

import java.util.Observable;

public class MainMenuModel extends Observable{
    private String gameID;


    public MainMenuModel() {
        this.gameID = "";
    }

    public String getGameID()  {
        return this.gameID;
    }

    public void setGameID(String gameID) {
        if (gameID.length() <= 5) {
            this.gameID = gameID;
            setChanged();
            notifyObservers();
        }
    }

}
