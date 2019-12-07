package com.example.scores;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class editGameActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ArrayList<Player> Players = new ArrayList<Player>();
    RecyclerView recyclerView;
    Player_Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //When this function is called
        super.onCreate(savedInstanceState);
        Intent loadIntent = getIntent();
        //Find out what the item's id is
        final String id = loadIntent.getStringExtra("ID");
        setContentView(R.layout.edit_game);
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

        db.collection("games").document(id).collection("players")
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
                                player.setScore(document.getDouble("score"));

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
                        adapter = new Player_Adapter(Players);

                        recyclerView.setAdapter(adapter);
                    }
                });
        
    }
}