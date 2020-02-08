package com.example.scores;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

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
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference ref = rootRef.collection("games");
        EditText editText = findViewById(R.id.player_name);
        game_name = editText.getText().toString();

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
                                Log.d(TAG, "onComplete: current name is " + document.getString("name"));
                                Log.d(TAG, "onComplete: Looking for: " + game_name);
                                if(document.getString("name").equals(game_name)){
                                    id = document.getId();
                                }
                            }
                        }
                        //Create intent
                        Intent intent = new Intent(hold, AddPlayerActivity.class);
                        intent.putExtra("GAME", id);
                        startActivity(intent);
                    }
                });
    }
}
