package com.sendlook.yeslap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
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

import ru.whalemare.sheetmenu.SheetMenu;

public class FavoritesActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference database;
    private GridView gvFavorite;
    private ArrayAdapter<Favorites> adapter;
    private ImageView btnGoToProfile,btnGoToSettings;
    private TextView tvFavorite;
    private ArrayList<Favorites> arrayFavorites;
    private ValueEventListener valueEventListener;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        mAuth = FirebaseAuth.getInstance();

        String uid = mAuth.getCurrentUser().getUid();

        gvFavorite = (GridView) findViewById(R.id.gvFavorites);
        btnGoToProfile = (ImageView) findViewById(R.id.btnGoToProfile);
        btnGoToSettings = (ImageView) findViewById(R.id.btnGoToSettings);
        tvFavorite = (TextView) findViewById(R.id.tvFavorite);

        arrayFavorites = new ArrayList<>();
        adapter = new FavoritesAdapter(getApplicationContext(), arrayFavorites);
        gvFavorite.setAdapter(adapter);

        mDatabase = FirebaseDatabase.getInstance().getReference().child(Utils.FAVORITES).child(uid);
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayFavorites.clear();
                for (DataSnapshot favorite : dataSnapshot.getChildren()) {
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

        gvFavorite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, final long id) {
                SheetMenu.with(FavoritesActivity.this)
                        .setTitle(Utils.FAVORITES)
                        .setMenu(R.menu.menu_favorite)
                        .setClick(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.nav_menu_view_profile:
                                        Intent intent = new Intent(FavoritesActivity.this, ProfileActivity.class);
                                        intent.putExtra(Utils.UID, (arrayFavorites.get(position).getUid()));
                                        startActivity(intent);
                                        break;
                                    case R.id.nav_menu_delete_favorite:
                                        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(Utils.FAVORITES).child(mAuth.getCurrentUser().getUid()).child(arrayFavorites.get(position).getUid());
                                        database.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                           @Override
                                           public void onComplete(@NonNull Task<Void> task) {
                                               if (task.isSuccessful()) {
                                                   Utils.toastySuccess(getApplicationContext(), getString(R.string.favorite_removed));
                                                   checkFavorite();
                                               }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                           @Override
                                           public void onFailure(@NonNull Exception e) {
                                                   Utils.toastyError(getApplicationContext(), e.getMessage());
                                            }
                                       });
                                       break;
                                    case R.id.nav_menu_chat:
                                        Intent intentChat = new Intent(FavoritesActivity.this, ChatActivity.class);
                                        intentChat.putExtra(Utils.UID, (arrayFavorites.get(position).getUid()));
                                        startActivity(intentChat);
                                        break;
                                }
                                return false;
                            }
                        }).show();
            }
        });

        btnGoToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnGoToSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FavoritesActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });


    }

    private void checkFavorite() {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(Utils.FAVORITES).child(mAuth.getCurrentUser().getUid());
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {
                    gvFavorite.setVisibility(View.INVISIBLE);
                    tvFavorite.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setStatusOnline() {
        database = FirebaseDatabase.getInstance().getReference().child(Utils.USERS).child(mAuth.getCurrentUser().getUid());
        HashMap<String, Object> status = new HashMap<>();
        status.put(Utils.STATUS, "online");
        database.updateChildren(status);
    }

    private void setStatusOffline() {
        database = FirebaseDatabase.getInstance().getReference().child(Utils.USERS).child(mAuth.getCurrentUser().getUid());
        HashMap<String, Object> status = new HashMap<>();
        status.put(Utils.STATUS, "offline");
        database.updateChildren(status);
    }

    @Override
    protected void onPause() {
        super.onPause();
        setStatusOffline();
        mDatabase.removeEventListener(valueEventListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setStatusOnline();
        checkFavorite();
        mDatabase.addValueEventListener(valueEventListener);
    }

}
