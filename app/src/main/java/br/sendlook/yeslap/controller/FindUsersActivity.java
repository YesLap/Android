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
    private TextView tvSun, tvMon, tvTue, tvWed, tvThu, tvFri, tvSat, tvNoUsers;
    private ImageView ivMorning, ivAfternoon, ivNight;
    private ImageView btnGoToProfile, btnGoToSettings;
    private ProgressDialog dialog;
    private String id, genderSearch, ageSearchMin, ageSearchMax;
    private Boolean sun = false, mon = false, tue = false, wed = false, thu = false, fri = false, sat = false, m = false, a = false, n = false;

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

        tvNoUsers = (TextView) findViewById(R.id.tvNoUsers);

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
                if (!sun) {
                    ivBgSun.setBackground(getResources().getDrawable(R.drawable.iconbubbleweekon));
                    tvSun.setTextColor(Color.WHITE);
                    sun = true;
                    loadUsersCalendar();
                } else {
                    ivBgSun.setBackground(getResources().getDrawable(R.drawable.iconbubbleweekoff));
                    tvSun.setTextColor(getResources().getColor(R.color.colorLightBlue));
                    sun = false;
                }
                //checkToEnableCalendar();
            }
        });

        ivBgMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!mon) {
                    ivBgMon.setBackground(getResources().getDrawable(R.drawable.iconbubbleweekon));
                    tvMon.setTextColor(Color.WHITE);
                    mon = true;
                    loadUsersCalendar();
                } else {
                    ivBgMon.setBackground(getResources().getDrawable(R.drawable.iconbubbleweekoff));
                    tvMon.setTextColor(getResources().getColor(R.color.colorLightBlue));
                    mon = false;
                }
                //checkToEnableCalendar();
            }
        });

        ivBgTue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!tue) {
                    ivBgTue.setBackground(getResources().getDrawable(R.drawable.iconbubbleweekon));
                    tvTue.setTextColor(Color.WHITE);
                    tue = true;
                    loadUsersCalendar();
                } else {
                    ivBgTue.setBackground(getResources().getDrawable(R.drawable.iconbubbleweekoff));
                    tvTue.setTextColor(getResources().getColor(R.color.colorLightBlue));
                    tue = false;
                }
                //checkToEnableCalendar();
            }
        });

        ivBgWed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!wed) {
                    ivBgWed.setBackground(getResources().getDrawable(R.drawable.iconbubbleweekon));
                    tvWed.setTextColor(Color.WHITE);
                    wed = true;
                    loadUsersCalendar();
                } else {
                    ivBgWed.setBackground(getResources().getDrawable(R.drawable.iconbubbleweekoff));
                    tvWed.setTextColor(getResources().getColor(R.color.colorLightBlue));
                    wed = false;
                }
                //checkToEnableCalendar();
            }
        });

        ivBgThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!thu) {
                    ivBgThu.setBackground(getResources().getDrawable(R.drawable.iconbubbleweekon));
                    tvThu.setTextColor(Color.WHITE);
                    thu = true;
                    loadUsersCalendar();
                } else {
                    ivBgThu.setBackground(getResources().getDrawable(R.drawable.iconbubbleweekoff));
                    tvThu.setTextColor(getResources().getColor(R.color.colorLightBlue));
                    thu = false;
                }
                //checkToEnableCalendar();
            }
        });

        ivBgFri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!fri) {
                    ivBgFri.setBackground(getResources().getDrawable(R.drawable.iconbubbleweekon));
                    tvFri.setTextColor(Color.WHITE);
                    fri = true;
                    loadUsersCalendar();
                } else {
                    ivBgFri.setBackground(getResources().getDrawable(R.drawable.iconbubbleweekoff));
                    tvFri.setTextColor(getResources().getColor(R.color.colorLightBlue));
                    fri = false;
                }
                checkToEnableCalendar();
            }
        });

        ivBgSat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!sat) {
                    ivBgSat.setBackground(getResources().getDrawable(R.drawable.iconbubbleweekon));
                    tvSat.setTextColor(Color.WHITE);
                    sat = true;
                    loadUsersCalendar();
                } else {
                    ivBgSat.setBackground(getResources().getDrawable(R.drawable.iconbubbleweekoff));
                    tvSat.setTextColor(getResources().getColor(R.color.colorLightBlue));
                    sat = false;
                }
                //checkToEnableCalendar();
            }
        });

        ivMorning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!m) {
                    ivMorning.setBackground(getResources().getDrawable(R.drawable.iconmorningon));
                    m = true;
                } else {
                    ivMorning.setBackground(getResources().getDrawable(R.drawable.iconmorningoff));
                    m = false;
                }
                checkToEnableCalendar();
            }
        });

        ivAfternoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!a) {
                    ivAfternoon.setBackground(getResources().getDrawable(R.drawable.iconafternoonon));
                    a = true;
                } else {
                    ivAfternoon.setBackground(getResources().getDrawable(R.drawable.iconafternoonoff));
                    a = false;
                }
                checkToEnableCalendar();
            }
        });

        ivNight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!n) {
                    ivNight.setBackground(getResources().getDrawable(R.drawable.iconnighton));
                    n = true;
                } else {
                    ivNight.setBackground(getResources().getDrawable(R.drawable.iconnightoff));
                    n = false;
                }
                checkToEnableCalendar();
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

    private void checkToEnableCalendar() {
        if (!m && !a && !n) {
            ivBgSun.setBackground(getResources().getDrawable(R.drawable.iconbubbleweekdisable));
            ivBgSun.setEnabled(false);
            ivBgMon.setBackground(getResources().getDrawable(R.drawable.iconbubbleweekdisable));
            ivBgMon.setEnabled(false);
            ivBgTue.setBackground(getResources().getDrawable(R.drawable.iconbubbleweekdisable));
            ivBgTue.setEnabled(false);
            ivBgWed.setBackground(getResources().getDrawable(R.drawable.iconbubbleweekdisable));
            ivBgWed.setEnabled(false);
            ivBgThu.setBackground(getResources().getDrawable(R.drawable.iconbubbleweekdisable));
            ivBgThu.setEnabled(false);
            ivBgFri.setBackground(getResources().getDrawable(R.drawable.iconbubbleweekdisable));
            ivBgFri.setEnabled(false);
            ivBgSat.setBackground(getResources().getDrawable(R.drawable.iconbubbleweekdisable));
            ivBgSat.setEnabled(false);
            tvSun.setTextColor(getResources().getColor(R.color.colorGrey));
            tvMon.setTextColor(getResources().getColor(R.color.colorGrey));
            tvTue.setTextColor(getResources().getColor(R.color.colorGrey));
            tvWed.setTextColor(getResources().getColor(R.color.colorGrey));
            tvThu.setTextColor(getResources().getColor(R.color.colorGrey));
            tvFri.setTextColor(getResources().getColor(R.color.colorGrey));
            tvSat.setTextColor(getResources().getColor(R.color.colorGrey));
        } else {
            if (!sun) {
                tvSun.setTextColor(getResources().getColor(R.color.colorLightBlue));
                ivBgSun.setBackground(getResources().getDrawable(R.drawable.iconbubbleweekoff));
                ivBgSun.setEnabled(true);
            }
            if (!mon) {
                tvMon.setTextColor(getResources().getColor(R.color.colorLightBlue));
                ivBgMon.setBackground(getResources().getDrawable(R.drawable.iconbubbleweekoff));
                ivBgMon.setEnabled(true);
            }
            if (!tue) {
                tvTue.setTextColor(getResources().getColor(R.color.colorLightBlue));
                ivBgTue.setBackground(getResources().getDrawable(R.drawable.iconbubbleweekoff));
                ivBgTue.setEnabled(true);
            }
            if (!wed) {
                tvWed.setTextColor(getResources().getColor(R.color.colorLightBlue));
                ivBgWed.setBackground(getResources().getDrawable(R.drawable.iconbubbleweekoff));
                ivBgWed.setEnabled(true);
            }
            if (!thu) {
                tvThu.setTextColor(getResources().getColor(R.color.colorLightBlue));
                ivBgThu.setBackground(getResources().getDrawable(R.drawable.iconbubbleweekoff));
                ivBgThu.setEnabled(true);
            }
            if (!fri) {
                tvFri.setTextColor(getResources().getColor(R.color.colorLightBlue));
                ivBgFri.setBackground(getResources().getDrawable(R.drawable.iconbubbleweekoff));
                ivBgFri.setEnabled(true);
            }
            if (!sat) {
                tvSat.setTextColor(getResources().getColor(R.color.colorLightBlue));
                ivBgSat.setBackground(getResources().getDrawable(R.drawable.iconbubbleweekoff));
                ivBgSat.setEnabled(true);
            }

        }
    }

    private String calendarSQL() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM tb_calendar WHERE");

        if (sun) {
            if (m) {
                sql.append(" sun_m = :ID OR");
            }
            if (a) {
                sql.append(" sun_a = :ID OR");
            }
            if (n) {
                sql.append(" sun_n = :ID OR");
            }
        }
        if (mon) {
            if (m) {
                sql.append(" mon_m = :ID OR");
            }
            if (a) {
                sql.append(" mon_a = :ID OR");
            }
            if (n) {
                sql.append(" mon_n = :ID OR");
            }
        }
        if (tue) {
            if (m) {
                sql.append(" tue_m = :ID OR");
            }
            if (a) {
                sql.append(" tue_a = :ID OR");
            }
            if (n) {
                sql.append(" tue_n = :ID OR");
            }
        }
        if (wed) {
            if (m) {
                sql.append(" wed_m = :ID OR");
            }
            if (a) {
                sql.append(" wed_a = :ID OR");
            }
            if (n) {
                sql.append(" wed_n = :ID OR");
            }
        }
        if (thu) {
            if (m) {
                sql.append(" thu_m = :ID OR");
            }
            if (a) {
                sql.append(" thu_a = :ID OR");
            }
            if (n) {
                sql.append(" thu_n = :ID OR");
            }
        }
        if (fri) {
            if (m) {
                sql.append(" fri_m = :ID OR");
            }
            if (a) {
                sql.append(" fri_a = :ID OR");
            }
            if (n) {
                sql.append(" fri_n = :ID OR");
            }
        }
        if (sat) {
            if (m) {
                sql.append(" sat_m = :ID OR");
            }
            if (a) {
                sql.append(" sat_a = :ID OR");
            }
            if (n) {
                sql.append(" sat_n = :ID OR");
            }
        }

        String newsql = sql.substring(0, sql.length() - 2);
        Log.d("QWERTY", newsql);

        return newsql;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkToEnableCalendar();
        updateStatus(id, Utils.ONLINE);
        loadUsers();
    }

    @Override
    protected void onPause() {
        super.onPause();
        updateStatus(id, Utils.OFFLINE);
    }

    private void loadUsersCalendar() {

        dialog = new ProgressDialog(FindUsersActivity.this);
        dialog.setMessage(getString(R.string.loading));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        usersList = new ArrayList<Users>();
        adapter = new UsersAdapter(FindUsersActivity.this, usersList);
        lstUsers.setAdapter(adapter);

        Ion.with(this)
                .load(Utils.URL_FIND_USERS_CALENDAR)
                .setBodyParameter(Utils.ID_USER_APP, id)
                .setBodyParameter(Utils.GENDER_SEARCH_APP, genderSearch)
                .setBodyParameter(Utils.AGE_SEARCH_MIN_APP, ageSearchMin)
                .setBodyParameter(Utils.AGE_SEARCH_MAX_APP, ageSearchMax)
                .setBodyParameter(Utils.SQL_CALENDAR, calendarSQL())
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        try {
                            if (result.size() == 0) {
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                tvNoUsers.setVisibility(View.VISIBLE);
                            } else {
                                tvNoUsers.setVisibility(View.VISIBLE);
                                for (int i = 0; i < result.size(); i++) {
                                    JsonObject jsonObject = result.get(i).getAsJsonObject();

                                    tvNoUsers.setVisibility(View.GONE);
                                    Users u = new Users();
                                    u.setId_user(jsonObject.get(Utils.ID_USER).getAsString());
                                    u.setStatus_user(jsonObject.get(Utils.STATUS_USER).getAsString());
                                    u.setUsername_user(jsonObject.get(Utils.USERNAME_USER).getAsString());
                                    u.setImage_user(jsonObject.get(Utils.IMAGE_USER_1).getAsString());
                                    u.setDistance(jsonObject.get(Utils.DISTANCE).getAsString());
                                    u.setDiff_distance(jsonObject.get(Utils.DIFF_DISTANCE).getAsString());
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

    private void loadUsers() {

        dialog = new ProgressDialog(FindUsersActivity.this);
        dialog.setMessage(getString(R.string.loading));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        usersList = new ArrayList<Users>();
        adapter = new UsersAdapter(FindUsersActivity.this, usersList);
        lstUsers.setAdapter(adapter);

        Ion.with(this)
                .load(Utils.URL_FIND_USERS)
                .setBodyParameter(Utils.ID_USER_APP, id)
                .setBodyParameter(Utils.GENDER_SEARCH_APP, genderSearch)
                .setBodyParameter(Utils.AGE_SEARCH_MIN_APP, ageSearchMin)
                .setBodyParameter(Utils.AGE_SEARCH_MAX_APP, ageSearchMax)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        try {
                            if (result.size() == 0) {
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                tvNoUsers.setVisibility(View.VISIBLE);
                            } else {
                                tvNoUsers.setVisibility(View.VISIBLE);
                                for (int i = 0; i < result.size(); i++) {
                                    JsonObject jsonObject = result.get(i).getAsJsonObject();

                                    tvNoUsers.setVisibility(View.GONE);
                                    Users u = new Users();
                                    u.setId_user(jsonObject.get(Utils.ID_USER).getAsString());
                                    u.setStatus_user(jsonObject.get(Utils.STATUS_USER).getAsString());
                                    u.setUsername_user(jsonObject.get(Utils.USERNAME_USER).getAsString());
                                    u.setImage_user(jsonObject.get(Utils.IMAGE_USER_1).getAsString());
                                    u.setDistance(jsonObject.get(Utils.DISTANCE).getAsString());
                                    u.setDiff_distance(jsonObject.get(Utils.DIFF_DISTANCE).getAsString());
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
