package com.example.liars_dice.api;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class LobbyAPI {
    private static LobbyAPI INSTANCE = null;

    public enum LobbyEvent {
        UPDATE("lobby:state:update"),
        DISCONNECT("disconnect"),
        READY("lobby:ready:toggle"),
        ALLREADY("lobby:game:start");

        private final String text;

        LobbyEvent(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return this.text;
        }
    }

    private Socket socket = null;

    public static LobbyAPI getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LobbyAPI();
        }
        return INSTANCE;
    }

    public void connect(String id) {
        System.out.println("Connecting with id " + id);
        try {
            socket = IO.socket("http://10.0.2.2:3000/lobbies/"+id);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        socket.connect();
    }

    public void on(LobbyEvent event, Emitter.Listener listener) {
        this.socket.on(event.toString(), listener);
    }

    public void off(LobbyEvent event, Emitter.Listener listener) {
        this.socket.off(event.toString(), listener);
    }

    public void toggleReady() {
        this.socket.emit("lobby:ready:toggle");
    }
}
