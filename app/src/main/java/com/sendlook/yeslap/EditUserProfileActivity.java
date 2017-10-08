package com.sendlook.yeslap;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class EditUserProfileActivity extends AppCompatActivity {

    //Variables
    private ImageView btnGoToProfile, btnGotToSettings;
    private CircleImageView cvImageUser;
    private TextView tvUsername;
    private RelativeLayout btnChat, btnCalendar, btnSearch;
    private FloatingActionButton btnEditUserProfile, btnEditUsername, btnChangeImage1, btnChangeImage2, btnChangeImage3;
    private ImageView ivImage1, ivImage2, ivImage3;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ProgressDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);

        //Instantiate Firebase
        mAuth = FirebaseAuth.getInstance();

        //Cast
        btnGoToProfile = (ImageView) findViewById(R.id.btnGoToProfile);
        btnGotToSettings = (ImageView)findViewById(R.id.btnGoToSettings);
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        cvImageUser = (CircleImageView) findViewById(R.id.cvImageUser);
        btnChat = (RelativeLayout) findViewById(R.id.btnChat);
        btnCalendar = (RelativeLayout) findViewById(R.id.btnCalendar);
        btnSearch = (RelativeLayout) findViewById(R.id.btnSearch);
        btnEditUserProfile = (FloatingActionButton) findViewById(R.id.btnEditUserProfile);
        btnEditUsername = (FloatingActionButton) findViewById(R.id.btnEditUsername);
        btnChangeImage1 = (FloatingActionButton) findViewById(R.id.btnChamgeImage1);
        btnChangeImage2 = (FloatingActionButton) findViewById(R.id.btnChangeImage2);
        btnChangeImage3 = (FloatingActionButton) findViewById(R.id.btnChangeImage3);
        ivImage1 = (ImageView) findViewById(R.id.ivImage1);
        ivImage2 = (ImageView) findViewById(R.id.ivImage2);
        ivImage3 = (ImageView) findViewById(R.id.ivImage3);

        //Function to get the user data
        getUserData();

        //GotoProfile Event Button
        btnGoToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //GoToSetting Event Button
        btnGotToSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //EditUsername Button Event
        btnEditUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Functio to update username
                updateUsername();
            }
        });

        //ChangeImage1 Event Button
        btnChangeImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //ChangeIamge2 Event Button
        btnChangeImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //ChangeImage3 Event Button
        btnChangeImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private void updateUsername() {
        //Dialog to change the user name
        new LovelyTextInputDialog(EditUserProfileActivity.this, R.style.EditTextTintTheme)
                .setTopColorRes(R.color.colorDarkBlue)
                .setTitle("Change your Username")
                //.setMessage("Change yout username")
                .setIcon(R.drawable.ic_create_white_24dp)
                .setInputFilter(R.string.text_input_error_message, new LovelyTextInputDialog.TextFilter() {
                    @Override
                    public boolean check(String text) {
                        return text.matches("\\w+");
                    }
                })
                .setConfirmButton(android.R.string.ok, new LovelyTextInputDialog.OnTextInputConfirmListener() {
                    @Override
                    public void onTextInputConfirmed(String text) {
                        //Progress Dialog
                        dialog = new ProgressDialog(EditUserProfileActivity.this);
                        dialog.setTitle(getString(R.string.loading));
                        dialog.setMessage(getString(R.string.loading_msg));
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();

                        //Function to save the user data at Firebase: Users > UID > user data
                        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());
                        Map<String, Object> user = new HashMap<>();
                        user.put("username", text);
                        mDatabase.updateChildren(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    dialog.dismiss();
                                    toastySuccess("Username Changed Successfully!");
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                dialog.dismiss();
                                toastyError(e.getMessage());
                            }
                        });
                    }
                })
                .show();
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
                String image1 = dataSnapshot.child("image1").getValue(String.class);
                String image2 = dataSnapshot.child("image2").getValue(String.class);
                String image3 = dataSnapshot.child("image3").getValue(String.class);

                tvUsername.setText(username);
                if (image != null && !Objects.equals(image, "")) {
                    Picasso.with(EditUserProfileActivity.this).load(image).placeholder(R.drawable.img_profile).into(cvImageUser);
                } else if (image1 != null && !Objects.equals(image1, "")) {
                    Picasso.with(EditUserProfileActivity.this).load(image1).placeholder(R.drawable.img_profile).into(ivImage1);
                } else if (image2 != null && !Objects.equals(image2, "")) {
                    Picasso.with(EditUserProfileActivity.this).load(image2).placeholder(R.drawable.img_profile).into(ivImage2);
                } else if (image3 != null && !Objects.equals(image3, "")) {
                    Picasso.with(EditUserProfileActivity.this).load(image3).placeholder(R.drawable.img_profile).into(ivImage3);
                }

                dialog.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


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
