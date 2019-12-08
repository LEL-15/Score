package com.example.scores;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Dictionary;
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
        String color_want = thing.getColor();

        int int_color = context.getResources().getIdentifier(color_want, "color", context.getPackageName());
        holder.cv.setCardBackgroundColor(context.getResources().getColor(int_color));


        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
                DocumentReference ref = rootRef.collection("games").document(thing.getGame_id()).collection("players").document(thing.getId());
                ref.update("score",  FieldValue.increment(1));

                Intent intent = new Intent(view.getContext(), editGameActivity.class);
                intent.putExtra("ID", thing.getGame_id());
                view.getContext().startActivity(intent);
            }
        });

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
                DocumentReference ref = rootRef.collection("games").document(thing.getGame_id()).collection("players").document(thing.getId());
                ref.update("score",  FieldValue.increment(-1));

                Intent intent = new Intent(view.getContext(), editGameActivity.class);
                intent.putExtra("ID", thing.getGame_id());
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return PlayerList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView score;
        CardView cv;
        ImageButton plus;
        ImageButton minus;

        //Create new itemView for each item in passed item list
        public ViewHolder(View gameView) {
            super(gameView);
            title = (TextView) gameView.findViewById(R.id.name);
            score = (TextView) gameView.findViewById(R.id.score);
            cv = (CardView) gameView.findViewById(R.id.cv);
            plus = (ImageButton) gameView.findViewById(R.id.plus);
            minus = (ImageButton) gameView.findViewById(R.id.minus);
        }
    }
}