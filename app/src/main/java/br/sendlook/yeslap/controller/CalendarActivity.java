package br.sendlook.yeslap.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import br.sendlook.yeslap.R;
import br.sendlook.yeslap.view.Utils;

public class CalendarActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ImageView ivMorningSun, ivMorningMon, ivMorningTue, ivMorningWed, ivMorningThu, ivMorningFri, ivMorningSat;
    private ImageView ivAfternoonSun, ivAfternoonMon, ivAfternoonTue, ivAfternoonWed, ivAfternoonThu, ivAfternoonFri, ivAfternoonSat;
    private ImageView ivNightSun, ivNightMon, ivNightTue, ivNightWed, ivNightThu, ivNightFri, ivNightSat;
    private com.suke.widget.SwitchButton tbAvailabilitySun, tbAvailabilityMon, tbAvailabilityTue, tbAvailabilityWed, tbAvailabilityThu, tbAvailabilityFri, tbAvailabilitySat;
    private ImageView btnGoToProfile, btnGoToSettings;
    private RelativeLayout btnCalendar, btnChat, btnSearch;
    private ProgressDialog dialog;
    private String id;
    private Boolean MorningSun = false, MorningMon = false, MorningTue = false, MorningWed = false, MorningThu = false, MorningFri = false, MorningSat = false;
    private Boolean AfternoonSun = false, AfternoonMon = false, AfternoonTue = false, AfternoonWed = false, AfternoonThu = false, AfternoonFri = false, AfternoonSat = false;
    private Boolean NightSun = false, NightMon = false, NightTue = false, NightWed = false, NightThu = false, NightFri = false, NightSat = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString(Utils.ID_USER);
        }

        //dialog = new ProgressDialog(CalendarActivity.this);
        //dialog.setMessage(getString(R.string.loading));
        //dialog.setCanceledOnTouchOutside(false);
        //dialog.show();

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

        tbAvailabilitySun = (com.suke.widget.SwitchButton) findViewById(R.id.tbAvailabilitySun);
        tbAvailabilityMon = (com.suke.widget.SwitchButton) findViewById(R.id.tbAvailabilityMon);
        tbAvailabilityTue = (com.suke.widget.SwitchButton) findViewById(R.id.tbAvailabilityTue);
        tbAvailabilityWed = (com.suke.widget.SwitchButton) findViewById(R.id.tbAvailabilityWed);
        tbAvailabilityThu = (com.suke.widget.SwitchButton) findViewById(R.id.tbAvailabilityThu);
        tbAvailabilityFri = (com.suke.widget.SwitchButton) findViewById(R.id.tbAvailabilityFri);
        tbAvailabilitySat = (com.suke.widget.SwitchButton) findViewById(R.id.tbAvailabilitySat);

        //getCalendar();

        btnGoToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnGoToSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentsettings = new Intent(CalendarActivity.this, SettingsActivity.class);
                intentsettings.putExtra(Utils.ID_USER, id);
                startActivity(intentsettings);
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
                if (MorningSun) {
                    //removeCalendar(Utils.SUNDAY, Utils.MORNING, ivMorningSun);
                    MorningSun = false;
                } else {
                    setCalendar(Utils.SUNDAY_M, tbAvailabilitySun);
                }
                //getCalendar();
            }
        });

        ivAfternoonSun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AfternoonSun) {
                    //removeCalendar(Utils.SUNDAY, Utils.AFTERNOON, ivAfternoonSun);
                    AfternoonSun = false;
                } else {
                    setCalendar(Utils.SUNDAY_A, tbAvailabilitySun);
                }
                //getCalendar();
            }
        });

        ivNightSun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NightSun) {
                    //removeCalendar(Utils.SUNDAY, Utils.NIGHT, ivNightSun);
                    NightSun = false;
                } else {
                    setCalendar(Utils.SUNDAY_N, tbAvailabilitySun);
                }
                //getCalendar();
            }
        });

        //MONDAY
        ivMorningMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MorningMon) {
                    //removeCalendar(Utils.MONDAY, Utils.MORNING, ivMorningMon);
                    MorningMon = false;
                } else {
                    setCalendar(Utils.MONDAY_M, tbAvailabilityMon);
                }
            }
            //getCalendar();
        });

        ivAfternoonMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AfternoonMon) {
                    //removeCalendar(Utils.MONDAY, Utils.AFTERNOON, ivAfternoonMon);
                    AfternoonMon = false;
                } else {
                    setCalendar(Utils.MONDAY_A, tbAvailabilityMon);
                }
                //getCalendar();
            }
        });

        ivNightMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NightMon) {
                    //removeCalendar(Utils.MONDAY, Utils.NIGHT, ivNightMon);
                    NightMon = false;
                } else {
                    setCalendar(Utils.MONDAY_N, tbAvailabilityMon);
                }
            }
            //getCalendar();
        });

        //TUESDAY
        ivMorningTue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MorningTue) {
                    //removeCalendar(Utils.TUESDAY, Utils.MORNING, ivMorningTue);
                    MorningTue = false;
                } else {
                    setCalendar(Utils.TUESDAY_M, tbAvailabilityTue);
                }
            }
            //getCalendar();
        });

        ivAfternoonTue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AfternoonTue) {
                    //removeCalendar(Utils.TUESDAY, Utils.AFTERNOON, ivAfternoonTue);
                    AfternoonTue = false;
                } else {
                    setCalendar(Utils.TUESDAY_A, tbAvailabilityTue);
                }
            }
            //getCalendar();
        });

        ivNightTue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NightTue) {
                    //removeCalendar(Utils.TUESDAY, Utils.NIGHT, ivNightTue);
                    NightTue = false;
                } else {
                    setCalendar(Utils.TUESDAY_N, tbAvailabilityTue);
                }
            }
            //getCalendar();
        });

        //WEDNESDAY
        ivMorningWed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MorningWed) {
                    //removeCalendar(Utils.WEDNESDAY, Utils.MORNING, ivMorningWed);
                    MorningWed = false;
                } else {
                    setCalendar(Utils.WEDNESDAY_M, tbAvailabilityWed);
                }
            }
            //getCalendar();
        });

        ivAfternoonWed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AfternoonWed) {
                    //removeCalendar(Utils.WEDNESDAY, Utils.AFTERNOON, ivAfternoonWed);
                    AfternoonWed = false;
                } else {
                    setCalendar(Utils.WEDNESDAY_A, tbAvailabilityWed);
                }
            }
            //getCalendar();
        });

        ivNightWed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NightWed) {
                    //removeCalendar(Utils.WEDNESDAY, Utils.NIGHT, ivNightWed);
                    NightWed = false;
                } else {
                    setCalendar(Utils.WEDNESDAY_N, tbAvailabilityWed);
                }
            }
            //getCalendar();
        });

        //THURSDAY
        ivMorningThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MorningThu) {
                    //removeCalendar(Utils.THURSDAY, Utils.MORNING, ivMorningThu);
                    MorningThu = false;
                } else {
                    setCalendar(Utils.THURSDAY_M, tbAvailabilityThu);
                }
            }
            //getCalendar();
        });

        ivAfternoonThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AfternoonThu) {
                    //removeCalendar(Utils.THURSDAY, Utils.AFTERNOON, ivAfternoonThu);
                    AfternoonThu = false;
                } else {
                    setCalendar(Utils.THURSDAY_A, tbAvailabilityThu);
                }
            }
            //getCalendar();
        });

        ivNightThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NightThu) {
                    //removeCalendar(Utils.THURSDAY, Utils.NIGHT, ivNightThu);
                    NightThu = false;
                } else {
                    setCalendar(Utils.THURSDAY_N, tbAvailabilityThu);
                }
            }
            //getCalendar();
        });

        //FRIDAY
        ivMorningFri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MorningFri) {
                    //removeCalendar(Utils.FRIDAY, Utils.MORNING, ivMorningFri);
                    MorningFri = false;
                } else {
                    setCalendar(Utils.FRIDAY_M, tbAvailabilityFri);
                }
            }
            //getCalendar();
        });

        ivAfternoonFri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AfternoonFri) {
                    //removeCalendar(Utils.FRIDAY, Utils.AFTERNOON, ivAfternoonFri);
                    AfternoonFri = false;
                } else {
                    setCalendar(Utils.FRIDAY_A, tbAvailabilityFri);
                }
            }
            //getCalendar();
        });

        ivNightFri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NightFri) {
                    //removeCalendar(Utils.FRIDAY, Utils.NIGHT, ivNightFri);
                    NightFri = false;
                } else {
                    setCalendar(Utils.FRIDAY_N, tbAvailabilityFri);
                }
            }
            //getCalendar();
        });

        //SATURDAY
        ivMorningSat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MorningSat) {
                    //removeCalendar(Utils.SATURDAY, Utils.MORNING, ivMorningSat);
                    MorningSat = false;
                } else {
                    setCalendar(Utils.SATURDAY_M, tbAvailabilitySat);
                }
            }
            //getCalendar();
        });

        ivAfternoonSat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AfternoonSat) {
                    //removeCalendar(Utils.SATURDAY, Utils.AFTERNOON, ivAfternoonSat);
                    AfternoonSat = false;
                } else {
                    setCalendar(Utils.SATURDAY_A, tbAvailabilitySat);
                }
            }
            //getCalendar();
        });

        ivNightSat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NightSat) {
                    //removeCalendar(Utils.SATURDAY, Utils.NIGHT, ivNightSat);
                    NightSat = false;
                } else {
                    setCalendar(Utils.SATURDAY_N, tbAvailabilitySat);
                }
            }
            //getCalendar();
        });

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

    private void setCalendar(String turn, com.suke.widget.SwitchButton toogle) {
        //Check if the button is activated
        if (!toogle.isChecked()) {
            Utils.toastyInfo(getApplicationContext(), "Please, active the button");
        } else {
            //Dialog
            dialog = new ProgressDialog(CalendarActivity.this);
            dialog.setMessage(getString(R.string.loading));
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            Ion.with(this)
                    .load(Utils.URL_UPDATE_CALENDAR_USER)
                    .setBodyParameter(Utils.ID_USER_APP, id)
                    .setBodyParameter(Utils.TURN_APP, turn)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            try {
                                String returnApp = result.get(Utils.CALENDAR).getAsString();

                                switch (returnApp) {
                                    case Utils.CODE_SUCCESS:
                                        if (dialog.isShowing()) {
                                            dialog.dismiss();
                                        }
                                        break;
                                    case Utils.CODE_ERROR:
                                        if (dialog.isShowing()) {
                                            dialog.dismiss();
                                        }
                                        Utils.toastyError(getApplicationContext(), e.getMessage());
                                        break;
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

    }

    /**
     * private void getCalendar() {
     * <p>
     * try {
     * //SUNDAY
     * getDayAndTurn(Utils.SUNDAY, Utils.MORNING, ivMorningSun, tbAvailabilitySun);
     * getDayAndTurn(Utils.SUNDAY, Utils.AFTERNOON, ivAfternoonSun, tbAvailabilitySun);
     * getDayAndTurn(Utils.SUNDAY, Utils.NIGHT, ivNightSun, tbAvailabilitySun);
     * //MONDAY
     * getDayAndTurn(Utils.MONDAY, Utils.MORNING, ivMorningMon, tbAvailabilityMon);
     * getDayAndTurn(Utils.MONDAY, Utils.AFTERNOON, ivAfternoonMon, tbAvailabilityMon);
     * getDayAndTurn(Utils.MONDAY, Utils.NIGHT, ivNightMon, tbAvailabilityMon);
     * //TUESDAY
     * getDayAndTurn(Utils.TUESDAY, Utils.MORNING, ivMorningTue, tbAvailabilityTue);
     * getDayAndTurn(Utils.TUESDAY, Utils.AFTERNOON, ivAfternoonTue, tbAvailabilityTue);
     * getDayAndTurn(Utils.TUESDAY, Utils.NIGHT, ivNightTue, tbAvailabilityTue);
     * //WEDNESDAY
     * getDayAndTurn(Utils.WEDNESDAY, Utils.MORNING, ivMorningWed, tbAvailabilityWed);
     * getDayAndTurn(Utils.WEDNESDAY, Utils.AFTERNOON, ivAfternoonWed, tbAvailabilityWed);
     * getDayAndTurn(Utils.WEDNESDAY, Utils.NIGHT, ivNightWed, tbAvailabilityWed);
     * //THURSDAY
     * getDayAndTurn(Utils.THURSDAY, Utils.MORNING, ivMorningThu, tbAvailabilityThu);
     * getDayAndTurn(Utils.THURSDAY, Utils.AFTERNOON, ivAfternoonThu, tbAvailabilityThu);
     * getDayAndTurn(Utils.THURSDAY, Utils.NIGHT, ivNightThu, tbAvailabilityThu);
     * //FRIDAY
     * getDayAndTurn(Utils.FRIDAY, Utils.MORNING, ivMorningFri, tbAvailabilityFri);
     * getDayAndTurn(Utils.FRIDAY, Utils.AFTERNOON, ivAfternoonFri, tbAvailabilityFri);
     * getDayAndTurn(Utils.FRIDAY, Utils.NIGHT, ivNightFri, tbAvailabilityFri);
     * //SATURDAY
     * getDayAndTurn(Utils.SATURDAY, Utils.MORNING, ivMorningSat, tbAvailabilitySat);
     * getDayAndTurn(Utils.SATURDAY, Utils.AFTERNOON, ivAfternoonSat, tbAvailabilitySat);
     * getDayAndTurn(Utils.SATURDAY, Utils.NIGHT, ivNightSat, tbAvailabilitySat);
     * } catch (Exception e) {
     * Utils.toastyError(getApplicationContext(), e.getMessage());
     * } finally {
     * if (dialog.isShowing()) {
     * dialog.dismiss();
     * }
     * }
     * <p>
     * }
     * <p>
     * private void removeCalendar(String week, final String turn, final ImageView imageView) {
     * dialog = new ProgressDialog(CalendarActivity.this);
     * dialog.setMessage(getString(R.string.loading));
     * dialog.setCanceledOnTouchOutside(false);
     * dialog.show();
     * <p>
     * final DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(Utils.CALENDAR).child(week).child(turn);
     * database.child(uid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
     *
     * @Override public void onComplete(@NonNull Task<Void> task) {
     * if (task.isSuccessful()) {
     * if (dialog.isShowing()) {
     * dialog.dismiss();
     * }
     * switch (turn) {
     * case Utils.MORNING:
     * imageView.setImageDrawable(getDrawable(R.drawable.iconmorningoff));
     * break;
     * case Utils.AFTERNOON:
     * imageView.setImageDrawable(getDrawable(R.drawable.iconafternoonoff));
     * break;
     * case Utils.NIGHT:
     * imageView.setImageDrawable(getDrawable(R.drawable.iconnightoff));
     * break;
     * }
     * }
     * }
     * }).addOnFailureListener(new OnFailureListener() {
     * @Override public void onFailure(@NonNull Exception e) {
     * Utils.toastyError(getApplicationContext(), e.getMessage());
     * }
     * });
     * }
     * <p>
     * private void getDayAndTurn(final String week, final String turn, final ImageView imageview, final com.suke.widget.SwitchButton toggleButton) {
     * final String uid = mAuth.getCurrentUser().getUid();
     * final DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(Utils.CALENDAR).child(week).child(turn);
     * database.addValueEventListener(new ValueEventListener() {
     * @Override public void onDataChange(DataSnapshot dataSnapshot) {
     * for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
     * if (Objects.equals(dataSnapshot1.child(Utils.UID).getValue(String.class), uid)) {
     * toggleButton.setChecked(true);
     * isOn(week, turn);
     * if (turn.equals(Utils.MORNING)) {
     * imageview.setImageDrawable(getDrawable(R.drawable.iconmorningon));
     * } else if (turn.equals(Utils.AFTERNOON)) {
     * imageview.setImageDrawable(getDrawable(R.drawable.iconafternoonon));
     * } else if (turn.equals(Utils.NIGHT)) {
     * imageview.setImageDrawable(getDrawable(R.drawable.iconnighton));
     * }
     * <p>
     * if (Objects.equals(week, Utils.SUNDAY) && Objects.equals(turn, Utils.MORNING)) {
     * MorningSun = true;
     * } else if (Objects.equals(week, Utils.SUNDAY) && Objects.equals(turn, Utils.AFTERNOON)) {
     * AfternoonSun = true;
     * } else if (Objects.equals(week, Utils.SUNDAY) && Objects.equals(turn, Utils.NIGHT)) {
     * NightSun = true;
     * } else if (Objects.equals(week, Utils.MONDAY) && Objects.equals(turn, Utils.MORNING)) {
     * MorningMon = true;
     * } else if (Objects.equals(week, Utils.MONDAY) && Objects.equals(turn, Utils.AFTERNOON)) {
     * AfternoonMon = true;
     * } else if (Objects.equals(week, Utils.MONDAY) && Objects.equals(turn, Utils.NIGHT)) {
     * NightMon = true;
     * } else if (Objects.equals(week, Utils.TUESDAY) && Objects.equals(turn, Utils.MORNING)) {
     * MorningTue = true;
     * } else if (Objects.equals(week, Utils.TUESDAY) && Objects.equals(turn, Utils.AFTERNOON)) {
     * AfternoonTue = true;
     * } else if (Objects.equals(week, Utils.TUESDAY) && Objects.equals(turn, Utils.NIGHT)) {
     * NightTue = true;
     * } else if (Objects.equals(week, Utils.WEDNESDAY) && Objects.equals(turn, Utils.MORNING)) {
     * MorningWed = true;
     * } else if (Objects.equals(week, Utils.WEDNESDAY) && Objects.equals(turn, Utils.AFTERNOON)) {
     * AfternoonWed = true;
     * } else if (Objects.equals(week, Utils.WEDNESDAY) && Objects.equals(turn, Utils.NIGHT)) {
     * NightWed = true;
     * } else if (Objects.equals(week, Utils.THURSDAY) && Objects.equals(turn, Utils.MORNING)) {
     * MorningThu = true;
     * } else if (Objects.equals(week, Utils.THURSDAY) && Objects.equals(turn, Utils.AFTERNOON)) {
     * AfternoonThu = true;
     * } else if (Objects.equals(week, Utils.THURSDAY) && Objects.equals(turn, Utils.NIGHT)) {
     * NightThu = true;
     * } else if (Objects.equals(week, Utils.FRIDAY) && Objects.equals(turn, Utils.MORNING)) {
     * MorningFri = true;
     * } else if (Objects.equals(week, Utils.FRIDAY) && Objects.equals(turn, Utils.AFTERNOON)) {
     * AfternoonFri = true;
     * } else if (Objects.equals(week, Utils.FRIDAY) && Objects.equals(turn, Utils.NIGHT)) {
     * NightFri = true;
     * } else if (Objects.equals(week, Utils.SATURDAY) && Objects.equals(turn, Utils.MORNING)) {
     * MorningSat = true;
     * } else if (Objects.equals(week, Utils.SATURDAY) && Objects.equals(turn, Utils.AFTERNOON)) {
     * AfternoonSat = true;
     * } else if (Objects.equals(week, Utils.SATURDAY) && Objects.equals(turn, Utils.NIGHT)) {
     * NightSat = true;
     * }
     * <p>
     * }
     * }
     * <p>
     * }
     * @Override public void onCancelled(DatabaseError databaseError) {
     * <p>
     * }
     * });
     * }
     * <p>
     * private void isOn(String week, String turn) {
     * if (Objects.equals(week, Utils.SUNDAY) && Objects.equals(turn, Utils.MORNING)) {
     * MorningSun = true;
     * } else if (Objects.equals(week, Utils.SUNDAY) && Objects.equals(turn, Utils.AFTERNOON)) {
     * AfternoonSun = true;
     * } else if (Objects.equals(week, Utils.SUNDAY) && Objects.equals(turn, Utils.NIGHT)) {
     * NightFri = true;
     * }
     * }
     */

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
