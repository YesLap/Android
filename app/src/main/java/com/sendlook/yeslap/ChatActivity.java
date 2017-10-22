package com.sendlook.yeslap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
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
import com.sendlook.yeslap.model.ChatMessage;
import com.sendlook.yeslap.model.MesageAdapter;
import com.sendlook.yeslap.model.Message;
import com.sendlook.yeslap.model.Utils;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private EditText etChat;
    private ImageView btnSendMessage;
    private ListView lvChat;
    private TextView tvUsername;
    private String uidAddressee;
    private String uidSender;
    private String usernameSender;
    private ArrayList<Message> messages;
    private ArrayAdapter<Message> adapter;
    private ValueEventListener valueEventListenerMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mAuth = FirebaseAuth.getInstance();

        etChat = (EditText) findViewById(R.id.etChat);
        btnSendMessage = (ImageView) findViewById(R.id.btnSendMessage);
        lvChat = (ListView) findViewById(R.id.lvChat);
        tvUsername = (TextView) findViewById(R.id.tvUsername);

        getBundleIntent();
        getUserData();

        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = etChat.getText().toString().trim();

                if (message.isEmpty()) {
                    Utils.toastyInfo(getApplicationContext(), "Enter a message to send");
                } else {

                    Message msg = new Message();
                    msg.setUid(uidSender);
                    msg.setMessage(message);

                    //salva para o remetente
                    Boolean returnSender = saveMessage(uidSender, uidAddressee, msg);
                    if (!returnSender) {
                        Utils.toastyError(getApplicationContext(), "Error sending message");
                    } else {
                        //salva para o destinatario
                        Boolean returnAddressee = saveMessage(uidAddressee, uidSender, msg);
                        if (!returnAddressee) {
                            Utils.toastyError(getApplicationContext(), "Error sending message");
                        } else {
                            etChat.setText("");
                        }
                    }

                    //Salva a Conversa para o Remetente
                    ChatMessage chatSender = new ChatMessage();
                    chatSender.setUid(uidAddressee);
                    chatSender.setName(tvUsername.getText().toString());
                    chatSender.setMessage(message);
                    Boolean returnSaveChatSender = saveChat(uidSender, uidAddressee, chatSender);
                    if (!returnSaveChatSender) {
                        Utils.toastyError(getApplicationContext(), "Error saving conversation");
                    } else {
                        //Salva a Conversa para o Remetente
                        ChatMessage chatAddressee = new ChatMessage();
                        chatAddressee.setUid(uidSender);
                        chatAddressee.setName(usernameSender);
                        chatAddressee.setMessage(message);
                        Boolean returnSaveChatAdressee = saveChat(uidAddressee, uidSender, chatAddressee);
                        if (!returnSaveChatAdressee) {
                            Utils.toastyError(getApplicationContext(), "Error saving conversation");
                        }
                    }




                }

            }
        });

        messages = new ArrayList<>();
        adapter = new MesageAdapter(ChatActivity.this, messages);
        lvChat.setAdapter(adapter);

        mDatabase = FirebaseDatabase.getInstance().getReference().child("messages").child(uidSender).child(uidAddressee);

        valueEventListenerMessages = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messages.clear();

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Message message = data.getValue(Message.class);
                    messages.add(message);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mDatabase.addValueEventListener(valueEventListenerMessages);

    }

    private boolean saveMessage(String uidSender, String uidAddressee, Message message) {
        try {

            mDatabase = FirebaseDatabase.getInstance().getReference().child("messages");
            mDatabase.child(uidSender).child(uidAddressee).push().setValue(message);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean saveChat(String uidSender, String uidAddressee, ChatMessage chatMessage) {
        try {

            mDatabase = FirebaseDatabase.getInstance().getReference().child("chatmessages");
            mDatabase.child(uidSender).child(uidAddressee).setValue(chatMessage);

            return true;
        } catch (Exception e) {
            Utils.toastyError(getApplicationContext(), e.getMessage());
            return false;
        }
    }

    private void getUserData() {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(uidAddressee);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username = dataSnapshot.child(Utils.USERNAME).getValue(String.class);

                tvUsername.setText(username);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void getBundleIntent() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            uidAddressee = bundle.getString("uid");
            uidSender = mAuth.getCurrentUser().getUid();
        }
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(uidSender);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                usernameSender = dataSnapshot.child("username").getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDatabase.removeEventListener(valueEventListenerMessages);
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
