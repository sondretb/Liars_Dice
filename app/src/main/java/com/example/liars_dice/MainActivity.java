package com.example.liars_dice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Observable;
import java.util.Observer;

import com.example.liars_dice.api.LobbyManagerAPI;
import com.example.liars_dice.model.MainMenuModel;

import org.json.JSONException;
import org.json.JSONObject;

import io.socket.client.Ack;


public class MainActivity extends AppCompatActivity implements Observer, View.OnClickListener{
    /* Model */
    private MainMenuModel model;

    /* API */
    private LobbyManagerAPI lobbyManagerAPI;

    /* View */
    private Button joinLobbyButton;
    private Button createLobbyButton;
    private EditText editTextGameID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Create model and observe it */
        this.model = new MainMenuModel();
        model.addObserver(this);
        /* Get ServerAPI instance */
        this.lobbyManagerAPI = LobbyManagerAPI.getInstance();
        if (!lobbyManagerAPI.connected()) {
            this.lobbyManagerAPI.connect();
        }

        this.joinLobbyButton = findViewById(R.id.joinLobbyButton);
        this.joinLobbyButton.setOnClickListener(this);

        this.createLobbyButton = findViewById(R.id.createLobbyButton);
        this.createLobbyButton.setOnClickListener(this);

        this.editTextGameID = findViewById(R.id.editTextGameID);
        this.editTextGameID.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
    }

    @Override
    public void update(Observable observable, Object o) {
        this.editTextGameID.setText(this.model.getGameID());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case (R.id.createLobbyButton):
                System.out.println("create lobby button clicked");
                if (this.lobbyManagerAPI.connected()) {
                    System.out.println("creating lobby");
                    this.lobbyManagerAPI.create(new Ack() {

                        @Override
                        public void call(Object... args) {
                            System.out.println("got callback");
                            String error = null;
                            String id = null;
                            try {
                                id = new JSONObject(args[0].toString())
                                    .getJSONObject("data")
                                    .getString("id");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (error != null) {
                                return;
                            }
                            if (id != null) {
                                System.out.println("ID: "+id);
                                moveToLobby(id);
                            }
                        }
                    });
                }
                break;

            case (R.id.joinLobbyButton):
                String id = this.editTextGameID.getText().toString().trim();
                if (id.length() == 5 && this.lobbyManagerAPI.connected()) {
                    this.lobbyManagerAPI.join(id, new Ack() {
                        @Override
                        public void call(Object... args) {
                            try {
                                JSONObject result = new JSONObject(args[0].toString());
                                if (result.has("error")){
                                    return;
                                }
                                if (!result.has("data")){
                                    return;
                                }
                                JSONObject data = result.getJSONObject("data");
                                moveToLobby(data.getString("id"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }
                break;
            }


    }

    private void moveToLobby(String id) {
        Intent intent = new Intent(this, LobbyActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }
}