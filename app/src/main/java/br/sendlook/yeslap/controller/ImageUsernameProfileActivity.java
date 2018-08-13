package br.sendlook.yeslap.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.util.Log;
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
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.net.IDN;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.sendlook.yeslap.R;
import br.sendlook.yeslap.view.Utils;
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

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_username_profile);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString(Utils.ID_USER_APP);
            //Utils.toastyInfo(getApplicationContext(), id);
        }

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

                Pattern p = Pattern.compile("[^a-z]", Pattern.CASE_INSENSITIVE);
                Matcher m = p.matcher(username);
                boolean b = m.find();
                boolean s = username.contains(" ");

                //if (downloadURL == null || downloadURL.equals("")) {
                //Utils.toastyInfo(getApplicationContext(), getString(R.string.select_image_to_your_profile));
                //} else
                if (username.equals("")) {
                    Utils.toastyInfo(getApplicationContext(), getString(R.string.fill_username_field));
                } else if (b || s) {
                    Utils.toastyInfo(getApplicationContext(), getString(R.string.username_must_have_only));
                } else {
                    dialog = new ProgressDialog(ImageUsernameProfileActivity.this);
                    dialog.setTitle(getString(R.string.loading));
                    dialog.setMessage(getString(R.string.loading_msg));
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.setCancelable(false);
                    dialog.show();

                    saveUsernameAndUpdateProfile(username, id);
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
                Utils.toastyInfo(getApplicationContext(), String.valueOf(mainImageURI));
                cvImageUser.setImageURI(mainImageURI);

                //TODO: METODO PARA SALVAR A IMAGEM NO MYSQL


                btnSave.setVisibility(View.VISIBLE);

            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void saveUsernameAndUpdateProfile(String username, String id) {

        Ion.with(this)
                .load(Utils.URL_UPDATE_USERNAME)
                .setBodyParameter(Utils.ID_USER_APP, id)
                .setBodyParameter(Utils.USERNAME_USER, username)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            String returnApp = result.get(Utils.UPDATE_USERNAME).getAsString();

                            if (Objects.equals(returnApp, Utils.CODE_SUCCESS)) {
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                goToUserProfile();
                            } else if (Objects.equals(returnApp, Utils.CODE_ERROR_USERNAME_EXISTS)) {
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                Utils.toastyInfo(getApplicationContext(), getString(R.string.username_already_used));
                            } else if (Objects.equals(returnApp, Utils.CODE_ERROR)) {
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                            }

                        } catch (Exception x) {
                            Utils.toastyError(getApplicationContext(), x.getMessage());
                        }
                    }
                });

    }

    private void goToUserProfile() {
        Intent intent = new Intent(ImageUsernameProfileActivity.this, UserProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void CheckIfProfileIsComplete() {
        dialog = new ProgressDialog(ImageUsernameProfileActivity.this);
        dialog.setMessage(getString(R.string.loading));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        btnSave.setVisibility(View.INVISIBLE);

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
        //CheckIfProfileIsComplete();
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStatus(id, Utils.ONLINE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        updateStatus(id, Utils.OFFLINE);
    }

    private void updateStatus(final String id_user, final String status) {
        Ion.with(this)
                .load(Utils.URL_STATUS_USER)
                .setBodyParameter(Utils.ID_USER_APP, id_user)
                .setBodyParameter(Utils.STATUS_USER_APP, status)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            String resultApp = result.get(Utils.STATUS).getAsString();

                            if (Objects.equals(resultApp, Utils.CODE_SUCCESS)) {
                                Log.d(Utils.STATUS, "User " + id_user + " updated the status to: " + status);
                            } else if (Objects.equals(resultApp, Utils.CODE_ERROR)) {
                                Log.d(Utils.STATUS, "updated status failed");
                            }

                        } catch (Exception x) {
                            Utils.toastyError(getApplicationContext(), x.getMessage());
                        }
                    }
                });
    }

}
