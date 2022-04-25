package com.example.liars_dice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WinActivity extends AppCompatActivity implements View.OnClickListener{
    private Button returnToMainButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);

        this.returnToMainButton = findViewById(R.id.returnButton);
        this.returnToMainButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        moveToMainMenu();
    }

    private void moveToMainMenu() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);}
}