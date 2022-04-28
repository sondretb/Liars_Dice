package com.example.liars_dice.api;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class GameAPI {
    private static GameAPI INSTANCE = null;

    public enum Event {
        DISCONNECT("disconnect"),
        UPDATE("game:update"),
        LOST("game:lost"),
        WON("game:won");


        private final String text;

        Event(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return this.text;
        }
    }

    private Socket socket = null;

    public static GameAPI getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GameAPI();
        }
        return INSTANCE;
    }

    public void connect(String id) {
        System.out.println("Connecting with id " + id);
        try {
            socket = IO.socket("http://10.0.2.2:3000/games/"+id);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        socket.connect();
    }

    public void on(Event event, Emitter.Listener listener) {
        this.socket.on(event.toString(), listener);
    }

    public void off(Event event, Emitter.Listener listener) {
        this.socket.off(event.toString(), listener);
    }

    public void toggleReady() {
        this.socket.emit("lobby:toggleReady");
    }

    public String getID() {
        return this.socket.id();
    }

    public void bet(Integer amount, Integer value) {
        JSONObject data = new JSONObject();
        try {
            data.put("amount", amount);
            data.put("value", value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.socket.emit("game:bet",data);
    }

    public void call() {
        this.socket.emit("game:call");
    }
}
