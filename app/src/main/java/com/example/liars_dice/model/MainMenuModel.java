package com.example.liars_dice.model;
import java.util.Observable;

public class MainMenuModel extends Observable{
    //lobbyConnectInfo = new LobbyConnectInfo();
    //lobbyCreateInfo = new LobbyCreateInfo();


    public MainMenuModel() {

    }

    protected void connectToLobby(){    //could take lobbyConnectInfo as argument
        //TODO: add multiplayer module logic (function could return the a GameModel for the lobby)
    }
    protected void createLobby(){ //could take lobbyCreateInfo as argument
        //TODO: add multiplayer model logic (could create a new GameModel)
    }

}
