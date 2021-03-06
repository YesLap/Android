package br.sendlook.yeslap.controller;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
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
import java.util.Objects;
import java.util.TimeZone;

import br.sendlook.yeslap.BuildConfig;
import br.sendlook.yeslap.R;
import br.sendlook.yeslap.view.Utils;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tvUsername;
    private CircleImageView cvImageUser;
    private ProgressDialog dialog;

    private String idUser, genderSearch, ageSearchMin, ageSearchMax, ageUser, genderUser, image_user_1, distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //Cast
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        cvImageUser = (CircleImageView) findViewById(R.id.cvImageUser);
        findViewById(R.id.btnEditUserProfile).setOnClickListener(this);
        findViewById(R.id.btnGoToSettings).setOnClickListener(this);
        findViewById(R.id.cvImageUser).setOnClickListener(this);
        findViewById(R.id.btnChat).setOnClickListener(this);
        findViewById(R.id.btnCalendar).setOnClickListener(this);
        findViewById(R.id.btnSearch).setOnClickListener(this);
        findViewById(R.id.ivFavorite).setOnClickListener(this);

        //chekcUpateApplication();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSearch:
                if (Objects.equals(genderUser, " ")) {
                    completePerfil(getString(R.string.select_genre));
                } else if (Objects.equals(ageUser, " ")) {
                    completePerfil(getString(R.string.select_yout_age));
                } else if (Objects.equals(genderSearch, " ")) {
                    completePerfil(getString(R.string.select_genre_search));
                } else if (Objects.equals(ageSearchMax, " ") || Objects.equals(ageSearchMin, " ")) {
                    completePerfil(getString(R.string.select_age_search));
                } else if (Objects.equals(distance, " ")) {
                    completePerfil(getString(R.string.select_distance));
                } else {
                    Intent intent = new Intent(UserProfileActivity.this, FindUsersActivity.class);
                    intent.putExtra(Utils.ID_USER, idUser);
                    intent.putExtra(Utils.GENDER_SEARCH, genderSearch);
                    intent.putExtra(Utils.AGE_SEARCH_MIN, ageSearchMin);
                    intent.putExtra(Utils.AGE_SEARCH_MAX, ageSearchMax);
                    intent.putExtra(Utils.GENDER_USER, genderUser);
                    intent.putExtra(Utils.AGE_USER, ageUser);
                    startActivity(intent);
                }
                break;
            case R.id.btnEditUserProfile:
                Intent intentuserprofile = new Intent(UserProfileActivity.this, EditUserProfileActivity.class);
                intentuserprofile.putExtra(Utils.ID_USER, idUser);
                startActivity(intentuserprofile);
                break;
            case R.id.btnGoToSettings:
                Intent intentsettings = new Intent(UserProfileActivity.this, SettingsActivity.class);
                intentsettings.putExtra(Utils.ID_USER, idUser);
                startActivity(intentsettings);
                break;
            case R.id.btnCalendar:
                Intent intentcalendar = new Intent(UserProfileActivity.this, CalendarActivity.class);
                intentcalendar.putExtra(Utils.ID_USER, idUser);
                startActivity(intentcalendar);
                break;
            case R.id.btnChat:
                if (Objects.equals(genderUser, " ")) {
                    completePerfil(getString(R.string.select_genre));
                } else if (Objects.equals(ageUser, " ")) {
                    completePerfil(getString(R.string.select_yout_age));
                } else if (Objects.equals(genderSearch, " ")) {
                    completePerfil(getString(R.string.select_genre_search));
                } else if (Objects.equals(ageSearchMax, " ") || Objects.equals(ageSearchMin, " ")) {
                    completePerfil(getString(R.string.select_age_search));
                } else if (Objects.equals(distance, " ")) {
                    completePerfil(getString(R.string.select_distance));
                } else {
                    Intent intentchat = new Intent(UserProfileActivity.this, ChatMessagesActivity.class);
                    intentchat.putExtra(Utils.ID_USER, idUser);
                    startActivity(intentchat);
                }
                break;
            case R.id.ivFavorite:
                Intent intentfavorite = new Intent(UserProfileActivity.this, FavoritesActivity.class);
                intentfavorite.putExtra(Utils.ID_USER, idUser);
                startActivity(intentfavorite);
                break;

        }
    }

    private void completePerfil(String description) {
        new TapTargetSequence(this)
                .targets(
                        TapTarget.forView(findViewById(R.id.btnGoToSettings), "Complete Seu Perfil", description)
                                //Cor de fora transparente
                                .dimColor(R.color.colorLightBlue)
                                //Cor de dentro do circulo
                                .outerCircleColor(R.color.colorLightBlue)
                                //Cor de dentro do alvo
                                .targetCircleColor(android.R.color.white)
                                //Cor do texto
                                .textColor(android.R.color.white)
                                .cancelable(false))
                .listener(new TapTargetSequence.Listener() {
                    @Override
                    public void onSequenceFinish() {

                    }

                    @Override
                    public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

                    }

                    @Override
                    public void onSequenceCanceled(TapTarget lastTarget) {

                    }
                }).start();
    }

    private void showSpotlight() {
        SharedPreferences preferences = getSharedPreferences(Utils.PREF_NAME, MODE_PRIVATE);
        String spotlight = preferences.getString(Utils.SPOTLIGHT, "");
        if (!Objects.equals(spotlight, Utils.DONE)) {
            new TapTargetSequence(this)
                    .targets(
                            TapTarget.forView(findViewById(R.id.ivChats), "Chats", "Here you can see all your chats!")
                                    //Cor de fora transparente
                                    .dimColor(R.color.colorLightBlue)
                                    //Cor de dentro do circulo
                                    .outerCircleColor(R.color.colorLightBlue)
                                    //Cor de dentro do alvo
                                    .targetCircleColor(android.R.color.white)
                                    //Cor do texto
                                    .textColor(android.R.color.white)
                                    .cancelable(false),
                            TapTarget.forView(findViewById(R.id.ivCalendar), "Calendar", "here you can change the days that you will be or not available")
                                    //Cor de fora transparente
                                    .dimColor(R.color.colorLightBlue)
                                    //Cor de dentro do circulo
                                    .outerCircleColor(R.color.colorLightBlue)
                                    //Cor de dentro do alvo
                                    .targetCircleColor(android.R.color.white)
                                    //Cor do texto
                                    .textColor(android.R.color.white)
                                    .cancelable(false),
                            TapTarget.forView(findViewById(R.id.ivSearch), "Search", "Here you can find the people you will talk to")
                                    //Cor de fora transparente
                                    .dimColor(R.color.colorLightBlue)
                                    //Cor de dentro do circulo
                                    .outerCircleColor(R.color.colorLightBlue)
                                    //Cor de dentro do alvo
                                    .targetCircleColor(android.R.color.white)
                                    //Cor do texto
                                    .textColor(android.R.color.white)
                                    .cancelable(false),
                            TapTarget.forView(findViewById(R.id.ivFavorite), "Favorites", "Here you can see your favorite contacts")
                                    //Cor de fora transparente
                                    .dimColor(R.color.colorLightBlue)
                                    //Cor de dentro do circulo
                                    .outerCircleColor(R.color.colorLightBlue)
                                    //Cor de dentro do alvo
                                    .targetCircleColor(android.R.color.white)
                                    //Cor do texto
                                    .textColor(android.R.color.white)
                                    .cancelable(false),
                            TapTarget.forView(findViewById(R.id.btnEditUserProfile), "Your Profile", "here you can edit your username and add your photos")
                                    //Cor de fora transparente
                                    .dimColor(R.color.colorLightBlue)
                                    //Cor de dentro do circulo
                                    .outerCircleColor(R.color.colorLightBlue)
                                    //Cor de dentro do alvo
                                    .targetCircleColor(android.R.color.white)
                                    //Cor do texto
                                    .textColor(android.R.color.white)
                                    .cancelable(false)
                    ).listener(new TapTargetSequence.Listener() {
                @Override
                public void onSequenceFinish() {
                    SharedPreferences.Editor editor = getSharedPreferences(Utils.PREF_NAME, MODE_PRIVATE).edit();
                    editor.putString(Utils.SPOTLIGHT, Utils.DONE);
                    editor.apply();
                }

                @Override
                public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

                }

                @Override
                public void onSequenceCanceled(TapTarget lastTarget) {

                }
            }).start();
        }
    }

    private void getUserData() {
        dialog = new ProgressDialog(UserProfileActivity.this);
        dialog.setMessage(getString(R.string.loading));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Ion.with(this)
                .load(Utils.URL_GET_USER_DATA)
                .setBodyParameter(Utils.ID_USER_APP, idUser)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            String returnApp = result.get(Utils.GET_USER_DATA).getAsString();

                            if (Objects.equals(returnApp, Utils.CODE_SUCCESS)) {

                                String username = result.get(Utils.USERNAME_USER).getAsString();
                                genderSearch = result.get(Utils.GENDER_SEARCH).getAsString();
                                ageSearchMin = result.get(Utils.AGE_SEARCH_MIN).getAsString();
                                ageSearchMax = result.get(Utils.AGE_SEARCH_MAX).getAsString();
                                ageUser = result.get(Utils.AGE_USER).getAsString();
                                genderUser = result.get(Utils.GENDER_USER).getAsString();
                                image_user_1 = result.get(Utils.IMAGE_USER_1).getAsString();
                                distance = result.get(Utils.DISTANCE).getAsString();

                                tvUsername.setText(username);
                                if (image_user_1 != null && !Objects.equals(image_user_1, " ")) {
                                    Picasso.with(UserProfileActivity.this).load(image_user_1).placeholder(R.drawable.img_profile).into(cvImageUser);
                                }
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }

                            } else if (Objects.equals(returnApp, Utils.CODE_ERROR)) {
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                Utils.toastyError(getApplicationContext(), "Error: " + Utils.CODE_ERROR);
                            } else if (Objects.equals(returnApp, Utils.CODE_ERROR_USERNAME_NULL)) {
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                goToCompleteProfile(idUser);
                            }

                        } catch (Exception x) {
                            Utils.toastyError(getApplicationContext(), x.getMessage());
                        }
                    }
                });

    }

    private void chekcUpateApplication() {
        try {
            Ion.with(this)
                    .load(Utils.URL_CHECK_VERSION_APP)
                    .setBodyParameter(Utils.VERSION_APP, BuildConfig.VERSION_NAME)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, final JsonObject result) {
                            String returnApp = result.get(Utils.SETTINGS).getAsString();

                            if (Objects.equals(returnApp, Utils.CODE_ERROR)) {
                                new MaterialDialog.Builder(UserProfileActivity.this)
                                        .title(getString(R.string.update_avaliable))
                                        .content(getString(R.string.update_avaliable_msg))
                                        .positiveText(getString(R.string.update))
                                        .cancelable(false)
                                        .canceledOnTouchOutside(false)
                                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                                            @Override
                                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                Intent i = new Intent(Intent.ACTION_VIEW);
                                                i.setData(Uri.parse(result.get(Utils.URL).getAsString()));
                                                startActivity(i);
                                            }
                                        })
                                        .show();
                            }

                        }
                    });
        } catch (Exception e) {
            Utils.toastyError(getApplicationContext(), e.getMessage());
        }
    }

    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();

        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    private void checkInternetConnection() {
        if (!haveNetworkConnection()) {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setTitle(R.string.no_internet_connection);
            alertDialogBuilder.setMessage(R.string.no_internet_connection_msg);
            alertDialogBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    finish();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        } else {
            if (isLoginAuth()) {

                checkIfProfileIsComplete();
                updateStatus(idUser, Utils.ONLINE);
                setLastSeen();
                chekcUpateApplication();

            } else {
                sendToStart();
            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkInternetConnection();
    }

    private void setLastSeen() {
        Ion.with(this)
                .load(Utils.URL_LAST_SEEN)
                .setBodyParameter(Utils.ID_USER_APP, idUser)
                .setBodyParameter(Utils.LAST_SEEN, getDateNow())
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            String returnApp = result.get(Utils.LAST_SEEN).getAsString();

                            if (Objects.equals(returnApp, Utils.CODE_SUCCESS)) {
                                Log.d(Utils.LAST_SEEN, "Last seen Updated: " + getDateNow());
                            } else if (Objects.equals(returnApp, Utils.CODE_ERROR)) {
                                Log.d(Utils.LAST_SEEN, "Last seen Updated Failed");
                            }

                        } catch (Exception x) {
                            Utils.toastyError(getApplicationContext(), x.getMessage());
                        }
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
    protected void onPause() {
        if (isLoginAuth()) {
            updateStatus(idUser, Utils.OFFLINE);
        }
        super.onPause();
    }

    private void sendToStart() {
        Intent intent = new Intent(UserProfileActivity.this, SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    private boolean isLoginAuth() {
        SharedPreferences preferences = getSharedPreferences(Utils.PREF_NAME, MODE_PRIVATE);
        idUser = preferences.getString(Utils.ID_USER, "");
        if (Objects.equals(idUser, "")) {
            return false;
        } else {
            return true;
        }
    }

    //Check if the username and image profile isn't null
    private void checkIfProfileIsComplete() {
        dialog = new ProgressDialog(UserProfileActivity.this);
        dialog.setMessage(getString(R.string.loading));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Ion.with(this)
                .load(Utils.URL_CHECK_COMPLETE_PROFILE)
                .setBodyParameter(Utils.ID_USER_APP, idUser)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            String returnApp = result.get(Utils.CHECK_COMPLETE_PROFILE).getAsString();

                            if (Objects.equals(returnApp, Utils.CODE_SUCCESS)) {
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                if (Objects.equals(result.get(Utils.USERNAME_USER).getAsString(), " ")) {
                                    goToCompleteProfile(idUser);
                                }
                                if (Objects.equals(result.get(Utils.IMAGE_USER_1).getAsString(), " ") && Objects.equals(result.get(Utils.IMAGE_USER_3).getAsString(), " ") && Objects.equals(result.get(Utils.IMAGE_USER_3).getAsString(), " ")) {
                                    goToCompleteProfile(idUser);
                                }
                                getUserData();
                                showSpotlight();
                            } else if (Objects.equals(returnApp, Utils.CODE_ERROR)) {
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                goToCompleteProfile(idUser);
                            }

                        } catch (Exception x) {
                            Utils.toastyError(getApplicationContext(), x.getMessage());
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        }
                    }
                });

    }

    private void goToCompleteProfile(String idUser) {
        Intent intent = new Intent(UserProfileActivity.this, ImageUsernameProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(Utils.ID_USER_APP, idUser);
        startActivity(intent);
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
