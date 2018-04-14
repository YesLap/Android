package com.sendlook.yeslap;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sendlook.yeslap.model.Utils;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ImageUsernameProfileActivity extends AppCompatActivity {

    private AppCompatEditText etUsername;
    private CircleImageView cvImageUser;
    private Button btnChangeImage, btnSave;

    private String username = "";
    private String downloadURL = "";
    private Uri mainImageURI = null;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase, mDatabase1, mDatabase2;
    private StorageReference mStorage;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_username_profile);

        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference().child(Utils.USER_IMAGES).child(mAuth.getCurrentUser().getUid());

        etUsername = (AppCompatEditText) findViewById(R.id.etUsername);
        cvImageUser = (CircleImageView) findViewById(R.id.cvImageUser);
        btnChangeImage = (Button) findViewById(R.id.btnChangeImage);
        btnSave = (Button) findViewById(R.id.btnSave);

        btnChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imagePicker();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username = etUsername.getText().toString().trim();
                if (downloadURL == null || downloadURL.equals("")) {
                    Utils.toastyInfo(getApplicationContext(), getString(R.string.select_image_to_your_profile));
                } else if (username.equals("")) {
                    Utils.toastyInfo(getApplicationContext(), getString(R.string.fill_username_field));
                } else {
                    dialog = new ProgressDialog(ImageUsernameProfileActivity.this);
                    dialog.setTitle(getString(R.string.loading));
                    dialog.setMessage(getString(R.string.loading_msg));
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setCancelable(false);
                    dialog.show();

                    saveUsernameAndUpdateProfile(username);
                }
            }
        });


    }

    private void imagePicker() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(ImageUsernameProfileActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                Utils.toastyInfo(getApplicationContext(), getString(R.string.sending_image));

                mainImageURI = result.getUri();
                cvImageUser.setImageURI(mainImageURI);

                StorageReference filePath = mStorage.child(Utils.IMAGE_1 + ".jpg");
                filePath.putFile(mainImageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if (task.isSuccessful()) {
                            DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(Utils.USERS).child(mAuth.getCurrentUser().getUid());
                            final HashMap<String, String> image = new HashMap<>();
                            final String taskDownload = task.getResult().getDownloadUrl().toString();
                            image.put(Utils.IMAGE_1, taskDownload);
                            database.setValue(image).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        Utils.toastySuccess(getApplicationContext(), "Profile Image Up-To-Date");
                                        downloadURL = taskDownload;
                                        btnSave.setVisibility(View.VISIBLE);
                                    }

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    if (dialog.isShowing()) {
                                        dialog.dismiss();
                                    }
                                    Utils.toastyError(getApplicationContext(), e.getMessage());
                                }
                            });

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        Utils.toastyError(getApplicationContext(), e.getMessage());
                    }
                });

            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void saveUsernameAndUpdateProfile(final String username) {
        mDatabase2 = FirebaseDatabase.getInstance().getReference().child(Utils.USERNAME).child(username);
        mDatabase2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Utils.toastyInfo(getApplicationContext(), String.valueOf(dataSnapshot.child("uid").toString()));
                if (dataSnapshot.getChildrenCount() == 0) {

                    mDatabase = FirebaseDatabase.getInstance().getReference().child(Utils.USERS).child(mAuth.getCurrentUser().getUid());
                    HashMap<String, Object> profile = new HashMap<>();
                    //profile.put("image1", downloadURL);
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
                                            Intent intent = new Intent(ImageUsernameProfileActivity.this, UserProfileActivity.class);
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

    private void CheckIfProfileIsComplete() {
        dialog = new ProgressDialog(ImageUsernameProfileActivity.this);
        dialog.setMessage(getString(R.string.loading));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        //btnSave.setVisibility(View.INVISIBLE);

        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(Utils.USERS).child(mAuth.getCurrentUser().getUid());
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                username = dataSnapshot.child(Utils.USERNAME).getValue(String.class);
                String image = dataSnapshot.child(Utils.IMAGE_1).getValue(String.class);
                downloadURL = image;

                if (!Objects.equals(image, "")) {

                    mainImageURI = Uri.parse(image);
                    btnSave.setVisibility(View.VISIBLE);
                }

                if (Objects.equals(username, "") || Objects.equals(username, null) || Objects.equals(downloadURL, "") || downloadURL == null) {
                    try {
                        etUsername.setText(username);
                        Picasso.with(ImageUsernameProfileActivity.this).load(downloadURL).placeholder(R.drawable.img_profile).into(cvImageUser);
                    } catch (Exception e) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    } finally {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                } else if (!Objects.equals(username, "") || !Objects.equals(image, "")) {
                    dialog.dismiss();
                    Intent intent = new Intent(ImageUsernameProfileActivity.this, UserProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
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
