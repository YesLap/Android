package br.sendlook.yeslap.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.sendlook.yeslap.R;
import br.sendlook.yeslap.view.DialogNewEmail;
import br.sendlook.yeslap.view.DialogNewPassword;
import br.sendlook.yeslap.view.Utils;
import im.delight.android.location.SimpleLocation;

public class SettingsActivity extends AppCompatActivity implements DialogNewEmail.DialogNewEmailListener, DialogNewPassword.DialogNewPasswordListener, View.OnClickListener {

    private ImageView ivGoToProfile;
    private ImageView ivGoToChat;
    private Button btnLocationUser;
    private Button btnGenderUser;
    private String genderUser = "";
    private TextView tvAgeUser, tvAgeSearch;
    private CrystalSeekbar rbAgeUser;

    private Button btnLocationSearch;
    private Button btnGenderSearch;
    private String genderSearch = "";
    private TextView tvRangeAgeSearch;
    private CrystalRangeSeekbar rbAgeSearch;
    private Button btnEmailUser;

    private SimpleLocation location;
    private ProgressDialog dialog;
    private String id;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString(Utils.ID_USER);
        }

        findViewById(R.id.btnGenderUser).setOnClickListener(this);
        btnGenderUser = (Button) findViewById(R.id.btnGenderUser);
        findViewById(R.id.btnGenderSearch).setOnClickListener(this);
        btnGenderSearch = (Button) findViewById(R.id.btnGenderSearch);

        ivGoToProfile = (ImageView) findViewById(R.id.imgGoToProfile);
        //ivGoToChat = (ImageView) findViewById(R.id.imgGoToChat);
        btnLocationUser = (Button) findViewById(R.id.btnLocationUser);
        findViewById(R.id.btnLocationUser).setOnClickListener(this);
        tvAgeUser = (TextView) findViewById(R.id.tvRangeAgeUser);
        rbAgeUser = (CrystalSeekbar) findViewById(R.id.rangeAgeUser);
        btnEmailUser = (Button) findViewById(R.id.btnEmailUser);
        findViewById(R.id.btnEmailUser).setOnClickListener(this);

        btnLocationSearch = (Button) findViewById(R.id.btnLocationSearch);
        tvAgeSearch = (TextView) findViewById(R.id.tvRangeAgeSearch);
        rbAgeSearch = (CrystalRangeSeekbar) findViewById(R.id.rangeAgeSearch);
        tvRangeAgeSearch = (TextView) findViewById(R.id.tvRangeAgeSearch);

        findViewById(R.id.btnPrivacyPolicy).setOnClickListener(this);
        findViewById(R.id.btnTermsService).setOnClickListener(this);
        findViewById(R.id.btnLicenses).setOnClickListener(this);
        findViewById(R.id.btnLogOut).setOnClickListener(this);
        findViewById(R.id.btnDeleteAccount).setOnClickListener(this);
        findViewById(R.id.btnPasswordUser).setOnClickListener(this);

        getCurrentLocation();
        getUserConfig();

        ivGoToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /*ivGoToChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, ChatMessagesActivity.class);
                startActivity(intent);
            }
        });*/

        rbAgeUser.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue) {
                tvAgeUser.setText(String.valueOf(minValue));
            }
        });

        rbAgeUser.setOnSeekbarFinalValueListener(new OnSeekbarFinalValueListener() {
            @Override
            public void finalValue(final Number value) {
                dialog = new ProgressDialog(SettingsActivity.this);
                dialog.setMessage(getString(R.string.loading));
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                Ion.with(getApplicationContext())
                        .load(Utils.URL_UPDATE_AGE_USER)
                        .setBodyParameter(Utils.ID_USER_APP, id)
                        .setBodyParameter(Utils.AGE_USER_APP, String.valueOf(value))
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                try {
                                    String returnApp = result.get(Utils.AGE_USER).getAsString();

                                    if (Objects.equals(returnApp, Utils.CODE_SUCCESS)) {
                                        if (dialog.isShowing()) {
                                            dialog.dismiss();
                                        }
                                        Log.d("Age User Updated", "Gender User Updated");
                                    } else if (Objects.equals(returnApp, Utils.CODE_ERROR)) {
                                        if (dialog.isShowing()) {
                                            dialog.dismiss();
                                        }
                                        Utils.toastyError(getApplicationContext(), e.getMessage());
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
        });

        rbAgeSearch.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                tvRangeAgeSearch.setText(String.format("%s - %s", String.valueOf(minValue), String.valueOf(maxValue)));
            }
        });

        rbAgeSearch.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                dialog = new ProgressDialog(SettingsActivity.this);
                dialog.setMessage(getString(R.string.loading));
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                Ion.with(getApplicationContext())
                        .load(Utils.URL_UPDATE_AGE_SEARCH)
                        .setBodyParameter(Utils.ID_USER_APP, id)
                        .setBodyParameter(Utils.AGE_SEARCH_MIN_APP, String.valueOf(minValue))
                        .setBodyParameter(Utils.AGE_SEARCH_MAX_APP, String.valueOf(maxValue))
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                try {
                                    String returnApp = result.get(Utils.AGE_SEARCH).getAsString();

                                    if (Objects.equals(returnApp, Utils.CODE_SUCCESS)) {
                                        if (dialog.isShowing()) {
                                            dialog.dismiss();
                                        }
                                    } else if (Objects.equals(returnApp, Utils.CODE_ERROR)) {
                                        if (dialog.isShowing()) {
                                            dialog.dismiss();
                                        }
                                        Utils.toastyError(getApplicationContext(), e.getMessage());
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
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLocationSearch:
                //TODO:Implementar Localização de procura do usuário
                Utils.toastyInfo(getApplicationContext(), "Buinding");
                break;
            case R.id.btnEmailUser:
                DialogNewEmail newEmail = new DialogNewEmail();
                newEmail.show(getSupportFragmentManager(), "newEmail");
                break;
            case R.id.btnPasswordUser:
                DialogNewPassword newPassword = new DialogNewPassword();
                newPassword.show(getSupportFragmentManager(), "newPassowrd");
                break;
            case R.id.btnPrivacyPolicy:
                Utils.toastyInfo(getApplicationContext(), "Privacy Policy");
                break;
            case R.id.btnTermsService:
                Utils.toastyInfo(getApplicationContext(), "Terms of Service");
                break;
            case R.id.btnLicenses:
                Utils.toastyInfo(getApplicationContext(), "Licenses");
                break;
            case R.id.btnLogOut:
                SharedPreferences.Editor editor = getSharedPreferences(Utils.PREF_NAME, MODE_PRIVATE).edit();
                editor.putString(Utils.ID_USER, "");
                editor.apply();
                Intent intent = new Intent(SettingsActivity.this, SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                Utils.toastyInfo(getApplicationContext(), "See you soon!");
                callActivity(UserProfileActivity.class);
                break;
            case R.id.btnDeleteAccount:
                //TODO
                Utils.toastyInfo(getApplicationContext(), "Delete Account");
                callActivity(UserProfileActivity.class);
                break;
            case R.id.btnGenderUser:
                Utils.toastySuccess(getApplicationContext(), genderUser);
                if (Objects.equals(genderUser, "male")) {
                    btnGenderUser.setCompoundDrawablesWithIntrinsicBounds(R.drawable.settings_icon_female, 0, 0, 0);
                    genderUser = "female";
                    btnGenderUser.setText(getString(R.string.female));

                    updateGenderUser(id, genderUser);
                } else if (Objects.equals(genderUser, "female")) {
                    btnGenderUser.setCompoundDrawablesWithIntrinsicBounds(R.drawable.settings_icon_gay, 0, 0, 0);
                    genderUser = "gay";
                    btnGenderUser.setText(getString(R.string.gay));

                    updateGenderUser(id, genderUser);
                } else if (Objects.equals(genderUser, "gay")) {
                    btnGenderUser.setCompoundDrawablesWithIntrinsicBounds(R.drawable.settings_icon_male, 0, 0, 0);
                    genderUser = "male";
                    btnGenderUser.setText(getString(R.string.male));

                    updateGenderUser(id, genderUser);
                }
                break;
            case R.id.btnGenderSearch:
                Utils.toastySuccess(getApplicationContext(), genderSearch);
                if (Objects.equals(genderSearch, "male")) {
                    btnGenderSearch.setCompoundDrawablesWithIntrinsicBounds(R.drawable.settings_icon_female, 0, 0, 0);
                    genderSearch = "female";
                    btnGenderSearch.setText(getString(R.string.female));

                    updateGenderSearch(id, genderSearch);
                } else if (Objects.equals(genderSearch, "female")) {
                    btnGenderSearch.setCompoundDrawablesWithIntrinsicBounds(R.drawable.settings_icon_gay, 0, 0, 0);
                    genderSearch = "gay";
                    btnGenderSearch.setText(getString(R.string.gay));

                    updateGenderSearch(id, genderSearch);
                } else if (Objects.equals(genderSearch, "gay")) {
                    btnGenderSearch.setCompoundDrawablesWithIntrinsicBounds(R.drawable.settings_icon_male, 0, 0, 0);
                    genderSearch = "male";
                    btnGenderSearch.setText(getString(R.string.male));

                    updateGenderSearch(id, genderSearch);
                }
                break;
        }
    }

    private void updateGenderUser(String id, String genderUser) {

        dialog = new ProgressDialog(SettingsActivity.this);
        dialog.setMessage(getString(R.string.loading));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Ion.with(getApplicationContext())
                .load(Utils.URL_UPDATE_GENDER_USER)
                .setBodyParameter(Utils.ID_USER_APP, id)
                .setBodyParameter(Utils.GENDER_USER_APP, genderUser)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            String returnApp = result.get(Utils.GENDER_USER).getAsString();

                            if (Objects.equals(returnApp, Utils.CODE_SUCCESS)) {
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                Log.d("Gender User Updated", "Gender User Updated");
                            } else if (Objects.equals(returnApp, Utils.CODE_ERROR)) {
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                Utils.toastyError(getApplicationContext(), e.getMessage());
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

    private void updateGenderSearch(String id, String gender) {
        dialog = new ProgressDialog(SettingsActivity.this);
        dialog.setMessage(getString(R.string.loading));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Ion.with(getApplicationContext())
                .load(Utils.URL_UPDATE_GENDER_SEARCH)
                .setBodyParameter(Utils.ID_USER_APP, id)
                .setBodyParameter(Utils.GENDER_SEARCH_APP, gender)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            String returnApp = result.get(Utils.GENDER_SEARCH).getAsString();

                            if (Objects.equals(returnApp, Utils.CODE_SUCCESS)) {
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                Log.d("Gender Search Updated", "Gender Search Updated");
                            } else if (Objects.equals(returnApp, Utils.CODE_ERROR)) {
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                Utils.toastyError(getApplicationContext(), e.getMessage());
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

    private boolean checkIfGpsIsOn() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return false;
        } else {
            return true;
        }
    }

    private void getCurrentLocation() {
        if (!checkIfGpsIsOn()) {
            new MaterialDialog.Builder(SettingsActivity.this)
                    .title("GPS OFF")
                    .content("You need to call your location so we can find you.")
                    .positiveText("Turn On")
                    .negativeText(getString(R.string.cancels))
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            dialog.dismiss();
                        }
                    })
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        }
                    }).show();
        } else {
            location = new SimpleLocation(this);

            if (!location.hasLocationEnabled()) {
                SimpleLocation.openSettings(this);
            }

            Ion.with(this)
                    .load(Utils.URL_UPDATE_LOCATION_USER)
                    .setBodyParameter(Utils.ID_USER_APP, id)
                    .setBodyParameter(Utils.LATITUDE_USER_APP, String.valueOf(location.getLatitude()))
                    .setBodyParameter(Utils.LONGITUDE_USER_APP, String.valueOf(location.getLongitude()))
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            try {
                                String returnApp = result.get(Utils.LOCATION_USER).getAsString();

                                if (Objects.equals(returnApp, Utils.CODE_SUCCESS)) {
                                    if (dialog.isShowing()) {
                                        dialog.dismiss();
                                    }

                                    try {
                                        Geocoder geocoder = new Geocoder(SettingsActivity.this, Locale.getDefault());
                                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                        btnLocationUser.setText(addresses.get(0).getLocality());
                                    } catch (IOException i) {
                                        Utils.toastyError(getApplicationContext(), i.getMessage());
                                    }

                                } else if (Objects.equals(returnApp, Utils.CODE_ERROR)) {
                                    if (dialog.isShowing()) {
                                        dialog.dismiss();
                                    }
                                    Utils.toastyError(getApplicationContext(), e.getMessage());
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

    private void getUserConfig() {
        dialog = new ProgressDialog(SettingsActivity.this);
        dialog.setMessage(getString(R.string.loading));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Ion.with(this)
                .load(Utils.URL_GET_USER_DATA)
                .setBodyParameter(Utils.ID_USER_APP, id)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            String returnApp = result.get(Utils.GET_USER_DATA).getAsString();

                            //Utils.toastySuccess(getApplicationContext(), "Gender User: " + result.get(Utils.GENDER_USER).getAsString());
                            //Utils.toastySuccess(getApplicationContext(), "Gender Search: " + result.get(Utils.GENDER_SEARCH).getAsString());
                            //Utils.toastySuccess(getApplicationContext(), "Age User: " + result.get(Utils.AGE_USER).getAsString());
                            //Utils.toastySuccess(getApplicationContext(), "Age Search Min: " + result.get(Utils.AGE_SEARCH_MIN).getAsString());
                            //Utils.toastySuccess(getApplicationContext(), "Age Search Max: " + result.get(Utils.AGE_SEARCH_MAX).getAsString());

                            //if (Objects.equals(result.get(Utils.GENDER_USER).getAsString(), " ")) {
                            //    Utils.toastySuccess(getApplicationContext(), "GENDER USER: " + result.get(Utils.GENDER_USER).getAsString());
                            //} else  if (Objects.equals(result.get(Utils.GENDER_SEARCH).getAsString(), " ")) {
                            //    Utils.toastySuccess(getApplicationContext(), "GENDER SEARCH: " + result.get(Utils.GENDER_SEARCH).getAsString());
                            //} else if (Objects.equals(result.get(Utils.AGE_USER).getAsString(), " ")) {
                            //    Utils.toastySuccess(getApplicationContext(), "AGE USER: " + result.get(Utils.AGE_USER).getAsString());
                            //} else if (Objects.equals(result.get(Utils.AGE_SEARCH_MIN).getAsString(), " ")) {
                            //   Utils.toastySuccess(getApplicationContext(), "AGE S MIN: " + result.get(Utils.AGE_SEARCH_MIN).getAsString());
                            //} else if (Objects.equals(result.get(Utils.AGE_SEARCH_MAX).getAsString(), " ")) {
                            //    Utils.toastySuccess(getApplicationContext(), "AGE S MAX: " + result.get(Utils.AGE_SEARCH_MAX).getAsString());
                            //}

                            if (Objects.equals(returnApp, Utils.CODE_SUCCESS)) {
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }

                                if (!Objects.equals(result.get(Utils.GENDER_USER).getAsString(), " ")) {
                                    genderUser = result.get(Utils.GENDER_USER).getAsString();
                                } else {
                                    genderUser = " ";
                                }

                                if (!Objects.equals(result.get(Utils.GENDER_SEARCH).getAsString(), " ")) {
                                    genderSearch = result.get(Utils.GENDER_SEARCH).getAsString();
                                } else {
                                    genderSearch = " ";
                                }

                                password = result.get(Utils.PASSWORD).getAsString();

                                //AGE
                                String ageUser = " ";
                                if (!Objects.equals(result.get(Utils.AGE_USER).getAsString(), " ")) {
                                    ageUser = result.get(Utils.AGE_USER).getAsString();

                                    tvAgeUser.setText(String.valueOf(ageUser));
                                    rbAgeUser.setMinStartValue(Float.valueOf(ageUser)).apply();
                                }

                                //GENDER USER
                                if (!Objects.equals(genderUser, " ")) {
                                    if (Objects.equals(genderUser, "male")) {
                                        btnGenderUser.setCompoundDrawablesWithIntrinsicBounds(R.drawable.settings_icon_male, 0, R.drawable.settings_right_arrow, 0);
                                        SettingsActivity.this.genderUser = genderUser;
                                        btnGenderUser.setText(genderUser);
                                    } else if (Objects.equals(genderUser, "female")) {
                                        btnGenderUser.setCompoundDrawablesWithIntrinsicBounds(R.drawable.settings_icon_female, 0, R.drawable.settings_right_arrow, 0);
                                        SettingsActivity.this.genderUser = genderUser;
                                        btnGenderUser.setText(genderUser);
                                    } else if (Objects.equals(genderUser, "gay")) {
                                        btnGenderUser.setCompoundDrawablesWithIntrinsicBounds(R.drawable.settings_icon_gay, 0, R.drawable.settings_right_arrow, 0);
                                        SettingsActivity.this.genderUser = genderUser;
                                        btnGenderUser.setText(genderUser);
                                    }
                                } else {
                                    btnGenderUser.setCompoundDrawablesWithIntrinsicBounds(R.drawable.settings_icon_gay, 0, R.drawable.settings_right_arrow, 0);
                                    SettingsActivity.this.genderUser = "female";
                                    btnGenderUser.setText(getString(R.string.choose_your_gender));
                                }

                                //GENDER SEARCH
                                if (!Objects.equals(genderSearch, " ")) {
                                    if (Objects.equals(genderSearch, "male")) {
                                        btnGenderSearch.setCompoundDrawablesWithIntrinsicBounds(R.drawable.settings_icon_male, 0, R.drawable.settings_right_arrow, 0);
                                        SettingsActivity.this.genderSearch = genderSearch;
                                        btnGenderSearch.setText(genderSearch);
                                    } else if (Objects.equals(genderSearch, "female")) {
                                        btnGenderSearch.setCompoundDrawablesWithIntrinsicBounds(R.drawable.settings_icon_female, 0, R.drawable.settings_right_arrow, 0);
                                        SettingsActivity.this.genderSearch = genderSearch;
                                        btnGenderSearch.setText(genderSearch);
                                    } else if (Objects.equals(genderSearch, "gay")) {
                                        btnGenderSearch.setCompoundDrawablesWithIntrinsicBounds(R.drawable.settings_icon_gay, 0, R.drawable.settings_right_arrow, 0);
                                        SettingsActivity.this.genderSearch = genderSearch;
                                        btnGenderSearch.setText(genderSearch);
                                    }
                                } else {
                                    btnGenderSearch.setCompoundDrawablesWithIntrinsicBounds(R.drawable.settings_icon_gay, 0, R.drawable.settings_right_arrow, 0);
                                    SettingsActivity.this.genderSearch = "female";
                                    btnGenderSearch.setText(getString(R.string.choose_partner_gender));
                                }

                                //EMAIL USER
                                btnEmailUser.setText(result.get(Utils.EMAIL).getAsString());

                                //AGE SEARCH
                                String ageSearchMin = " ";
                                if (!Objects.equals(result.get(Utils.AGE_SEARCH_MIN).getAsString(), " ")) {
                                    ageSearchMin = result.get(Utils.AGE_SEARCH_MIN).getAsString();
                                }

                                String ageSearchMax = " ";
                                if (!Objects.equals(result.get(Utils.AGE_SEARCH_MAX).getAsString(), " ")) {
                                    ageSearchMax = result.get(Utils.AGE_SEARCH_MAX).getAsString();
                                }

                                if (!Objects.equals(ageSearchMin, " ") && !Objects.equals(ageSearchMax, " ")) {
                                    rbAgeSearch.setMinStartValue(Float.valueOf(ageSearchMin)).apply();
                                    rbAgeSearch.setMaxStartValue(Float.valueOf(ageSearchMax)).apply();
                                    tvRangeAgeSearch.setText(String.format("%s - %s", String.valueOf(ageSearchMin), String.valueOf(ageSearchMax)));
                                }

                            } else if (Objects.equals(returnApp, Utils.CODE_ERROR)) {
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                Utils.toastyError(getApplicationContext(), e.getMessage());
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

    @Override
    public void changeEmail(String currentPassword, final String newEmail) {

        if (Objects.equals(currentPassword, "")) {
            Utils.toastyInfo(getApplicationContext(), getString(R.string.fill_your_password));
        } else if (!Objects.equals(currentPassword, password)) {
            Utils.toastyInfo(getApplicationContext(), "Wrong Password");
        } else if (Objects.equals(newEmail, "")) {
            Utils.toastyInfo(getApplicationContext(), getString(R.string.fill_new_email));
        } else if (!validar(newEmail)) {
            Utils.toastyInfo(getApplicationContext(), getString(R.string.valid_email));
        } else {

            dialog = new ProgressDialog(SettingsActivity.this);
            dialog.setMessage(getString(R.string.loading));
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            Ion.with(this)
                    .load(Utils.URL_UPDATE_EMAIL_USER)
                    .setBodyParameter(Utils.ID_USER_APP, id)
                    .setBodyParameter(Utils.EMAIL_APP, newEmail)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            try {
                                String returnApp = result.get(Utils.EMAIL_USER).getAsString();

                                switch (returnApp) {
                                    case Utils.CODE_SUCCESS:
                                        if (dialog.isShowing()) {
                                            dialog.dismiss();
                                        }
                                        btnEmailUser.setText(newEmail);
                                        Utils.toastySuccess(getApplicationContext(), getString(R.string.email_changed));
                                        break;
                                    case Utils.CODE_ERROR_EMAIL:
                                        if (dialog.isShowing()) {
                                            dialog.dismiss();
                                        }
                                        Utils.toastyError(getApplicationContext(), getString(R.string.email_used));
                                        break;
                                    case Utils.CODE_ERROR:
                                        if (dialog.isShowing()) {
                                            dialog.dismiss();
                                        }
                                        Utils.toastyError(getApplicationContext(), getString(R.string.error_signup));
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

    @Override
    public void changePassword(String currentPassword, final String newPassword) {

        if (Objects.equals(currentPassword, "")) {
            Utils.toastyInfo(getApplicationContext(), getString(R.string.fill_your_password));
        } else if (Objects.equals(newPassword, "")) {
            Utils.toastyInfo(getApplicationContext(), getString(R.string.fill_new_password));
        } else if (!Objects.equals(currentPassword, password)) {
            Utils.toastyInfo(getApplicationContext(), "Wrong Password");
        } else if (newPassword.length() < 6) {
            Utils.toastyInfo(getApplicationContext(), getString(R.string.password_size_msg));
        } else {

            dialog = new ProgressDialog(SettingsActivity.this);
            dialog.setMessage(getString(R.string.loading));
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            Ion.with(this)
                    .load(Utils.URL_UPDATE_PASSWORD_USER)
                    .setBodyParameter(Utils.ID_USER_APP, id)
                    .setBodyParameter(Utils.PASSWORD_APP, newPassword)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            try {
                                String returnApp = result.get(Utils.PASSWORD).getAsString();

                                switch (returnApp) {
                                    case Utils.CODE_SUCCESS:
                                        if (dialog.isShowing()) {
                                            dialog.dismiss();
                                        }
                                        Utils.toastySuccess(getApplicationContext(), getString(R.string.changed_password));
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

    public static boolean validar(String email) {
        boolean isEmailIdValid = false;
        if (email != null && email.length() > 0) {
            String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(email);
            if (matcher.matches()) {
                isEmailIdValid = true;
            }
        }
        return isEmailIdValid;
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


    private View.OnClickListener callActivity(final Class<?> activityClass) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // save data
                startActivity(new Intent(SettingsActivity.this, activityClass));
            }
        };
    }

}
