package br.sendlook.yeslap.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import br.sendlook.yeslap.R;
import br.sendlook.yeslap.model.ChatMessageAdapter;
import br.sendlook.yeslap.view.ChatMessage;
import br.sendlook.yeslap.view.Utils;
import ru.whalemare.sheetmenu.SheetMenu;

public class ChatMessagesActivity extends AppCompatActivity {

    private ImageView btnGoToProfile, btnGoToSettings;
    private ListView lstChatMessages;
    private RelativeLayout btnChat, btnCalendar, btnSearch;
    private TextView tvHaveChat;
    private ProgressDialog dialog;
    private String id;
    private ChatMessageAdapter adapter;
    private List<ChatMessage> chatMessageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_messages);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            id = bundle.getString(Utils.ID_USER);
        }

        //Cast
        btnGoToProfile = (ImageView) findViewById(R.id.btnGoToProfile);
        btnGoToSettings = (ImageView) findViewById(R.id.btnGoToSettings);
        lstChatMessages = (ListView) findViewById(R.id.lstChatMessages);
        btnChat = (RelativeLayout) findViewById(R.id.btnChat);
        btnCalendar = (RelativeLayout) findViewById(R.id.btnCalendar);
        btnSearch = (RelativeLayout) findViewById(R.id.btnSearch);
        tvHaveChat = (TextView) findViewById(R.id.tvHaveChat);

        loadChatMessages();

        /* //Click item on listView
        lstChatMessages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ChatMessage chat = arrayChatMessages.get(i);
                Intent intent = new Intent(ChatMessagesActivity.this, ChatActivity.class);
                intent.putExtra("uid", chat.getUid());
                startActivity(intent);
            }
        });

        lstChatMessages.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final ChatMessage chat = arrayChatMessages.get(position);
                SheetMenu.with(ChatMessagesActivity.this)
                        .setTitle(arrayChatMessages.get(position).getName())
                        .setMenu(R.menu.menu_chat_messages)
                        .setClick(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.nav_menu_delete_chat:
                                        new MaterialDialog.Builder(ChatMessagesActivity.this)
                                                .title(arrayChatMessages.get(position).getName())
                                                .content(R.string.delete_chat_msg)
                                                .positiveText(R.string.delete)
                                                .negativeText(R.string.cancels)
                                                .onNegative(new MaterialDialog.SingleButtonCallback() {
                                                    @Override
                                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                        dialog.dismiss();
                                                    }
                                                })
                                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                                    @Override
                                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                                        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(Utils.CHAT).child(mAuth.getCurrentUser().getUid()).child(chat.getUid());
                                                        database.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    Utils.toastySuccess(getApplicationContext(), getString(R.string.chat_removed));
                                                                }
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Utils.toastyError(getApplicationContext(), e.getMessage());
                                                            }
                                                        });
                                                    }
                                                })
                                                .show();
                                        break;
                                }
                                return false;
                            }
                        }).show();
                return false;
            }
        });*/

        //btnCalendar Event Button
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatMessagesActivity.this, CalendarActivity.class);
                intent.putExtra(Utils.ID_USER, id);
                startActivity(intent);
            }
        });

        //btnSearch Event button
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //checkUsernameAndImage();
            }
        });

        //btnChat Event button
        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.toastyInfo(getApplicationContext(), getString(R.string.are_you_already_here));
            }
        });

        btnGoToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnGoToSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatMessagesActivity.this, SettingsActivity.class);
                intent.putExtra(Utils.ID_USER, id);
                startActivity(intent);
            }
        });

    }

    private void loadChatMessages() {
        dialog = new ProgressDialog(ChatMessagesActivity.this);
        dialog.setMessage(getString(R.string.loading));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        chatMessageList = new ArrayList<ChatMessage>();
        adapter = new ChatMessageAdapter(ChatMessagesActivity.this, chatMessageList);
        lstChatMessages.setAdapter(adapter);

        Ion.with(this)
                .load(Utils.URL_LOAD_CHAT_MESSAGES)
                .setBodyParameter(Utils.ID_USER_APP, id)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        try {
                            if (result.size() == 0) {
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                tvHaveChat.setVisibility(View.VISIBLE);
                            } else {
                                tvHaveChat.setVisibility(View.GONE);

                                for (int i = 0; i < result.size(); i++) {
                                    JsonObject j = result.get(i).getAsJsonObject();
                                    ChatMessage c = new ChatMessage();

                                    c.setUsername(j.get(Utils.USERNAME_USER).getAsString());
                                    c.setStatus(j.get(Utils.STATUS_USER).getAsString());
                                    c.setMessage(j.get(Utils.LAST_MESSAGE).getAsString());

                                    chatMessageList.add(c);
                                }
                                adapter.notifyDataSetChanged();
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
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


    @Override
    protected void onPause() {
        super.onPause();
        updateStatus(Utils.OFFLINE, id);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateStatus(Utils.ONLINE, id);
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
