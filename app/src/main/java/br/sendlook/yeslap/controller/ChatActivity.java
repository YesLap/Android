package br.sendlook.yeslap.controller;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEvent;
import net.yslibrary.android.keyboardvisibilityevent.KeyboardVisibilityEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;

import br.sendlook.yeslap.R;
import br.sendlook.yeslap.model.MessagesAdapter;
import br.sendlook.yeslap.view.ChatMessage;
import br.sendlook.yeslap.model.MesageAdapter;
import br.sendlook.yeslap.view.Message;
import br.sendlook.yeslap.view.Utils;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText etChat;
    private ListView lvChat;
    private TextView tvUsername, tvStatus, tvNoMessages;
    private String idReceiver, idSender, username;
    private MessagesAdapter adapter;
    private List<Message> messageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

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
        loadMessages();

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
                    playSoundSentMessage();

                    //SAVING MESSAGE
                    Ion.with(getApplicationContext())
                            .load(Utils.URL_SEND_MESSAGE)
                            .setBodyParameter(Utils.ID_SENDER_APP, idSender)
                            .setBodyParameter(Utils.ID_RECEIVER_APP, idReceiver)
                            .setBodyParameter(Utils.MESSAGE_APP, message)
                            .asJsonObject()
                            .setCallback(new FutureCallback<JsonObject>() {
                                @Override
                                public void onCompleted(Exception e, JsonObject result) {
                                    try {
                                        String returnApp = result.get(Utils.MESSAGES).getAsString();

                                        switch (returnApp) {
                                            case Utils.CODE_SUCCESS:
                                                etChat.setText("");

                                                //SAVING CHAT SENDER
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
                                                                                                loadMessages();
                                                                                                break;
                                                                                            case Utils.CODE_ERROR:
                                                                                                checkIfHaveMessages();
                                                                                                loadMessages();
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

                                                break;
                                            case Utils.CODE_ERROR:
                                                Utils.toastyError(getApplicationContext(), getString(R.string.error_send_message));
                                                break;
                                        }

                                    } catch (Exception x) {
                                        Utils.toastyError(getApplicationContext(), x.getMessage());
                                    }
                                }
                            });


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

    private void loadMessages() {
        messageList = new ArrayList<Message>();
        adapter = new MessagesAdapter(ChatActivity.this, messageList);
        lvChat.setAdapter(adapter);

        Ion.with(this)
                .load(Utils.URL_LOAD_MESSAGES)
                .setBodyParameter(Utils.ID_SENDER_APP, idSender)
                .setBodyParameter(Utils.ID_RECEIVER_APP, idReceiver)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        try {
                            if (result.size() == 0) {
                                tvNoMessages.setVisibility(View.VISIBLE);
                            } else {
                                for (int i = 0; i < result.size(); i++) {
                                    JsonObject j = result.get(i).getAsJsonObject();
                                    Message m = new Message();

                                    m.setIdSender(j.get(Utils.ID_SENDER_APP).getAsString());
                                    m.setIdReceiver(j.get(Utils.ID_RECEIVER_APP).getAsString());
                                    m.setMessage(j.get(Utils.MESSAGE_APP).getAsString());
                                    m.setStatus(j.get(Utils.STATUS_USER).getAsString());
                                    m.setImage(j.get(Utils.IMAGE_USER_1).getAsString());

                                    messageList.add(m);
                                }
                                adapter.notifyDataSetChanged();
                                lvChat.smoothScrollToPosition(messageList.size() - 1);
                            }
                        } catch (Exception x) {
                            Utils.toastyError(getApplicationContext(), x.getMessage());
                        }
                    }
                });

    }

    private void checkIfHaveMessages() {

        Ion.with(ChatActivity.this)
                .load(Utils.URL_CHECK_IF_HAVE_MESSAGE)
                .setBodyParameter(Utils.ID_SENDER_APP, idSender)
                .setBodyParameter(Utils.ID_RECEIVER_APP, idReceiver)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        String returnApp = result.get(Utils.MESSAGES).getAsString();

                        switch (returnApp) {
                            case Utils.CODE_SUCCESS:
                                tvNoMessages.setVisibility(View.GONE);
                                break;
                            case Utils.CODE_ERROR:
                                tvNoMessages.setVisibility(View.VISIBLE);
                                break;
                        }

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

}
