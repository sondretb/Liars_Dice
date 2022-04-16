package com.example.liars_dice;

import android.util.Log;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class ServerAPI {
    private static ServerAPI INSTANCE = null;

    private Socket mSocket = null;

    private ServerAPI() {
        connectToServer();
    }

    public static ServerAPI getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ServerAPI();
        }
        return INSTANCE;
    }

    private boolean connectToServer() {
        Log.d("connecting", "server");
        try {
            mSocket = IO.socket("http://10.0.2.2:3000");
        }
        catch (URISyntaxException e) {
            // TODO: Handle exception
            Log.d("error", "unable to connect");
            return false;
        }
        Log.d("connecting", "socket");
        mSocket.connect();
        return true;
    }

    public boolean createLobby() {
        if (mSocket.connected()) {
            Log.d("creating", "lobby");
            return true;
        }
        return false;
    }

    public boolean joinLobby(String lobbyID) {
        if (mSocket.connected()) {
            Log.d("joining", "lobby: " + lobbyID);
            return true;
        }
        return false;
    }
}
