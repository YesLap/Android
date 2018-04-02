package com.sendlook.yeslap;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
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

public class NewImageUserActivity extends AppCompatActivity {

    private CircleImageView cvImageUser;
    private Button btnChangeImage;

    private Uri mainImageURI = null;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private StorageReference mStorage;

    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_image_user);

        mAuth = FirebaseAuth.getInstance();

        cvImageUser = (CircleImageView) findViewById(R.id.cvImageUser);
        btnChangeImage = (Button) findViewById(R.id.btnChangeImage);

        btnChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    private void imagePicker() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(NewImageUserActivity.this);
    }

    private void CheckIfProfileIsComplete() {
        dialog = new ProgressDialog(NewImageUserActivity.this);
        dialog.setMessage(getString(R.string.loading));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(Utils.USERS).child(mAuth.getCurrentUser().getUid());
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String image = dataSnapshot.child("image1").getValue(String.class);
                Utils.toastyInfo(getApplicationContext(), image);
                mainImageURI = Uri.parse(image);

                if (mainImageURI == null) {
                    try {
                        cvImageUser.setImageURI(mainImageURI);
                        //Picasso.with(NewImageUserActivity.this).load(image).placeholder(R.drawable.img_profile).into(cvImageUser);
                    } catch (Exception e) {
                        dialog.dismiss();
                    }
                } else {
                    dialog.dismiss();
                    Intent intent = new Intent(NewImageUserActivity.this, NewUsernameUserActivity.class);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                mainImageURI = result.getUri();
                cvImageUser.setImageURI(mainImageURI);

                Utils.toastyInfo(getApplicationContext(), getString(R.string.uploading_image_msg));

                mStorage = FirebaseStorage.getInstance().getReference();
                StorageReference filePath = mStorage.child(Utils.USER_IMAGES).child(mAuth.getCurrentUser().getUid()).child(Utils.IMAGE_1 + ".jpg");

                filePath.putFile(mainImageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        if (task.isSuccessful()) {
                            Utils.toastySuccess(getApplicationContext(), getString(R.string.image_uploaded));

                            Intent intent = new Intent(NewImageUserActivity.this, NewUsernameUserActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        } else {
                            Utils.toastyError(getApplicationContext(), task.getException().getMessage());
                        }
                    }
                });

            }
        }

        super.onActivityResult(requestCode, resultCode, data);
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
