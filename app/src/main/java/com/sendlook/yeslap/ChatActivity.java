package com.sendlook.yeslap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sendlook.yeslap.model.Base64Custom;
import com.sendlook.yeslap.model.Utils;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private EditText etChat;
    private ImageView btnSendMessage;
    private ListView lvChat;
    private CircleImageView cvImage;
    private TextView tvUsername;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        etChat = (EditText) findViewById(R.id.etChat);
        btnSendMessage = (ImageView) findViewById(R.id.btnSendMessage);
        lvChat = (ListView) findViewById(R.id.lvChat);
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        cvImage = (CircleImageView)findViewById(R.id.cvImageUser);

        getBundleIntent();
        getUserData();


    }

    private void getUserData() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username = dataSnapshot.child(Utils.USERNAME).getValue(String.class);
                String image = dataSnapshot.child(Utils.IMAGE).getValue(String.class);
                
                tvUsername.setText(Base64Custom.decodeBase64(username));
                if (image != null && !Objects.equals(image, "")) {
                    Picasso.with(ChatActivity.this).load(Base64Custom.decodeBase64(image)).placeholder(R.drawable.img_profile).into(cvImage);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getBundleIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            uid = bundle.getString("uid");
        }
    }
}
