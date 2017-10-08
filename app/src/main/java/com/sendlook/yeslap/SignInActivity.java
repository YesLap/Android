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

import java.util.Objects;

import es.dmoral.toasty.Toasty;

public class SignInActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private TextView tvForgotPassword, tvNewAccount;
    private ImageView btnSignIn;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        //Instantiate Firebase
        mAuth = FirebaseAuth.getInstance();

        //Cast
        etEmail = (EditText) findViewById(R.id.etEmail);
        etPassword = (EditText) findViewById(R.id.etPassword);
        tvForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);
        tvNewAccount = (TextView) findViewById(R.id.tvNewAccount);
        btnSignIn = (ImageView) findViewById(R.id.btnSignIn);

        //Button Event
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Get the fields data
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();

                //Make sure the fields are empty
                if (Objects.equals(email, "")) {
                    toastyInfo(getString(R.string.fill_email));
                } else if (Objects.equals(password, "")) {
                    toastyInfo(getString(R.string.fill_password));
                } else {

                    dialog = new ProgressDialog(SignInActivity.this);
                    dialog.setTitle(getString(R.string.logging));
                    dialog.setMessage(getString(R.string.logging_msg));
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                    //Login in
                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                dialog.dismiss();
                                Intent intent = new Intent(SignInActivity.this, UserProfileActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
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

        tvNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
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
