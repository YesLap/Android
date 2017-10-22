package com.sendlook.yeslap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sendlook.yeslap.model.Utils;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    private ImageView ivGoToProfile;
    private ImageView ivGoToChat;
    private Button btnLocationUser;
    private Button btnGenderUser;
    private ImageView ivGenderUser;
    private String genderUser = "female";
    private TextView tvAgeUser;
    private CrystalSeekbar rbAgeUser;

    private Button btnLocationSearch;
    private Button btnGenderSearch;
    private ImageView ivGenderSearch;
    private String genderSearch = "male";
    private TextView tvAgeSearch;
    private CrystalRangeSeekbar rbAgeSearch;
    private Button btnPrivacyPolicy;
    private Button btnTermsService;
    private Button btnLicense;
    private Button btnLogOut;
    private Button btnDeleteAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mAuth = FirebaseAuth.getInstance();

        ivGoToProfile = (ImageView) findViewById(R.id.imgGoToProfile);
        ivGoToChat = (ImageView) findViewById(R.id.imgGoToChat);
        btnLocationUser = (Button) findViewById(R.id.btnLocationUser);
        btnGenderUser = (Button) findViewById(R.id.btnGenderUser);
        ivGenderUser = (ImageView) findViewById(R.id.imgGenderUser);
        tvAgeUser = (TextView) findViewById(R.id.tvRangeAgeUser);
        rbAgeUser = (CrystalSeekbar) findViewById(R.id.rangeAgeUser);

        btnLocationSearch = (Button) findViewById(R.id.btnLocationSearch);
        btnGenderSearch = (Button) findViewById(R.id.btnGenderSearch);
        ivGenderSearch = (ImageView) findViewById(R.id.imgGenderSearch);
        tvAgeSearch = (TextView) findViewById(R.id.tvRangeAgeSearch);
        rbAgeSearch = (CrystalRangeSeekbar) findViewById(R.id.rangeAgeSearch);

        btnPrivacyPolicy = (Button) findViewById(R.id.btnPrivacyPolicy);
        btnTermsService = (Button) findViewById(R.id.btnTermsService);
        btnLicense = (Button) findViewById(R.id.btnLicenses);
        btnLogOut = (Button) findViewById(R.id.btnLogOut);
        btnDeleteAccount = (Button) findViewById(R.id.btnDeleteAccount);

        // click events
        ivGoToProfile.setOnClickListener(callActivity(UserProfileActivity.class));

        ivGoToChat.setOnClickListener(callActivity(ChatActivity.class));

        btnLocationUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // implementar
                Utils.toastyInfo(getApplicationContext(), "Building");
            }
        });

        btnGenderUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (genderUser) {
                    case "male":
                        ivGenderUser.setImageResource(R.drawable.settings_icon_female);
                        genderUser = "female";
                        break;
                    case "female":
                        ivGenderUser.setImageResource(R.drawable.settings_icon_gay);
                        genderUser = "gay";
                        break;
                    case "gay":
                        ivGenderUser.setImageResource(R.drawable.settings_icon_male);
                        genderUser = "male";
                        break;
                }
            }
        });

        rbAgeUser.setOnSeekbarChangeListener(new OnSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue) {
                tvAgeUser.setText(String.valueOf(minValue));
            }
        });

        btnLocationSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // implementar
                Utils.toastyInfo(getApplicationContext(), "Buinding");
            }
        });

        btnGenderSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (genderSearch) {
                    case "male":
                        ivGenderSearch.setImageResource(R.drawable.settings_icon_female);
                        genderSearch = "female";
                        break;
                    case "female":
                        ivGenderSearch.setImageResource(R.drawable.settings_icon_gay);
                        genderSearch = "gay";
                        break;
                    case "gay":
                        ivGenderSearch.setImageResource(R.drawable.settings_icon_male);
                        genderSearch = "male";
                        break;
                }
            }
        });

        rbAgeSearch.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                tvAgeSearch.setText(String.valueOf(minValue) + "-" + String.valueOf(maxValue));
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
                if (mAuth != null) {
                    mAuth.signOut();
                }
                Utils.toastyInfo(getApplicationContext(), "Log Out");
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


    @Override
    protected void onResume() {
        super.onResume();
        setStatusOnline();
    }

    @Override
    protected void onPause() {
        super.onPause();
        setStatusOffline();
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
