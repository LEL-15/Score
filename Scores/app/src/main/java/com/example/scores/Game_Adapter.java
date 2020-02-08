package com.example.scores;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//Adapter to create card for items and add them to recyclerView in buy page
public class Game_Adapter extends RecyclerView.Adapter<Game_Adapter.ViewHolder> {
    String TAG = "testing";

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
        Log.d(TAG, "onBindViewHolder: Went into adapter");

        holder.title.setText(thing.getName());
        String players_string = "Players:";
        ArrayList<String>  playerList = thing.getPlayers();
        boolean start = true;
        for(int i = 0; i< playerList.size(); i++){
            if(!start){
                players_string = players_string + ", " + playerList.get(i);
            }
            else{
                start = false;
                players_string = players_string + " " + playerList.get(i);
            }

        }
        holder.title.setText(thing.getName());
        holder.players.setText(players_string);

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

        holder.replay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
                final CollectionReference ref = rootRef.collection("games");

                Map<String, Object> data = new HashMap<>();
                data.put("name", thing.getName());

                ref.add(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {

                        final String newId = task.getResult().getId();
                        ref.document(thing.getID()).collection("players").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "onComplete: One success");
                                    for (QueryDocumentSnapshot sub_document : task.getResult()) {
                                        Log.d(TAG, "onComplete: A player");
                                        ref.document(newId).collection("players").add(sub_document.getData());
                                    }
                                    setTo0(newId);
                                }
                            }
                        });
                    }
                });
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
        TextView players;
        Button replay;
        //Create new itemView for each item in passed item list
        public ViewHolder(View gameView)
        {
            super(gameView);
            title = (TextView)gameView.findViewById(R.id.name);
            cv = (CardView)gameView.findViewById(R.id.cv);
            players = (TextView)gameView.findViewById(R.id.players);
            replay = (Button)gameView.findViewById(R.id.replay);
        }
    }

    public void setTo0(final String newID){
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        final CollectionReference ref = rootRef.collection("games").document(newID).collection("players");
        ref.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot player : task.getResult()) {
                        ref.document(player.getId()).update("score", 0);
                    }
                    //Create intent
                    Intent intent = new Intent(context.getApplicationContext(), editGameActivity.class);
                    intent.putExtra("ID", newID);
                    context.startActivity(intent);
                }
            }
        });
    }
}