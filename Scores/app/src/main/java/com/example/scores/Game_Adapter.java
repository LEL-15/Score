package com.example.scores;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

//Adapter to create card for items and add them to recyclerView in buy page
public class Game_Adapter extends RecyclerView.Adapter<Game_Adapter.ViewHolder> {

    List<Game> GameList;
    Context context;

    public Game_Adapter(List<Game>GameList)
    {
        this.GameList = GameList;
    }

    @Override
    //When new view is made, make a new view holder
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }

    @Override
    //When this adapter is bound to a view, set the holder title and price
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Game thing = GameList.get(position);

        holder.title.setText(thing.getName());

        holder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                String print = "" + thing.getID();
                Intent intent = new Intent(view.getContext(), editGameActivity.class);
                intent.putExtra("ID", thing.getID());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return GameList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView title;
        CardView cv;
        //Create new itemView for each item in passed item list
        public ViewHolder(View gameView)
        {
            super(gameView);
            title = (TextView)gameView.findViewById(R.id.text_view_name);
            cv = (CardView)gameView.findViewById(R.id.cv);
        }
    }
}