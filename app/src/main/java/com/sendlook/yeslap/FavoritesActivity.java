package com.sendlook.yeslap;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sendlook.yeslap.model.ChatMessage;
import com.sendlook.yeslap.model.Favorites;
import com.sendlook.yeslap.model.FavoritesAdapter;
import com.sendlook.yeslap.model.Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class FavoritesActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private GridView gvFavorite;
    private ArrayAdapter<Favorites> adapter;
    private ArrayList<Favorites> arrayFavorites;
    private ValueEventListener valueEventListener;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        mAuth = FirebaseAuth.getInstance();

        gvFavorite = (GridView) findViewById(R.id.gvFavorites);

        arrayFavorites = new ArrayList<>();
        adapter = new FavoritesAdapter(getApplicationContext(), arrayFavorites);
        gvFavorite.setAdapter(adapter);

        mDatabase = FirebaseDatabase.getInstance().getReference().child(Utils.FAVORITES).child(mAuth.getCurrentUser().getUid());
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayFavorites.clear();
                for (DataSnapshot favorite: dataSnapshot.getChildren()) {
                    try {
                        Favorites favorites = favorite.getValue(Favorites.class);
                        arrayFavorites.add(favorites);
                    } catch (Exception e) {
                        Utils.toastyError(getApplicationContext(), e.getMessage());
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };


    }

    private void setStatusOnline() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Utils.USERS).child(mAuth.getCurrentUser().getUid());
        HashMap<String, Object> status = new HashMap<>();
        status.put("status", "online");
        mDatabase.updateChildren(status);
    }

    private void setStatusOffline() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Utils.USERS).child(mAuth.getCurrentUser().getUid());
        HashMap<String, Object> status = new HashMap<>();
        status.put("status", "offline");
        mDatabase.updateChildren(status);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Stop the EventListener
        mDatabase.removeEventListener(valueEventListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        setStatusOffline();
        mDatabase.removeEventListener(valueEventListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Iniciate the EventListener
        mDatabase.addValueEventListener(valueEventListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setStatusOnline();
        mDatabase.addValueEventListener(valueEventListener);
    }

}
