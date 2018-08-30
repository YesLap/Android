package br.sendlook.yeslap.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.siyamed.shapeimageview.RoundedImageView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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
import com.vansuita.pickimage.dialog.PickImageDialog;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.sendlook.yeslap.R;
import br.sendlook.yeslap.view.Utils;
import de.hdodenhof.circleimageview.CircleImageView;

public class EditUserProfileActivity extends AppCompatActivity implements View.OnClickListener {

    //Variables
    private CircleImageView cvImageUser;
    private TextView tvUsername;
    private RoundedImageView ivImage1, ivImage2, ivImage3;
    private FirebaseAuth mAuth;
    private StorageReference mStorage;
    private ProgressDialog dialog;
    private String image_user_1, image_user_2, image_user_3;
    private Integer ImageStatus = 0;
    private Uri mainImageURI = null;
    private String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);

        mAuth = FirebaseAuth.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString(Utils.ID_USER);
        }

        //Cast
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        cvImageUser = (CircleImageView) findViewById(R.id.cvImageUser);
        ivImage1 = (RoundedImageView) findViewById(R.id.ivImage1);
        ivImage2 = (RoundedImageView) findViewById(R.id.ivImage2);
        ivImage3 = (RoundedImageView) findViewById(R.id.ivImage3);
        findViewById(R.id.btnGoToProfile).setOnClickListener(this);
        findViewById(R.id.btnGoToSettings).setOnClickListener(this);
        findViewById(R.id.btnChat).setOnClickListener(this);
        findViewById(R.id.btnCalendar).setOnClickListener(this);
        findViewById(R.id.btnSearch).setOnClickListener(this);
        findViewById(R.id.btnEditUsername).setOnClickListener(this);
        findViewById(R.id.btnChamgeImage1).setOnClickListener(this);
        findViewById(R.id.btnChangeImage2).setOnClickListener(this);
        findViewById(R.id.btnChangeImage3).setOnClickListener(this);

        //Function to get the user data
        getUserData();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnGoToProfile:
                finish();
                break;
            case R.id.btnGoToSettings:
                Intent intentsettings = new Intent(EditUserProfileActivity.this, SettingsActivity.class);
                intentsettings.putExtra(Utils.ID_USER, id);
                startActivity(intentsettings);
                break;
            case R.id.btnEditUsername:
                updateUsername();
                break;
            case R.id.btnChat:
                Intent intentchat = new Intent(EditUserProfileActivity.this, ChatMessagesActivity.class);
                startActivity(intentchat);
                break;
            case R.id.btnCalendar:
                Intent intentcalendar = new Intent(EditUserProfileActivity.this, CalendarActivity.class);
                intentcalendar.putExtra(Utils.ID_USER, id);
                startActivity(intentcalendar);
                break;
            case R.id.btnSearch:
                Intent intentsearch = new Intent(EditUserProfileActivity.this, FindUsersActivity.class);
                intentsearch.putExtra(Utils.ID_USER, id);
                startActivity(intentsearch);
                break;
            case R.id.btnChamgeImage1:
                ImageStatus = 1;
                imagePicker();
                break;
            case R.id.btnChangeImage2:
                ImageStatus = 2;
                imagePicker();
                break;
            case R.id.btnChangeImage3:
                ImageStatus = 3;
                imagePicker();
                break;
        }
    }

    private void imagePicker() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {

                mainImageURI = result.getUri();

                if (ImageStatus == 1) {
                    ivImage1.setImageURI(mainImageURI);
                    uploadImage(Utils.URL_UPDATE_IMAGE_USER_1, Utils.IMAGE_1, mainImageURI, ivImage1, cvImageUser);
                } else if (ImageStatus == 2) {
                    ivImage2.setImageURI(mainImageURI);
                    uploadImage(Utils.URL_UPDATE_IMAGE_USER_2, Utils.IMAGE_2, mainImageURI, ivImage2, null);
                } else if (ImageStatus == 3) {
                    ivImage3.setImageURI(mainImageURI);
                    uploadImage(Utils.URL_UPDATE_IMAGE_USER_3, Utils.IMAGE_3, mainImageURI, ivImage3, null);
                }


            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadImage(final String URL, String PATH, Uri IMAGE, final ImageView ivImage, final CircleImageView cvImage) {
        dialog = new ProgressDialog(EditUserProfileActivity.this);
        dialog.setTitle(getString(R.string.uploading_image));
        dialog.setMessage(getString(R.string.sending_image));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        final StorageReference filePath = mStorage.child("images_user").child(id).child(PATH + ".jpg");
        filePath.putFile(IMAGE).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!urlTask.isSuccessful());
                final Uri downloadUrl = urlTask.getResult();

                Ion.with(getApplicationContext())
                        .load(URL)
                        .setBodyParameter(Utils.ID_USER_APP, id)
                        .setBodyParameter(Utils.IMAGE_APP, downloadUrl.toString())
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                String returnApp = result.get(Utils.IMAGE).getAsString();
                                switch (returnApp) {
                                    case Utils.CODE_SUCCESS:
                                        if (dialog.isShowing()) {
                                            dialog.dismiss();
                                        }
                                        if (cvImage != null)  {
                                            Picasso.with(EditUserProfileActivity.this).load(downloadUrl.toString()).placeholder(R.drawable.img_profile).into(ivImage);
                                            Picasso.with(EditUserProfileActivity.this).load(downloadUrl.toString()).placeholder(R.drawable.img_profile).into(cvImage);
                                        } else {
                                            Picasso.with(EditUserProfileActivity.this).load(downloadUrl.toString()).placeholder(R.drawable.img_profile).into(ivImage);
                                        }
                                        Utils.toastySuccess(getApplicationContext(), getString(R.string.image_uploaded));
                                        break;
                                    case Utils.CODE_ERROR:
                                        Utils.toastyError(getApplicationContext(), getString(R.string.error_upload));
                                        break;
                                }
                            }
                        });
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

                            final ProgressDialog dialogs;
                            dialogs = new ProgressDialog(EditUserProfileActivity.this);
                            dialogs.setTitle(getString(R.string.loading));
                            dialogs.setMessage(getString(R.string.loading_msg));
                            dialogs.setCanceledOnTouchOutside(false);
                            dialogs.show();

                            Ion.with(getApplicationContext())
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
                                                    if (dialogs.isShowing()) {
                                                        dialogs.dismiss();
                                                    }
                                                    tvUsername.setText(username);
                                                    Utils.toastySuccess(getApplicationContext(), getString(R.string.username_changed));
                                                } else if (Objects.equals(returnApp, Utils.CODE_ERROR_USERNAME_EXISTS)) {
                                                    if (dialogs.isShowing()) {
                                                        dialogs.dismiss();
                                                    }
                                                    Utils.toastyInfo(getApplicationContext(), getString(R.string.username_already_used));
                                                } else if (Objects.equals(returnApp, Utils.CODE_ERROR)) {
                                                    if (dialogs.isShowing()) {
                                                        dialogs.dismiss();
                                                    }
                                                }

                                            } catch (Exception x) {
                                                if (dialogs.isShowing()) {
                                                    dialogs.dismiss();
                                                }
                                                Utils.toastyError(getApplicationContext(), x.getMessage());
                                            }
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

        dialog = new ProgressDialog(EditUserProfileActivity.this);
        dialog.setMessage(getString(R.string.loading));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Ion.with(this)
                .load(Utils.URL_GET_USER_DATA)
                .setBodyParameter(Utils.ID_USER_APP, id)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        try {
                            String returnApp = result.get(Utils.GET_USER_DATA).getAsString();

                            if (Objects.equals(returnApp, Utils.CODE_SUCCESS)) {
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }

                                String username = result.get(Utils.USERNAME_USER).getAsString();
                                image_user_1 = result.get(Utils.IMAGE_USER_1).getAsString();
                                image_user_2 = result.get(Utils.IMAGE_USER_2).getAsString();
                                image_user_3 = result.get(Utils.IMAGE_USER_3).getAsString();

                                tvUsername.setText(username);

                                if (image_user_1 != null && !Objects.equals(image_user_1, " ")) {
                                    Picasso.with(EditUserProfileActivity.this).load(image_user_1).placeholder(R.drawable.img_profile).into(ivImage1);
                                    Picasso.with(EditUserProfileActivity.this).load(image_user_1).placeholder(R.drawable.img_profile).into(cvImageUser);
                                }
                                if (image_user_2 != null && !Objects.equals(image_user_2, " ")) {
                                    Picasso.with(EditUserProfileActivity.this).load(image_user_2).placeholder(R.drawable.img_profile).into(ivImage2);
                                }
                                if (image_user_3 != null && !Objects.equals(image_user_3, " ")) {
                                    Picasso.with(EditUserProfileActivity.this).load(image_user_3).placeholder(R.drawable.img_profile).into(ivImage3);
                                }

                            } else if (Objects.equals(returnApp, Utils.CODE_ERROR)) {
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                Utils.toastyError(getApplicationContext(), "Error: " + Utils.CODE_ERROR);
                            }

                        } catch (Exception x) {
                            Utils.toastyError(getApplicationContext(), x.getMessage());
                        }
                    }
                });


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
