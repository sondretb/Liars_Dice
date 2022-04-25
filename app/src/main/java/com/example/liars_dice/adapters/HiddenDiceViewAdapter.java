package com.example.liars_dice.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.liars_dice.R;
import com.example.liars_dice.model.game.Dice;

import java.util.ArrayList;

public class HiddenDiceViewAdapter extends RecyclerView.Adapter<HiddenDiceViewAdapter.ViewHolder> {
    private ArrayList<Dice> dice;

    public void setDice(ArrayList<Dice> dice) {
        this.dice = dice;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvDiceValue;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDiceValue = itemView.findViewById(R.id.tvOpponentDiceValue);
        }

        public TextView getTvDiceValue() {
            return tvDiceValue;
        }
    }

    public HiddenDiceViewAdapter(ArrayList<Dice> dice) {
        this.dice = dice;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_dice, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.getTvDiceValue().setText("?");
    }

    @Override
    public int getItemCount() {
        return dice.size();
    }
}
