package br.sendlook.yeslap.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class FindUsersActivity extends AppCompatActivity {

    private ListView lstUsers;
    private ImageView ivBgOffSun, ivBgOffMon, ivBgOffTue, ivBgOffWed, ivBgOffThu, ivBgOffFri, ivBgOffSat;
    private TextView tvSun, tvMon, tvTue, tvWed, tvThu, tvFri, tvSat;
    private ImageView ivMorning, ivAfternoon, ivNight;
    private ImageView btnGoToProfile, btnGoToSettings;
    private ProgressDialog dialog;
    private String id;

    private UsersAdapter adapter;
    private List<Users> usersList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_users);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString(Utils.ID_USER);
        }

        lstUsers = (ListView) findViewById(R.id.lstUsers);

        ivBgOffSun = (ImageView) findViewById(R.id.ivBgOffSun);
        ivBgOffMon = (ImageView) findViewById(R.id.ivBgOffMon);
        ivBgOffTue = (ImageView) findViewById(R.id.ivBgOffTue);
        ivBgOffWed = (ImageView) findViewById(R.id.ivBgOffWed);
        ivBgOffThu = (ImageView) findViewById(R.id.ivBgOffThu);
        ivBgOffFri = (ImageView) findViewById(R.id.ivBgOffFri);
        ivBgOffSat = (ImageView) findViewById(R.id.ivBgOffSat);

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

        ivBgOffSun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivBgOffSun.setImageResource(R.drawable.iconbubbleweekon);
                tvSun.setTextColor(Color.WHITE);
            }
        });

        ivBgOffMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivBgOffMon.setImageResource(R.drawable.iconbubbleweekon);
                tvMon.setTextColor(Color.WHITE);
            }
        });

        ivBgOffTue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivBgOffTue.setImageResource(R.drawable.iconbubbleweekon);
                tvTue.setTextColor(Color.WHITE);
            }
        });

        ivBgOffWed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivBgOffWed.setImageResource(R.drawable.iconbubbleweekon);
                tvWed.setTextColor(Color.WHITE);
            }
        });

        ivBgOffThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivBgOffThu.setImageResource(R.drawable.iconbubbleweekon);
                tvThu.setTextColor(Color.WHITE);
            }
        });

        ivBgOffFri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivBgOffFri.setImageResource(R.drawable.iconbubbleweekon);
                tvFri.setTextColor(Color.WHITE);
            }
        });

        ivBgOffSat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivBgOffSat.setImageResource(R.drawable.iconbubbleweekon);
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
                Users u = (Users) parent.getAdapter().getItem(i);
                Intent intent = new Intent(FindUsersActivity.this, ProfileActivity.class);
                intent.putExtra(Utils.ID_USER, id);
                intent.putExtra(Utils.ID_FAVORITE_USER_APP, u.getId_user());
                startActivity(intent);
            }
        });

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
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                         try {

                             for (int i = 0; i < result.size(); i++) {
                                 JsonObject jsonObject = result.get(i).getAsJsonObject();

                                 if (!Objects.equals(jsonObject.get(Utils.ID_USER).getAsString(), id)) {
                                     Users u = new Users();

                                     u.setId_user(jsonObject.get(Utils.ID_USER).getAsString());
                                     u.setStatus_user(jsonObject.get(Utils.STATUS_USER).getAsString());
                                     u.setUsername_user(jsonObject.get(Utils.USERNAME_USER).getAsString());

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
