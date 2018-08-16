package br.sendlook.yeslap.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.Objects;

import br.sendlook.yeslap.R;
import br.sendlook.yeslap.view.Utils;

public class CalendarActivity extends AppCompatActivity {

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

        dialog = new ProgressDialog(CalendarActivity.this);
        dialog.setMessage(getString(R.string.loading));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

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
                Intent intent = new Intent(CalendarActivity.this, FindUsersActivity.class);
                intent.putExtra(Utils.ID_USER, id);
                startActivity(intent);
            }
        });

        //SUNDAY
        ivMorningSun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MorningSun) {
                    removeCalendar(Utils.SUNDAY_M ,ivMorningSun);
                } else {
                    setCalendar(Utils.SUNDAY_M, tbAvailabilitySun);
                }
                getCalendar();
            }
        });

        ivAfternoonSun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AfternoonSun) {
                    removeCalendar(Utils.SUNDAY_A, ivAfternoonSun);
                } else {
                    setCalendar(Utils.SUNDAY_A, tbAvailabilitySun);
                }
                getCalendar();
            }
        });

        ivNightSun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NightSun) {
                    removeCalendar(Utils.SUNDAY_N, ivNightSun);
                } else {
                    setCalendar(Utils.SUNDAY_N, tbAvailabilitySun);
                }
                getCalendar();
            }
        });

        //MONDAY
        ivMorningMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MorningMon) {
                    removeCalendar(Utils.MONDAY_M, ivMorningMon);
                } else {
                    setCalendar(Utils.MONDAY_M, tbAvailabilityMon);
                }
                getCalendar();
            }
            //getCalendar();
        });

        ivAfternoonMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AfternoonMon) {
                    removeCalendar(Utils.MONDAY_A, ivAfternoonMon);
                } else {
                    setCalendar(Utils.MONDAY_A, tbAvailabilityMon);
                }
                getCalendar();
            }
        });

        ivNightMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NightMon) {
                    removeCalendar(Utils.MONDAY_N, ivNightMon);
                } else {
                    setCalendar(Utils.MONDAY_N, tbAvailabilityMon);
                }
                getCalendar();
            }
            //getCalendar();
        });

        //TUESDAY
        ivMorningTue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MorningTue) {
                    removeCalendar(Utils.TUESDAY_M, ivMorningTue);
                    MorningTue = false;
                } else {
                    setCalendar(Utils.TUESDAY_M, tbAvailabilityTue);
                }
                getCalendar();
            }
            //getCalendar();
        });

        ivAfternoonTue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AfternoonTue) {
                    removeCalendar(Utils.TUESDAY_A, ivAfternoonTue);
                } else {
                    setCalendar(Utils.TUESDAY_A, tbAvailabilityTue);
                }
                getCalendar();
            }
            //getCalendar();
        });

        ivNightTue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NightTue) {
                    removeCalendar(Utils.TUESDAY_N, ivNightTue);
                } else {
                    setCalendar(Utils.TUESDAY_N, tbAvailabilityTue);
                }
                getCalendar();
            }
            //getCalendar();
        });

        //WEDNESDAY
        ivMorningWed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MorningWed) {
                    removeCalendar(Utils.WEDNESDAY_M, ivMorningWed);
                } else {
                    setCalendar(Utils.WEDNESDAY_M, tbAvailabilityWed);
                }
                getCalendar();
            }
            //getCalendar();
        });

        ivAfternoonWed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AfternoonWed) {
                    removeCalendar(Utils.WEDNESDAY_A, ivAfternoonWed);
                } else {
                    setCalendar(Utils.WEDNESDAY_A, tbAvailabilityWed);
                }
                getCalendar();
            }
            //getCalendar();
        });

        ivNightWed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NightWed) {
                    removeCalendar(Utils.WEDNESDAY_N, ivNightWed);
                } else {
                    setCalendar(Utils.WEDNESDAY_N, tbAvailabilityWed);
                }
                getCalendar();
            }
            //getCalendar();
        });

        //THURSDAY
        ivMorningThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MorningThu) {
                    removeCalendar(Utils.THURSDAY_M, ivMorningThu);
                } else {
                    setCalendar(Utils.THURSDAY_M, tbAvailabilityThu);
                }
                getCalendar();
            }
            //getCalendar();
        });

        ivAfternoonThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AfternoonThu) {
                    removeCalendar(Utils.THURSDAY_A, ivAfternoonThu);
                } else {
                    setCalendar(Utils.THURSDAY_A, tbAvailabilityThu);
                }
                getCalendar();
            }
            //getCalendar();
        });

        ivNightThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NightThu) {
                    removeCalendar(Utils.THURSDAY_N, ivNightThu);
                } else {
                    setCalendar(Utils.THURSDAY_N, tbAvailabilityThu);
                }
                getCalendar();
            }
            //getCalendar();
        });

        //FRIDAY
        ivMorningFri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MorningFri) {
                    removeCalendar(Utils.FRIDAY_M, ivMorningFri);
                } else {
                    setCalendar(Utils.FRIDAY_M, tbAvailabilityFri);
                }
                getCalendar();
            }
            //getCalendar();
        });

        ivAfternoonFri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AfternoonFri) {
                    removeCalendar(Utils.FRIDAY_A, ivAfternoonFri);
                } else {
                    setCalendar(Utils.FRIDAY_A, tbAvailabilityFri);
                }
                getCalendar();
            }
            //getCalendar();
        });

        ivNightFri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NightFri) {
                    removeCalendar(Utils.FRIDAY_N, ivNightFri);
                } else {
                    setCalendar(Utils.FRIDAY_N, tbAvailabilityFri);
                }
                getCalendar();
            }
            //getCalendar();
        });

        //SATURDAY
        ivMorningSat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MorningSat) {
                    removeCalendar(Utils.SATURDAY_M,ivMorningSat);
                } else {
                    setCalendar(Utils.SATURDAY_M, tbAvailabilitySat);
                }
                getCalendar();
            }
            //getCalendar();
        });

        ivAfternoonSat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AfternoonSat) {
                    removeCalendar(Utils.SATURDAY_A, ivAfternoonSat);
                } else {
                    setCalendar(Utils.SATURDAY_A, tbAvailabilitySat);
                }
                getCalendar();
            }
            //getCalendar();
        });

        ivNightSat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (NightSat) {
                    removeCalendar(Utils.SATURDAY_N, ivNightSat);
                } else {
                    setCalendar(Utils.SATURDAY_N, tbAvailabilitySat);
                }
                getCalendar();
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


    private void getCalendar() {

        try {
            //SUNDAY
            getDayAndTurn(Utils.SUNDAY_M, ivMorningSun, tbAvailabilitySun);
            getDayAndTurn(Utils.SUNDAY_A, ivAfternoonSun, tbAvailabilitySun);
            getDayAndTurn(Utils.SUNDAY_N, ivNightSun, tbAvailabilitySun);
            //MONDAY
            getDayAndTurn(Utils.MONDAY_M, ivMorningMon, tbAvailabilityMon);
            getDayAndTurn(Utils.MONDAY_A, ivAfternoonMon, tbAvailabilityMon);
            getDayAndTurn(Utils.MONDAY_N, ivNightMon, tbAvailabilityMon);
            //TUESDAY
            getDayAndTurn(Utils.TUESDAY_M, ivMorningTue, tbAvailabilityTue);
            getDayAndTurn(Utils.TUESDAY_A, ivAfternoonTue, tbAvailabilityTue);
            getDayAndTurn(Utils.TUESDAY_N, ivNightTue, tbAvailabilityTue);
            //WEDNESDAY
            getDayAndTurn(Utils.WEDNESDAY_M, ivMorningWed, tbAvailabilityWed);
            getDayAndTurn(Utils.WEDNESDAY_A, ivAfternoonWed, tbAvailabilityWed);
            getDayAndTurn(Utils.WEDNESDAY_N, ivNightWed, tbAvailabilityWed);
            //THURSDAY
            getDayAndTurn(Utils.THURSDAY_M, ivMorningThu, tbAvailabilityThu);
            getDayAndTurn(Utils.THURSDAY_A, ivAfternoonThu, tbAvailabilityThu);
            getDayAndTurn(Utils.THURSDAY_N, ivNightThu, tbAvailabilityThu);
            //FRIDAY
            getDayAndTurn(Utils.FRIDAY_M, ivMorningFri, tbAvailabilityFri);
            getDayAndTurn(Utils.FRIDAY_A, ivAfternoonFri, tbAvailabilityFri);
            getDayAndTurn(Utils.FRIDAY_N, ivNightFri, tbAvailabilityFri);
            //SATURDAY
            getDayAndTurn(Utils.SATURDAY_M, ivMorningSat, tbAvailabilitySat);
            getDayAndTurn(Utils.SATURDAY_A, ivAfternoonSat, tbAvailabilitySat);
            getDayAndTurn(Utils.SATURDAY_N, ivNightSat, tbAvailabilitySat);
        } catch (Exception e) {
            Utils.toastyError(getApplicationContext(), e.getMessage());
        } finally {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }

    }

    private void removeCalendar(final String turn, final ImageView imageView) {
        dialog = new ProgressDialog(CalendarActivity.this);
        dialog.setMessage(getString(R.string.loading));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Ion.with(this)
                .load(Utils.URL_DELETE_CALENDAR_USER)
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
                                    if (Objects.equals(turn, Utils.SUNDAY_M)) {
                                        MorningSun = false;
                                        imageView.setImageDrawable(getDrawable(R.drawable.iconmorningoff));
                                    } else if (Objects.equals(turn, Utils.SUNDAY_A)) {
                                        AfternoonSun = false;
                                        imageView.setImageDrawable(getDrawable(R.drawable.iconafternoonoff));
                                    } else if (Objects.equals(turn, Utils.SUNDAY_N)) {
                                        NightSun = false;
                                        imageView.setImageDrawable(getDrawable(R.drawable.iconnightoff));
                                    } else if (Objects.equals(turn, Utils.MONDAY_M)) {
                                        MorningMon = false;
                                        imageView.setImageDrawable(getDrawable(R.drawable.iconmorningoff));
                                    } else if (Objects.equals(turn, Utils.MONDAY_A)) {
                                        AfternoonMon = false;
                                        imageView.setImageDrawable(getDrawable(R.drawable.iconafternoonoff));
                                    } else if (Objects.equals(turn, Utils.MONDAY_N)) {
                                        NightMon = false;
                                        imageView.setImageDrawable(getDrawable(R.drawable.iconnightoff));
                                    } else if (Objects.equals(turn, Utils.TUESDAY_M)) {
                                        MorningTue = false;
                                        imageView.setImageDrawable(getDrawable(R.drawable.iconmorningoff));
                                    } else if (Objects.equals(turn, Utils.TUESDAY_A)) {
                                        AfternoonTue = false;
                                        imageView.setImageDrawable(getDrawable(R.drawable.iconafternoonoff));
                                    } else if (Objects.equals(turn, Utils.TUESDAY_N)) {
                                        NightTue = false;
                                        imageView.setImageDrawable(getDrawable(R.drawable.iconnightoff));
                                    } else if (Objects.equals(turn, Utils.WEDNESDAY_M)) {
                                        MorningWed = false;
                                        imageView.setImageDrawable(getDrawable(R.drawable.iconmorningoff));
                                    } else if (Objects.equals(turn, Utils.WEDNESDAY_A)) {
                                        AfternoonWed = false;
                                        imageView.setImageDrawable(getDrawable(R.drawable.iconafternoonoff));
                                    } else if (Objects.equals(turn, Utils.WEDNESDAY_N)) {
                                        NightWed = false;
                                        imageView.setImageDrawable(getDrawable(R.drawable.iconnightoff));
                                    } else if (Objects.equals(turn, Utils.THURSDAY_M)) {
                                        MorningThu = false;
                                        imageView.setImageDrawable(getDrawable(R.drawable.iconmorningoff));
                                    } else if (Objects.equals(turn, Utils.THURSDAY_A)) {
                                        AfternoonThu = false;
                                        imageView.setImageDrawable(getDrawable(R.drawable.iconafternoonoff));
                                    } else if (Objects.equals(turn, Utils.THURSDAY_N)) {
                                        NightThu = false;
                                        imageView.setImageDrawable(getDrawable(R.drawable.iconnightoff));
                                    } else if (Objects.equals(turn, Utils.FRIDAY_M)) {
                                        MorningFri = false;
                                        imageView.setImageDrawable(getDrawable(R.drawable.iconmorningoff));
                                    } else if (Objects.equals(turn, Utils.FRIDAY_A)) {
                                        AfternoonFri = false;
                                        imageView.setImageDrawable(getDrawable(R.drawable.iconafternoonoff));
                                    } else if (Objects.equals(turn, Utils.FRIDAY_N)) {
                                        NightFri = false;
                                        imageView.setImageDrawable(getDrawable(R.drawable.iconnightoff));
                                    } else if (Objects.equals(turn, Utils.SATURDAY_M)) {
                                        MorningSat = false;
                                        imageView.setImageDrawable(getDrawable(R.drawable.iconmorningoff));
                                    } else if (Objects.equals(turn, Utils.SATURDAY_A)) {
                                        AfternoonSat = false;
                                        imageView.setImageDrawable(getDrawable(R.drawable.iconafternoonoff));
                                    } else if (Objects.equals(turn, Utils.SATURDAY_N)) {
                                        NightSat = false;
                                        imageView.setImageDrawable(getDrawable(R.drawable.iconnightoff));
                                    }
                                    break;
                                case Utils.CODE_ERROR:
                                    if (dialog.isShowing()) {
                                        dialog.dismiss();
                                    }
                                    Utils.toastyInfo(getApplicationContext(), e.getMessage());
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

    private void getDayAndTurn(final String turn, final ImageView imageview, final com.suke.widget.SwitchButton toggleButton) {

        Ion.with(this)
                .load(Utils.URL_GET_CALENDAR_USER)
                .setBodyParameter(Utils.ID_USER_APP, id)
                .setBodyParameter(Utils.TURN_APP, turn)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            String resultApp = result.get(Utils.CALENDAR).getAsString();
                            switch (resultApp) {
                                case Utils.CODE_SUCCESS:
                                    toggleButton.setChecked(true);

                                    if (Objects.equals(turn, Utils.SUNDAY_M)) {
                                        MorningSun = true;
                                        imageview.setImageDrawable(getDrawable(R.drawable.iconmorningon));
                                    } else if (Objects.equals(turn, Utils.SUNDAY_A)) {
                                        AfternoonSun = true;
                                        imageview.setImageDrawable(getDrawable(R.drawable.iconafternoonon));
                                    } else if (Objects.equals(turn, Utils.SUNDAY_N)) {
                                        NightSun = true;
                                        imageview.setImageDrawable(getDrawable(R.drawable.iconnighton));
                                    } else if (Objects.equals(turn, Utils.MONDAY_M)) {
                                        MorningMon = true;
                                        imageview.setImageDrawable(getDrawable(R.drawable.iconmorningon));
                                    } else if (Objects.equals(turn, Utils.MONDAY_A)) {
                                        AfternoonMon = true;
                                        imageview.setImageDrawable(getDrawable(R.drawable.iconafternoonon));
                                    } else if (Objects.equals(turn, Utils.MONDAY_N)) {
                                        NightMon = true;
                                        imageview.setImageDrawable(getDrawable(R.drawable.iconnighton));
                                    } else if (Objects.equals(turn, Utils.TUESDAY_M)) {
                                        MorningTue = true;
                                        imageview.setImageDrawable(getDrawable(R.drawable.iconmorningon));
                                    } else if (Objects.equals(turn, Utils.TUESDAY_A)) {
                                        AfternoonTue = true;
                                        imageview.setImageDrawable(getDrawable(R.drawable.iconafternoonon));
                                    } else if (Objects.equals(turn, Utils.TUESDAY_N)) {
                                        NightTue = true;
                                        imageview.setImageDrawable(getDrawable(R.drawable.iconnighton));
                                    } else if (Objects.equals(turn, Utils.WEDNESDAY_M)) {
                                        MorningWed = true;
                                        imageview.setImageDrawable(getDrawable(R.drawable.iconmorningon));
                                    } else if (Objects.equals(turn, Utils.WEDNESDAY_A)) {
                                        AfternoonWed = true;
                                        imageview.setImageDrawable(getDrawable(R.drawable.iconafternoonon));
                                    } else if (Objects.equals(turn, Utils.WEDNESDAY_N)) {
                                        NightWed = true;
                                        imageview.setImageDrawable(getDrawable(R.drawable.iconnighton));
                                    } else if (Objects.equals(turn, Utils.THURSDAY_M)) {
                                        MorningThu = true;
                                        imageview.setImageDrawable(getDrawable(R.drawable.iconmorningon));
                                    } else if (Objects.equals(turn, Utils.THURSDAY_A)) {
                                        AfternoonThu = true;
                                        imageview.setImageDrawable(getDrawable(R.drawable.iconafternoonon));
                                    } else if (Objects.equals(turn, Utils.THURSDAY_N)) {
                                        NightThu = true;
                                        imageview.setImageDrawable(getDrawable(R.drawable.iconnighton));
                                    } else if (Objects.equals(turn, Utils.FRIDAY_M)) {
                                        MorningFri = true;
                                        imageview.setImageDrawable(getDrawable(R.drawable.iconmorningon));
                                    } else if (Objects.equals(turn, Utils.FRIDAY_A)) {
                                        AfternoonFri = true;
                                        imageview.setImageDrawable(getDrawable(R.drawable.iconafternoonon));
                                    } else if (Objects.equals(turn, Utils.FRIDAY_N)) {
                                        NightFri = true;
                                        imageview.setImageDrawable(getDrawable(R.drawable.iconnighton));
                                    } else if (Objects.equals(turn, Utils.SATURDAY_M)) {
                                        MorningSat = true;
                                        imageview.setImageDrawable(getDrawable(R.drawable.iconmorningon));
                                    } else if (Objects.equals(turn, Utils.SATURDAY_A)) {
                                        AfternoonSat = true;
                                        imageview.setImageDrawable(getDrawable(R.drawable.iconafternoonon));
                                    } else if (Objects.equals(turn, Utils.SATURDAY_N)) {
                                        NightSat = true;
                                        imageview.setImageDrawable(getDrawable(R.drawable.iconnighton));
                                    }
                                    break;
                                case Utils.CODE_ERROR_NO_CALENDAR:

                                    break;
                                case Utils.CODE_ERROR:

                                    break;
                            }
                        } catch (Exception x) {
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
