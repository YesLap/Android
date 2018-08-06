package br.sendlook.yeslap.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import br.sendlook.yeslap.R;
import br.sendlook.yeslap.view.Utils;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etEmail, etPassword, etRetypePassword;
    private TextView tvHaveAccount;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Cast
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etRetypePassword = (EditText) findViewById(R.id.etRetypePassword);
        findViewById(R.id.btnSignUp).setOnClickListener(this);
        tvHaveAccount = (TextView) findViewById(R.id.tvHaveAccount);

        /** //Button Event
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = etEmail.getText().toString();
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
                } else {

                    dialog = new ProgressDialog(SignUpActivity.this);
                    dialog.setTitle(getString(R.string.loading));
                    dialog.setMessage(getString(R.string.loading_msg));
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                    //Function to create a new user account with email and password
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull final Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //Fucntion to put the user data at Firebase Database
                                mDatabase = FirebaseDatabase.getInstance().getReference().child(Utils.USERS).child(mAuth.getCurrentUser().getUid());
                                final HashMap<String, String> user = new HashMap<>();
                                user.put(Utils.USERNAME, "");
                                user.put(Utils.EMAIL, (email));
                                user.put(Utils.IMAGE_1, "");
                                user.put(Utils.IMAGE_2, "");
                                user.put(Utils.IMAGE_3, "");
                                user.put(Utils.SPOTLIGHT, "false");
                                //user.put(Utils.SINCE, getTime());
                                mDatabase.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            dialog.dismiss();
                                            Utils.toastySuccess(getApplicationContext(), getString(R.string.account_created));
                                            Intent intent = new Intent(SignUpActivity.this, ImageUsernameProfileActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        }
                                    }
                                });
                            } else {
                                //Errors Messages
                                try {
                                    throw task.getException();
                                } catch (FirebaseAuthWeakPasswordException f) {
                                    dialog.dismiss();
                                    Utils.toastyError(getApplicationContext(), getString(R.string.weak_password));
                                } catch (FirebaseAuthInvalidCredentialsException g) {
                                    dialog.dismiss();
                                    Utils.toastyError(getApplicationContext(), getString(R.string.invalid_credential));
                                } catch (FirebaseAuthUserCollisionException h) {
                                    dialog.dismiss();
                                    Utils.toastyError(getApplicationContext(), getString(R.string.email_used));
                                } catch (Exception i) {
                                    dialog.dismiss();
                                    Utils.toastyError(getApplicationContext(), i.getMessage());
                                }
                            }
                        }
                    });

                }

            }
        });*/

        //tvHaveAccount Event Button
        tvHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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
                                                Intent intent = new Intent(SignUpActivity.this, ImageUsernameProfileActivity.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                break;
                                            case Utils.CODE_ERROR_EMAIL:
                                                if (dialog.isShowing()) {
                                                    dialog.dismiss();
                                                }
                                                Utils.toastyError(SignUpActivity.this, "This email is already being used");
                                                break;
                                            case Utils.CODE_ERROR:
                                                if (dialog.isShowing()) {
                                                    dialog.dismiss();
                                                }
                                                Utils.toastyError(SignUpActivity.this, "Excuse me! An unexpected error has occurred!");
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
        }
    }

    //Function to get the actual Date and Time
    private String getTime() {
        Calendar cal = Calendar.getInstance();
        Date data = cal.getTime();
        cal.setTime(data);
        return String.valueOf(data.getTime());
    }
}
