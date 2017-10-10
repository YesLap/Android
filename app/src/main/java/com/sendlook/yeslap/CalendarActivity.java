package com.sendlook.yeslap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.Objects;

public class CalendarActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ImageView ivMorningSun, ivMorningMon, ivMorningTue, ivMorningWed, ivMorningThu, ivMorningFri, ivMorningSat;
    private ImageView ivAfternoonSun, ivAfternoonMon, ivAfternoonTue, ivAfternoonWed, ivAfternoonThu, ivAfternoonFri, ivAfternoonSat;
    private ImageView ivNightSun, ivNightMon, ivNightTue, ivNightWed, ivNightThu, ivNightFri, ivNightSat;
    private ImageView ivAvailabilitySun, ivAvailabilityMon, ivAvailabilityTue, ivAvailabilityWed, ivAvailabilityThu, ivAvailabilityFri, ivAvailabilitySat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        mAuth = FirebaseAuth.getInstance();

        ivMorningSun = (ImageView) findViewById(R.id.ivMorningSun);
        ivMorningMon = (ImageView) findViewById(R.id.ivMorningMon);
        ivMorningTue = (ImageView) findViewById(R.id.ivMorningTue);
        ivMorningWed = (ImageView) findViewById(R.id.ivMorningWed);
        ivMorningThu = (ImageView) findViewById(R.id.ivMorningThu);
        ivMorningFri = (ImageView) findViewById(R.id.ivMorningFri);
        ivMorningSat = (ImageView) findViewById(R.id.ivMorningSat);

        ivAfternoonSun = (ImageView) findViewById(R.id.ivAfternoonSun);
        ivAfternoonMon = (ImageView) findViewById(R.id.ivAfternoonMon);
        ivAfternoonTue = (ImageView) findViewById(R.id.ivAfternoonTue);
        ivAfternoonWed = (ImageView) findViewById(R.id.ivAfternoonWed);
        ivAfternoonThu = (ImageView) findViewById(R.id.ivAfternoonThu);
        ivAfternoonFri = (ImageView) findViewById(R.id.ivAfternoonFri);
        ivAfternoonSat = (ImageView) findViewById(R.id.ivAfternoonSat);

        ivNightSun = (ImageView) findViewById(R.id.ivNightSun);
        ivNightMon = (ImageView) findViewById(R.id.ivNightMon);
        ivNightTue = (ImageView) findViewById(R.id.ivNightTue);
        ivNightWed = (ImageView) findViewById(R.id.ivNightWed);
        ivNightThu = (ImageView) findViewById(R.id.ivNightThu);
        ivNightFri = (ImageView) findViewById(R.id.ivNightFri);
        ivNightSat = (ImageView) findViewById(R.id.ivNightSat);

        ivAvailabilitySun = (ImageView) findViewById(R.id.ivAvailabilitySun);
        ivAvailabilityMon = (ImageView) findViewById(R.id.ivAvailabilityMon);
        ivAvailabilityTue = (ImageView) findViewById(R.id.ivAvailabilityTue);
        ivAvailabilityWed = (ImageView) findViewById(R.id.ivAvailabilityWed);
        ivAvailabilityThu = (ImageView) findViewById(R.id.ivAvailabilityThu);
        ivAvailabilityFri = (ImageView) findViewById(R.id.ivAvailabilityFri);
        ivAvailabilitySat = (ImageView) findViewById(R.id.ivAvailabilitySat);


        ivMorningSun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCalendar(ivMorningSun, ivAvailabilitySun);
            }
        });

        ivAfternoonSun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCalendar(ivAfternoonSun, ivAfternoonSun);
            }
        });

        ivNightSun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCalendar(ivNightSun, ivAfternoonSun);
            }
        });


    }

    private void setCalendar(ImageView image, ImageView toogle) {
        if (Objects.equals(toogle.getResources(), getDrawable(R.drawable.off))) {
            Utils.toastyInfo(getApplicationContext(), getString(R.string.click_red_button));
        }

    }

}
