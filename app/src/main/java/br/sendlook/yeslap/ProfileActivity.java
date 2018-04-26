package br.sendlook.yeslap;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;
import java.util.TimeZone;

import br.sendlook.yeslap.model.Utils;
import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private CircleImageView cvImageUser;
    private ImageView btnFavorite, btnReport, btnChatMessage, ivImage1, ivImage2, ivImage3, btnGoToProfile, btnGoToSettings;
    private RelativeLayout btnChat, btnCalendar, btnFind;
    private TextView tvUsername;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();

        cvImageUser = (CircleImageView) findViewById(R.id.cvImageUser);
        btnFavorite = (ImageView) findViewById(R.id.ivFavorite);
        btnReport = (ImageView) findViewById(R.id.btnReport);
        btnChatMessage = (ImageView) findViewById(R.id.btnChatMessage);
        ivImage1 = (ImageView) findViewById(R.id.ivImage1);
        ivImage2 = (ImageView) findViewById(R.id.ivImage2);
        ivImage3 = (ImageView) findViewById(R.id.ivImage3);
        btnChat = (RelativeLayout) findViewById(R.id.btnChat);
        btnCalendar = (RelativeLayout) findViewById(R.id.btnCalendar);
        btnFind = (RelativeLayout) findViewById(R.id.btnSearch);
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        btnGoToProfile = (ImageView) findViewById(R.id.btnGoToProfile);
        btnGoToSettings = (ImageView) findViewById(R.id.btnGoToSettings);

        getIntentBundle();
        getUserData();

        btnFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDatabase = FirebaseDatabase.getInstance().getReference().child(Utils.FAVORITES).child(mAuth.getCurrentUser().getUid()).child(uid);
                HashMap<String, String> favorite = new HashMap<>();
                favorite.put(Utils.SINCE, getDateNow());
                favorite.put(Utils.UID, uid);
                mDatabase.setValue(favorite).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Utils.toastySuccess(getApplicationContext(), getString(R.string.addeedd_favorite));
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Utils.toastyError(getApplicationContext(), e.getMessage());
                    }
                });
            }
        });

        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, ReportActivity.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
            }
        });

        btnChatMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, ChatActivity.class);
                intent.putExtra("uid", uid);
                startActivity(intent);
            }
        });

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, ChatMessagesActivity.class);
                startActivity(intent);
            }
        });

        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, FindUsersActivity.class);
                startActivity(intent);
            }
        });

    }



    private String getDateNow() {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int yyyy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        return yyyy + "-" + mm + "-" + dd;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setStatusOnline();
    }

    @Override
    protected void onPause() {
        super.onPause();
        setStatusOffline();
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
                String image1 = dataSnapshot.child(Utils.IMAGE_1).getValue(String.class);
                String image2 = dataSnapshot.child(Utils.IMAGE_2).getValue(String.class);
                String image3 = dataSnapshot.child(Utils.IMAGE_3).getValue(String.class);
                String username = dataSnapshot.child(Utils.USERNAME).getValue(String.class);

                tvUsername.setText(username);
                if (image1 != null && !Objects.equals(image1, "")) {
                    Picasso.with(ProfileActivity.this).load(image1).placeholder(R.drawable.img_profile).into(cvImageUser);
                    Picasso.with(ProfileActivity.this).load(image1).placeholder(R.drawable.img_profile).into(ivImage1);
                }

                if (image2 != null && !Objects.equals(image2, "")) {
                    Picasso.with(ProfileActivity.this).load(image2).placeholder(R.drawable.img_profile).into(ivImage2);
                }

                if (image3 != null && !Objects.equals(image3, "")) {
                    Picasso.with(ProfileActivity.this).load(image3).placeholder(R.drawable.img_profile).into(ivImage3);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setStatusOnline() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Utils.USERS).child(mAuth.getCurrentUser().getUid());
        HashMap<String, Object> status = new HashMap<>();
        status.put("status", "online");
        mDatabase.updateChildren(status);
    }

    private void setStatusOffline() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Utils.USERS).child(mAuth.getCurrentUser().getUid());
        HashMap<String, Object> status = new HashMap<>();
        status.put("status", "offline");
        mDatabase.updateChildren(status);
    }

}
