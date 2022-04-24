package com.example.liars_dice.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.liars_dice.R;
import com.example.liars_dice.model.LobbyPlayerModel;

import java.util.ArrayList;

public class LobbyPlayerViewAdapter extends RecyclerView.Adapter<LobbyPlayerViewAdapter.ViewHolder> {
    private ArrayList<LobbyPlayerModel> players;

    public void setPlayers(ArrayList<LobbyPlayerModel> players) {
        this.players = players;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView lobbyPlayerName;
        private final TextView lobbyPlayerReady;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            lobbyPlayerName = itemView.findViewById(R.id.lobbyPlayerName);
            lobbyPlayerReady = itemView.findViewById(R.id.lobbyPlayerReady);
        }

        public TextView getLobbyPlayerName() {
            return lobbyPlayerName;
        }

        public TextView getLobbyPlayerReady() {
            return lobbyPlayerReady;
        }
    }

    public LobbyPlayerViewAdapter(ArrayList<LobbyPlayerModel> players) {
        this.players = players;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.getLobbyPlayerName().setText(players.get(position).getId());
        holder.getLobbyPlayerReady().setText(players.get(position).getReady() ? "Ready" : "Not Ready");
    }

    @Override
    public int getItemCount() {
        return players.size();
    }
}
