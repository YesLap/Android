package com.sendlook.yeslap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sendlook.yeslap.model.Base64Custom;
import com.sendlook.yeslap.model.Utils;
import com.squareup.picasso.Picasso;

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

        //Get the user data
        getUserData();

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
                Utils.toastyInfo(getApplicationContext(), "Soon!");
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

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfileActivity.this, FindUsersActivity.class);
                startActivity(intent);
            }
        });

    }

    private void getUserData() {
        if (mAuth != null && mAuth.getCurrentUser() != null) {
            mDatabase = FirebaseDatabase.getInstance().getReference().child(Utils.USERS).child(mAuth.getCurrentUser().getUid());

            dialog = new ProgressDialog(this);
            dialog.setTitle(getString(R.string.loading));
            dialog.setMessage(getString(R.string.loading_msg));
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String username = dataSnapshot.child(Utils.USERNAME).getValue(String.class);
                    String image = dataSnapshot.child(Utils.IMAGE).getValue(String.class);

                    if (!(username == null || Objects.equals(username, ""))) {
                        tvUsername.setText(Base64Custom.decodeBase64(username));
                    } else {
                        tvUsername.setText("Username");
                    }
                    
                    if (image != null && !Objects.equals(image, "")) {
                        Picasso.with(UserProfileActivity.this).load(Base64Custom.decodeBase64(image)).placeholder(R.drawable.img_profile).into(cvImageUser);
                    }

                    dialog.dismiss();

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

    private void sendToStart() {
        //Method that displaces the user and sends to the SignIn screen
        mAuth.signOut();
        Intent intent = new Intent(UserProfileActivity.this, SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
