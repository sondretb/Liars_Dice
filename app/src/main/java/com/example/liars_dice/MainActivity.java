package com.example.liars_dice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Observable;
import java.util.Observer;

import com.example.liars_dice.model.MainMenuModel;
import java.util.Observer;

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
        this.joinLobbyButton = (Button) findViewById(R.id.joinLobbyButton);
        this.createLobbyButton = (Button) findViewById(R.id.createLobbyButton);
        this.joinLobbyButton.setOnClickListener(this);
        this.createLobbyButton.setOnClickListener(this);

    }

    @Override
    public void update(Observable observable, Object o) {

    }

    @Override
    public void onClick(View view) {

    }
}