package br.sendlook.yeslap.controller;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.JsonObject;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.List;
import java.util.Objects;

import br.sendlook.yeslap.R;
import br.sendlook.yeslap.view.Utils;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etEmail, etPassword;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        showMessage();
        grantPermissions();


        //Cast
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        findViewById(R.id.tvForgotPassword).setOnClickListener(this);
        findViewById(R.id.tvNewAccount).setOnClickListener(this);
        findViewById(R.id.btnSignIn).setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSignIn:

                //Get the fields data
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                //Make sure the fields are empty
                if (Objects.equals(email, "")) {
                    Utils.toastyInfo(getApplicationContext(), getString(R.string.fill_email));
                } else if (Objects.equals(password, "")) {
                    Utils.toastyInfo(getApplicationContext(), getString(R.string.fill_password));
                } else {

                    dialog = new ProgressDialog(SignInActivity.this);
                    dialog.setTitle(getString(R.string.loading));
                    dialog.setMessage(getString(R.string.loading_msg));
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                    Ion.with(this)
                            .load(Utils.URL_SIGN_IN)
                            .setBodyParameter(Utils.EMAIL_APP, email)
                            .setBodyParameter(Utils.PASSWORD_APP, password)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {
                                    try {

                                        String returnApp = result.get(Utils.SIGN_IN_CODE).getAsString();

                                        if (Objects.equals(returnApp, Utils.CODE_SUCCESS)) {
                                            if (dialog.isShowing()) {
                                                dialog.dismiss();
                                            }
                                            saveLogin(Integer.parseInt(result.get(Utils.ID_USER).getAsString()));
                                            Intent intent = new Intent(getApplicationContext(), UserProfileActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        } else if (Objects.equals(returnApp, Utils.CODE_ERROR)) {
                                            if (dialog.isShowing()) {
                                                dialog.dismiss();
                                            }
                                            Utils.toastyError(getApplicationContext(), getString(R.string.email_or_password_incorrect));
                                        }

                                    } catch (Exception x) {
                                        if (dialog.isShowing()) {
                                            dialog.dismiss();
                                        }
                                        Utils.toastyError(getApplicationContext(), x.getMessage());
                                    }
                                }
                            });
                }
                break;
            case R.id.tvNewAccount:
                Intent intentnewacc = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intentnewacc);
                break;
            case R.id.tvForgotPassword:
                Intent intentforgot = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
                startActivity(intentforgot);
                break;
        }
    }

    private void saveLogin(int id) {
        SharedPreferences.Editor editor = getSharedPreferences(Utils.PREF_NAME, MODE_PRIVATE).edit();
        editor.putString(Utils.ID_USER, String.valueOf(id));
        editor.apply();
    }

    private void showMessage() {
        final SharedPreferences sharedPreferences = getSharedPreferences(Utils.MESSAGE, MODE_PRIVATE);
        String result = sharedPreferences.getString(Utils.MESSAGE, "");

        if (Objects.equals(result, "")) {
            new MaterialDialog.Builder(this)
                    .title(getString(R.string.warning))
                    .content(getString(R.string.warning_msg))
                    .positiveText(getString(R.string.iagree))
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(Utils.MESSAGE, "checked");
                            editor.apply();
                        }
                    })
                    .show();
        }

    }

    private void grantPermissions() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.INTERNET,
                        Manifest.permission.CAMERA,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_NETWORK_STATE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Utils.toastySuccess(getApplicationContext(), getString(R.string.permission_granted));
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            Utils.toastySuccess(getApplicationContext(), getString(R.string.permission_need_be_granted));
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();
    }

}
