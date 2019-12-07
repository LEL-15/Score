package com.example.scores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

//Adapter to create card for items and add them to recyclerView in buy page
public class Player_Adapter extends RecyclerView.Adapter<Player_Adapter.ViewHolder> {

    List<Player> PlayerList;
    Context context;

    public Player_Adapter(List<Player>PlayerList)
    {
        this.PlayerList = PlayerList;
    }

    @Override
    //When new view is made, make a new view holder
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.player, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }

    @Override
    //When this adapter is bound to a view, set the holder title and price
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Player thing = PlayerList.get(position);

        holder.title.setText(thing.getName());
        holder.score.setText(thing.getScore().toString());
    }

    @Override
    public int getItemCount() {
        return PlayerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView title;
        TextView score;
        CardView cv;
        //Create new itemView for each item in passed item list
        public ViewHolder(View gameView)
        {
            super(gameView);
            title = (TextView)gameView.findViewById(R.id.name);
            score = (TextView)gameView.findViewById(R.id.score);
            cv = (CardView)gameView.findViewById(R.id.cv);
        }
    }
}