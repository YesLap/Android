package com.sendlook.yeslap;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.siyamed.shapeimageview.RoundedImageView;
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
import com.vansuita.pickimage.dialog.PickImageDialog;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditUserProfileActivity extends AppCompatActivity {

    //Variables
    private ImageView btnGoToProfile, btnGotToSettings;
    private CircleImageView cvImageUser;
    private TextView tvUsername;
    private RelativeLayout btnChat, btnCalendar, btnSearch;
    private FloatingActionButton btnEditUserProfile, btnEditUsername, btnChangeImage1, btnChangeImage2, btnChangeImage3;
    private RoundedImageView ivImage1, ivImage2, ivImage3;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private StorageReference mStorage;
    private ProgressDialog dialog;
    private static final int GALLERY_PICK_IMAGE_1 = 1;
    private static final int GALLERY_PICK_IMAGE_2 = 2;
    private static final int GALLERY_PICK_IMAGE_3 = 3;
    private String downloadURL = "";
    private Integer ImageStatus = 0;
    private Integer Image = 0;
    private PickImageDialog pickImage;
    private Boolean isExistUsername = false;
    private String oldUsername;
    private Uri mainImageURI = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);

        //Instantiate Firebase
        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference().child(Utils.USER_IMAGES).child(mAuth.getCurrentUser().getUid());

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
        ivImage1 = (RoundedImageView) findViewById(R.id.ivImage1);
        ivImage2 = (RoundedImageView) findViewById(R.id.ivImage2);
        ivImage3 = (RoundedImageView) findViewById(R.id.ivImage3);

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
                Intent intent = new Intent(EditUserProfileActivity.this, FindUsersActivity.class);
                startActivity(intent);
            }
        });

        //EditUsername Button Event
        btnEditUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Function to update username
                updateUsername();
            }
        });

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditUserProfileActivity.this, ChatMessagesActivity.class);
                startActivity(intent);
            }
        });

        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditUserProfileActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditUserProfileActivity.this, FindUsersActivity.class);
                startActivity(intent);
            }
        });

        //ChangeImage1 Event Button
        btnChangeImage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new ProgressDialog(EditUserProfileActivity.this);
                dialog.setTitle(getString(R.string.uploading_image));
                dialog.setMessage(getString(R.string.uploading_image_msg));
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                ImageStatus = 1;

                imagePicker();

            }
        });

        //ChangeIamge2 Event Button
        btnChangeImage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new ProgressDialog(EditUserProfileActivity.this);
                dialog.setTitle(getString(R.string.uploading_image));
                dialog.setMessage(getString(R.string.uploading_image_msg));
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                ImageStatus = 2;

                imagePicker();
            }
        });

        //ChangeImage3 Event Button
        btnChangeImage3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog = new ProgressDialog(EditUserProfileActivity.this);
                dialog.setTitle(getString(R.string.uploading_image));
                dialog.setMessage(getString(R.string.uploading_image_msg));
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                ImageStatus = 3;

                imagePicker();
            }
        });

    }

    private void imagePicker() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1,1)
                .start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                Utils.toastyInfo(getApplicationContext(), getString(R.string.sending_image));

                mainImageURI = result.getUri();

                if (ImageStatus == 1) {
                    ivImage1.setImageURI(mainImageURI);
                } else if (ImageStatus == 2) {
                    ivImage2.setImageURI(mainImageURI);
                } else if (ImageStatus == 3) {
                    ivImage3.setImageURI(mainImageURI);
                }

                final String user = String.format("image%s", ImageStatus);

                StorageReference filePath = mStorage.child(user + ".jpg");
                filePath.putFile(mainImageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(Utils.USERS).child(mAuth.getCurrentUser().getUid());
                            HashMap<String, Object> image = new HashMap<>();
                            String taskDownload = task.getResult().getDownloadUrl().toString();
                            image.put(user, taskDownload);
                            database.updateChildren(image).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Utils.toastySuccess(getApplicationContext(), "Profile Image Up-To-Date");
                                        if (dialog.isShowing()) {
                                            dialog.dismiss();
                                        }
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

    private void updateUsername() {


        new MaterialDialog.Builder(this)
                .title(getString(R.string.username))
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input(getString(R.string.new_username_msg), null, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        final String username = input.toString();

                        Pattern p = Pattern.compile("[^a-z]", Pattern.CASE_INSENSITIVE);
                        Matcher m = p.matcher(username);
                        boolean b = m.find();
                        boolean s = username.contains(" ");

                        if (b || s) {
                            Utils.toastyInfo(getApplicationContext(), getString(R.string.username_must_have_only));
                        } else if (Objects.equals(username, "")) {
                            Utils.toastyInfo(getApplicationContext(), getString(R.string.insert_username));
                        } else {
                            DatabaseReference mDatabase2 = FirebaseDatabase.getInstance().getReference().child(Utils.USERNAME).child(username);
                            mDatabase2.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.getChildrenCount() == 0) {
                                        //Progress Dialog
                                        final ProgressDialog dialog;
                                        dialog = new ProgressDialog(EditUserProfileActivity.this);
                                        dialog.setTitle(getString(R.string.loading));
                                        dialog.setMessage(getString(R.string.loading_msg));
                                        dialog.setCanceledOnTouchOutside(false);
                                        dialog.show();

                                        //get the username
                                        mDatabase = FirebaseDatabase.getInstance().getReference().child(Utils.USERS).child(mAuth.getCurrentUser().getUid());
                                        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                oldUsername = dataSnapshot.child(Utils.USERNAME).getValue(String.class);

                                                //delete the old username from Firebase: Username
                                                DatabaseReference mDatabase1 = FirebaseDatabase.getInstance().getReference().child(Utils.USERNAME).child(oldUsername);
                                                mDatabase1.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {

                                                            //save the new username in Firebase: Username
                                                            DatabaseReference mDatabase3 = FirebaseDatabase.getInstance().getReference().child(Utils.USERNAME).child(username);
                                                            HashMap<String, String> usernames = new HashMap<>();
                                                            usernames.put("uid", mAuth.getCurrentUser().getUid());
                                                            mDatabase3.setValue(usernames).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                    if (task.isSuccessful()) {

                                                                        //Function to save the user data at Firebase: Users > UID > user data
                                                                        Map<String, Object> user = new HashMap<>();
                                                                        user.put(Utils.USERNAME, username);
                                                                        mDatabase.updateChildren(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                if (task.isSuccessful()) {
                                                                                    dialog.dismiss();
                                                                                    Utils.toastySuccess(getApplicationContext(), getString(R.string.username_changed));
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

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                    } else if (dataSnapshot.getChildrenCount() >= 1) {
                                        Utils.toastyInfo(getApplicationContext(), getString(R.string.username_already_used));
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }

                    }
                })
                .negativeText(getString(R.string.cancels))
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();

    }

    private void getUserData() {
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
                String image1 = dataSnapshot.child(Utils.IMAGE_1).getValue(String.class);
                String image2 = dataSnapshot.child(Utils.IMAGE_2).getValue(String.class);
                String image3 = dataSnapshot.child(Utils.IMAGE_3).getValue(String.class);

                if (!(username == null || Objects.equals(username, ""))) {
                    tvUsername.setText(username);
                }

                if (image1 != null && !Objects.equals(image1, "")) {
                    Picasso.with(EditUserProfileActivity.this).load(image1).placeholder(R.drawable.img_profile).into(ivImage1);
                    Picasso.with(EditUserProfileActivity.this).load(image1).placeholder(R.drawable.img_profile).into(cvImageUser);
                }
                if (image2 != null && !Objects.equals(image2, "")) {
                    Picasso.with(EditUserProfileActivity.this).load(image2).placeholder(R.drawable.img_profile).into(ivImage2);
                }
                if (image3 != null && !Objects.equals(image3, "")) {
                    Picasso.with(EditUserProfileActivity.this).load((image3)).placeholder(R.drawable.img_profile).into(ivImage3);
                }

                dialog.dismiss();

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
