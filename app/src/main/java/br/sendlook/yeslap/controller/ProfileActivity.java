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
import android.widget.TextView;

import com.ceylonlabs.imageviewpopup.ImagePopup;
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
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;
import java.util.TimeZone;

import br.sendlook.yeslap.R;
import br.sendlook.yeslap.view.Utils;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private ProgressDialog dialog;
    private CircleImageView cvImageUser;
    private ImageView btnFavorite, btnReport, btnChatMessage, ivImage1, ivImage2, ivImage3, btnGoToProfile, btnGoToSettings;
    private RelativeLayout btnChat, btnCalendar, btnFind;
    private TextView tvUsername;
    private String id, idSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString(Utils.ID_USER);
            idSearch = bundle.getString(Utils.ID_FAVORITE_USER_APP);
        }

        cvImageUser = (CircleImageView) findViewById(R.id.cvImageUser);
        btnFavorite = (ImageView) findViewById(R.id.ivFavorite);
        btnReport = (ImageView) findViewById(R.id.btnReport);
        btnChatMessage = (ImageView) findViewById(R.id.btnChatMessage);
        ivImage1 = (ImageView) findViewById(R.id.ivImage1);
        ivImage2 = (ImageView) findViewById(R.id.ivImage2);
        ivImage3 = (ImageView) findViewById(R.id.ivImage3);
        btnChat = (RelativeLayout) findViewById(R.id.btnChat);
        btnCalendar = (RelativeLayout) findViewById(R.id.btnCalendar);
        btnFind = (RelativeLayout) findViewById(R.id.btnSearch);
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        btnGoToProfile = (ImageView) findViewById(R.id.btnGoToProfile);
        btnGoToSettings = (ImageView) findViewById(R.id.btnGoToSettings);

        getUserData();

        btnGoToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnGoToSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentsettings = new Intent(ProfileActivity.this, SettingsActivity.class);
                intentsettings.putExtra(Utils.ID_USER, id);
                startActivity(intentsettings);
            }
        });

        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new ProgressDialog(ProfileActivity.this);
                dialog.setMessage(getString(R.string.loading));
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                Ion.with(getApplicationContext())
                        .load(Utils.URL_ADD_NEW_FAVORITE)
                        .setBodyParameter(Utils.ID_USER_APP, id)
                        .setBodyParameter(Utils.ID_FAVORITE_USER_APP, idSearch)
                        .setBodyParameter(Utils.DATE, getDateNow())
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                String returnApp = result.get(Utils.FAVORITES).getAsString();

                                if (Objects.equals(returnApp, Utils.CODE_SUCCESS)) {
                                    if (dialog.isShowing()) {
                                        dialog.dismiss();
                                    }
                                    Utils.toastySuccess(getApplicationContext(), getString(R.string.addeedd_favorite));
                                } else if (Objects.equals(returnApp, Utils.CODE_ERROR)) {
                                    if (dialog.isShowing()) {
                                        dialog.dismiss();
                                    }
                                    Utils.toastyError(getApplicationContext(), e.getMessage());
                                } else if (Objects.equals(returnApp, Utils.CODE_FAVORITE)) {
                                    if (dialog.isShowing()) {
                                        dialog.dismiss();
                                    }
                                    Utils.toastyInfo(getApplicationContext(), getString(R.string.already_exist_favorite));
                                }

                            }
                        });

            }
        });

        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, ReportActivity.class);
                intent.putExtra(Utils.ID_USER, id);
                startActivity(intent);
            }
        });

        btnChatMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(ProfileActivity.this, ChatActivity.class);
                //intent.putExtra(Utils.ID_USER, id);
                //startActivity(intent);
            }
        });

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(ProfileActivity.this, ChatMessagesActivity.class);
                //startActivity(intent);
            }
        });

        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentcalendar = new Intent(ProfileActivity.this, CalendarActivity.class);
                intentcalendar.putExtra(Utils.ID_USER, id);
                startActivity(intentcalendar);
            }
        });

        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, FindUsersActivity.class);
                intent.putExtra(Utils.ID_USER, id);
                startActivity(intent);
            }
        });

        ivImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePopup imagePopup = new ImagePopup(ProfileActivity.this);
                imagePopup.initiatePopup(ivImage1.getDrawable());
                imagePopup.setImageOnClickClose(true);
                imagePopup.viewPopup();
            }
        });

        ivImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePopup imagePopup = new ImagePopup(ProfileActivity.this);
                imagePopup.initiatePopup(ivImage2.getDrawable());
                imagePopup.setImageOnClickClose(true);
                imagePopup.viewPopup();
            }
        });

        ivImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePopup imagePopup = new ImagePopup(ProfileActivity.this);
                imagePopup.initiatePopup(ivImage3.getDrawable());
                imagePopup.setImageOnClickClose(true);
                imagePopup.viewPopup();
            }
        });

    }


    private String getDateNow() {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int yyyy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        return yyyy + "-" + mm + "-" + dd;
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


    private void getUserData() {
        dialog = new ProgressDialog(ProfileActivity.this);
        dialog.setMessage(getString(R.string.loading));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Ion.with(this)
                .load(Utils.URL_GET_USER_DATA)
                .setBodyParameter(Utils.ID_USER_APP, idSearch)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        String returnApp = result.get(Utils.GET_USER_DATA).getAsString();

                        try {
                            switch (returnApp) {
                                case Utils.CODE_SUCCESS:
                                    if (dialog.isShowing()) {
                                        dialog.dismiss();
                                    }
                                    tvUsername.setText(result.get(Utils.USERNAME_USER).getAsString());
                                    //TODO: CARREGAR AS IMAGENS
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
