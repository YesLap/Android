package br.sendlook.yeslap.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Objects;

import br.sendlook.yeslap.R;
import br.sendlook.yeslap.view.Utils;
import de.hdodenhof.circleimageview.CircleImageView;

public class ReportActivity extends AppCompatActivity {

    private CircleImageView cvImageUser;
    private RadioGroup rgOptions;
    private EditText etMsgReport;
    private Button btnSend;
    private TextView tvUsername;
    private String id, idReported, reason;
    private ProgressDialog dialog;
    private ImageView btnGoToProfile, btnGoToSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString(Utils.ID_USER);
            idReported = bundle.getString(Utils.ID_FAVORITE_USER_APP);
        }

        cvImageUser = (CircleImageView) findViewById(R.id.cvImageUser);
        rgOptions = (RadioGroup) findViewById(R.id.rgOptions);
        etMsgReport = (EditText) findViewById(R.id.etMsgReport);
        btnSend = (Button) findViewById(R.id.btnSend);
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        btnGoToProfile = (ImageView) findViewById(R.id.btnGoToProfile);
        btnGoToSettings = (ImageView) findViewById(R.id.btnGoToSettings);

        getUserData();

        btnGoToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnGoToSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ReportActivity.this, SettingsActivity.class);
                intent.putExtra(Utils.ID_USER, id);
                startActivity(intent);
            }
        });

        rgOptions.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton button = (RadioButton) radioGroup.findViewById(i);
                reason = button.getText().toString();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Objects.equals(reason, null)) {
                    Utils.toastyInfo(getApplicationContext(), getString(R.string.select_option_to_send_message));
                } else if (Objects.equals(etMsgReport.getText().toString().trim(), "")) {
                    Utils.toastyInfo(getApplicationContext(), getString(R.string.msg_report));
                } else {

                    dialog = new ProgressDialog(ReportActivity.this);
                    dialog.setMessage(getString(R.string.loading));
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                    Ion.with(getApplicationContext())
                            .load(Utils.URL_ADD_NEW_REPORT)
                            .setBodyParameter(Utils.ID_USER_APP, id)
                            .setBodyParameter(Utils.ID_REPORTED_APP, idReported)
                            .setBodyParameter(Utils.MSG_REPORT_APP, etMsgReport.getText().toString().trim())
                            .setBodyParameter(Utils.REASON_REPORT_APP, reason)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {
                                    try {
                                        String returnApp = result.get(Utils.REPORTS).getAsString();

                                        switch (returnApp) {
                                            case Utils.CODE_SUCCESS:
                                                if (dialog.isShowing()) {
                                                    dialog.dismiss();
                                                }
                                                Utils.toastySuccess(getApplicationContext(), getString(R.string.report_sended));
                                                finish();
                                                break;
                                            case Utils.CODE_ERROR:
                                                if (dialog.isShowing()) {
                                                    dialog.dismiss();
                                                }
                                                Utils.toastyError(getApplicationContext(), e.getMessage());
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
            }
        });

    }

    @Override
    protected void onResume() {
        updateStatus(id, Utils.ONLINE);
        super.onResume();
    }

    @Override
    protected void onPause() {
        updateStatus(id, Utils.OFFLINE);
        super.onPause();
    }

    private void getUserData() {
        dialog = new ProgressDialog(ReportActivity.this);
        dialog.setMessage(getString(R.string.loading));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        Ion.with(this)
                .load(Utils.URL_GET_USER_DATA)
                .setBodyParameter(Utils.ID_USER_APP, idReported)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        String returnApp = result.get(Utils.GET_USER_DATA).getAsString();

                        try {
                            switch (returnApp) {
                                case Utils.CODE_SUCCESS:
                                    if (dialog.isShowing()) {
                                        dialog.dismiss();
                                    }
                                    tvUsername.setText(result.get(Utils.USERNAME_USER).getAsString());
                                    if (result.get(Utils.IMAGE_USER_1).getAsString() != null || !Objects.equals(result.get(Utils.IMAGE_USER_1).getAsString(), " ")) {
                                        Picasso.with(ReportActivity.this).load(result.get(Utils.IMAGE_USER_1).getAsString()).placeholder(R.drawable.img_profile).into(cvImageUser);
                                    }
                                    break;
                                case Utils.CODE_ERROR:
                                    if (dialog.isShowing()) {
                                        dialog.dismiss();
                                    }
                                    Utils.toastyError(getApplicationContext(), e.getMessage());
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
