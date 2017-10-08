package com.sendlook.yeslap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class UserProfileActivity extends AppCompatActivity {

    //Variables
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private FirebaseUser mUser;
    private FloatingActionButton btnEditUserProfile;
    private ImageView btnGotoProfile, btnGoToSettings;
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

        //Get the user data
        getUserData();

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
                toastyInfo("Soon!");
            }
        });

        /**btnGotoProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toastyInfo("Soon");
            }
        });*/

    }

    private void getUserData() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());

        dialog = new ProgressDialog(this);
        dialog.setTitle(getString(R.string.loading));
        dialog.setMessage(getString(R.string.loading_msg));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username = dataSnapshot.child("username").getValue(String.class);
                String image = dataSnapshot.child("image").getValue(String.class);

                tvUsername.setText(username);
                if (image != null && !Objects.equals(image, "")) {
                    Picasso.with(UserProfileActivity.this).load(image).placeholder(R.drawable.img_profile).into(cvImageUser);
                }

                dialog.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


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

    private void toastySuccess(String msg) {
        Toasty.success(getApplicationContext(), msg, Toast.LENGTH_LONG, true).show();
    }

    private void toastyError(String msg) {
        Toasty.error(getApplicationContext(), msg, Toast.LENGTH_LONG, true).show();
    }

    private void toastyInfo(String msg) {
        Toasty.info(getApplicationContext(), msg, Toast.LENGTH_LONG, true).show();
    }

    private void toastyUsual(String msg, Drawable icon) {
        Toasty.normal(getApplicationContext(), msg, Toast.LENGTH_LONG, icon).show();
    }

}
