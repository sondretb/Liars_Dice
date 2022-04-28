package com.example.liars_dice.api;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;

public class LobbyManagerAPI {
    private static LobbyManagerAPI INSTANCE = null;

    private Socket socket = null;

    public static LobbyManagerAPI getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LobbyManagerAPI();
        }
        return INSTANCE;
    }

    public void connect() {
        System.out.println("CONNECTING!!!");
        try {
            System.out.println("TRYING!!!");
            socket = IO.socket("http://10.0.2.2:3000/lobbies");
            System.out.println("DONE TRYING!!!");
        } catch (URISyntaxException e) {
            System.out.println("ERROR!!!");
            e.printStackTrace();
        }
        System.out.println("CONNECTING FOR REALZ!!!");
        socket.connect();
        System.out.println("DONE CONNECTING FOR REALZ!!!");
    }



    public boolean connected() {
        return socket.connected();
    }

    public void create(Ack ack) {
        if (socket.connected()) {
            socket.emit("lobby:create", new JSONObject(), ack);
        }
    }

    public void join(String lobbyID, Ack ack) {
        if (socket.connected()) {
            JSONObject data = new JSONObject();
            try {
                data.put("lobbyID", lobbyID);
                socket.emit("lobby:join", data, ack);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
