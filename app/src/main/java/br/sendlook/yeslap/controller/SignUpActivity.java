package br.sendlook.yeslap.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.sendlook.yeslap.R;
import br.sendlook.yeslap.view.Utils;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etEmail, etPassword, etRetypePassword;
    private ProgressDialog dialog;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Cast
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etRetypePassword = (EditText) findViewById(R.id.etRetypePassword);
        findViewById(R.id.btnSignUp).setOnClickListener(this);
        findViewById(R.id.tvHaveAccount).setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSignUp:

                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                String retypePassword = etRetypePassword.getText().toString();

                if (Objects.equals(email, "")) {
                    Utils.toastyInfo(getApplicationContext(), getString(R.string.fill_email));
                } else if (Objects.equals(password, "")) {
                    Utils.toastyInfo(getApplicationContext(), getString(R.string.fill_password));
                } else if (Objects.equals(retypePassword, "")) {
                    Utils.toastyInfo(getApplicationContext(), getString(R.string.retype_password));
                } else if (!Objects.equals(password, retypePassword)) {
                    Utils.toastyInfo(getApplicationContext(), getString(R.string.password_not_match));
                } else if (!validar(email)) {
                    Utils.toastyInfo(getApplicationContext(), getString(R.string.valid_email));
                } else if (password.length() < 6) {
                    Utils.toastyInfo(getApplicationContext(), getString(R.string.password_size_msg));
                } else {

                    dialog = new ProgressDialog(SignUpActivity.this);
                    dialog.setTitle(getString(R.string.loading));
                    dialog.setMessage(getString(R.string.loading_msg));
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                    Ion.with(this)
                            .load(Utils.URL_SIGN_UP)
                            .setBodyParameter(Utils.EMAIL_APP, email)
                            .setBodyParameter(Utils.PASSWORD_APP, password)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {
                                    try {
                                        String returnApp = result.get(Utils.SIGN_UP_CODE).getAsString();

                                        switch (returnApp) {
                                            case Utils.CODE_SUCCESS:
                                                dialog.dismiss();
                                                Utils.toastySuccess(getApplicationContext(), getString(R.string.account_created));
                                                id = Integer.parseInt(result.get(Utils.ID_USER).getAsString());
                                                saveLogin(id);
                                                goToUserProfile(String.valueOf(id));
                                                break;
                                            case Utils.CODE_ERROR_EMAIL:
                                                if (dialog.isShowing()) {
                                                    dialog.dismiss();
                                                }
                                                Utils.toastyError(SignUpActivity.this, getString(R.string.email_used));
                                                break;
                                            case Utils.CODE_ERROR:
                                                if (dialog.isShowing()) {
                                                    dialog.dismiss();
                                                }
                                                Utils.toastyError(SignUpActivity.this, getString(R.string.error_signup));
                                                break;
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
            case R.id.tvHaveAccount:
                finish();
                break;
        }
    }

    private void saveLogin(int id) {
        SharedPreferences.Editor editor = getSharedPreferences(Utils.PREF_NAME, MODE_PRIVATE).edit();
        editor.putString(Utils.ID_USER, String.valueOf(id));
        editor.apply();
    }

    private void goToUserProfile(String id_user) {
        Intent intent = new Intent(SignUpActivity.this, ImageUsernameProfileActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(Utils.ID_USER_APP, id_user);
        startActivity(intent);
    }

    public static boolean validar(String email)
    {
        boolean isEmailIdValid = false;
        if (email != null && email.length() > 0) {
            String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(email);
            if (matcher.matches()) {
                isEmailIdValid = true;
            }
        }
        return isEmailIdValid;
    }


    //Function to get the actual Date and Time
    private String getTime() {
        Calendar cal = Calendar.getInstance();
        Date data = cal.getTime();
        cal.setTime(data);
        return String.valueOf(data.getTime());
    }
}
