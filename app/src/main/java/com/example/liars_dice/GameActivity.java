package com.example.liars_dice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.liars_dice.adapters.DiceViewAdapter;
import com.example.liars_dice.adapters.GamePlayerViewAdapter;
import com.example.liars_dice.api.GameAPI;
import com.example.liars_dice.model.GameModel;
import com.example.liars_dice.model.game.Bet;
import com.example.liars_dice.model.game.Dice;
import com.example.liars_dice.model.game.GamePlayer;
import com.fasterxml.jackson.core.JsonProcessingException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class GameActivity extends AppCompatActivity implements Observer, View.OnClickListener {
    /* Model */
    private GameModel model;

    /* API */
    private GameAPI gameAPI;

    /* View */

    private RecyclerView rvOpponents;
    private GamePlayerViewAdapter rvOpponentsAdapter;

    private TextView tvTurnIndicator;
    private TextView tvPlayerID;
    private TextView tvPlayerBet;
    private RecyclerView rvPlayerDice;
    private DiceViewAdapter rvPlayerDiceAdapter;

    private EditText etDiceNumber;
    private EditText etDiceValue;
    private Button betButton;
    private Button callButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        /* Model */
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String id = extras.getString("id");
            this.model = new GameModel(id);
        }
        this.model.addObserver(this);

        /* API stuff */
        this.gameAPI = gameAPI.getInstance();
        this.gameAPI.connect(model.getId());
        this.gameAPI.on(GameAPI.Event.UPDATE , args -> {
            try {
                System.out.println("UPDATA DATA: "+args[0].toString());
                JSONObject result = new JSONObject(args[0].toString());
                JSONObject data = result.getJSONObject("data");
                GameModel gameModel = GameModel.fromJSON(data.toString());
                this.model.setGameModel(gameModel);
            } catch (JsonProcessingException | JSONException e) {
                e.printStackTrace();
            }
        });
        this.gameAPI.on(GameAPI.Event.DISCONNECT, args -> {
            moveToMainMenu();
        });

        this.gameAPI.on(GameAPI.Event.LOST, args -> {
            moveToLossScreen();
        });

        this.gameAPI.on(GameAPI.Event.WON, args -> {
            moveToWinScreen();
        });

        /* View stuff */
        this.rvOpponents = findViewById(R.id.rvOpponents);
        this.rvOpponents.setLayoutManager(new LinearLayoutManager(this));
        ArrayList<GamePlayer> players = new ArrayList<GamePlayer>(this.model.getPlayers().values());
        this.rvOpponentsAdapter = new GamePlayerViewAdapter(players);
        this.rvOpponents.setAdapter(rvOpponentsAdapter);
        this.tvTurnIndicator = findViewById(R.id.tvTurnIndicator);
        this.tvTurnIndicator.setText("Waiting for players to join");
        this.tvPlayerID = findViewById(R.id.tvPlayerID);
        this.tvPlayerID.setText("You");
        this.tvPlayerBet = findViewById(R.id.tvPlayerBet);
        this.tvPlayerBet.setText("No Bet");
        this.rvPlayerDice = findViewById(R.id.rvPlayerDice);
        this.rvPlayerDice.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        this.rvPlayerDiceAdapter = new DiceViewAdapter(new ArrayList<Dice>());
        this.rvPlayerDice.setAdapter(this.rvPlayerDiceAdapter);
        this.etDiceNumber = findViewById(R.id.editTextDiceNumber);
        this.etDiceValue = findViewById(R.id.editTextDiceValue);

        this.betButton = findViewById(R.id.betButton);
        this.betButton.setOnClickListener(this);
        this.betButton.setEnabled(false);

        this.callButton = findViewById(R.id.callButton);
        this.callButton.setOnClickListener(this);
        this.callButton.setEnabled(false);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case (R.id.betButton):
                System.out.println("Do bet stuff");
                Integer amount = Integer.parseInt(this.etDiceNumber.getText().toString());
                Integer value = Integer.parseInt(this.etDiceValue.getText().toString());
                gameAPI.bet(amount,value);
                break;
            case (R.id.callButton):
                System.out.println("Do call stuff");
                gameAPI.call();
                break;
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        HashMap<String, GamePlayer> playerHashMap = new HashMap<String,GamePlayer>(model.getPlayers());
        String myID = gameAPI.getID();
        GamePlayer me = playerHashMap.get(gameAPI.getID());
        Bet myLastBet = me.getLastBet();
        ArrayList<Dice> myDice = me.getDice();

        playerHashMap.remove(myID);
        ArrayList<GamePlayer> players = new ArrayList<GamePlayer>(playerHashMap.values());


        GamePlayer currentPlayer = model.getCurrentPlayer();
        String currentPlayerID = currentPlayer != null ? currentPlayer.getId() : null ;
        Integer prevPlayerIndex = (model.getCurrentTurn() - 1 + model.getTurnList().size()) % model.getTurnList().size();
        String prevPlayerID = (model.getTurnList().get(prevPlayerIndex));
        GamePlayer prevPlayer = model.getPlayers().get(prevPlayerID);
        runOnUiThread(() -> {
            if (currentPlayerID != null) {
                this.tvTurnIndicator.setText(currentPlayerID.equals(myID) ? "Your turn!" : "Waiting for opponents...");
            }
            if (currentPlayer != null && currentPlayerID.equals(myID)) {
                this.betButton.setEnabled(true);
                // TODO: if previous player has made a bet activte call button
                if (prevPlayer.getLastBet() != null) {
                    this.callButton.setEnabled(true);
                }
            }
            else {
                this.betButton.setEnabled(false);
                this.callButton.setEnabled(false);
            }
            this.rvOpponentsAdapter.setPlayers(players);
            this.rvPlayerDiceAdapter.setDice(myDice);
            this.tvPlayerBet.setText(myLastBet == null ? "No Bet" : myLastBet.getAmount() + " x " + myLastBet.getValue());

        });
    }

    private void moveToMainMenu() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private void moveToLossScreen() {
        Intent intent = new Intent(this, LoseActivity.class);
        startActivity(intent);
    }

    private void moveToWinScreen() {
        Intent intent = new Intent(this, WinActivity.class);
        startActivity(intent);
    }
}