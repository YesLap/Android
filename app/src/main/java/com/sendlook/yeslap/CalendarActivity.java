package com.sendlook.yeslap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
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
import com.sendlook.yeslap.model.Base64Custom;
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
    private Boolean isFilled = false;
    private Boolean MorningSun = false, MorningMon = false, MorningTue = false, MorningWed = false, MorningThu = false, MorningFri = false, MorningSat = false;
    private Boolean AfternoonSun = false, AfternoonMon = false, AfternoonTue = false, AfternoonWed = false, AfternoonThu = false, AfternoonFri = false, AfternoonSat = false;
    private Boolean NightSun = false, NightMon = false, NightTue = false, NightWed = false, NightThu = false, NightFri = false, NightSat = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        dialog = new ProgressDialog(CalendarActivity.this);
        dialog.setMessage(getString(R.string.loading));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        mAuth = FirebaseAuth.getInstance();

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
                //Utils.toastyError(getApplicationContext(), String.valueOf(MorningSun));
                //if (MorningSun) {
                //removeCalendar(Utils.SUNDAY, Utils.MORNING, ivMorningSun);
                //getCalendar();
                //MorningSun = false;
                //} else {
                setCalendar(Utils.SUNDAY, Utils.MORNING, tbAvailabilitySun);
                getCalendar();
                //}
            }
        });

        ivAfternoonSun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCalendar(Utils.SUNDAY, Utils.AFTERNOON, tbAvailabilitySun);
                getCalendar();
            }
        });

        ivNightSun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCalendar(Utils.SUNDAY, Utils.NIGHT, tbAvailabilitySun);
                getCalendar();
            }
        });

        //MONDAY
        ivMorningMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCalendar(Utils.MONDAY, Utils.MORNING, tbAvailabilityMon);
                getCalendar();
            }
        });

        ivAfternoonMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCalendar(Utils.MONDAY, Utils.AFTERNOON, tbAvailabilityMon);
                getCalendar();
            }
        });

        ivNightMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCalendar(Utils.MONDAY, Utils.NIGHT, tbAvailabilityMon);
                getCalendar();
            }
        });

        //TUESDAY
        ivMorningTue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCalendar(Utils.TUESDAY, Utils.MORNING, tbAvailabilityTue);
                getCalendar();
            }
        });

        ivAfternoonTue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCalendar(Utils.TUESDAY, Utils.AFTERNOON, tbAvailabilityTue);
                getCalendar();
            }
        });

        ivNightTue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCalendar(Utils.TUESDAY, Utils.NIGHT, tbAvailabilityTue);
                getCalendar();
            }
        });

        //WEDNESDAY
        ivMorningWed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCalendar(Utils.WEDNESDAY, Utils.MORNING, tbAvailabilityWed);
                getCalendar();
            }
        });

        ivAfternoonWed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCalendar(Utils.WEDNESDAY, Utils.AFTERNOON, tbAvailabilityWed);
                getCalendar();
            }
        });

        ivNightWed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCalendar(Utils.WEDNESDAY, Utils.NIGHT, tbAvailabilityWed);
                getCalendar();
            }
        });

        //THURSDAY
        ivMorningThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCalendar(Utils.THURSDAY, Utils.MORNING, tbAvailabilityThu);
                getCalendar();
            }
        });

        ivAfternoonThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCalendar(Utils.THURSDAY, Utils.AFTERNOON, tbAvailabilityThu);
                getCalendar();
            }
        });

        ivNightThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCalendar(Utils.THURSDAY, Utils.NIGHT, tbAvailabilityThu);
                getCalendar();
            }
        });

        //FRIDAY
        ivMorningFri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCalendar(Utils.FRIDAY, Utils.MORNING, tbAvailabilityFri);
                getCalendar();
            }
        });

        ivAfternoonFri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCalendar(Utils.FRIDAY, Utils.AFTERNOON, tbAvailabilityFri);
                getCalendar();
            }
        });

        ivNightFri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCalendar(Utils.FRIDAY, Utils.NIGHT, tbAvailabilityFri);
                getCalendar();
            }
        });

        //SATURDAY
        ivMorningSat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCalendar(Utils.SATURDAY, Utils.MORNING, tbAvailabilitySat);
                getCalendar();
            }
        });

        ivAfternoonSat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCalendar(Utils.SATURDAY, Utils.AFTERNOON, tbAvailabilitySat);
                getCalendar();
            }
        });

        ivNightSat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCalendar(Utils.SATURDAY, Utils.NIGHT, tbAvailabilitySat);
                getCalendar();
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

    private void setCalendar(String week, String turn, ToggleButton toogle) {
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
            mDatabase = FirebaseDatabase.getInstance().getReference().child(Utils.CALENDAR).child(week).child(turn).child(mAuth.getCurrentUser().getUid());
            HashMap<String, String> uid = new HashMap<>();
            uid.put(Utils.UID, mAuth.getCurrentUser().getUid());
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

    private void getCalendar() {

        try {
            //SUNDAY
            getDayAndTurn(Utils.SUNDAY, Utils.MORNING, ivMorningSun, tbAvailabilitySun);
            getDayAndTurn(Utils.SUNDAY, Utils.AFTERNOON, ivAfternoonSun, tbAvailabilitySun);
            getDayAndTurn(Utils.SUNDAY, Utils.NIGHT, ivNightSun, tbAvailabilitySun);
            //MONDAY
            getDayAndTurn(Utils.MONDAY, Utils.MORNING, ivMorningMon, tbAvailabilityMon);
            getDayAndTurn(Utils.MONDAY, Utils.AFTERNOON, ivAfternoonMon, tbAvailabilityMon);
            getDayAndTurn(Utils.MONDAY, Utils.NIGHT, ivNightMon, tbAvailabilityMon);
            //TUESDAY
            getDayAndTurn(Utils.TUESDAY, Utils.MORNING, ivMorningTue, tbAvailabilityTue);
            getDayAndTurn(Utils.TUESDAY, Utils.AFTERNOON, ivAfternoonTue, tbAvailabilityTue);
            getDayAndTurn(Utils.TUESDAY, Utils.NIGHT, ivNightTue, tbAvailabilityTue);
            //WEDNESDAY
            getDayAndTurn(Utils.WEDNESDAY, Utils.MORNING, ivMorningWed, tbAvailabilityWed);
            getDayAndTurn(Utils.WEDNESDAY, Utils.AFTERNOON, ivAfternoonWed, tbAvailabilityWed);
            getDayAndTurn(Utils.WEDNESDAY, Utils.NIGHT, ivNightWed, tbAvailabilityWed);
            //THURSDAY
            getDayAndTurn(Utils.THURSDAY, Utils.MORNING, ivMorningThu, tbAvailabilityThu);
            getDayAndTurn(Utils.THURSDAY, Utils.AFTERNOON, ivAfternoonThu, tbAvailabilityThu);
            getDayAndTurn(Utils.THURSDAY, Utils.NIGHT, ivNightThu, tbAvailabilityThu);
            //FRIDAY
            getDayAndTurn(Utils.FRIDAY, Utils.MORNING, ivMorningFri, tbAvailabilityFri);
            getDayAndTurn(Utils.FRIDAY, Utils.AFTERNOON, ivAfternoonFri, tbAvailabilityFri);
            getDayAndTurn(Utils.FRIDAY, Utils.NIGHT, ivNightFri, tbAvailabilityFri);
            //SATURDAY
            getDayAndTurn(Utils.SATURDAY, Utils.MORNING, ivMorningSat, tbAvailabilitySat);
            getDayAndTurn(Utils.SATURDAY, Utils.AFTERNOON, ivAfternoonSat, tbAvailabilitySat);
            getDayAndTurn(Utils.SATURDAY, Utils.NIGHT, ivNightSat, tbAvailabilitySat);
        } catch (Exception e) {
            Utils.toastyError(getApplicationContext(), e.getMessage());
        } finally {
            dialog.dismiss();
        }

    }

    private void removeCalendar(String week, final String turn, final ImageView imageView) {
        dialog = new ProgressDialog(CalendarActivity.this);
        dialog.setMessage(getString(R.string.loading));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(week).child(turn).child(mAuth.getCurrentUser().getUid());
        database.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    dialog.dismiss();
                    switch (turn) {
                        case Utils.MORNING:
                            imageView.setImageDrawable(getDrawable(R.drawable.iconmorningoff));
                            break;
                        case Utils.AFTERNOON:
                            imageView.setImageDrawable(getDrawable(R.drawable.iconafternoonoff));
                            break;
                        case Utils.NIGHT:
                            imageView.setImageDrawable(getDrawable(R.drawable.iconnightoff));
                            break;
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Utils.toastyError(getApplicationContext(), e.getMessage());
            }
        });
    }

    private void getDayAndTurn(final String week, final String turn, final ImageView imageview, final ToggleButton toggleButton) {
        final String uid = mAuth.getCurrentUser().getUid();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(Utils.CALENDAR).child(week).child(turn);
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    if (Objects.equals(dataSnapshot1.child(Utils.UID).getValue(String.class), uid)) {
                        toggleButton.setChecked(true);
                        isOn(week, turn);
                        if (turn.equals(Utils.MORNING)) {
                            imageview.setImageDrawable(getDrawable(R.drawable.iconmorningon));
                        } else if (turn.equals(Utils.AFTERNOON)) {
                            imageview.setImageDrawable(getDrawable(R.drawable.iconafternoonon));
                        } else if (turn.equals(Utils.NIGHT)) {
                            imageview.setImageDrawable(getDrawable(R.drawable.iconnighton));
                        }
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void isOn(String week, String turn) {
        if (Objects.equals(week, Utils.SUNDAY) && Objects.equals(turn, Utils.MORNING)) {
            MorningSun = true;
        } else if (Objects.equals(week, Utils.SUNDAY) && Objects.equals(turn, Utils.AFTERNOON)) {
            AfternoonSun = true;
        } else if (Objects.equals(week, Utils.SUNDAY) && Objects.equals(turn, Utils.NIGHT)) {
            NightFri = true;
        }
    }

    private void checkUsernameAndImage() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Utils.USERS).child(mAuth.getCurrentUser().getUid());
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username = dataSnapshot.child(Utils.USERNAME).getValue(String.class);
                String image = dataSnapshot.child(Utils.IMAGE_1).getValue(String.class);

                if (Objects.equals(username, Utils.USERNAME) || Objects.equals(image, "")) {
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

}
