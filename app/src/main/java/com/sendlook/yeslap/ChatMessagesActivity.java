package com.sendlook.yeslap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sendlook.yeslap.model.ChatMessage;
import com.sendlook.yeslap.model.ChatMessageAdapter;
import com.sendlook.yeslap.model.Utils;

import java.util.ArrayList;
import java.util.Objects;

public class ChatMessagesActivity extends AppCompatActivity {

    private ImageView btnGoToProfile, btnGoToSettings;
    private ListView lstChatMessages;
    private RelativeLayout btnChat, btnCalendar, btnSearch;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private ValueEventListener valueEventListener;
    private ArrayAdapter<ChatMessage> adapter;
    private ArrayList<ChatMessage> arrayChatMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_messages);

        mAuth = FirebaseAuth.getInstance();

        btnGoToProfile = (ImageView) findViewById(R.id.btnGoToProfile);
        btnGoToSettings = (ImageView) findViewById(R.id.btnGoToSettings);
        lstChatMessages = (ListView) findViewById(R.id.lstChatMessages);
        btnChat = (RelativeLayout) findViewById(R.id.btnChat);
        btnCalendar = (RelativeLayout) findViewById(R.id.btnCalendar);
        btnSearch = (RelativeLayout) findViewById(R.id.btnSearch);

        arrayChatMessages = new ArrayList<>();
        adapter = new ChatMessageAdapter(getApplicationContext(), arrayChatMessages);
        lstChatMessages.setAdapter(adapter);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("chatmessages").child(mAuth.getCurrentUser().getUid());
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayChatMessages.clear();
                for (DataSnapshot chat: dataSnapshot.getChildren()) {
                    ChatMessage chatMessage = chat.getValue(ChatMessage.class);
                    arrayChatMessages.add(chatMessage);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        lstChatMessages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ChatMessage chat = arrayChatMessages.get(i);
                Intent intent = new Intent(ChatMessagesActivity.this, ChatActivity.class);
                intent.putExtra("uid", chat.getUid());
                startActivity(intent);
            }
        });

        //btnCalendar Event Button
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatMessagesActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUsernameAndImage();
            }
        });

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.toastyInfo(getApplicationContext(), "Are you already here!");
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        mDatabase.removeEventListener(valueEventListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mDatabase.addValueEventListener(valueEventListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDatabase.addValueEventListener(valueEventListener);
    }

    private void checkUsernameAndImage() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Utils.USERS).child(mAuth.getCurrentUser().getUid());
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username = dataSnapshot.child("username").getValue(String.class);
                String image = dataSnapshot.child("image1").getValue(String.class);

                if (Objects.equals(username, "Username") || Objects.equals(image, "")) {
                    Utils.toastyInfo(getApplicationContext(), "Please, change your username and add a profile photo to look for someone!");
                } else {
                    Intent intent = new Intent(ChatMessagesActivity.this, FindUsersActivity.class);
                    startActivity(intent);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}