package com.sendlook.yeslap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.sendlook.yeslap.model.Utils;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class NewUsernameUserActivity extends AppCompatActivity {

    private AppCompatEditText etUsername;
    private TextInputLayout tlUsername;
    private Button btnSave;

    private String username = "";

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase, mDatabase1, mDatabase2;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_username_user);

        mAuth = FirebaseAuth.getInstance();

        etUsername = (AppCompatEditText) findViewById(R.id.etUsername);
        btnSave = (Button) findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = etUsername.getText().toString().trim();
                if (username.equals("")) {
                    Utils.toastyInfo(getApplicationContext(), getString(R.string.fill_username_field));
                } else {
                    dialog = new ProgressDialog(NewUsernameUserActivity.this);
                    dialog.setTitle(getString(R.string.loading));
                    dialog.setMessage(getString(R.string.loading_msg));
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setCancelable(false);
                    dialog.show();

                    CheckUsernameAndUpdateProfile(username);
                }
            }
        });

    }

    private void CheckIfProfileIsComplete() {
        dialog = new ProgressDialog(NewUsernameUserActivity.this);
        dialog.setMessage(getString(R.string.loading));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(Utils.USERS).child(mAuth.getCurrentUser().getUid());
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username = dataSnapshot.child("username").getValue(String.class);

                if (Objects.equals(username, "")) {
                    try {
                        etUsername.setText(username);
                    } catch (Exception e) {
                        dialog.dismiss();
                    } finally {
                        dialog.dismiss();
                    }
                } else if (!Objects.equals(username, "")) {
                    dialog.dismiss();
                    Intent intent = new Intent(NewUsernameUserActivity.this, UserProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void CheckUsernameAndUpdateProfile(final String username) {
        mDatabase2 = FirebaseDatabase.getInstance().getReference().child(Utils.USERNAME).child(username);
        mDatabase2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {

                    mDatabase = FirebaseDatabase.getInstance().getReference().child(Utils.USERS).child(mAuth.getCurrentUser().getUid());
                    HashMap<String, Object> profile = new HashMap<>();
                    profile.put("username", username);
                    mDatabase.updateChildren(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                mDatabase1 = FirebaseDatabase.getInstance().getReference().child(Utils.USERNAME).child(username);
                                HashMap<String, String> username = new HashMap<>();
                                username.put("uid", mAuth.getCurrentUser().getUid());
                                mDatabase1.setValue(username).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            dialog.dismiss();
                                            Intent intent = new Intent(NewUsernameUserActivity.this, UserProfileActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        dialog.dismiss();
                                        Utils.toastyError(getApplicationContext(), e.getMessage());
                                    }
                                });
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Utils.toastyError(getApplicationContext(), e.getMessage());
                        }
                    });

                } else {
                    dialog.dismiss();
                    Utils.toastyInfo(getApplicationContext(), getString(R.string.username_already_used));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        CheckIfProfileIsComplete();
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAuth != null) {
            setStatusOnline();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuth != null) {
            setStatusOffline();
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

}
