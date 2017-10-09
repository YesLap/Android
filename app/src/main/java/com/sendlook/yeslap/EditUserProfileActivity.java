package com.sendlook.yeslap;

import android.app.ProgressDialog;
import android.content.*;
import android.graphics.drawable.Drawable;
import android.net.Uri;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
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
    private StorageReference mStorage;
    private ProgressDialog dialog;
    private static final int GALLERY_PICK_IMAGE_1 = 1;
    private static final int GALLERY_PICK_IMAGE_2 = 2;
    private static final int GALLERY_PICK_IMAGE_3 = 3;
    private static final int GALLERY_PICK_IMAGE_4 = 4;
    private String downloadURL = "";
    private Integer ImageStatus = 0;
    private Integer Image = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);

        //Instantiate Firebase
        mAuth = FirebaseAuth.getInstance();

        //Cast
        btnGoToProfile = (ImageView) findViewById(R.id.btnGoToProfile);
        btnGotToSettings = (ImageView) findViewById(R.id.btnGoToSettings);
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

        //cvImageUser EventButton
        cvImageUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Image = 1;
                Intent intentImage1 = new Intent();
                intentImage1.setType("image/*");
                intentImage1.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intentImage1, getString(R.string.select_image)), GALLERY_PICK_IMAGE_4);
            }
        });

        //ChangeImage1 Event Button
        btnChangeImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageStatus = 1;
                Intent intentImage1 = new Intent();
                intentImage1.setType("image/*");
                intentImage1.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intentImage1, getString(R.string.select_image)), GALLERY_PICK_IMAGE_1);
            }
        });

        //ChangeIamge2 Event Button
        btnChangeImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageStatus = 2;
                Intent intentImage2 = new Intent();
                intentImage2.setType("image/*");
                intentImage2.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intentImage2, getString(R.string.select_image)), GALLERY_PICK_IMAGE_2);
            }
        });

        //ChangeImage3 Event Button
        btnChangeImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageStatus = 3;
                Intent intentImage3 = new Intent();
                intentImage3.setType("image/*");
                intentImage3.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intentImage3, getString(R.string.select_image)), GALLERY_PICK_IMAGE_3);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        try {

            if (Image == 1) {
                if (requestCode == GALLERY_PICK_IMAGE_4 && resultCode == RESULT_OK) {
                    Uri imgUri = data.getData();
                    CropImage.activity(imgUri).setAspectRatio(1, 1).start(this);
                }

                if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

                    dialog = new ProgressDialog(EditUserProfileActivity.this);
                    dialog.setTitle(getString(R.string.uploading_image));
                    dialog.setMessage(getString(R.string.uploading_image_msg));
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                    CropImage.ActivityResult mResult = CropImage.getActivityResult(data);
                    //Saving Image1
                    if (resultCode == RESULT_OK) {
                        Uri resultUri = mResult.getUri();
                        if (resultUri != null) {

                            mStorage = FirebaseStorage.getInstance().getReference();
                            StorageReference filePath = mStorage.child("user_images").child(mAuth.getCurrentUser().getUid()).child("Image.jpg");

                            filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        downloadURL = task.getResult().getDownloadUrl().toString();
                                        toastySuccess(getString(R.string.image_uploaded));

                                        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());
                                        Map<String, Object> user = new HashMap<>();
                                        user.put("image", downloadURL);
                                        mDatabase.updateChildren(user);

                                        Picasso.with(EditUserProfileActivity.this).load(downloadURL).placeholder(R.drawable.img_profile).into(cvImageUser);
                                        dialog.dismiss();
                                    } else {
                                        toastyError(task.getException().getMessage());
                                        dialog.hide();
                                    }
                                }
                            });
                        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                            Exception error = mResult.getError();
                            toastyError(error.getMessage());
                            dialog.hide();
                        }
                        Image = 0;
                    }
                }
            }
            if (ImageStatus == 1) {
                if (requestCode == GALLERY_PICK_IMAGE_1 && resultCode == RESULT_OK) {
                    Uri imgUri = data.getData();
                    CropImage.activity(imgUri).setAspectRatio(1, 1).start(this);
                }

                if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

                    dialog = new ProgressDialog(EditUserProfileActivity.this);
                    dialog.setTitle(getString(R.string.uploading_image));
                    dialog.setMessage(getString(R.string.uploading_image_msg));
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                    CropImage.ActivityResult mResult = CropImage.getActivityResult(data);
                    //Saving Image1
                    if (resultCode == RESULT_OK && ImageStatus == 1) {
                        Uri resultUri = mResult.getUri();
                        if (resultUri != null) {

                            mStorage = FirebaseStorage.getInstance().getReference();
                            StorageReference filePath = mStorage.child("user_images").child(mAuth.getCurrentUser().getUid()).child("Image1.jpg");

                            filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        downloadURL = task.getResult().getDownloadUrl().toString();
                                        toastySuccess(getString(R.string.image_uploaded));

                                        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());
                                        Map<String, Object> user = new HashMap<>();
                                        user.put("image1", downloadURL);
                                        mDatabase.updateChildren(user);

                                        Picasso.with(EditUserProfileActivity.this).load(downloadURL).placeholder(R.drawable.img_profile).into(ivImage1);
                                        dialog.dismiss();
                                    } else {
                                        toastyError(task.getException().getMessage());
                                        dialog.hide();
                                    }
                                }
                            });
                        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                            Exception error = mResult.getError();
                            toastyError(error.getMessage());
                            dialog.hide();
                        }
                        ImageStatus = 0;
                    }
                }
            }
            if (ImageStatus == 2) {
                if (requestCode == GALLERY_PICK_IMAGE_2 && resultCode == RESULT_OK) {
                    Uri imgUri = data.getData();
                    CropImage.activity(imgUri).setAspectRatio(1, 1).start(this);
                }

                if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

                    dialog = new ProgressDialog(EditUserProfileActivity.this);
                    dialog.setTitle(getString(R.string.uploading_image));
                    dialog.setMessage(getString(R.string.uploading_image_msg));
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                    CropImage.ActivityResult mResult = CropImage.getActivityResult(data);
                    //Saving Image1
                    if (resultCode == RESULT_OK) {
                        Uri resultUri = mResult.getUri();
                        if (resultUri != null) {

                            mStorage = FirebaseStorage.getInstance().getReference();
                            StorageReference filePath = mStorage.child("user_images").child(mAuth.getCurrentUser().getUid()).child("Image2.jpg");

                            filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        downloadURL = task.getResult().getDownloadUrl().toString();
                                        toastySuccess(getString(R.string.image_uploaded));

                                        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());
                                        Map<String, Object> user = new HashMap<>();
                                        user.put("image2", downloadURL);
                                        mDatabase.updateChildren(user);

                                        Picasso.with(EditUserProfileActivity.this).load(downloadURL).placeholder(R.drawable.img_profile).into(ivImage2);
                                        dialog.dismiss();
                                    } else {
                                        toastyError(task.getException().getMessage());
                                        dialog.hide();
                                    }
                                }
                            });
                        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                            Exception error = mResult.getError();
                            toastyError(error.getMessage());
                            dialog.hide();
                        }
                        ImageStatus = 0;
                    }
                }
            }
            if (ImageStatus == 3) {
                if (requestCode == GALLERY_PICK_IMAGE_3 && resultCode == RESULT_OK) {
                    Uri imgUri = data.getData();
                    CropImage.activity(imgUri).setAspectRatio(1, 1).start(this);
                }

                if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

                    dialog = new ProgressDialog(EditUserProfileActivity.this);
                    dialog.setTitle(getString(R.string.uploading_image));
                    dialog.setMessage(getString(R.string.uploading_image_msg));
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                    CropImage.ActivityResult mResult = CropImage.getActivityResult(data);
                    //Saving Image1
                    if (resultCode == RESULT_OK) {
                        Uri resultUri = mResult.getUri();
                        if (resultUri != null) {

                            mStorage = FirebaseStorage.getInstance().getReference();
                            StorageReference filePath = mStorage.child("user_images").child(mAuth.getCurrentUser().getUid()).child("Image3.jpg");

                            filePath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        downloadURL = task.getResult().getDownloadUrl().toString();
                                        toastySuccess(getString(R.string.image_uploaded));

                                        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());
                                        Map<String, Object> user = new HashMap<>();
                                        user.put("image3", downloadURL);
                                        mDatabase.updateChildren(user);

                                        Picasso.with(EditUserProfileActivity.this).load(downloadURL).placeholder(R.drawable.img_profile).into(ivImage3);
                                        dialog.dismiss();
                                    } else {
                                        toastyError(task.getException().getMessage());
                                        dialog.hide();
                                    }
                                }
                            });
                        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                            Exception error = mResult.getError();
                            toastyError(error.getMessage());
                            dialog.hide();
                        }
                        ImageStatus = 0;
                    }
                }
            }




        } catch (Exception e) {
            toastyError(e.getMessage());
        }

        super.onActivityResult(requestCode, resultCode, data);
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
                }
                if (image1 != null && !Objects.equals(image1, "")) {
                    Picasso.with(EditUserProfileActivity.this).load(image1).placeholder(R.drawable.img_profile).into(ivImage1);
                }
                if (image2 != null && !Objects.equals(image2, "")) {
                    Picasso.with(EditUserProfileActivity.this).load(image2).placeholder(R.drawable.img_profile).into(ivImage2);
                }
                if (image3 != null && !Objects.equals(image3, "")) {
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
