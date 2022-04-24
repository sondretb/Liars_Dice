package com.example.liars_dice.api;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.function.Function;

import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


/* ServerAPI should be a singleton use ServerAPI.getInstance() to initialize instead of new ServerAPI()*/
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
        mSocket.connect().emit("createlobby");
        return true;
    }
    /* TODO: CreateLobby */
    public boolean createRoom(Ack ack) {
        if (mSocket.connected()) {
            mSocket.emit("room:create", new JSONObject(), ack);
            return true;
        }
        return false;
    }

    /* TODO: JoinLobby */
    public void joinRoom(String roomID, Ack ack) {
        if (mSocket.connected()) {
            JSONObject data = new JSONObject();
            try {
                data.put("roomID", roomID);
                mSocket.emit("room:join", data, ack);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void subscribeRoomState(Emitter.Listener listener) {
        if (mSocket.connected()) {
            mSocket.on("room:state", listener);
        }
    }

    public void unsubscribeRoomState(Emitter.Listener listener) {
        mSocket.off("room:state", listener);
    }

    public void getRoomState(String roomID, Ack ack) {
        if (mSocket.connected()) {
            JSONObject data = new JSONObject();
            System.out.println("Trying to get "+roomID);
            try {
                data.put("roomID", roomID);
                mSocket.emit("room:state",data, ack);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
