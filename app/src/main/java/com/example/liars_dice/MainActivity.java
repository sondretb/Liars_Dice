package com.example.liars_dice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Observable;
import java.util.Observer;

import com.example.liars_dice.model.MainMenuModel;

public class MainActivity extends AppCompatActivity implements Observer, View.OnClickListener{
    private MainMenuModel model;
    private Button joinLobbyButton;
    private Button createLobbyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.model = new MainMenuModel();
        model.addObserver(this);
        this.joinLobbyButton = findViewById(R.id.joinLobbyButton);
        this.createLobbyButton = findViewById(R.id.createLobbyButton);
        this.joinLobbyButton.setOnClickListener(this);
        this.createLobbyButton.setOnClickListener(this);
    }

    @Override
    public void update(Observable observable, Object o) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case (R.id.createLobbyButton):
                model.createLobby();
                break;

            case (R.id.joinLobbyButton):
                model.connectToLobby();
                break;
            }


    }
}