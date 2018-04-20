package com.sendlook.yeslap;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sendlook.yeslap.model.Users;
import com.sendlook.yeslap.model.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import ru.whalemare.sheetmenu.SheetMenu;

public class FindUsersActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private RecyclerView rvUsers;
    private ImageView ivBgOffSun, ivBgOffMon, ivBgOffTue, ivBgOffWed, ivBgOffThu, ivBgOffFri, ivBgOffSat;
    private TextView tvSun, tvMon, tvTue, tvWed, tvThu, tvFri, tvSat;
    private ImageView ivMorning, ivAfternoon, ivNight;
    private ImageView btnGoToProfile, btnGoToSettings;
    private ArrayList<String> arrayUsers;
    private ArrayAdapter<String> adapter;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_users);

        //Instantiate Firebase and Iniciate the DatabaseReference
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("users_list").child(mAuth.getCurrentUser().getUid());

        arrayUsers = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayUsers);

        rvUsers = (RecyclerView) findViewById(R.id.rvUsers);
        rvUsers.setHasFixedSize(true);
        rvUsers.setLayoutManager(new LinearLayoutManager(this));

        ivBgOffSun = (ImageView) findViewById(R.id.ivBgOffSun);
        ivBgOffMon = (ImageView) findViewById(R.id.ivBgOffMon);
        ivBgOffTue = (ImageView) findViewById(R.id.ivBgOffTue);
        ivBgOffWed = (ImageView) findViewById(R.id.ivBgOffWed);
        ivBgOffThu = (ImageView) findViewById(R.id.ivBgOffThu);
        ivBgOffFri = (ImageView) findViewById(R.id.ivBgOffFri);
        ivBgOffSat = (ImageView) findViewById(R.id.ivBgOffSat);

        tvSun = (TextView) findViewById(R.id.tvSun);
        tvMon = (TextView) findViewById(R.id.tvMon);
        tvTue = (TextView) findViewById(R.id.tvTue);
        tvWed = (TextView) findViewById(R.id.tvWed);
        tvThu = (TextView) findViewById(R.id.tvThu);
        tvFri = (TextView) findViewById(R.id.tvFri);
        tvSat = (TextView) findViewById(R.id.tvSat);

        ivMorning = (ImageView) findViewById(R.id.ivMorning);
        ivAfternoon = (ImageView) findViewById(R.id.ivAfternoon);
        ivNight = (ImageView) findViewById(R.id.ivNight);

        btnGoToProfile = (ImageView) findViewById(R.id.btnGoToProfile);
        btnGoToSettings = (ImageView) findViewById(R.id.btnGoToSettings);

        btnGoToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FindUsersActivity.this, UserProfileActivity.class);
                startActivity(intent);
            }
        });

        btnGoToSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FindUsersActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });

        ivBgOffSun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivBgOffSun.setImageResource(R.drawable.iconbubbleweekon);
                tvSun.setTextColor(Color.WHITE);
            }
        });

        ivBgOffMon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivBgOffMon.setImageResource(R.drawable.iconbubbleweekon);
                tvMon.setTextColor(Color.WHITE);
            }
        });

        ivBgOffTue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivBgOffTue.setImageResource(R.drawable.iconbubbleweekon);
                tvTue.setTextColor(Color.WHITE);
            }
        });

        ivBgOffWed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivBgOffWed.setImageResource(R.drawable.iconbubbleweekon);
                tvWed.setTextColor(Color.WHITE);
            }
        });

        ivBgOffThu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivBgOffThu.setImageResource(R.drawable.iconbubbleweekon);
                tvThu.setTextColor(Color.WHITE);
            }
        });

        ivBgOffFri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivBgOffFri.setImageResource(R.drawable.iconbubbleweekon);
                tvFri.setTextColor(Color.WHITE);
            }
        });

        ivBgOffSat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivBgOffSat.setImageResource(R.drawable.iconbubbleweekon);
                tvSat.setTextColor(Color.WHITE);
            }
        });

        ivMorning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivMorning.setImageResource(R.drawable.iconmorningon);
            }
        });

        ivAfternoon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivAfternoon.setImageResource(R.drawable.iconafternoonon);
            }
        });

        ivNight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ivNight.setImageResource(R.drawable.iconnighton);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        loadUsers();
        dialog.dismiss();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mAuth != null) {
            setStatusOnline();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mAuth != null) {
            setStatusOffline();
        }
    }

    private void loadUsers() {

        dialog = new ProgressDialog(FindUsersActivity.this);
        dialog.setMessage(getString(R.string.loading));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        mDatabase = FirebaseDatabase.getInstance().getReference().child(Utils.USERS);

        FirebaseRecyclerAdapter<Users, FindUsersViewHolder> adapter = new FirebaseRecyclerAdapter<Users, FindUsersViewHolder>(Users.class, R.layout.list_users, FindUsersViewHolder.class, mDatabase) {
            @Override
            protected void populateViewHolder(final FindUsersViewHolder v, final Users m, int position) {
                //if (Objects.equals(m.getUid(), mAuth.getCurrentUser().getUid())){
                //arrayUsers.remove(position);
                //notifyDataSetChanged();
                //Utils.toastyInfo(getApplicationContext(), "m.getUid(): " + m.getUid() + "\n" + "Current User: " + mAuth.getCurrentUser().getUid() +  "ArraySize: " + arrayUsers.size());
                //} else {
                v.setName(m.getUsername());
                v.setImage(m.getImage1(), getApplicationContext());

                DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(Utils.USERS).child(m.getUid());
                database.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //v.setImage(dataSnapshot.child(Utils.IMAGE_1).getValue(String.class), getApplicationContext());
                        v.setStatus(dataSnapshot.child(Utils.STATUS).getValue(String.class));
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                //}

                v.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        SheetMenu.with(FindUsersActivity.this)
                                .setTitle(m.getUsername())
                                .setMenu(R.menu.menu_find)
                                .setClick(new MenuItem.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(MenuItem menuItem) {
                                        switch (menuItem.getItemId()) {
                                            case R.id.nav_menu_chat:
                                                Intent intent = new Intent(FindUsersActivity.this, ChatActivity.class);
                                                intent.putExtra("uid", (m.getUid()));
                                                startActivity(intent);
                                                break;
                                            case R.id.nav_menu_view_profile:
                                                Intent intentProfile = new Intent(FindUsersActivity.this, ProfileActivity.class);
                                                intentProfile.putExtra("uid", (m.getUid()));
                                                startActivity(intentProfile);
                                                break;
                                        }
                                        return false;
                                    }
                                }).show();

                    }
                });
            }
        };

        rvUsers.setAdapter(adapter);

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

    public static class FindUsersViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public FindUsersViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setUID(String uid, Context context) {
            Utils.toastyInfo(context, (uid));
        }

        public void setName(String name) {
            TextView tvUsername = (TextView) mView.findViewById(R.id.tvUsername);
            tvUsername.setText((name));
        }

        public void setImage(String image, Context context) {
            CircleImageView ivUser = (CircleImageView) mView.findViewById(R.id.cvImageUser);
            if (Objects.equals(image, "") || image == null) {
                ivUser.setImageResource(R.drawable.img_profile);
            } else {
                Picasso.with(context).load((image)).placeholder(R.drawable.img_profile).into(ivUser);
            }
        }

        public void setStatus(String status) {
            ImageView ivStatus = (ImageView) mView.findViewById(R.id.ivStatus);
            if (Objects.equals(status, "online")) {
                ivStatus.setImageResource(R.drawable.on_user);
            } else {
                ivStatus.setImageResource(R.drawable.off_user);
            }
        }


    }

}
