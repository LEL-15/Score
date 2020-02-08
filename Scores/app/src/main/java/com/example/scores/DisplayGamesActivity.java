package com.example.scores;

import android.content.Intent;
import android.os.Bundle;
import android.content.ClipData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DisplayGamesActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    //For prints
    String TAG = "Testing";
    RecyclerView recyclerView;
    Game_Adapter adapter;
    ArrayList<Game> Games = new ArrayList<Game>();
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Hold this context
        final RecyclerView.LayoutManager hold = new LinearLayoutManager(this);
        //Get all items from firebase
        //Display buy activity on screen
        setContentView(R.layout.activity_main);
        db.collection("games")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        //If succesfully accessed firebase
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: Here");
                            //For every item in the database
                            //Create an item card, set its name and price
                            //Add item card to item list
                            final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            for (final QueryDocumentSnapshot document : task.getResult()) {
                                db.collection("games").document(document.getId()).collection("players")
                                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        for (QueryDocumentSnapshot player : task.getResult()) {
                                            Log.d(TAG, "onComplete: UID" + player.getString("uid"));
                                            if (player.getString("uid").equals(user.getUid())) {
                                                Log.d(TAG, "onComplete: HEre");
                                                final Game game = new Game();
                                                game.setName(document.getString("name"));
                                                game.setId(document.getId());
                                                Games.add(game);
                                            }
                                        }
                                        Log.d(TAG, "onCreate: Got to this point.");
                                        Log.d(TAG, "onComplete: size" + Games.size());
                                        for (int i = 0; i < Games.size(); i++) {
                                            Log.d(TAG, "game name is: " + Games.get(i).getName());
                                        }
                                        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
                                        recyclerView.setLayoutManager(hold);
                                        adapter = new Game_Adapter(Games);
                                        recyclerView.setAdapter(adapter);
                                    }
                                });
                            }
                        }
                        //If failed to access firebase
                        else {
                            Log.d("Testing", "Problem");
                        }
                    }
                });
        //Navigation Bar
        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addGame(fab);
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
    public void addGame(View item){
        //Write new player
        //Create intent
        Intent intent = new Intent(this, NewGameActivity.class);
        startActivity(intent);
    }
}
