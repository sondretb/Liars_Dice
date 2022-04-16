package com.example.liars_dice.model;
import com.example.liars_dice.ServerAPI;

import java.util.Observable;

public class MainMenuModel extends Observable{
    //lobbyConnectInfo = new LobbyConnectInfo();
    //lobbyCreateInfo = new LobbyCreateInfo();
    private ServerAPI serverAPI;


    public MainMenuModel() {
        this.serverAPI = ServerAPI.getInstance();
    }

    public void connectToLobby(){    //could take lobbyConnectInfo as argument
        //TODO: add multiplayer module logic (function could return the a GameModel for the lobby)
        this.serverAPI.joinLobby("LOBBY_ID");
    }

    public void createLobby(){ //could take lobbyCreateInfo as argument
        //TODO: add multiplayer model logic (could create a new GameModel)
        this.serverAPI.createLobby();
    }

}
