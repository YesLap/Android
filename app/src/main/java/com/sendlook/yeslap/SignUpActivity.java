package com.sendlook.yeslap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class SignUpActivity extends AppCompatActivity {

    private EditText etEmail, etPassword, etRetypePassword;
    private ImageView btnSignUp;
    private TextView tvHaveAccount;
    private ProgressDialog dialog;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Instantiate Firebase
        mAuth = FirebaseAuth.getInstance();

        //Cast
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        etRetypePassword = (EditText) findViewById(R.id.etRetypePassword);
        btnSignUp = (ImageView) findViewById(R.id.btnSignUp);
        tvHaveAccount = (TextView) findViewById(R.id.tvHaveAccount);

        //Button Event
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                String retypePassword = etRetypePassword.getText().toString();

                if (Objects.equals(email, "")) {
                    toastyInfo(getString(R.string.fill_email));
                } else if (Objects.equals(password, "")) {
                    toastyInfo(getString(R.string.fill_password));
                } else if (Objects.equals(retypePassword, "")) {
                    toastyInfo(getString(R.string.retype_password));
                } else if (!Objects.equals(password, retypePassword)){
                    toastyInfo(getString(R.string.password_not_match));
                } else {

                    dialog = new ProgressDialog(SignUpActivity.this);
                    dialog.setTitle(getString(R.string.logging));
                    dialog.setMessage(getString(R.string.logging_msg));
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getCurrentUser().getUid());
                                HashMap<String, String> user = new HashMap<>();
                                user.put("username", "");
                                user.put("email", email);
                                user.put("image", "");
                                user.put("image1", "");
                                user.put("image2", "");
                                user.put("image3", "");
                                user.put("since", getTime());
                                mDatabase.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            dialog.dismiss();
                                            toastySuccess("Account Created Successfully");
                                            Intent intent = new Intent(SignUpActivity.this, UserProfileActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        toastyError(e.getMessage());
                                    }
                                });
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

            }
        });

        tvHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    private String getTime() {
        Calendar cal = Calendar.getInstance();
        Date data = cal.getTime();
        cal.setTime(data);
        return String.valueOf(data.getTime());
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
