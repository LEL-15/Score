package com.example.scores;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;

public class EditTableGameActivity extends AppCompatActivity {
    String TAG = "TEST";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<Player> Players = new ArrayList<Player>();
    ArrayList<Row> Rows = new ArrayList<Row>();
    RecyclerView recyclerView;
    Player_Adapter adapter;
    String id;
    Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //When this function is called
        super.onCreate(savedInstanceState);
        Intent loadIntent = getIntent();
        //Find out what the item's id is
        id = loadIntent.getStringExtra("ID");
        setContentView(R.layout.activity_table_game);
        final RecyclerView.LayoutManager hold = new LinearLayoutManager(this);
        Log.d(TAG, "onCreate: id is" + id);
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
        startActivity(intent);
    }
    public void exitGame(View item){
        //Create intent
        Intent intent = new Intent(this, DisplayGamesActivity.class);
        startActivity(intent);
    }

    private void addRealtimeUpdate() {
        DocumentReference contactListener = db.collection("games").document(id);
        contactListener.addSnapshotListener(new EventListener< DocumentSnapshot >() {
            @Override
            public void onEvent(DocumentSnapshot documentSnapshot, FirebaseFirestoreException e) {
                if (e != null) {
                    Log.d("ERROR", e.getMessage());
                    return;
                }
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    reloadPlayers();
                    reloadRows();
                }
            }
        });
    }
    public void reloadPlayers(){
        Players = new ArrayList<Player>();
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
                        TableRow nameRow = findViewById(R.id.players);
                        for(int i = 0; i < Players.size(); i++ ){
                            Player player = Players.get(i);
                            TextView playerName = new TextView(context);
                            playerName.setWidth(300);
                            String name = player.getName();
                            String color_want = player.getColor();
                            int int_color = context.getResources().getIdentifier(color_want, "color", context.getPackageName());
                            playerName.setText(name);
                            playerName.setBackgroundColor(context.getResources().getColor(int_color));
                            nameRow.addView(playerName);
                        }
                    }
                });
    }
    public void reloadRows(){
        Rows = new ArrayList<Row>();
        final RecyclerView.LayoutManager hold = new LinearLayoutManager(this);
        db.collection("games").document(id).collection("rows")
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
                                Row row = new Row();
                                row.setName(document.getString("name"));
                                row.setMultiplier((int) Math.round(document.getDouble("multiplier")));
                                Rows.add(row);
                            }
                        }

                        //If failed to access firebase
                        else {
                            Log.d("Testing", "Problem");
                        }
                        TableLayout table = findViewById(R.id.table);
                        for(int i = 0; i < Rows.size(); i++ ){
                            Row row = Rows.get(i);
                            TableRow newRow = new TableRow(context);
                            TextView rowName = new TextView(context);
                            rowName.setWidth(400);
                            String name = row.getName();
                            rowName.setText(name);
                            newRow.addView(rowName);
                            table.addView(newRow);
                        }

                    }
                });
    }
}
