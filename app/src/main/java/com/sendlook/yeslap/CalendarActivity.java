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
    private ArrayList<String> arraySundayMorning, arraySundayAfternoon, arraySundayNight, arrayMondayMorning, arrayMondayAfternoon, arrayMondayNight, arrayTuesdayMorning, arrayTuesdayAfternoon, arrayTuesdayNight, arrayWednesdayMorning, arrayWednesdayAfternoon, arrayWednesdayNight, arrayThursdayMorning, arrayThursdayAfternoon, arrayThursdayNight, arrayFridayMorning, arrayFridayAfternoon, arrayFridayNight, arraySaturdayMorning, arraySaturdayAfternoon, arraySaturdayNight;
    private String uid;
    private Boolean morningSun = false, morningMon = false, morningTue = false, morningWed = false, morningThu = false, morningFri = false, morningSat = false;
    private Boolean afternoonSun = false, afternoonMon = false, afternoonTue = false, afternoonWed = false, afternoonThu = false, afternoonFri = false, afternoonSat = false;
    private Boolean nightSun = false, nightMon = false, nightTue = false, nightWed = false, nightThu = false, nightFri = false, nightSat = false;

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
                if (!morningSun) {
                    setCalendar(Utils.SUNDAY, Utils.MORNING, tbAvailabilitySun);
                } else {
                    removeCalendar(Utils.SUNDAY, Utils.MORNING, uid);
                    ivMorningSun.setImageResource(R.drawable.iconmorningoff);
                    morningSun = false;
                }
            }
        });

        ivAfternoonSun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!afternoonSun) {
                    setCalendar(Utils.SUNDAY, Utils.AFTERNOON, tbAvailabilitySun);
                } else {
                    removeCalendar(Utils.SUNDAY, Utils.AFTERNOON, uid);
                    ivAfternoonSun.setImageResource(R.drawable.iconafternoonoff);
                    afternoonSun = false;
                }
            }
        });

        ivNightSun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!nightSun) {
                    setCalendar(Utils.SUNDAY, Utils.NIGHT, tbAvailabilitySun);
                } else {
                    removeCalendar(Utils.SUNDAY, Utils.NIGHT, uid);
                    ivNightSun.setImageResource(R.drawable.iconnightoff);
                    nightSun = false;
                }
            }
        });

        //MONDAY
        ivMorningMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!morningMon) {
                    setCalendar(Utils.MONDAY, Utils.MORNING, tbAvailabilityMon);
                } else {
                    removeCalendar(Utils.MONDAY, Utils.MORNING, uid);
                    ivMorningMon.setImageResource(R.drawable.iconmorningoff);
                    morningMon = false;
                }
            }
        });

        ivAfternoonMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!afternoonMon) {
                    setCalendar(Utils.MONDAY, Utils.AFTERNOON, tbAvailabilityMon);
                } else {
                    removeCalendar(Utils.MONDAY, Utils.AFTERNOON, uid);
                    ivAfternoonMon.setImageResource(R.drawable.iconafternoonoff);
                    afternoonMon = false;
                }
            }
        });

        ivNightMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!nightMon) {
                    setCalendar(Utils.MONDAY, Utils.NIGHT, tbAvailabilityMon);
                } else {
                    removeCalendar(Utils.MONDAY, Utils.NIGHT, uid);
                    ivNightMon.setImageResource(R.drawable.iconnightoff);
                    nightMon = false;
                }
            }
        });

        //TUESDAY
        ivMorningTue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!morningTue) {
                    setCalendar(Utils.TUESDAY, Utils.MORNING, tbAvailabilityTue);
                } else {
                    removeCalendar(Utils.TUESDAY, Utils.MORNING, uid);
                    ivMorningTue.setImageResource(R.drawable.iconmorningoff);
                    morningTue = false;
                }
            }
        });

        ivAfternoonTue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!afternoonTue) {
                    setCalendar(Utils.TUESDAY, Utils.AFTERNOON, tbAvailabilityTue);
                } else {
                    removeCalendar(Utils.TUESDAY, Utils.AFTERNOON, uid);
                    ivAfternoonTue.setImageResource(R.drawable.iconafternoonoff);
                    afternoonTue = false;
                }
            }
        });

        ivNightTue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!nightTue) {
                    setCalendar(Utils.TUESDAY, Utils.NIGHT, tbAvailabilityTue);
                } else {
                    removeCalendar(Utils.TUESDAY, Utils.NIGHT, uid);
                    ivNightTue.setImageResource(R.drawable.iconnightoff);
                    nightTue = false;
                }
            }
        });

        //WEDNESDAY
        ivMorningWed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!morningWed) {
                    setCalendar(Utils.WEDNESDAY, Utils.MORNING, tbAvailabilityWed);
                } else {
                    removeCalendar(Utils.WEDNESDAY, Utils.MORNING, uid);
                    ivMorningWed.setImageResource(R.drawable.iconmorningoff);
                    morningWed = false;
                }
            }
        });

        ivAfternoonWed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!afternoonWed) {
                    setCalendar(Utils.WEDNESDAY, Utils.AFTERNOON, tbAvailabilityWed);
                } else {
                    removeCalendar(Utils.WEDNESDAY, Utils.AFTERNOON, uid);
                    ivAfternoonWed.setImageResource(R.drawable.iconafternoonoff);
                    afternoonWed = false;
                }
            }
        });

        ivNightWed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!nightWed) {
                    setCalendar(Utils.WEDNESDAY, Utils.NIGHT, tbAvailabilityWed);
                } else {
                    removeCalendar(Utils.WEDNESDAY, Utils.NIGHT, uid);
                    ivNightWed.setImageResource(R.drawable.iconnightoff);
                    nightWed = false;
                }
            }
        });

        //THURSDAY
        ivMorningThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!morningThu) {
                    setCalendar(Utils.THURSDAY, Utils.MORNING, tbAvailabilityThu);
                } else {
                    removeCalendar(Utils.THURSDAY, Utils.MORNING, uid);
                    ivMorningThu.setImageResource(R.drawable.iconmorningoff);
                    morningThu = false;
                }
            }
        });

        ivAfternoonThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!afternoonThu) {
                    setCalendar(Utils.THURSDAY, Utils.AFTERNOON, tbAvailabilityThu);
                } else {
                    removeCalendar(Utils.THURSDAY, Utils.AFTERNOON, uid);
                    ivAfternoonThu.setImageResource(R.drawable.iconafternoonoff);
                    afternoonThu = false;
                }
            }
        });

        ivNightThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!nightThu) {
                    setCalendar(Utils.THURSDAY, Utils.NIGHT, tbAvailabilityThu);
                } else {
                    removeCalendar(Utils.THURSDAY, Utils.NIGHT, uid);
                    ivNightThu.setImageResource(R.drawable.iconnightoff);
                    nightThu = false;
                }
            }
        });

        //FRIDAY
        ivMorningFri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!morningFri) {
                    setCalendar(Utils.FRIDAY, Utils.MORNING, tbAvailabilityFri);
                } else {
                    removeCalendar(Utils.FRIDAY, Utils.MORNING, uid);
                    ivMorningFri.setImageResource(R.drawable.iconmorningoff);
                    morningFri = false;
                }
            }
        });

        ivAfternoonFri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!afternoonFri) {
                    setCalendar(Utils.FRIDAY, Utils.AFTERNOON, tbAvailabilityFri);
                } else {
                    removeCalendar(Utils.FRIDAY, Utils.AFTERNOON, uid);
                    ivAfternoonFri.setImageResource(R.drawable.iconafternoonoff);
                    afternoonFri = false;
                }
            }
        });

        ivNightFri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!nightFri) {
                    setCalendar(Utils.FRIDAY, Utils.NIGHT, tbAvailabilityFri);
                } else {
                    removeCalendar(Utils.FRIDAY, Utils.NIGHT, uid);
                    ivNightFri.setImageResource(R.drawable.iconnightoff);
                    nightFri = false;
                }
            }
        });

        //SATURDAY
        ivMorningSat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!morningSat) {
                    setCalendar(Utils.SATURDAY, Utils.MORNING, tbAvailabilitySat);
                } else {
                    removeCalendar(Utils.SATURDAY, Utils.MORNING, uid);
                    ivMorningSat.setImageResource(R.drawable.iconmorningoff);
                    morningSat = false;
                }
            }
        });

        ivAfternoonSat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!afternoonSat) {
                    setCalendar(Utils.SATURDAY, Utils.AFTERNOON, tbAvailabilitySat);
                } else {
                    removeCalendar(Utils.SATURDAY, Utils.AFTERNOON, uid);
                    ivAfternoonSat.setImageResource(R.drawable.iconafternoonoff);
                    afternoonSat = false;
                }
            }
        });

        ivNightSat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!nightSat) {
                    setCalendar(Utils.SATURDAY, Utils.NIGHT, tbAvailabilitySat);
                } else {
                    removeCalendar(Utils.SATURDAY, Utils.NIGHT, uid);
                    ivNightSat.setImageResource(R.drawable.iconnightoff);
                    nightSat = false;
                }
            }
        });

    }

    private void removeCalendar(String week, final String state, String uid) {
        dialog = new ProgressDialog(CalendarActivity.this);
        dialog.setMessage(getString(R.string.loading));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        mDatabase = FirebaseDatabase.getInstance().getReference().child(Utils.CALENDAR).child(week).child(state);
        mDatabase.child(uid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                getCalendar();
                dialog.dismiss();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAuth != null) {
            setStatusOnline();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuth != null) {
            setStatusOffline();
        }
    }

    private void setCalendar(final String week, final String state, ToggleButton toogle) {
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
            mDatabase = FirebaseDatabase.getInstance().getReference().child("calendar").child(week).child(state).child(uid);
            HashMap<String, String> uid = new HashMap<>();
            uid.put("uid", mAuth.getCurrentUser().getUid());
            mDatabase.setValue(uid).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        dialog.dismiss();

                        if (Objects.equals(week, "1") && Objects.equals(state, "morning")) {
                            morningSun = true;
                        } else if (Objects.equals(week, "1") && Objects.equals(state, "afternoon")) {
                            afternoonSun = true;
                        } else if (Objects.equals(week, "1") && Objects.equals(state, "night")) {
                            nightSun = true;
                        } else if (Objects.equals(week, "2") && Objects.equals(state, "morning")) {
                            morningMon = true;
                        } else if (Objects.equals(week, "2") && Objects.equals(state, "afternoon")) {
                            afternoonMon = true;
                        } else if (Objects.equals(week, "2") && Objects.equals(state, "night")) {
                            nightMon = true;
                        } else if (Objects.equals(week, "3") && Objects.equals(state, "morning")) {
                            morningTue = true;
                        } else if (Objects.equals(week, "3") && Objects.equals(state, "afternoon")) {
                            afternoonTue = true;
                        } else if (Objects.equals(week, "3") && Objects.equals(state, "night")) {
                            nightTue = true;
                        } else if (Objects.equals(week, "4") && Objects.equals(state, "morning")) {
                            morningWed = true;
                        } else if (Objects.equals(week, "4") && Objects.equals(state, "afternoon")) {
                            afternoonWed = true;
                        } else if (Objects.equals(week, "4") && Objects.equals(state, "night")) {
                            nightWed = true;
                        } else if (Objects.equals(week, "5") && Objects.equals(state, "morning")) {
                            morningThu = true;
                        } else if (Objects.equals(week, "5") && Objects.equals(state, "afternoon")) {
                            afternoonThu = true;
                        } else if (Objects.equals(week, "5") && Objects.equals(state, "night")) {
                            nightThu = true;
                        } else if (Objects.equals(week, "6") && Objects.equals(state, "morning")) {
                            morningFri = true;
                        } else if (Objects.equals(week, "6") && Objects.equals(state, "afternoon")) {
                            afternoonFri = true;
                        } else if (Objects.equals(week, "6") && Objects.equals(state, "night")) {
                            nightFri = true;
                        } else if (Objects.equals(week, "7") && Objects.equals(state, "morning")) {
                            morningSat = true;
                        } else if (Objects.equals(week, "7") && Objects.equals(state, "afternoon")) {
                            afternoonSat = true;
                        } else if (Objects.equals(week, "7") && Objects.equals(state, "night")) {
                            nightSat = true;
                        }

                        getCalendar();

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
        //cleanImages();
        getCalendar(Utils.SUNDAY, arraySundayMorning, arraySundayAfternoon, arraySundayNight, ivMorningSun, ivAfternoonSun, ivNightSun);
        getCalendar(Utils.MONDAY, arrayMondayMorning, arrayMondayAfternoon, arrayMondayNight, ivMorningMon, ivAfternoonMon, ivNightMon);
        getCalendar(Utils.TUESDAY, arrayTuesdayMorning, arrayTuesdayAfternoon, arrayTuesdayNight, ivMorningTue, ivAfternoonTue, ivNightTue);
        getCalendar(Utils.WEDNESDAY, arrayWednesdayMorning, arrayWednesdayAfternoon, arrayWednesdayNight, ivMorningWed, ivAfternoonWed, ivNightWed);
        getCalendar(Utils.THURSDAY, arrayThursdayMorning, arrayThursdayAfternoon, arrayThursdayNight, ivMorningThu, ivAfternoonThu, ivNightThu);
        getCalendar(Utils.FRIDAY, arrayFridayMorning, arrayFridayAfternoon, arrayFridayNight, ivMorningFri, ivAfternoonFri, ivNightFri);
        getCalendar(Utils.SATURDAY, arraySaturdayMorning, arraySaturdayAfternoon, arraySaturdayNight, ivMorningSat, ivAfternoonSat, ivNightSat);
    }

    private void getCalendar(final String week, final ArrayList<String> arrayMorning, final ArrayList<String> arrayAfternoon, final ArrayList<String> arrayNight, final ImageView ivMorning, final ImageView ivAfternoon, final ImageView ivNight) {
        //Morning
        clearObjects();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Utils.CALENDAR).child(week).child("morning");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot i : dataSnapshot.getChildren()) {
                    String r1 = i.getValue().toString().replace("{uid=", "");
                    String r2 = r1.replace("}", "");
                    arrayMorning.add(r2);
                }

                if (arrayMorning.contains(uid)) {
                    ivMorning.setImageResource(R.drawable.iconmorningon);

                    if (Objects.equals(week, "1")) {
                        morningSun = true;
                    }
                    if (Objects.equals(week, "2")) {
                        morningMon = true;
                    }
                    if (Objects.equals(week, "3")) {
                        morningTue = true;
                    }
                    if (Objects.equals(week, "4")) {
                        morningWed = true;
                    }
                    if (Objects.equals(week, "5")) {
                        morningThu = true;
                    }
                    if (Objects.equals(week, "6")) {
                        morningFri = true;
                    }
                    if (Objects.equals(week, "7")) {
                        morningSat = true;
                    }

                    arrayMorning.clear();

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
                for (DataSnapshot i : dataSnapshot.getChildren()) {
                    String r1 = i.getValue().toString().replace("{uid=", "");
                    String r2 = r1.replace("}", "");
                    arrayAfternoon.add(r2);
                }

                if (arrayAfternoon.contains(uid)) {
                    ivAfternoon.setImageResource(R.drawable.iconafternoonon);

                    if (Objects.equals(week, "1")) {
                        afternoonSun = true;
                    }
                    if (Objects.equals(week, "2")) {
                        afternoonMon = true;
                    }
                    if (Objects.equals(week, "3")) {
                        afternoonTue = true;
                    }
                    if (Objects.equals(week, "4")) {
                        afternoonWed = true;
                    }
                    if (Objects.equals(week, "5")) {
                        afternoonThu = true;
                    }
                    if (Objects.equals(week, "6")) {
                        afternoonFri = true;
                    }
                    if (Objects.equals(week, "7")) {
                        afternoonSat = true;
                    }

                    arrayAfternoon.clear();

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
                for (DataSnapshot i : dataSnapshot.getChildren()) {
                    String r1 = i.getValue().toString().replace("{uid=", "");
                    String r2 = r1.replace("}", "");
                    arrayNight.add(r2);
                }

                if (arrayNight.contains(uid)) {
                    ivNight.setImageResource(R.drawable.iconnighton);

                    if (Objects.equals(week, "1")) {
                        nightSun = true;
                    }
                    if (Objects.equals(week, "2")) {
                        nightMon = true;
                    }
                    if (Objects.equals(week, "3")) {
                        nightTue = true;
                    }
                    if (Objects.equals(week, "4")) {
                        nightWed = true;
                    }
                    if (Objects.equals(week, "5")) {
                        nightThu = true;
                    }
                    if (Objects.equals(week, "6")) {
                        nightFri = true;
                    }
                    if (Objects.equals(week, "7")) {
                        nightSat = true;
                    }

                    arrayNight.clear();

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void clearObjects() {
        morningSun = false;
        morningMon = false;
        morningTue = false;
        morningWed = false;
        morningThu = false;
        morningFri = false;
        morningSat = false;
        afternoonSun = false;
        afternoonMon = false;
        afternoonTue = false;
        afternoonWed = false;
        afternoonThu = false;
        afternoonFri = false;
        afternoonSat = false;
        nightSun = false;
        nightMon = false;
        nightTue = false;
        nightWed = false;
        nightThu = false;
        nightFri = false;
        nightSat = false;

        ivMorningSun.setImageResource(R.drawable.iconmorningoff);
        ivMorningMon.setImageResource(R.drawable.iconmorningoff);
        ivMorningTue.setImageResource(R.drawable.iconmorningoff);
        ivMorningWed.setImageResource(R.drawable.iconmorningoff);
        ivMorningThu.setImageResource(R.drawable.iconmorningoff);
        ivMorningFri.setImageResource(R.drawable.iconmorningoff);
        ivMorningSat.setImageResource(R.drawable.iconmorningoff);

        ivAfternoonSun.setImageResource(R.drawable.iconafternoonoff);
        ivAfternoonMon.setImageResource(R.drawable.iconafternoonoff);
        ivAfternoonTue.setImageResource(R.drawable.iconafternoonoff);
        ivAfternoonWed.setImageResource(R.drawable.iconafternoonoff);
        ivAfternoonThu.setImageResource(R.drawable.iconafternoonoff);
        ivAfternoonFri.setImageResource(R.drawable.iconafternoonoff);
        ivAfternoonSat.setImageResource(R.drawable.iconafternoonoff);

        ivNightSun.setImageResource(R.drawable.iconnightoff);
        ivNightMon.setImageResource(R.drawable.iconnightoff);
        ivNightTue.setImageResource(R.drawable.iconnightoff);
        ivNightWed.setImageResource(R.drawable.iconnightoff);
        ivNightThu.setImageResource(R.drawable.iconnightoff);
        ivNightFri.setImageResource(R.drawable.iconnightoff);
        ivNightSat.setImageResource(R.drawable.iconnightoff);

    }

}
