package br.sendlook.yeslap.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.sendlook.yeslap.R;
import br.sendlook.yeslap.model.UsersAdapter;
import br.sendlook.yeslap.view.Users;
import br.sendlook.yeslap.view.Utils;
import ru.whalemare.sheetmenu.SheetMenu;

public class FindUsersActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView lstUsers;
    private ImageView ivBgSun, ivBgMon, ivBgTue, ivBgWed, ivBgThu, ivBgFri, ivBgSat;
    private TextView tvSun, tvMon, tvTue, tvWed, tvThu, tvFri, tvSat;
    private ImageView ivMorning, ivAfternoon, ivNight;
    private ImageView btnGoToProfile, btnGoToSettings;
    private ProgressDialog dialog;
    private String id, genderSearch, ageSearchMin, ageSearchMax;

    private UsersAdapter adapter;
    private List<Users> usersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_users);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString(Utils.ID_USER);
            genderSearch = bundle.getString(Utils.GENDER_SEARCH);
            ageSearchMin = bundle.getString(Utils.AGE_SEARCH_MIN);
            ageSearchMax = bundle.getString(Utils.AGE_SEARCH_MAX);
        }

        lstUsers = (ListView) findViewById(R.id.lstUsers);

        ivBgSun = (ImageView) findViewById(R.id.ivBgSun);
        findViewById(R.id.ivBgSun).setOnClickListener(this);
        ivBgMon = (ImageView) findViewById(R.id.ivBgMon);
        findViewById(R.id.ivBgMon).setOnClickListener(this);
        ivBgTue = (ImageView) findViewById(R.id.ivBgTue);
        findViewById(R.id.ivBgTue).setOnClickListener(this);
        ivBgWed = (ImageView) findViewById(R.id.ivBgWed);
        findViewById(R.id.ivBgWed).setOnClickListener(this);
        ivBgThu = (ImageView) findViewById(R.id.ivBgThu);
        findViewById(R.id.ivBgThu).setOnClickListener(this);
        ivBgFri = (ImageView) findViewById(R.id.ivBgFri);
        findViewById(R.id.ivBgFri).setOnClickListener(this);
        ivBgSat = (ImageView) findViewById(R.id.ivBgSat);
        findViewById(R.id.ivBgSat).setOnClickListener(this);

        tvSun = (TextView) findViewById(R.id.tvSun);
        tvMon = (TextView) findViewById(R.id.tvMon);
        tvTue = (TextView) findViewById(R.id.tvTue);
        tvWed = (TextView) findViewById(R.id.tvWed);
        tvThu = (TextView) findViewById(R.id.tvThu);
        tvFri = (TextView) findViewById(R.id.tvFri);
        tvSat = (TextView) findViewById(R.id.tvSat);

        ivMorning = (ImageView) findViewById(R.id.ivMorning);
        ivAfternoon = (ImageView) findViewById(R.id.ivAfternoon);
        ivNight = (ImageView) findViewById(R.id.ivNight);

        btnGoToProfile = (ImageView) findViewById(R.id.btnGoToProfile);
        btnGoToSettings = (ImageView) findViewById(R.id.btnGoToSettings);

        btnGoToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnGoToSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FindUsersActivity.this, SettingsActivity.class);
                intent.putExtra(Utils.ID_USER, id);
                startActivity(intent);
            }
        });

        ivBgSun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivBgSun.setImageResource(R.drawable.iconbubbleweekon);
                tvSun.setTextColor(Color.WHITE);
            }
        });

        ivBgMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivBgMon.setImageResource(R.drawable.iconbubbleweekon);
                tvMon.setTextColor(Color.WHITE);
            }
        });

        ivBgTue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivBgTue.setImageResource(R.drawable.iconbubbleweekon);
                tvTue.setTextColor(Color.WHITE);
            }
        });

        ivBgWed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivBgWed.setImageResource(R.drawable.iconbubbleweekon);
                tvWed.setTextColor(Color.WHITE);
            }
        });

        ivBgThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivBgThu.setImageResource(R.drawable.iconbubbleweekon);
                tvThu.setTextColor(Color.WHITE);
            }
        });

        ivBgFri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivBgFri.setImageResource(R.drawable.iconbubbleweekon);
                tvFri.setTextColor(Color.WHITE);
            }
        });

        ivBgSat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivBgSat.setImageResource(R.drawable.iconbubbleweekon);
                tvSat.setTextColor(Color.WHITE);
            }
        });

        ivMorning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivMorning.setImageResource(R.drawable.iconmorningon);
            }
        });

        ivAfternoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivAfternoon.setImageResource(R.drawable.iconafternoonon);
            }
        });

        ivNight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivNight.setImageResource(R.drawable.iconnighton);
            }
        });

        lstUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
                final Users user = (Users) parent.getAdapter().getItem(i);

                SheetMenu.with(FindUsersActivity.this)
                        .setTitle(user.getUsername_user())
                        .setMenu(R.menu.menu_find)
                        .setClick(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                switch (menuItem.getItemId()) {
                                    case R.id.nav_menu_view_profile:
                                        Intent intent = new Intent(FindUsersActivity.this, ProfileActivity.class);
                                        intent.putExtra(Utils.ID_USER, id);
                                        intent.putExtra(Utils.ID_FAVORITE_USER_APP, user.getId_user());
                                        startActivity(intent);
                                        break;
                                    case R.id.nav_menu_chat:
                                        Intent intentChat = new Intent(FindUsersActivity.this, ChatActivity.class);
                                        intentChat.putExtra(Utils.UID_SENDER, id);
                                        intentChat.putExtra(Utils.UID_ADDRESSEE, user.getId_user());
                                        intentChat.putExtra(Utils.USERNAME, user.getUsername_user());
                                        startActivity(intentChat);
                                        break;
                                }
                                return false;
                            }
                        }).show();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadUsers();
        dialog.dismiss();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStatus(id, Utils.ONLINE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        updateStatus(id, Utils.OFFLINE);
    }

    private void loadUsers() {

        dialog = new ProgressDialog(FindUsersActivity.this);
        dialog.setMessage(getString(R.string.loading));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        usersList = new ArrayList<Users>();
        adapter = new UsersAdapter(FindUsersActivity.this, usersList);
        lstUsers.setAdapter(adapter);

        Ion.with(getApplicationContext())
                .load(Utils.URL_FIND_USERS)
                .setBodyParameter(Utils.GENDER_SEARCH_APP, genderSearch)
                .setBodyParameter(Utils.AGE_SEARCH_MIN_APP, ageSearchMin)
                .setBodyParameter(Utils.AGE_SEARCH_MAX_APP, ageSearchMax)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        try {

                            for (int i = 0; i < result.size(); i++) {
                                JsonObject jsonObject = result.get(i).getAsJsonObject();

                                if (!Objects.equals(jsonObject.get(Utils.ID_USER).getAsString(), id) && !Objects.equals(genderSearch, " ") && !Objects.equals(ageSearchMin, " ") && !Objects.equals(ageSearchMax, " ")) {
                                    Users u = new Users();
                                    u.setId_user(jsonObject.get(Utils.ID_USER).getAsString());
                                    u.setStatus_user(jsonObject.get(Utils.STATUS_USER).getAsString());
                                    u.setUsername_user(jsonObject.get(Utils.USERNAME_USER).getAsString());
                                    u.setSun_m(jsonObject.get(Utils.SUNDAY_M).getAsString());
                                    u.setSun_a(jsonObject.get(Utils.SUNDAY_A).getAsString());
                                    u.setSun_n(jsonObject.get(Utils.SUNDAY_N).getAsString());
                                    u.setMon_m(jsonObject.get(Utils.MONDAY_M).getAsString());
                                    u.setMon_a(jsonObject.get(Utils.MONDAY_A).getAsString());
                                    u.setMon_n(jsonObject.get(Utils.MONDAY_N).getAsString());
                                    u.setTue_m(jsonObject.get(Utils.TUESDAY_M).getAsString());
                                    u.setTue_a(jsonObject.get(Utils.TUESDAY_A).getAsString());
                                    u.setTue_n(jsonObject.get(Utils.TUESDAY_N).getAsString());
                                    u.setWed_m(jsonObject.get(Utils.WEDNESDAY_M).getAsString());
                                    u.setWed_a(jsonObject.get(Utils.WEDNESDAY_A).getAsString());
                                    u.setWed_n(jsonObject.get(Utils.WEDNESDAY_N).getAsString());
                                    u.setThu_m(jsonObject.get(Utils.THURSDAY_M).getAsString());
                                    u.setThu_a(jsonObject.get(Utils.THURSDAY_A).getAsString());
                                    u.setThu_n(jsonObject.get(Utils.THURSDAY_N).getAsString());
                                    u.setFri_m(jsonObject.get(Utils.FRIDAY_M).getAsString());
                                    u.setFri_a(jsonObject.get(Utils.FRIDAY_A).getAsString());
                                    u.setFri_n(jsonObject.get(Utils.FRIDAY_N).getAsString());
                                    u.setSat_m(jsonObject.get(Utils.SATURDAY_M).getAsString());
                                    u.setSat_a(jsonObject.get(Utils.SATURDAY_A).getAsString());
                                    u.setSat_n(jsonObject.get(Utils.SATURDAY_N).getAsString());
                                    usersList.add(u);
                                }

                            }

                            adapter.notifyDataSetChanged();

                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }

                        } catch (Exception x) {
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            Utils.toastyError(getApplicationContext(), x.getMessage());
                        }
                    }
                });

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
