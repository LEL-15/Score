package com.example.scores;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddPlayerActivity extends AppCompatActivity {
    final FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    String game_id;
    String TAG ="testing";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //When this function is called
        super.onCreate(savedInstanceState);
        final Intent loadIntent = getIntent();
        final Bundle extras = loadIntent.getExtras();
        //If there are extras, grab them all
        if (extras != null) {
            game_id = loadIntent.getStringExtra("GAME");
        }
        setContentView(R.layout.activity_add_player);
    }
    //Functions for each button pushed
    public void startGame(View item){
        //Create intent
        Intent intent = new Intent(this, editGameActivity.class);
        intent.putExtra("ID", game_id);
        startActivity(intent);
    }
    public void addAnotherPlayer(View item){
        //Write new player
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference ref = rootRef.collection("games").document(game_id).collection("players");
        EditText editText = findViewById(R.id.player_name);
        String name = editText.getText().toString();
        Spinner colorSpinner = (Spinner) findViewById(R.id.color_pick);
        String color = colorSpinner.getSelectedItem().toString();


        Map<String, Object> data = new HashMap<>();
        data.put("score", 0);
        data.put("name", name);
        data.put("color", color);
        data.put("uid", "guest");

        ref.add(data);

        //Create intent
        Intent intent = new Intent(this, AddPlayerActivity.class);
        intent.putExtra("GAME", game_id);
        startActivity(intent);
    }
    public void addById(View item){
        //Add logged in player
        TextView givenId = findViewById(R.id.uid);
        final String userID = givenId.getText().toString();
        final CollectionReference players = rootRef.collection("games").document(game_id).collection("players");
        DocumentReference userRef = rootRef.collection("users").document(userID);
        userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String name = documentSnapshot.getString("name");
                String color = documentSnapshot.getString("color");
                Map<String, Object> data = new HashMap<>();
                data.put("score", 0);
                data.put("name", name);
                data.put("color", color);
                data.put("uid", userID);

                players.add(data);
            }
        });
        //Create intent
        Intent intent = new Intent(this, AddPlayerActivity.class);
        intent.putExtra("GAME", game_id);
        startActivity(intent);
    }
}
