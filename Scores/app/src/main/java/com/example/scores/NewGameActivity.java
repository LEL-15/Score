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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Collections;
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
        final Context hold = this;

        ref.add(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                id = task.getResult().getId();
                Log.d(TAG, "onComplete: " + id);
                //Create intent
                Intent intent = new Intent(hold, AddPlayerActivity.class);
                intent.putExtra("GAME", id);
                startActivity(intent);
            }
        });
    }
}
