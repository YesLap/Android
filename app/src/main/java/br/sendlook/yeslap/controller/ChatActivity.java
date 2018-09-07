package br.sendlook.yeslap.controller;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;
import java.util.TimeZone;

import br.sendlook.yeslap.R;
import br.sendlook.yeslap.model.MesageAdapter;
import br.sendlook.yeslap.view.Message;
import br.sendlook.yeslap.view.Utils;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etChat;
    private ListView lvChat;
    private TextView tvUsername, tvStatus, tvNoMessages;
    private String idReceiver, idSender, username;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference datadase;
    private ArrayList<Message> messages;
    private ArrayAdapter<Message> adapter;
    private ValueEventListener valueEventListenerMessages;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mAuth = FirebaseAuth.getInstance();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            idSender = bundle.getString(Utils.UID_SENDER);
            idReceiver = bundle.getString(Utils.UID_ADDRESSEE);
            username = bundle.getString(Utils.USERNAME);
        }

        findViewById(R.id.btnSendMessage).setOnClickListener(this);
        findViewById(R.id.btnGoToProfile).setOnClickListener(this);
        findViewById(R.id.btnGoToSettings).setOnClickListener(this);
        findViewById(R.id.tvUsername).setOnClickListener(this);
        etChat = (EditText) findViewById(R.id.etChat);
        lvChat = (ListView) findViewById(R.id.lvChat);
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvStatus = (TextView) findViewById(R.id.tvStatus);
        tvNoMessages = (TextView) findViewById(R.id.tvNoMessages);

        tvUsername.setText(username);

        setStatus();
        checkIfHaveMessages();

        //LOADING MESSAGES
        messages = new ArrayList<>();
        adapter = new MesageAdapter(ChatActivity.this, messages);
        lvChat.setAdapter(adapter);

        mDatabase = FirebaseDatabase.getInstance().getReference().child(Utils.MESSAGES).child(idSender).child(idReceiver);
        valueEventListenerMessages = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                checkIfHaveMessages();
                messages.clear();

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    Message message = data.getValue(Message.class);
                    messages.add(message);
                    lvChat.smoothScrollToPosition(messages.size());
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        mDatabase.addValueEventListener(valueEventListenerMessages);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnGoToProfile:
                finish();
                break;
            case R.id.btnGoToSettings:
                Intent intent = new Intent(ChatActivity.this, SettingsActivity.class);
                intent.putExtra(Utils.ID_USER, idSender);
                startActivity(intent);
                break;
            case R.id.btnSendMessage:
                final String message = etChat.getText().toString().trim();

                if (message.isEmpty()) {
                    Utils.toastyInfo(getApplicationContext(), getString(R.string.enter_a_messege_to_send));
                } else {
                    //SAVING MESSAGE ON FIREBASE
                    mDatabase = FirebaseDatabase.getInstance().getReference().child(Utils.MESSAGES).child(idSender).child(idReceiver).push();
                    String push = mDatabase.getKey();

                    Boolean returnsender = saveMessage(idSender, idReceiver, message, getDateTimeNow(), mDatabase);
                    if (!returnsender) {
                        Utils.toastyError(getApplicationContext(), getString(R.string.error_send_message));
                    } else {
                        mDatabase = FirebaseDatabase.getInstance().getReference().child(Utils.MESSAGES).child(idReceiver).child(idSender).child(push);
                        Boolean returnReceiver = saveMessage(idSender, idReceiver, message, getDateTimeNow(), mDatabase);
                        if (!returnReceiver) {
                            Utils.toastyError(getApplicationContext(), getString(R.string.error_send_message));
                        } else {
                            playSoundSentMessage();
                            etChat.setText("");

                            //SAVING CHAT ON MYSQL DATABASE
                            Ion.with(ChatActivity.this)
                                    .load(Utils.URL_SAVE_UPDATE_CHAT)
                                    .setBodyParameter(Utils.ID_SENDER_APP, idSender)
                                    .setBodyParameter(Utils.ID_RECEIVER_APP, idReceiver)
                                    .setBodyParameter(Utils.MESSAGE_APP, message)
                                    .asJsonObject()
                                    .setCallback(new FutureCallback<JsonObject>() {
                                        @Override
                                        public void onCompleted(Exception e, JsonObject result) {
                                            String returnApp = result.get(Utils.CHAT).getAsString();

                                            switch (returnApp) {
                                                case Utils.CODE_SUCCESS:

                                                    //SAVIND CHAT RECEIVER
                                                    Ion.with(ChatActivity.this)
                                                            .load(Utils.URL_SAVE_UPDATE_CHAT)
                                                            .setBodyParameter(Utils.ID_SENDER_APP, idReceiver)
                                                            .setBodyParameter(Utils.ID_RECEIVER_APP, idSender)
                                                            .setBodyParameter(Utils.MESSAGE_APP, message)
                                                            .asJsonObject()
                                                            .setCallback(new FutureCallback<JsonObject>() {
                                                                @Override
                                                                public void onCompleted(Exception e, JsonObject result) {
                                                                    String returnApp = result.get(Utils.CHAT).getAsString();

                                                                    switch (returnApp) {
                                                                        case Utils.CODE_SUCCESS:
                                                                            checkIfHaveMessages();
                                                                            break;
                                                                        case Utils.CODE_ERROR:
                                                                            checkIfHaveMessages();
                                                                            Utils.toastyError(getApplicationContext(), e.getMessage());
                                                                            break;
                                                                    }

                                                                }
                                                            });

                                                    break;
                                                case Utils.CODE_ERROR:
                                                    Utils.toastyError(getApplicationContext(), e.getMessage());
                                                    break;
                                            }

                                        }
                                    });

                        }
                    }
                    lvChat.smoothScrollToPosition(messages.size() - 1);

                }
                break;
            case R.id.tvUsername:
                Intent intentprofile = new Intent(ChatActivity.this, ProfileActivity.class);
                intentprofile.putExtra(Utils.ID_USER, idSender);
                intentprofile.putExtra(Utils.ID_FAVORITE_USER_APP, idReceiver);
                startActivity(intentprofile);
                break;
        }
    }

    private Boolean saveMessage(String idSender, String idReceiver, String message, String date, DatabaseReference db) {
        try {

            HashMap<String, String> msg = new HashMap<>();
            msg.put(Utils.ID_SENDER, idSender);
            msg.put(Utils.ID_RECEIVER, idReceiver);
            msg.put(Utils.MESSAGE, message);
            msg.put(Utils.DATE, date);
            msg.put(Utils.KEY, mDatabase.getKey());
            db.setValue(msg).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Utils.toastyError(getApplicationContext(), e.getMessage());
                }
            });

            return true;
        } catch (Exception e) {
            Utils.toastyError(getApplicationContext(), e.getMessage());
            return false;
        }
    }

    private void checkIfHaveMessages() {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(Utils.MESSAGES).child(idSender).child(idReceiver);
        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {
                    DatabaseReference database1 = FirebaseDatabase.getInstance().getReference().child(Utils.MESSAGES).child(idReceiver).child(idSender);
                    database1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot1) {
                            if (dataSnapshot1.getChildrenCount() == 0) {
                                tvNoMessages.setVisibility(View.VISIBLE);
                            } else {
                                tvNoMessages.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                } else {
                    tvNoMessages.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void playSoundSentMessage() {
        MediaPlayer whooap = MediaPlayer.create(this, R.raw.whooap);
        whooap.start();
    }

    private void playSoundSentMessageSent() {
        MediaPlayer whooap = MediaPlayer.create(this, R.raw.whooap_sent);
        whooap.start();
    }

    private void setStatus() {
        KeyboardVisibilityEvent.setEventListener(ChatActivity.this, new KeyboardVisibilityEventListener() {
            @Override
            public void onVisibilityChanged(boolean isOpen) {
                if (isOpen) {
                    updateStatusChat("Typing ...");
                } else {
                    updateStatusChat("");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStatus(idSender, Utils.ONLINE);
        checkIfHaveMessages();
    }

    @Override
    protected void onPause() {
        super.onPause();
        updateStatus(idSender, Utils.OFFLINE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mDatabase.removeEventListener(valueEventListenerMessages);
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

    private void updateStatusChat(String status) {
        Ion.with(ChatActivity.this)
                .load(Utils.URL_UPDATE_STATUS_CHAT)
                .setBodyParameter(Utils.ID_SENDER_APP, idSender)
                .setBodyParameter(Utils.ID_RECEIVER_APP, idReceiver)
                .setBodyParameter(Utils.STATUS_CHAT_APP, status)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        String returnApp = result.get(Utils.STATUS_CHAT).getAsString();
                        switch (returnApp) {
                            case Utils.CODE_ERROR:
                                Utils.toastyError(getApplicationContext(), e.getMessage());
                                break;
                        }
                    }
                });
    }

    private String getDateTimeNow() {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int yyyy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        return yyyy + "-" + mm + "-" + dd;
    }

}
