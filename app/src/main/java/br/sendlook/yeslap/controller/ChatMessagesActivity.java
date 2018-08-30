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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import br.sendlook.yeslap.R;
import br.sendlook.yeslap.model.ChatMessageAdapter;
import br.sendlook.yeslap.view.ChatMessage;
import br.sendlook.yeslap.view.Utils;
import ru.whalemare.sheetmenu.SheetMenu;

public class ChatMessagesActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView btnGoToProfile, btnGoToSettings;
    private ListView lstChatMessages;
    private RelativeLayout btnChat, btnCalendar, btnSearch;
    private TextView tvHaveChat;
    private ProgressDialog dialogs;
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
        findViewById(R.id.btnGoToProfile).setOnClickListener(this);
        findViewById(R.id.btnGoToSettings).setOnClickListener(this);
        lstChatMessages = (ListView) findViewById(R.id.lstChatMessages);
        findViewById(R.id.btnChat).setOnClickListener(this);
        findViewById(R.id.btnCalendar).setOnClickListener(this);
        findViewById(R.id.btnSearch).setOnClickListener(this);
        tvHaveChat = (TextView) findViewById(R.id.tvHaveChat);

        loadChatMessages();

        lstChatMessages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ChatMessage chatMessage = (ChatMessage) adapterView.getAdapter().getItem(i);
                Intent intentChat = new Intent(ChatMessagesActivity.this, ChatActivity.class);
                intentChat.putExtra(Utils.UID_SENDER, chatMessage.getId_sender());
                intentChat.putExtra(Utils.UID_ADDRESSEE, chatMessage.getId_receiver());
                intentChat.putExtra(Utils.USERNAME, chatMessage.getUsername());
                startActivity(intentChat);
            }
        });

        lstChatMessages.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final ChatMessage chatMessage = (ChatMessage) parent.getAdapter().getItem(position);

                SheetMenu.with(ChatMessagesActivity.this)
                        .setTitle(chatMessage.getUsername())
                        .setMenu(R.menu.menu_chat_messages)
                        .setClick(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.nav_menu_delete_chat:
                                        new MaterialDialog.Builder(ChatMessagesActivity.this)
                                                .title(chatMessage.getUsername())
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
                                                    public void onClick(@NonNull final MaterialDialog dialog, @NonNull DialogAction which) {
                                                        dialogs = new ProgressDialog(ChatMessagesActivity.this);
                                                        dialogs.setMessage(getString(R.string.loading));
                                                        dialogs.setCanceledOnTouchOutside(false);
                                                        dialogs.show();

                                                        Ion.with(ChatMessagesActivity.this)
                                                                .load(Utils.URL_DELETE_CHAT_MESSAGE)
                                                                .setBodyParameter(Utils.ID_SENDER_APP, chatMessage.getId_sender())
                                                                .setBodyParameter(Utils.ID_RECEIVER_APP, chatMessage.getId_receiver())
                                                                .asJsonObject()
                                                                .setCallback(new FutureCallback<JsonObject>() {
                                                                    @Override
                                                                    public void onCompleted(Exception e, JsonObject result) {
                                                                        if (result == null) {
                                                                            if (dialogs.isShowing()) {
                                                                                dialogs.dismiss();
                                                                            }
                                                                            tvHaveChat.setVisibility(View.VISIBLE);
                                                                        } else {
                                                                            tvHaveChat.setVisibility(View.GONE);
                                                                            String returnApp = result.get(Utils.CHAT).getAsString();
                                                                            switch (returnApp) {
                                                                                case Utils.CODE_SUCCESS:
                                                                                    if (dialogs.isShowing()) {
                                                                                        dialogs.dismiss();
                                                                                    }
                                                                                    loadChatMessages();
                                                                                    Utils.toastySuccess(getApplicationContext(), getString(R.string.chat_removed));
                                                                                    break;
                                                                                case Utils.CODE_ERROR:
                                                                                    if (dialogs.isShowing()) {
                                                                                        dialogs.dismiss();
                                                                                    }
                                                                                    Utils.toastyError(getApplicationContext(), e.getMessage());
                                                                                    break;
                                                                            }
                                                                        }

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
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnGoToSettings:
                Intent intentsettings = new Intent(ChatMessagesActivity.this, SettingsActivity.class);
                intentsettings.putExtra(Utils.ID_USER, id);
                startActivity(intentsettings);
                break;
            case R.id.btnGoToProfile:
                finish();
                break;
            case R.id.btnChat:
                Utils.toastyInfo(getApplicationContext(), getString(R.string.are_you_already_here));
                break;
            case R.id.btnSearch:
                Ion.with(this)
                        .load(Utils.URL_GET_USER_DATA)
                        .setBodyParameter(Utils.ID_USER_APP, id)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                try {
                                    String returnApp = result.get(Utils.GET_USER_DATA).getAsString();

                                    if (Objects.equals(returnApp, Utils.CODE_SUCCESS)) {

                                        Intent intent = new Intent(ChatMessagesActivity.this, FindUsersActivity.class);
                                        intent.putExtra(Utils.ID_USER, id);
                                        intent.putExtra(Utils.GENDER_SEARCH, result.get(Utils.GENDER_SEARCH).getAsString());
                                        intent.putExtra(Utils.AGE_SEARCH_MIN, result.get(Utils.AGE_SEARCH_MIN).getAsString());
                                        intent.putExtra(Utils.AGE_SEARCH_MAX, result.get(Utils.AGE_SEARCH_MAX).getAsString());
                                        intent.putExtra(Utils.GENDER_USER, result.get(Utils.GENDER_USER).getAsString());
                                        intent.putExtra(Utils.AGE_USER, result.get(Utils.AGE_USER).getAsString());
                                        startActivity(intent);

                                    } else if (Objects.equals(returnApp, Utils.CODE_ERROR)) {
                                        Utils.toastyError(getApplicationContext(), e.getMessage());
                                    }
                                } catch (Exception x) {
                                    Utils.toastyError(getApplicationContext(), x.getMessage());
                                }
                            }
                        });
                break;
            case R.id.btnCalendar:
                Intent intentcalendar = new Intent(ChatMessagesActivity.this, CalendarActivity.class);
                intentcalendar.putExtra(Utils.ID_USER, id);
                startActivity(intentcalendar);
                break;
        }
    }

    private void loadChatMessages() {
        dialogs = new ProgressDialog(ChatMessagesActivity.this);
        dialogs.setMessage(getString(R.string.loading));
        dialogs.setCanceledOnTouchOutside(false);
        dialogs.show();

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
                                if (dialogs.isShowing()) {
                                    dialogs.dismiss();
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
                                    c.setId_sender(j.get(Utils.ID_SENDER).getAsString());
                                    c.setId_receiver(j.get(Utils.ID_RECEIVER).getAsString());
                                    c.setImage_user(j.get(Utils.IMAGE_USER_1).getAsString());
                                    c.setSun_m(j.get(Utils.SUNDAY_M).getAsString());
                                    c.setSun_a(j.get(Utils.SUNDAY_A).getAsString());
                                    c.setSun_n(j.get(Utils.SUNDAY_N).getAsString());
                                    c.setMon_m(j.get(Utils.MONDAY_M).getAsString());
                                    c.setMon_a(j.get(Utils.MONDAY_A).getAsString());
                                    c.setMon_n(j.get(Utils.MONDAY_N).getAsString());
                                    c.setTue_m(j.get(Utils.TUESDAY_M).getAsString());
                                    c.setTue_a(j.get(Utils.TUESDAY_A).getAsString());
                                    c.setTue_n(j.get(Utils.TUESDAY_N).getAsString());
                                    c.setWed_m(j.get(Utils.WEDNESDAY_M).getAsString());
                                    c.setWed_a(j.get(Utils.WEDNESDAY_A).getAsString());
                                    c.setWed_n(j.get(Utils.WEDNESDAY_N).getAsString());
                                    c.setThu_m(j.get(Utils.THURSDAY_M).getAsString());
                                    c.setThu_a(j.get(Utils.THURSDAY_A).getAsString());
                                    c.setThu_n(j.get(Utils.THURSDAY_N).getAsString());
                                    c.setFri_m(j.get(Utils.FRIDAY_M).getAsString());
                                    c.setFri_a(j.get(Utils.FRIDAY_A).getAsString());
                                    c.setFri_n(j.get(Utils.FRIDAY_N).getAsString());
                                    c.setSat_m(j.get(Utils.SATURDAY_M).getAsString());
                                    c.setSat_a(j.get(Utils.SATURDAY_A).getAsString());
                                    c.setSat_n(j.get(Utils.SATURDAY_N).getAsString());

                                    chatMessageList.add(c);
                                }
                                adapter.notifyDataSetChanged();
                                if (dialogs.isShowing()) {
                                    dialogs.dismiss();
                                }
                            }
                        } catch (Exception x) {
                            if (dialogs.isShowing()) {
                                dialogs.dismiss();
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
