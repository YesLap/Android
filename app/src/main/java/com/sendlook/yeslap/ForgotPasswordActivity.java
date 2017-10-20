package com.sendlook.yeslap;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.sendlook.yeslap.model.Utils;

import java.util.Objects;

public class ForgotPasswordActivity extends AppCompatActivity {

    //Variables
    private EditText etEmail;
    private ImageView btnForgot;
    private TextView tvRemember;
    private ProgressDialog dialog;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //Instantiate Firebase
        mAuth = FirebaseAuth.getInstance();

        //Cast
        etEmail = (EditText) findViewById(R.id.etEmail);
        btnForgot = (ImageView) findViewById(R.id.btnForgot);
        tvRemember = (TextView) findViewById(R.id.tvRemember);

        //btnForgot Event Button
        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = etEmail.getText().toString().trim();

                if (Objects.equals(email, "")) {
                    Utils.toastyInfo(getApplicationContext(), getString(R.string.insert_email));
                } else {

                    dialog = new ProgressDialog(ForgotPasswordActivity.this);
                    dialog.setTitle(getString(R.string.loading));
                    dialog.setMessage(getString(R.string.loading_msg));
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                    //Function to recovery a new password
                    mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                dialog.dismiss();
                                Utils.toastySuccess(getApplicationContext(), getString(R.string.password_resend));
                                finish();
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
        });

        //tvRemeber Event Button
        tvRemember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}
