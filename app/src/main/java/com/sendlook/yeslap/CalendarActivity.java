package com.sendlook.yeslap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sendlook.yeslap.model.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class CalendarActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ImageView ivMorningSun, ivMorningMon, ivMorningTue, ivMorningWed, ivMorningThu, ivMorningFri, ivMorningSat;
    private ImageView ivAfternoonSun, ivAfternoonMon, ivAfternoonTue, ivAfternoonWed, ivAfternoonThu, ivAfternoonFri, ivAfternoonSat;
    private ImageView ivNightSun, ivNightMon, ivNightTue, ivNightWed, ivNightThu, ivNightFri, ivNightSat;
    private ToggleButton tbAvailabilitySun, tbAvailabilityMon, tbAvailabilityTue, tbAvailabilityWed, tbAvailabilityThu, tbAvailabilityFri, tbAvailabilitySat;
    private ImageView btnGoToProfile, btnGoToSettings;
    private RelativeLayout btnCalendar, btnChat, btnSearch;
    private ProgressDialog dialog;
    private ArrayList<String> arraySundayMorning, arraySundayAfternoon, arraySundayNight, arrayMondayMorning, arrayMondayAfternoon, arrayMondayNight, arrayTuesdayMorning, arrayTuesdayAfternoon, arrayTuesdayNight, arrayWednesdayMorning, arrayWednesdayAfternoon, arrayWednesdayNight,arrayThursdayMorning, arrayThursdayAfternoon, arrayThursdayNight,arrayFridayMorning, arrayFridayAfternoon, arrayFridayNight, arraySaturdayMorning, arraySaturdayAfternoon, arraySaturdayNight;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();

        btnGoToProfile = (ImageView) findViewById(R.id.btnGoToProfile);
        btnGoToSettings = (ImageView) findViewById(R.id.btnGoToSettings);

        btnCalendar = (RelativeLayout) findViewById(R.id.btnCalendar);
        btnChat = (RelativeLayout) findViewById(R.id.btnChat);
        btnSearch = (RelativeLayout) findViewById(R.id.btnSearch);

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

        tbAvailabilitySun = (ToggleButton) findViewById(R.id.tbAvailabilitySun);
        tbAvailabilityMon = (ToggleButton) findViewById(R.id.tbAvailabilityMon);
        tbAvailabilityTue = (ToggleButton) findViewById(R.id.tbAvailabilityTue);
        tbAvailabilityWed = (ToggleButton) findViewById(R.id.tbAvailabilityWed);
        tbAvailabilityThu = (ToggleButton) findViewById(R.id.tbAvailabilityThu);
        tbAvailabilityFri = (ToggleButton) findViewById(R.id.tbAvailabilityFri);
        tbAvailabilitySat = (ToggleButton) findViewById(R.id.tbAvailabilitySat);

        arraySundayMorning = new ArrayList<String>();
        arraySundayAfternoon = new ArrayList<String>();
        arraySundayNight = new ArrayList<String>();
        arrayMondayMorning = new ArrayList<String>();
        arrayMondayAfternoon = new ArrayList<String>();
        arrayMondayNight = new ArrayList<String>();
        arrayTuesdayMorning = new ArrayList<String>();
        arrayTuesdayAfternoon = new ArrayList<String>();
        arrayTuesdayNight = new ArrayList<String>();
        arrayWednesdayMorning = new ArrayList<String>();
        arrayWednesdayAfternoon = new ArrayList<String>();
        arrayWednesdayNight = new ArrayList<String>();
        arrayThursdayMorning = new ArrayList<String>();
        arrayThursdayAfternoon = new ArrayList<String>();
        arrayThursdayNight = new ArrayList<String>();
        arrayFridayMorning = new ArrayList<String>();
        arrayFridayAfternoon = new ArrayList<String>();
        arrayFridayNight = new ArrayList<String>();
        arraySaturdayMorning = new ArrayList<String>();
        arraySaturdayAfternoon = new ArrayList<String>();
        arraySaturdayNight = new ArrayList<String>();

        getCalendar();

        btnGoToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnGoToSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CalendarActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.toastyInfo(getApplicationContext(), getString(R.string.are_you_already_here));
            }
        });

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CalendarActivity.this, ChatMessagesActivity.class);
                startActivity(intent);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUsernameAndImage();
            }
        });

        //SUNDAY
        ivMorningSun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCalendar(Utils.SUNDAY, Utils.MORNING, tbAvailabilitySun);
            }
        });

        ivAfternoonSun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCalendar(Utils.SUNDAY, Utils.AFTERNOON, tbAvailabilitySun);
            }
        });

        ivNightSun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCalendar(Utils.SUNDAY, Utils.NIGHT, tbAvailabilitySun);
            }
        });

        //MONDAY
        ivMorningMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCalendar(Utils.MONDAY, Utils.MORNING, tbAvailabilityMon);
            }
        });

        ivAfternoonMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCalendar(Utils.MONDAY, Utils.AFTERNOON, tbAvailabilityMon);
            }
        });

        ivNightMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCalendar(Utils.MONDAY, Utils.NIGHT, tbAvailabilityMon);
            }
        });

        //TUESDAY
        ivMorningTue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCalendar(Utils.TUESDAY, Utils.MORNING, tbAvailabilityTue);
            }
        });

        ivAfternoonTue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCalendar(Utils.TUESDAY, Utils.AFTERNOON, tbAvailabilityTue);
            }
        });

        ivNightTue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCalendar(Utils.TUESDAY, Utils.NIGHT, tbAvailabilityTue);
            }
        });

        //WEDNESDAY
        ivMorningWed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCalendar(Utils.WEDNESDAY, Utils.MORNING, tbAvailabilityWed);
            }
        });

        ivAfternoonWed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCalendar(Utils.WEDNESDAY, Utils.AFTERNOON, tbAvailabilityWed);
            }
        });

        ivNightWed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCalendar(Utils.WEDNESDAY, Utils.NIGHT, tbAvailabilityWed);
            }
        });

        //THURSDAY
        ivMorningThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCalendar(Utils.THURSDAY, Utils.MORNING, tbAvailabilityThu);
            }
        });

        ivAfternoonThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCalendar(Utils.THURSDAY, Utils.AFTERNOON, tbAvailabilityThu);
            }
        });

        ivNightThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCalendar(Utils.THURSDAY, Utils.NIGHT, tbAvailabilityThu);
            }
        });

        //FRIDAY
        ivMorningFri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCalendar(Utils.FRIDAY, Utils.MORNING, tbAvailabilityFri);
            }
        });

        ivAfternoonFri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCalendar(Utils.FRIDAY, Utils.AFTERNOON, tbAvailabilityFri);
            }
        });

        ivNightFri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCalendar(Utils.FRIDAY, Utils.NIGHT, tbAvailabilityFri);
            }
        });

        //SATURDAY
        ivMorningSat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCalendar(Utils.SATURDAY, Utils.MORNING, tbAvailabilitySat);
            }
        });

        ivAfternoonSat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCalendar(Utils.SATURDAY, Utils.AFTERNOON, tbAvailabilitySat);
            }
        });

        ivNightSat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCalendar(Utils.SATURDAY, Utils.NIGHT, tbAvailabilitySat);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if  (mAuth != null) {
            setStatusOnline();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if  (mAuth != null) {
            setStatusOffline();
        }
    }

    private void setCalendar(String week, String state, ToggleButton toogle) {
        //Check if the button is activated
        if (!toogle.isChecked()) {
            Utils.toastyInfo(getApplicationContext(), "Please, active the button");
        } else {
            //Dialog
            dialog = new ProgressDialog(CalendarActivity.this);
            dialog.setMessage(getString(R.string.loading));
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            //Saving at the Firebase
            mDatabase = FirebaseDatabase.getInstance().getReference().child("calendar").child(week).child(state).push();
            HashMap<String, String> uid = new HashMap<>();
            uid.put("uid", mAuth.getCurrentUser().getUid());
            mDatabase.setValue(uid).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        dialog.dismiss();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Utils.toastyError(getApplicationContext(), e.getMessage());
                }
            });

        }

    }

    private void checkUsernameAndImage() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Utils.USERS).child(mAuth.getCurrentUser().getUid());
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username = dataSnapshot.child("username").getValue(String.class);
                String image = dataSnapshot.child("image1").getValue(String.class);

                if (Objects.equals(username, "Username") || Objects.equals(image, "")) {
                    Utils.toastyInfo(getApplicationContext(), getString(R.string.please_change_image_username));
                } else {
                    Intent intent = new Intent(CalendarActivity.this, FindUsersActivity.class);
                    startActivity(intent);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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

    private void getCalendar() {
        getCalendar(Utils.SUNDAY, arraySundayMorning, arraySundayAfternoon, arraySundayNight, ivMorningSun, ivAfternoonSun, ivNightSun);
        getCalendar(Utils.MONDAY, arrayMondayMorning, arrayMondayAfternoon, arrayMondayNight, ivMorningMon, ivAfternoonMon, ivNightMon);
        getCalendar(Utils.TUESDAY, arrayTuesdayMorning, arrayTuesdayAfternoon, arrayTuesdayNight, ivMorningTue, ivAfternoonTue, ivNightTue);
        getCalendar(Utils.WEDNESDAY, arrayWednesdayMorning, arrayWednesdayAfternoon, arrayWednesdayNight, ivMorningWed, ivAfternoonWed, ivNightWed);
        getCalendar(Utils.THURSDAY, arrayThursdayMorning, arrayThursdayAfternoon, arrayThursdayNight, ivMorningThu, ivAfternoonThu, ivNightThu);
        getCalendar(Utils.FRIDAY, arrayFridayMorning, arrayFridayAfternoon, arrayFridayNight, ivMorningFri, ivAfternoonFri, ivNightFri);
        getCalendar(Utils.SATURDAY, arraySaturdayMorning, arraySaturdayAfternoon, arraySaturdayNight, ivMorningSat, ivAfternoonSat, ivNightSat);
    }

    private void getCalendar(String week, final ArrayList<String> arrayMorning, final ArrayList<String> arrayAfternoon, final ArrayList<String> arrayNight, final ImageView ivMorning, final ImageView ivAfternoon, final ImageView ivNight) {
        //Morning
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Utils.CALENDAR).child(week).child("morning");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot i: dataSnapshot.getChildren()) {
                    String r1 = i.getValue().toString().replace("{uid=", "");
                    String r2 = r1.replace("}","");
                    arrayMorning.add(r2);
                }

                if (arrayMorning.contains(uid)) {
                    ivMorning.setImageResource(R.drawable.iconmorningon);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Afternoon
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Utils.CALENDAR).child(week).child("afternoon");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot i: dataSnapshot.getChildren()) {
                    String r1 = i.getValue().toString().replace("{uid=", "");
                    String r2 = r1.replace("}","");
                    arrayAfternoon.add(r2);
                }

                if (arrayAfternoon.contains(uid)) {
                    ivAfternoon.setImageResource(R.drawable.iconafternoonon);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Night
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Utils.CALENDAR).child(week).child("night");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot i: dataSnapshot.getChildren()) {
                    String r1 = i.getValue().toString().replace("{uid=", "");
                    String r2 = r1.replace("}","");
                    arrayNight.add(r2);
                }

                if (arrayNight.contains(uid)) {
                    ivNight.setImageResource(R.drawable.iconnighton);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
