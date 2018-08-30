package br.sendlook.yeslap.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.sendlook.yeslap.R;
import br.sendlook.yeslap.view.Favorites;
import br.sendlook.yeslap.model.FavoritesAdapter;
import br.sendlook.yeslap.view.Utils;
import ru.whalemare.sheetmenu.SheetMenu;

public class FavoritesActivity extends AppCompatActivity {

    private GridView gvFavorite;
    private ImageView btnGoToProfile, btnGoToSettings;
    private TextView tvFavorite;
    private ProgressDialog dialogs;
    private String id;
    private FavoritesAdapter adapter;
    private List<Favorites> favoritesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString(Utils.ID_USER);
        }

        gvFavorite = (GridView) findViewById(R.id.gvFavorites);
        btnGoToProfile = (ImageView) findViewById(R.id.btnGoToProfile);
        btnGoToSettings = (ImageView) findViewById(R.id.btnGoToSettings);
        tvFavorite = (TextView) findViewById(R.id.tvFavorite);

        gvFavorite.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, final long ids) {
                final Favorites favorites = (Favorites) parent.getAdapter().getItem(position);

                SheetMenu.with(FavoritesActivity.this)
                        .setTitle(favorites.getUsername_user())
                        .setMenu(R.menu.menu_favorite)
                        .setClick(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.nav_menu_view_profile:
                                        Intent intent = new Intent(FavoritesActivity.this, ProfileActivity.class);
                                        intent.putExtra(Utils.ID_FAVORITE_USER_APP, favorites.getId_user());
                                        intent.putExtra(Utils.ID_USER, id);
                                        startActivity(intent);
                                        break;
                                    case R.id.nav_menu_delete_favorite:

                                        new MaterialDialog.Builder(FavoritesActivity.this)
                                                .title(favorites.getUsername_user())
                                                .content(R.string.delete_favorite_msg)
                                                .positiveText(R.string.confirm)
                                                .negativeText(R.string.cancel)
                                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                    @Override
                                                    public void onClick(@NonNull final MaterialDialog dialog, @NonNull DialogAction which) {
                                                        dialogs = new ProgressDialog(FavoritesActivity.this);
                                                        dialogs.setMessage(getString(R.string.loading));
                                                        dialogs.setCanceledOnTouchOutside(false);
                                                        dialogs.show();

                                                        Ion.with(getApplicationContext())
                                                                .load(Utils.URL_DELETE_FAVORITE)
                                                                .setBodyParameter(Utils.ID_USER_APP, id)
                                                                .setBodyParameter(Utils.ID_FAVORITE_USER_APP, favorites.getId_user())
                                                                .asJsonObject()
                                                                .setCallback(new FutureCallback<JsonObject>() {
                                                                    @Override
                                                                    public void onCompleted(Exception e, JsonObject result) {
                                                                        try {
                                                                            String returnApp = result.get(Utils.FAVORITES).getAsString();
                                                                            switch (returnApp) {
                                                                                case Utils.CODE_SUCCESS:
                                                                                    if (dialogs.isShowing()) {
                                                                                        dialogs.dismiss();
                                                                                    }
                                                                                    loadFavorites();
                                                                                    Utils.toastySuccess(getApplicationContext(), getString(R.string.favorite_removed));
                                                                                    break;
                                                                                case Utils.CODE_ERROR:
                                                                                    if (dialogs.isShowing()) {
                                                                                        dialogs.dismiss();
                                                                                    }
                                                                                    Utils.toastyError(getApplicationContext(), e.getMessage());
                                                                                    break;
                                                                            }
                                                                        } catch (Exception x) {
                                                                            if (dialogs.isShowing()) {
                                                                                dialogs.dismiss();
                                                                            }
                                                                            Utils.toastyError(getApplicationContext(), x.getMessage());
                                                                        }
                                                                    }
                                                                });

                                                    }
                                                })
                                                .onNegative(new MaterialDialog.SingleButtonCallback() {
                                                    @Override
                                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                        dialog.dismiss();
                                                    }
                                                }).show();


                                        break;
                                    case R.id.nav_menu_chat:
                                        Utils.toastySuccess(getApplicationContext(), getString(R.string.soon));
                                        //Intent intentChat = new Intent(FavoritesActivity.this, ChatActivity.class);
                                        //intentChat.putExtra(Utils.UID, (arrayFavorites.get(position).getUid()));
                                        //startActivity(intentChat);
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
                intent.putExtra(Utils.ID_USER, id);
                startActivity(intent);
            }
        });


    }

    private void loadFavorites() {
        dialogs = new ProgressDialog(FavoritesActivity.this);
        dialogs.setMessage(getString(R.string.loading));
        dialogs.setCanceledOnTouchOutside(false);
        dialogs.show();

        favoritesList = new ArrayList<Favorites>();
        adapter = new FavoritesAdapter(FavoritesActivity.this, favoritesList);
        gvFavorite.setAdapter(adapter);

        Ion.with(this)
                .load(Utils.URL_GET_FAVORITES)
                .setBodyParameter(Utils.ID_USER_APP, id)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        try {
                            if (result.size() == 0) {
                                if (dialogs.isShowing()) {
                                    dialogs.dismiss();
                                }
                                gvFavorite.setVisibility(View.INVISIBLE);
                                tvFavorite.setVisibility(View.VISIBLE);
                            } else {
                                for (int i = 0; i < result.size(); i++) {
                                    JsonObject jsonObject = result.get(i).getAsJsonObject();
                                    Favorites f = new Favorites();

                                    f.setId_user(jsonObject.get(Utils.ID_USER).getAsString());
                                    f.setStatus_user(jsonObject.get(Utils.STATUS_USER).getAsString());
                                    f.setUsername_user(jsonObject.get(Utils.USERNAME_USER).getAsString());
                                    f.setLast_seen(jsonObject.get(Utils.LAST_SEEN_USER).getAsString());
                                    f.setImage(jsonObject.get(Utils.IMAGE_USER_1).getAsString());

                                    favoritesList.add(f);

                                }
                                adapter.notifyDataSetChanged();
                                if (dialogs.isShowing()) {
                                    dialogs.dismiss();
                                }
                            }

                        } catch (Exception x) {
                            if (dialogs.isShowing()) {
                                dialogs.dismiss();
                            }
                            Utils.toastyError(getApplicationContext(), x.getMessage());
                        }
                    }
                });

    }

    @Override
    protected void onPause() {
        updateStatus(id, Utils.OFFLINE);
        super.onPause();
    }

    @Override
    protected void onResume() {
        updateStatus(id, Utils.ONLINE);
        loadFavorites();
        super.onResume();
    }

    private void updateStatus(final String id_user, final String status) {
        Ion.with(this)
                .load(Utils.URL_STATUS_USER)
                .setBodyParameter(Utils.ID_USER_APP, id_user)
                .setBodyParameter(Utils.STATUS_USER_APP, status)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            String resultApp = result.get(Utils.STATUS).getAsString();

                            if (Objects.equals(resultApp, Utils.CODE_SUCCESS)) {
                                Log.d(Utils.STATUS, "User " + id_user + " updated the status to: " + status);
                            } else if (Objects.equals(resultApp, Utils.CODE_ERROR)) {
                                Log.d(Utils.STATUS, "updated status failed");
                            }

                        } catch (Exception x) {
                            Utils.toastyError(getApplicationContext(), x.getMessage());
                        }
                    }
                });
    }

}
