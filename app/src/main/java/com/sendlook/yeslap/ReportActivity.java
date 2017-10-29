package com.sendlook.yeslap;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sendlook.yeslap.model.Utils;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ReportActivity extends AppCompatActivity {

    private CircleImageView cvImageUser;
    private RadioGroup rgOptions;
    private EditText etMsgReport;
    private Button btnSend;
    private TextView tvUsername;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private String uid;
    private String answer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        mAuth = FirebaseAuth.getInstance();

        cvImageUser = (CircleImageView) findViewById(R.id.cvImageUser);
        rgOptions = (RadioGroup) findViewById(R.id.rgOptions);
        etMsgReport = (EditText) findViewById(R.id.etMsgReport);
        btnSend = (Button) findViewById(R.id.btnSend);
        tvUsername = (TextView) findViewById(R.id.tvUsername);

        getIntentBundle();
        getUserData();

        rgOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton button = (RadioButton) radioGroup.findViewById(i);
                answer = button.getText().toString();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Objects.equals(answer, null)) {
                    Utils.toastyInfo(getApplicationContext(), "Please, select a option to send your report!");
                } else {
                    String message = etMsgReport.getText().toString().trim();
                    mDatabase = FirebaseDatabase.getInstance().getReference().child("reports").child(uid).push();
                    HashMap<String, String> report = new HashMap<>();
                    report.put("reason", answer);
                    report.put("message", message);
                    report.put("userWhoReported", mAuth.getCurrentUser().getUid());
                    mDatabase.setValue(report).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Utils.toastySuccess(getApplicationContext(), "Report Sended!");
                                finish();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Utils.toastyError(getApplicationContext(), e.getMessage());
                        }
                    });
                }
            }
        });

    }

    private void getIntentBundle() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            uid = bundle.getString("uid");
        }
    }

    private void getUserData() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Utils.USERS).child(uid);
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username = dataSnapshot.child("username").getValue(String.class);
                String image = dataSnapshot.child("image1").getValue(String.class);

                tvUsername.setText(username);
                if (image != null && !Objects.equals(image, "")) {
                    Picasso.with(ReportActivity.this).load(image).placeholder(R.drawable.img_profile).into(cvImageUser);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
