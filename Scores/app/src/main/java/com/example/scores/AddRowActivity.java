package com.example.scores;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddRowActivity extends AppCompatActivity {
    final FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
    String game_id;
    String game_type;
    String TAG ="TEST";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //When this function is called
        super.onCreate(savedInstanceState);
        final Intent loadIntent = getIntent();
        final Bundle extras = loadIntent.getExtras();
        //If there are extras, grab them all
        if (extras != null) {
            game_id = loadIntent.getStringExtra("ID");
        }
        setContentView(R.layout.activity_add_row);
    }
    //Functions for each button pushed
    public void startGame(View item){
        //Create intent
        Log.d(TAG, "startGame: id is " + game_id);
        Intent intent = new Intent(this, EditTableGameActivity.class);
        intent.putExtra("ID", game_id);
        startActivity(intent);
    }
    public void addAnotherRow(View item){
        //Write new player
        FirebaseFirestore rootRef = FirebaseFirestore.getInstance();
        CollectionReference ref = rootRef.collection("games").document(game_id).collection("rows");
        EditText editText = findViewById(R.id.player_name);
        String name = editText.getText().toString();
        EditText multiplierText = findViewById(R.id.multiplier);
        String multiplier = multiplierText.getText().toString();
        Log.d(TAG, "addAnotherRow: multiplier is" + multiplier);
        Double multiplierNum = Double.valueOf(multiplier);

        Map<String, Object> data = new HashMap<>();
        data.put("multiplier", multiplierNum);
        data.put("name", name);

        ref.add(data);

        //Create intent
        Intent intent = new Intent(this, AddRowActivity.class);
        intent.putExtra("ID", game_id);
        intent.putExtra("TYPE", game_type);
        startActivity(intent);
    }
}
