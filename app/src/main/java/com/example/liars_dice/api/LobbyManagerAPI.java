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
            socket = IO.socket("http://10.0.2.2:3000/lobbies");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        socket.connect();
    }



    public boolean connected() {
        if (socket == null) return false;
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
                data.put("id", lobbyID);
                socket.emit("lobby:join", data, ack);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
