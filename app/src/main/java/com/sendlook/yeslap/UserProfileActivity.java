package com.sendlook.yeslap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class UserProfileActivity extends AppCompatActivity {

    //Firebase Variables
    private FirebaseAuth mAuth;
    private DatabaseReference mReference;
    private  FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
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
