package com.example.scores;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class NewGameActivity extends AppCompatActivity {
    String game_name;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String id;
    String TAG = "testing";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //When this function is called
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_game);
    }
    //Functions for each button pushed
    public void done(View item){
        //Write new player
        final FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference ref = rootRef.collection("games");
        EditText editText = findViewById(R.id.player_name);
        game_name = editText.getText().toString();
        Spinner colorSpinner = (Spinner) findViewById(R.id.game_pick);
        final String gameType = colorSpinner.getSelectedItem().toString();

        Map<String, Object> data = new HashMap<>();
        data.put("name", game_name);

        ref.add(data);
        final Context hold = this;
        db.collection("games")
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
                                if(document.getString("name").equals(game_name)){
                                    id = document.getId();
                                }
                            }
                        }
                        //Add logged in player
                        final CollectionReference players = rootRef.collection("games").document(id).collection("players");
                        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        DocumentReference userRef = rootRef.collection("users").document(user.getUid());
                        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                String name = documentSnapshot.getString("name");
                                String color = documentSnapshot.getString("color");
                                Map<String, Object> data = new HashMap<>();
                                data.put("score", 0);
                                data.put("name", name);
                                data.put("color", color);
                                data.put("uid", user.getUid());

                                players.add(data);
                            }
                        });
                        //Create intent
                        Intent intent = new Intent(hold, AddPlayerActivity.class);
                        intent.putExtra("GAME", id);
                        intent.putExtra("TYPE", gameType);
                        startActivity(intent);
                    }
                });
    }
}
