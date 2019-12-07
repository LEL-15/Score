package com.example.scores;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class editGameActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //When this function is called
        super.onCreate(savedInstanceState);
        Intent loadIntent = getIntent();
        //Find out what the item's id is
        final String id = loadIntent.getStringExtra("ID");
        setContentView(R.layout.edit_game);

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
        
    }
}
