package com.sendlook.yeslap;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class FindUsersActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ListView lstUsers;
    private ImageView ivBgOffSun, ivBgOffMon, ivBgOffTue, ivBgOffWed, ivBgOffThu, ivBgOffFri, ivBgOffSat;
    private TextView tvSun, tvMon, tvTue, tvWed, tvThu, tvFri, tvSat;
    private ImageView ivMorning, ivAfternoon,ivNight;
    private ArrayList<String> arrayUsers;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_users);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users_list");
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

    }
}
