package br.sendlook.yeslap.controller;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;
import java.util.TimeZone;

import br.sendlook.yeslap.BuildConfig;
import br.sendlook.yeslap.R;
import br.sendlook.yeslap.view.Utils;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {

    //Variables
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser mUser;
    private FloatingActionButton btnEditUserProfile;
    private ImageView btnGotoProfile, btnGoToSettings, btnFavorite;
    private RelativeLayout btnChat, btnCalendar, btnSearch;
    private TextView tvUsername;
    private CircleImageView cvImageUser;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        //Instantiate Firebase
        mAuth = FirebaseAuth.getInstance();

        //Cast
        btnEditUserProfile = (FloatingActionButton) findViewById(R.id.btnEditUserProfile);
        btnGotoProfile = (ImageView) findViewById(R.id.btnGoToProfile);
        btnGoToSettings = (ImageView) findViewById(R.id.btnGoToSettings);
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        cvImageUser = (CircleImageView) findViewById(R.id.cvImageUser);
        btnChat = (RelativeLayout) findViewById(R.id.btnChat);
        btnCalendar = (RelativeLayout) findViewById(R.id.btnCalendar);
        btnSearch = (RelativeLayout) findViewById(R.id.btnSearch);
        btnFavorite = (ImageView) findViewById(R.id.ivFavorite);

        chekcUpateApplication();

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToStart();
            }
        });

        //btnEditUserProfile Event Button
        btnEditUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfileActivity.this, EditUserProfileActivity.class);
                startActivity(intent);
            }
        });

        //btnGoToSetting Event Button
        btnGoToSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfileActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        //btnCalendar Event Button
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfileActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

        //btnSearch Event Button
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //checkUsernameAndImage();
                Intent intent = new Intent(UserProfileActivity.this, FindUsersActivity.class);
                startActivity(intent);
            }
        });

        //btnChat Event Button
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfileActivity.this, ChatMessagesActivity.class);
                startActivity(intent);
            }
        });

        //btnFavorite Event Button
        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfileActivity.this, FavoritesActivity.class);
                startActivity(intent);
            }
        });

    }

    private void showSpotlight() {
        new TapTargetSequence(this)
                .targets(
                        TapTarget.forView(findViewById(R.id.ivChats), "Chats","Here you can see all your chats!")
                                //Cor de fora transparente
                                .dimColor(R.color.colorLightBlue)
                                //Cor de dentro do circulo
                                .outerCircleColor(R.color.colorLightBlue)
                                //Cor de dentro do alvo
                                .targetCircleColor(android.R.color.white)
                                //Cor do texto
                                .textColor(android.R.color.white)
                                .cancelable(false),
                        TapTarget.forView(findViewById(R.id.ivCalendar), "Calendar","here you can change the days that you will be or not available")
                                //Cor de fora transparente
                                .dimColor(R.color.colorLightBlue)
                                //Cor de dentro do circulo
                                .outerCircleColor(R.color.colorLightBlue)
                                //Cor de dentro do alvo
                                .targetCircleColor(android.R.color.white)
                                //Cor do texto
                                .textColor(android.R.color.white)
                                .cancelable(false),
                        TapTarget.forView(findViewById(R.id.ivSearch), "Search","Here you can find the people you will talk to")
                                //Cor de fora transparente
                                .dimColor(R.color.colorLightBlue)
                                //Cor de dentro do circulo
                                .outerCircleColor(R.color.colorLightBlue)
                                //Cor de dentro do alvo
                                .targetCircleColor(android.R.color.white)
                                //Cor do texto
                                .textColor(android.R.color.white)
                                .cancelable(false),
                        TapTarget.forView(findViewById(R.id.ivFavorite), "Favorites","Here you can see your favorite contacts")
                                //Cor de fora transparente
                                .dimColor(R.color.colorLightBlue)
                                //Cor de dentro do circulo
                                .outerCircleColor(R.color.colorLightBlue)
                                //Cor de dentro do alvo
                                .targetCircleColor(android.R.color.white)
                                //Cor do texto
                                .textColor(android.R.color.white)
                                .cancelable(false),
                        TapTarget.forView(findViewById(R.id.btnEditUserProfile), "Your Profile","here you can edit your username and add your photos")
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
                mDatabase = FirebaseDatabase.getInstance().getReference().child(Utils.USERS).child(mAuth.getCurrentUser().getUid());
                HashMap<String, Object> spotlight = new HashMap<>();
                spotlight.put("spotlight", "true");
                mDatabase.updateChildren(spotlight);
            }

            @Override
            public void onSequenceStep(TapTarget lastTarget, boolean targetClicked) {

            }

            @Override
            public void onSequenceCanceled(TapTarget lastTarget) {

            }
        }).start();
    }

    //Get the user data from Firebase
    private void getUserData() {
        if (mAuth != null && mAuth.getCurrentUser() != null) {
            mDatabase = FirebaseDatabase.getInstance().getReference().child(Utils.USERS).child(mAuth.getCurrentUser().getUid());

            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String username = dataSnapshot.child(Utils.USERNAME).getValue(String.class);
                    String image = dataSnapshot.child(Utils.IMAGE_1).getValue(String.class);
                    String spotlight = dataSnapshot.child(Utils.SPOTLIGHT).getValue(String.class);

                    try {
                        if (!(username == null || Objects.equals(username, ""))) {
                            tvUsername.setText(username);
                        }
                        if (image != null && !Objects.equals(image, "")) {
                            Picasso.with(UserProfileActivity.this).load((image)).placeholder(R.drawable.img_profile).into(cvImageUser);
                        }
                        if (spotlight == null || spotlight.equals("")){
                            showSpotlight();
                        }
                    } catch (Exception e) {
                        Utils.toastyError(getApplicationContext(), e.getMessage());
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }

            });

        }
    }

    private void chekcUpateApplication()  {
        try {
            final String currentVersionApp = BuildConfig.VERSION_NAME;
            final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(Utils.APP_CONFIG).child(Utils.APP_VERSION);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String currentVersionPlayStore = dataSnapshot.child(Utils.CURRENT_VERSION).getValue(String.class);
                    final String url = dataSnapshot.child(Utils.PLAYSTORE_LINK).getValue(String.class);
                    if (!Objects.equals(currentVersionApp, currentVersionPlayStore)) {

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
                                        i.setData(Uri.parse(url));
                                        startActivity(i);
                                    }
                                })
                                .show();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

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
            // Display message in dialog box if you have not internet connection
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
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
            //Get the user data
            getUserData();
            //Set status Online
            setStatusOnline();
            //Check if the user profile is complete
            checkIfProfileIsComplete();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //If there is no one logged in to Firebase then it sends you to the SignIn screen.
        mUser = mAuth.getCurrentUser();
        if (mUser == null) {
            sendToStart();
        } else {
            //Check Internet Connection
            checkInternetConnection();
            setLastSeen();
        }
    }

    private void setLastSeen() {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(Utils.USERS).child(mAuth.getCurrentUser().getUid());
        HashMap<String, Object> user = new HashMap<>();
        user.put(Utils.LAST_SEEN, getDateNow());
        database.updateChildren(user);
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
        super.onPause();
        mUser = mAuth.getCurrentUser();
        //Chek if the user is logged
        if (mUser != null) {
            //Set status Offline
            setStatusOffline();
        }
    }

    private void sendToStart() {
        //Method that displaces the user and sends to the SignIn screen
        mAuth.signOut();
        Intent intent = new Intent(UserProfileActivity.this, SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    //Check if the username and image profile isn't null
    private void checkIfProfileIsComplete() {
        dialog = new ProgressDialog(UserProfileActivity.this);
        dialog.setMessage(getString(R.string.loading));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(Utils.USERS).child(mAuth.getCurrentUser().getUid());
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String username = dataSnapshot.child(Utils.USERNAME).getValue(String.class);
                final String image = dataSnapshot.child(Utils.IMAGE_1).getValue(String.class);

                DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(Utils.USERS).child(mAuth.getCurrentUser().getUid());
                HashMap<String, Object> user = new HashMap<>();
                user.put(Utils.UID, mAuth.getCurrentUser().getUid());
                database.updateChildren(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            if (Objects.equals(username, "") || Objects.equals(username, null) || Objects.equals(image, "")) {
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                Intent intent = new Intent(UserProfileActivity.this, ImageUsernameProfileActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            } else {
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
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

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setStatusOnline() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Utils.USERS).child(mAuth.getCurrentUser().getUid());
        HashMap<String, Object> status = new HashMap<>();
        status.put(Utils.STATUS, "online");
        mDatabase.updateChildren(status);
    }

    private void setStatusOffline() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Utils.USERS).child(mAuth.getCurrentUser().getUid());
        HashMap<String, Object> status = new HashMap<>();
        status.put(Utils.STATUS, "offline");
        mDatabase.updateChildren(status);
    }

}
