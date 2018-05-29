package br.sendlook.yeslap.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
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
import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import br.sendlook.yeslap.R;
import br.sendlook.yeslap.view.DialogNewEmail;
import br.sendlook.yeslap.view.Utils;
import im.delight.android.location.SimpleLocation;

public class SettingsActivity extends AppCompatActivity implements DialogNewEmail.DialogNewEmailListener {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser mUser;

    private ImageView ivGoToProfile;
    private ImageView ivGoToChat;
    private Button btnLocationUser;
    private Button btnGenderUser;
    private String genderUser = "female";
    private TextView tvAgeUser;
    private CrystalSeekbar rbAgeUser;

    private Button btnLocationSearch;
    private Button btnGenderSearch;
    private String genderSearch = "female";
    private TextView tvAgeSearch, tvRangeAgeSearch;
    private CrystalRangeSeekbar rbAgeSearch;
    private Button btnPrivacyPolicy;
    private Button btnTermsService;
    private Button btnLicense;
    private Button btnLogOut;
    private Button btnDeleteAccount;
    private Button btnEmailUser;
    private Button btnPasswordUser;

    private SimpleLocation location;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();

        ivGoToProfile = (ImageView) findViewById(R.id.imgGoToProfile);
        ivGoToChat = (ImageView) findViewById(R.id.imgGoToChat);
        btnLocationUser = (Button) findViewById(R.id.btnLocationUser);
        btnGenderUser = (Button) findViewById(R.id.btnGenderUser);
        tvAgeUser = (TextView) findViewById(R.id.tvRangeAgeUser);
        rbAgeUser = (CrystalSeekbar) findViewById(R.id.rangeAgeUser);
        btnEmailUser = (Button) findViewById(R.id.btnEmailUser);

        btnLocationSearch = (Button) findViewById(R.id.btnLocationSearch);
        btnGenderSearch = (Button) findViewById(R.id.btnGenderSearch);
        tvAgeSearch = (TextView) findViewById(R.id.tvRangeAgeSearch);
        rbAgeSearch = (CrystalRangeSeekbar) findViewById(R.id.rangeAgeSearch);
        tvRangeAgeSearch = (TextView) findViewById(R.id.tvRangeAgeSearch);

        btnPrivacyPolicy = (Button) findViewById(R.id.btnPrivacyPolicy);
        btnTermsService = (Button) findViewById(R.id.btnTermsService);
        btnLicense = (Button) findViewById(R.id.btnLicenses);
        btnLogOut = (Button) findViewById(R.id.btnLogOut);
        btnDeleteAccount = (Button) findViewById(R.id.btnDeleteAccount);
        btnPasswordUser = (Button) findViewById(R.id.btnPasswordUser);


        getCurrentLocation();
        getUserConfig();

        // click events
        ivGoToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ivGoToChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, ChatMessagesActivity.class);
                startActivity(intent);
            }
        });

        btnGenderUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(Utils.USERS).child(mAuth.getCurrentUser().getUid());

                switch (genderUser) {
                    case "male":

                        btnGenderUser.setCompoundDrawablesWithIntrinsicBounds(R.drawable.settings_icon_female, 0, 0, 0);
                        genderUser = "female";
                        btnGenderUser.setText(getString(R.string.female));

                        HashMap<String, Object> gender = new HashMap<>();
                        gender.put(Utils.GENDER_USER, genderUser);
                        database.updateChildren(gender).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("Gender User Updated", String.format("Gender User Updated: %s", genderUser));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Utils.toastyError(getApplicationContext(), e.getMessage());
                            }
                        });

                        break;
                    case "female":

                        btnGenderUser.setCompoundDrawablesWithIntrinsicBounds(R.drawable.settings_icon_gay, 0, 0, 0);
                        genderUser = "gay";
                        btnGenderUser.setText(getString(R.string.gay));

                        HashMap<String, Object> gender1 = new HashMap<>();
                        gender1.put(Utils.GENDER_USER, genderUser);
                        database.updateChildren(gender1).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("Gender Updated", String.format("Gender Updated: %s", genderUser));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Utils.toastyError(getApplicationContext(), e.getMessage());
                            }
                        });

                        break;
                    case "gay":

                        btnGenderUser.setCompoundDrawablesWithIntrinsicBounds(R.drawable.settings_icon_male, 0, 0, 0);
                        genderUser = "male";
                        btnGenderUser.setText(getString(R.string.male));

                        HashMap<String, Object> gender2 = new HashMap<>();
                        gender2.put(Utils.GENDER_USER, genderUser);
                        database.updateChildren(gender2).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Log.d("Gender Updated", String.format("Gender Updated: %s", genderUser));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Utils.toastyError(getApplicationContext(), e.getMessage());
                            }
                        });

                        break;
                }
            }
        });

        btnPasswordUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(SettingsActivity.this)
                        .title(R.string.reset_password)
                        .content(R.string.reset_password_msg)
                        .positiveText(getString(R.string.confirm))
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
                                mAuth.sendPasswordResetEmail(mAuth.getCurrentUser().getEmail()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Utils.toastySuccess(getApplicationContext(), getString(R.string.email_sent));
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Utils.toastyError(getApplicationContext(), e.getMessage());
                                    }
                                });
                            }
                        }).show();
            }
        });

        rbAgeUser.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue) {
                tvAgeUser.setText(String.valueOf(minValue));
            }
        });

        rbAgeUser.setOnSeekbarFinalValueListener(new OnSeekbarFinalValueListener() {
            @Override
            public void finalValue(final Number value) {
                DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(Utils.USERS).child(mAuth.getCurrentUser().getUid());
                HashMap<String, Object> age = new HashMap<>();
                age.put(Utils.AGE_USER, String.valueOf(value));
                database.updateChildren(age).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("age updated", "Age: " + value);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Utils.toastyError(getApplicationContext(), e.getMessage());
                    }
                });
            }
        });

        btnLocationSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:Implementar Localização de procura do usuário
                Utils.toastyInfo(getApplicationContext(), "Buinding");
            }
        });

        btnGenderSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(Utils.USERS).child(mAuth.getCurrentUser().getUid());

                switch (genderSearch) {
                    case "male":

                        btnGenderSearch.setCompoundDrawablesWithIntrinsicBounds(R.drawable.settings_icon_female, 0, 0, 0);
                        genderSearch = "female";
                        btnGenderSearch.setText(getString(R.string.female));

                        HashMap<String, Object> searchFemale = new HashMap<>();
                        searchFemale.put(Utils.GENDER_SEARCH, genderSearch);
                        database.updateChildren(searchFemale).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("Gender Search Updated", String.format("Gender Search Updated: %s", genderUser));
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Utils.toastyError(getApplicationContext(), e.getMessage());
                            }
                        });

                        break;
                    case "female":

                        btnGenderSearch.setCompoundDrawablesWithIntrinsicBounds(R.drawable.settings_icon_gay, 0, 0, 0);
                        genderSearch = "gay";
                        btnGenderSearch.setText(getString(R.string.gay));

                        HashMap<String, Object> searchGay = new HashMap<>();
                        searchGay.put(Utils.GENDER_SEARCH, genderSearch);
                        database.updateChildren(searchGay).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("Gender Search Updated", String.format("Gender Search Updated: %s", genderUser));
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Utils.toastyError(getApplicationContext(), e.getMessage());
                            }
                        });

                        break;
                    case "gay":

                        btnGenderSearch.setCompoundDrawablesWithIntrinsicBounds(R.drawable.settings_icon_male, 0, 0, 0);
                        genderSearch = "male";
                        btnGenderSearch.setText(getString(R.string.male));

                        HashMap<String, Object> searchMale = new HashMap<>();
                        searchMale.put(Utils.GENDER_SEARCH, genderSearch);
                        database.updateChildren(searchMale).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("Gender Search Updated", String.format("Gender Search Updated: %s", genderUser));
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Utils.toastyError(getApplicationContext(), e.getMessage());
                            }
                        });

                        break;
                }
            }
        });

        btnEmailUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogNewEmail newEmail = new DialogNewEmail();
                newEmail.show(getSupportFragmentManager(), "newEmail");
            }
        });

        rbAgeSearch.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(final Number minValue, final Number maxValue) {
                tvRangeAgeSearch.setText(String.format("%s - %s", String.valueOf(minValue), String.valueOf(maxValue)));
                DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(Utils.USERS).child(mAuth.getCurrentUser().getUid());
                HashMap<String, Object> ageSearch = new HashMap<>();
                ageSearch.put(Utils.AGE_SEARCH_MIN, String.valueOf(minValue));
                ageSearch.put(Utils.AGE_SEARCH_MAX, String.valueOf(maxValue));
                database.updateChildren(ageSearch).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("AGE SEARCH", String.format("%s - %s", String.valueOf(minValue), String.valueOf(maxValue)));
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Utils.toastyError(getApplicationContext(), e.getMessage());
                    }
                });
            }
        });

        btnPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.toastyInfo(getApplicationContext(), "Privacy Policy");
            }
        });

        btnTermsService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.toastyInfo(getApplicationContext(), "Terms of Service");
            }
        });

        btnLicense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.toastyInfo(getApplicationContext(), "Licenses");
            }
        });

        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setStatusOffline();
                mAuth.signOut();
                Intent intent = new Intent(SettingsActivity.this, SignInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                Utils.toastyInfo(getApplicationContext(), "See you soon!");
                callActivity(UserProfileActivity.class);
            }
        });

        btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.toastyInfo(getApplicationContext(), "Delete Account");
                callActivity(UserProfileActivity.class);
            }
        });

    }

    private void checkIfGpsIsOn() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
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
        }
    }

    private void getCurrentLocation() {
        location = new SimpleLocation(this);

        if (!location.hasLocationEnabled()) {
            SimpleLocation.openSettings(this);
        }

        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(Utils.USERS).child(mAuth.getCurrentUser().getUid());
        HashMap<String, Object> loc = new HashMap<>();
        loc.put(Utils.LATITUDE_USER, location.getLatitude());
        loc.put(Utils.LONGITUDE_USER, location.getLongitude());
        database.updateChildren(loc).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    try {
                        Geocoder geocoder = new Geocoder(SettingsActivity.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        btnLocationUser.setText(addresses.get(0).getLocality());
                    } catch (IOException e) {
                        Utils.toastyError(getApplicationContext(), e.getMessage());
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

    private void getUserConfig() {
        dialog = new ProgressDialog(SettingsActivity.this);
        dialog.setMessage(getString(R.string.loading));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        final DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(Utils.USERS).child(mAuth.getCurrentUser().getUid());
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String ageUser = dataSnapshot.child(Utils.AGE_USER).getValue(String.class);
                String genderUser = dataSnapshot.child(Utils.GENDER_USER).getValue(String.class);

                String genderSearch = dataSnapshot.child(Utils.GENDER_SEARCH).getValue(String.class);
                String ageSearchMin = dataSnapshot.child(Utils.AGE_SEARCH_MIN).getValue(String.class);
                String ageSearchMax = dataSnapshot.child(Utils.AGE_SEARCH_MAX).getValue(String.class);

                //AGE
                if (ageUser != null) {
                    tvAgeUser.setText(String.valueOf(ageUser));
                    rbAgeUser.setMinStartValue(Float.valueOf(ageUser)).apply();
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                } else {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }

                //GENDER USER
                if (genderUser != null) {
                    switch (genderUser) {
                        case "male":
                            btnGenderUser.setCompoundDrawablesWithIntrinsicBounds(R.drawable.settings_icon_male, 0, R.drawable.settings_right_arrow, 0);
                            SettingsActivity.this.genderUser = genderUser;
                            btnGenderUser.setText(genderUser);
                            break;
                        case "female":
                            btnGenderUser.setCompoundDrawablesWithIntrinsicBounds(R.drawable.settings_icon_female, 0, R.drawable.settings_right_arrow, 0);
                            SettingsActivity.this.genderUser = genderUser;
                            btnGenderUser.setText(genderUser);
                            break;
                        case "gay":
                            btnGenderUser.setCompoundDrawablesWithIntrinsicBounds(R.drawable.settings_icon_gay, 0, R.drawable.settings_right_arrow, 0);
                            SettingsActivity.this.genderUser = genderUser;
                            btnGenderUser.setText(genderUser);
                            break;
                    }
                }

                //GENDER SEARCH
                if (genderSearch != null) {
                    switch (genderSearch) {
                        case "male":
                            btnGenderSearch.setCompoundDrawablesWithIntrinsicBounds(R.drawable.settings_icon_male, 0, R.drawable.settings_right_arrow, 0);
                            SettingsActivity.this.genderSearch = genderSearch;
                            btnGenderSearch.setText(genderSearch);
                            break;
                        case "female":
                            btnGenderSearch.setCompoundDrawablesWithIntrinsicBounds(R.drawable.settings_icon_female, 0, R.drawable.settings_right_arrow, 0);
                            SettingsActivity.this.genderSearch = genderSearch;
                            btnGenderSearch.setText(genderSearch);
                            break;
                        case "gay":
                            btnGenderSearch.setCompoundDrawablesWithIntrinsicBounds(R.drawable.settings_icon_gay, 0, R.drawable.settings_right_arrow, 0);
                            SettingsActivity.this.genderSearch = genderSearch;
                            btnGenderSearch.setText(genderSearch);
                            break;
                    }
                }

                //EMAIL USER
                btnEmailUser.setText(mAuth.getCurrentUser().getEmail());

                //AGE SEARCH
                if (ageSearchMin != null && ageSearchMax != null) {
                    rbAgeSearch.setMinStartValue(Float.valueOf(ageSearchMin)).apply();
                    rbAgeSearch.setMaxStartValue(Float.valueOf(ageSearchMax)).apply();
                    tvRangeAgeSearch.setText(String.format("%s - %s", String.valueOf(ageSearchMin), String.valueOf(ageSearchMax)));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void changeEmail(String currentEmail, String currentPassword, final String newEmail) {

        if (Objects.equals(currentPassword, "")) {
            Utils.toastyInfo(getApplicationContext(), getString(R.string.fill_your_password));
        } else if (Objects.equals(newEmail, "")) {
            Utils.toastyInfo(getApplicationContext(), getString(R.string.fill_new_email));
        } else if (Objects.equals(newEmail, mAuth.getCurrentUser().getEmail())) {
            Utils.toastyInfo(getApplicationContext(), getString(R.string.email_equal_new));
        } else {
            dialog = new ProgressDialog(SettingsActivity.this);
            dialog.setMessage(getString(R.string.loading));
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            AuthCredential credential = EmailAuthProvider.getCredential(currentEmail, currentPassword);
            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        user.updateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    if (dialog.isShowing()) {
                                        dialog.dismiss();
                                    }
                                    btnEmailUser.setText(mAuth.getCurrentUser().getEmail());
                                    Utils.toastySuccess(getApplicationContext(), getString(R.string.email_changed));
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                Utils.toastyError(getApplicationContext(), e.getMessage());
                            }
                        });
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Utils.toastyError(getApplicationContext(), e.getMessage());

                }
            });
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mUser = mAuth.getCurrentUser();
        if (mUser != null) {
            checkIfGpsIsOn();
            location.beginUpdates();
            setStatusOnline();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mUser = mAuth.getCurrentUser();
        if (mUser != null) {
            setStatusOffline();
            location.endUpdates();
        }
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
