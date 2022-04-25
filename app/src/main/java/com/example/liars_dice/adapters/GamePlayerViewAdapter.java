package com.example.liars_dice.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.liars_dice.R;
import com.example.liars_dice.model.game.Bet;
import com.example.liars_dice.model.game.Dice;
import com.example.liars_dice.model.game.GamePlayer;

import java.util.ArrayList;

public class GamePlayerViewAdapter extends RecyclerView.Adapter<GamePlayerViewAdapter.ViewHolder> {
    private ArrayList<GamePlayer> players;
    private HiddenDiceViewAdapter hiddenDiceViewAdapter;

    public void setPlayers(ArrayList<GamePlayer> players) {
        this.players = players;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvPlayerName;
        private final TextView tvPlayerBet;
        private final RecyclerView rvDice;
        private HiddenDiceViewAdapter hiddenDiceViewAdapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPlayerName = itemView.findViewById(R.id.tvPlayerName);
            tvPlayerBet = itemView.findViewById(R.id.textViewBet);
            rvDice = itemView.findViewById(R.id.rvPlayerDices);
            rvDice.setLayoutManager(new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false));
            hiddenDiceViewAdapter = new HiddenDiceViewAdapter(new ArrayList<Dice>());
            rvDice.setAdapter(hiddenDiceViewAdapter);
        }

        public TextView getGamePlayerName() {
            return tvPlayerName;
        }

        public TextView getGamePlayerBet() {
            return tvPlayerBet;
        }

        public RecyclerView getRvDice() { return rvDice; }

        public HiddenDiceViewAdapter getDiceViewAdapter() {
            return hiddenDiceViewAdapter;
        }
    }

    public GamePlayerViewAdapter(ArrayList<GamePlayer> players) {
        this.players = players;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_game_player, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        GamePlayer player = players.get(position);
        if (player != null) {
            holder.getGamePlayerName().setText(player.getDice().size() > 0 ? player.getId() : "Lost");
            Bet lastBet = player.getLastBet();
            holder.getDiceViewAdapter().setDice(player.getDice());
            if (lastBet == null) {
                holder.getGamePlayerBet().setText("No bet");
            }
            else {
                String betAmount = Integer.toString(lastBet.getAmount());
                String betValue = Integer.toString(lastBet.getValue());
                holder.getGamePlayerBet().setText(betAmount + " x " + betValue);
            }

        }
    }

    @Override
    public int getItemCount() {
        return players.size();
    }
}
