package br.sendlook.yeslap;

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

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import br.sendlook.yeslap.model.ChatMessage;
import br.sendlook.yeslap.model.ChatMessageAdapter;
import br.sendlook.yeslap.model.Utils;
import ru.whalemare.sheetmenu.SheetMenu;

public class ChatMessagesActivity extends AppCompatActivity {

    private ImageView btnGoToProfile, btnGoToSettings;
    private ListView lstChatMessages;
    private RelativeLayout btnChat, btnCalendar, btnSearch;
    private TextView tvHaveChat;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference database;
    private ValueEventListener valueEventListener;
    private ArrayAdapter<ChatMessage> adapter;
    private ArrayList<ChatMessage> arrayChatMessages;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_messages);

        //TODO: METODO PARA VERIFICAR SE HA MENSAGENS SENAO COLOCAR MENSGEM QUE NAO HÁ MENSAGEM

        //Instatiate Firebase
        mAuth = FirebaseAuth.getInstance();

        checkIfHaveChats();

        //Cast
        btnGoToProfile = (ImageView) findViewById(R.id.btnGoToProfile);
        btnGoToSettings = (ImageView) findViewById(R.id.btnGoToSettings);
        lstChatMessages = (ListView) findViewById(R.id.lstChatMessages);
        btnChat = (RelativeLayout) findViewById(R.id.btnChat);
        btnCalendar = (RelativeLayout) findViewById(R.id.btnCalendar);
        btnSearch = (RelativeLayout) findViewById(R.id.btnSearch);
        tvHaveChat = (TextView) findViewById(R.id.tvHaveChat);

        //Array of chat messages
        arrayChatMessages = new ArrayList<>();
        //Custom adapter
        adapter = new ChatMessageAdapter(getApplicationContext(), arrayChatMessages);
        //adapter of list of chat message
        lstChatMessages.setAdapter(adapter);

        //Get the Chat Messages from firebase
        mDatabase = FirebaseDatabase.getInstance().getReference().child(Utils.CHAT_MESSAGES).child(mAuth.getCurrentUser().getUid());
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                arrayChatMessages.clear();
                for (DataSnapshot chat : dataSnapshot.getChildren()) {
                    try {
                        ChatMessage chatMessage = chat.getValue(ChatMessage.class);
                        arrayChatMessages.add(chatMessage);
                    } catch (Exception e) {
                        Log.d("TAGS", e.getMessage());
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };

        //Click item on listView
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
                                                        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(Utils.CHAT_MESSAGES).child(mAuth.getCurrentUser().getUid()).child(chat.getUid());
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
        });

        //btnCalendar Event Button
        btnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatMessagesActivity.this, CalendarActivity.class);
                startActivity(intent);
            }
        });

        //btnSearch Event button
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUsernameAndImage();
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
                startActivity(intent);
            }
        });

    }

    private void checkIfHaveChats() {
        DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(Utils.CHAT_MESSAGES).child(mAuth.getCurrentUser().getUid());;
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() == 0) {
                    tvHaveChat.setVisibility(View.VISIBLE);
                } else {
                    tvHaveChat.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Stop the EventListener
        //mDatabase.removeEventListener(valueEventListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mDatabase.removeEventListener(valueEventListener);
        setStatusOffline();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Iniciate the EventListener
        //mDatabase.addValueEventListener(valueEventListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mDatabase.addValueEventListener(valueEventListener);
        setStatusOnline();
    }

    //Check if the username and image profile isn't null
    private void checkUsernameAndImage() {
        dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.loading));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        mDatabase = FirebaseDatabase.getInstance().getReference().child(Utils.USERS).child(mAuth.getCurrentUser().getUid());
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String username = dataSnapshot.child("username").getValue(String.class);
                String image = dataSnapshot.child("image1").getValue(String.class);

                if (Objects.equals(username, "Username") || Objects.equals(image, "")) {
                    dialog.dismiss();
                    Utils.toastyInfo(getApplicationContext(), getString(R.string.please_change_image_username));
                } else {
                    dialog.dismiss();
                    Intent intent = new Intent(ChatMessagesActivity.this, FindUsersActivity.class);
                    startActivity(intent);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setStatusOnline() {
        database = FirebaseDatabase.getInstance().getReference().child(Utils.USERS).child(mAuth.getCurrentUser().getUid());
        HashMap<String, Object> status = new HashMap<>();
        status.put("status", "online");
        database.updateChildren(status);
    }

    private void setStatusOffline() {
        database = FirebaseDatabase.getInstance().getReference().child(Utils.USERS).child(mAuth.getCurrentUser().getUid());
        HashMap<String, Object> status = new HashMap<>();
        status.put("status", "offline");
        database.updateChildren(status);
    }

}
