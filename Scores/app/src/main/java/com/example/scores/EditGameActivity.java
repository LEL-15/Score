package com.example.scores;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;

public class EditGameActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<Player> Players = new ArrayList<Player>();
    ArrayList<Row> Rows = new ArrayList<Row>();
    RecyclerView recyclerView;
    Player_Adapter adapter;
    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //When this function is called
        super.onCreate(savedInstanceState);
        Intent loadIntent = getIntent();
        //Find out what the item's id is
        id = loadIntent.getStringExtra("ID");
        setContentView(R.layout.activity_edit_game);
        final RecyclerView.LayoutManager hold = new LinearLayoutManager(this);

        db.collection("games")
                .document(id)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        //If succesfully accessed firebase
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            //Set name
                            TextView use = findViewById(R.id.game_name);
                            use.setText(document.getString("name"));
                        }
                        //If failed to access firebase
                        else {
                            Log.d("Debug", "Problem");
                        }
                    }
                });
        addRealtimeUpdate();
    }

    //Functions for each button pushed
    public void newPlayer(View item){
        //Create intent
        Intent intent = new Intent(this, AddPlayerActivity.class);
        intent.putExtra("GAME", id);
        intent.putExtra("TYPE", "simple");
        startActivity(intent);
    }
    public void exitGame(View item){
        //Create intent
        Intent intent = new Intent(this, DisplayGamesActivity.class);
        startActivity(intent);
    }

    private void addRealtimeUpdate() {
        DocumentReference contactListener = db.collection("games").document(id);
        contactListener.addSnapshotListener(new EventListener < DocumentSnapshot > () {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d("ERROR", e.getMessage());
                    return;
                }
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    reloadPlayers();
                }
            }
        });
    }
    public void reloadPlayers(){
        Players = new ArrayList<Player>();
        final RecyclerView.LayoutManager hold = new LinearLayoutManager(this);
        db.collection("games").document(id).collection("players").orderBy("score")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        //If succesfully accessed firebase
                        if (task.isSuccessful()) {
                            //For every item in the database
                            //Create an item card, set its name and price
                            //Add item card to item list
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Player player = new Player();
                                player.setName(document.getString("name"));
                                player.setScore((int) Math.round(document.getDouble("score")));
                                player.setGame_id(id);
                                player.setId(document.getId());
                                player.setColor(document.getString("color"));

                                Players.add(player);
                            }
                        }

                        //If failed to access firebase
                        else {
                            Log.d("Testing", "Problem");
                        }

                        //Set the adapter to add all the item cards to the recycler view
                        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                        recyclerView.setLayoutManager(hold);
                        Collections.reverse(Players);
                        adapter = new Player_Adapter(Players);

                        recyclerView.setAdapter(adapter);
                    }
                });
    }
}
