package com.sendlook.yeslap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sendlook.yeslap.model.Utils;
import com.squareup.picasso.Picasso;
import com.takusemba.spotlight.OnSpotlightEndedListener;
import com.takusemba.spotlight.OnSpotlightStartedListener;
import com.takusemba.spotlight.OnTargetStateChangedListener;
import com.takusemba.spotlight.SimpleTarget;
import com.takusemba.spotlight.Spotlight;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfileActivity extends AppCompatActivity {

    //Variables
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser mUser;
    private FloatingActionButton btnEditUserProfile;
    private ImageView btnGotoProfile, btnGoToSettings;
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
                checkUsernameAndImage();
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

    }

    private void showSpotlight() {
        SimpleTarget targetEditProfile = new SimpleTarget.Builder(this)
                .setPoint(440f, 343f)
                .setRadius(50f)
                .setTitle("Welcome to YesLap!")
                .setDescription("Click here to edit your profile")
                .build();

        SimpleTarget targetChatMessages = new SimpleTarget.Builder(this)
                .setPoint(160f, 600f)
                .setRadius(90f)
                .setTitle("Chat Messages")
                .setDescription("Here you can see your chat messages")
                .build();

        SimpleTarget targetCalendar = new SimpleTarget.Builder(this)
                .setPoint(360f, 600f)
                .setRadius(90f)
                .setTitle("Appointment Book")
                .setDescription("Here you can change when you are free or busy")
                .build();

        SimpleTarget targetFind = new SimpleTarget.Builder(this)
                .setPoint(565f, 600f)
                .setRadius(90f)
                .setTitle("Find Users")
                .setDescription("Here you can find users to chat")
                .build();

        SimpleTarget targetFavorite = new SimpleTarget.Builder(this)
                .setPoint(360f, 820f)
                .setRadius(90f)
                .setTitle("Favorite Users")
                .setDescription("Here you can see your favorite users")
                .build();

        SimpleTarget targetSettings = new SimpleTarget.Builder(this)
                .setPoint(618f, 120f)
                .setRadius(50f)
                .setTitle("Settings")
                .setDescription("Here you can change your settings")
                .build();

        SimpleTarget targetEnd = new SimpleTarget.Builder(this)
                .setPoint(300f, 300f)
                .setRadius(1f)
                .setTitle("Enjoy the YesLap")
                .setDescription("Now you already know how the app works, enjoy it to the fullest. Sincerely, YesLap Team!")
                .build();

        Spotlight.with(this)
                .setDuration(1000L)
                .setAnimation(new DecelerateInterpolator(2f))
                .setTargets(targetEditProfile, targetChatMessages, targetCalendar, targetFind, targetFavorite, targetSettings, targetEnd)
                .setOnSpotlightStartedListener(new OnSpotlightStartedListener() {
                    @Override
                    public void onStarted() {

                    }
                })
                .setOnSpotlightEndedListener(new OnSpotlightEndedListener() {
                    @Override
                    public void onEnded() {
                        mDatabase = FirebaseDatabase.getInstance().getReference().child(Utils.USERS).child(mAuth.getCurrentUser().getUid());
                        HashMap<String, Object> spotlight = new HashMap<>();
                        spotlight.put("spotlight", "true");
                        mDatabase.updateChildren(spotlight);
                    }
                })
                .start();
    }

    //Get the user data from Firebase
    private void getUserData() {
        if (mAuth != null && mAuth.getCurrentUser() != null) {
            mDatabase = FirebaseDatabase.getInstance().getReference().child(Utils.USERS).child(mAuth.getCurrentUser().getUid());

            dialog = new ProgressDialog(this);
            dialog.setTitle(getString(R.string.loading));
            dialog.setMessage(getString(R.string.loading_msg));
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String username = dataSnapshot.child(Utils.USERNAME).getValue(String.class);
                    String image = dataSnapshot.child(Utils.IMAGE_1).getValue(String.class);
                    String spotlight = dataSnapshot.child(Utils.SPOTLIGHT).getValue(String.class);

                    if (!(username == null || Objects.equals(username, ""))) {
                        tvUsername.setText((username));
                    } else {
                        tvUsername.setText("Username");
                    }

                    if (image != null && !Objects.equals(image, "")) {
                        Picasso.with(UserProfileActivity.this).load((image)).placeholder(R.drawable.img_profile).into(cvImageUser);
                    }

                    dialog.dismiss();

                    if (!Objects.equals(spotlight, "true")) {
                        showSpotlight();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
    }

    @Override
    protected void onStart() {
        //If there is no one logged in to Firebase then it sends you to the SignIn screen.
        super.onStart();
        mUser = mAuth.getCurrentUser();

        if (mUser == null) {
            sendToStart();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        //Get the user data
        getUserData();
        //
        setStatusOnline();
    }

    @Override
    protected void onPause() {
        super.onPause();
        setStatusOffline();
    }

    private void sendToStart() {
        //Method that displaces the user and sends to the SignIn screen
        mAuth.signOut();
        Intent intent = new Intent(UserProfileActivity.this, SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    //Check if the username and image profile isn't null
    private void checkUsernameAndImage() {
        dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.loading));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        mDatabase = FirebaseDatabase.getInstance().getReference().child(Utils.USERS).child(mAuth.getCurrentUser().getUid());
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username = dataSnapshot.child("username").getValue(String.class);
                String image = dataSnapshot.child("image1").getValue(String.class);

                if (Objects.equals(username, "Username") || Objects.equals(image, "")) {
                    dialog.dismiss();
                    Utils.toastyInfo(getApplicationContext(), "Please, change your username and add a profile photo to look for someone!");
                } else {
                    dialog.dismiss();
                    Intent intent = new Intent(UserProfileActivity.this, FindUsersActivity.class);
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
