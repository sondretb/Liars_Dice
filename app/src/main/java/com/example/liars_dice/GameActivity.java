package com.example.liars_dice;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.liars_dice.model.GameModel;

import java.util.Collection;

public class GameActivity extends AppCompatActivity {
    private GameModel gameModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Bundle gameSpecificationBundle = getIntent().getExtras();
        int playerAmount = gameSpecificationBundle.getInt("PLAYER_AMOUNT");
        int playerLives = gameSpecificationBundle.getInt("PLAYER_LIVES");
        Collection<String> playersJoining = gameSpecificationBundle.getStringArrayList("PLAYERS_JOINING");
        gameModel = new GameModel(playerAmount, playersJoining, playerLives);
    }


}