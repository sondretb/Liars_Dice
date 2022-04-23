package com.example.liars_dice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.liars_dice.adapters.LobbyPlayerViewAdapter;
import com.example.liars_dice.api.LobbyAPI;
import com.example.liars_dice.model.LobbyModel;
import com.example.liars_dice.model.LobbyPlayerModel;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class LobbyActivity extends AppCompatActivity implements Observer, View.OnClickListener{
    /* Model */
    private LobbyModel model;

    /* API */
    private LobbyAPI lobbyAPI;

    /* View */
    private Button readyButton;
    private LobbyPlayerViewAdapter playerAdapter;
    private RecyclerView recyclerView;
    private TextView gameID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        /* Model */
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String id = extras.getString("id");
            this.model = new LobbyModel(id);
        }
        this.model.addObserver(this);

        /* API stuff */
        this.lobbyAPI = LobbyAPI.getInstance();
        this.lobbyAPI.connect(model.getId());
        this.lobbyAPI.on(LobbyAPI.LobbyEvent.UPDATE, args -> {
            try {
                LobbyModel lobbyModel = LobbyModel.fromJSON(args[0].toString());
                this.model.setLobbyModel(lobbyModel);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        this.lobbyAPI.on(LobbyAPI.LobbyEvent.DISCONNECT, args -> {
            moveToMainMenu();
        });

        /* View stuff */
        this.gameID = findViewById(R.id.textViewLobbyID);
        this.gameID.setText("GameID: " + this.model.getId());
        this.recyclerView = findViewById(R.id.rvLobbyPlayers);
        this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<LobbyPlayerModel> players = new ArrayList<>(model.getPlayers().values());
        this.playerAdapter = new LobbyPlayerViewAdapter(players);
        this.recyclerView.setAdapter(this.playerAdapter);
        this.readyButton = findViewById(R.id.readyButton);
        this.readyButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.readyButton) :
                this.lobbyAPI.toggleReady();
                break;
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        ArrayList<LobbyPlayerModel> players = new ArrayList<>(model.getPlayers().values());
        String id = this.model.getId();
        runOnUiThread(() -> {
            playerAdapter.setPlayers(players);
            gameID.setText("GameID: " + id);
        });
    }

    private void moveToMainMenu() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}