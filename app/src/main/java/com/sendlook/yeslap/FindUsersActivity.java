package com.sendlook.yeslap;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class FindUsersActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private RecyclerView rvUsers;
    private ImageView ivBgOffSun, ivBgOffMon, ivBgOffTue, ivBgOffWed, ivBgOffThu, ivBgOffFri, ivBgOffSat;
    private TextView tvSun, tvMon, tvTue, tvWed, tvThu, tvFri, tvSat;
    private ImageView ivMorning, ivAfternoon, ivNight;
    private ArrayList<String> arrayUsers;
    private ArrayAdapter<String> adapter;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_users);

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

    private void loadUsers() {

        dialog = new ProgressDialog(FindUsersActivity.this);
        dialog.setMessage(getString(R.string.loading));
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

        mDatabase = FirebaseDatabase.getInstance().getReference().child("users");
        mDatabase.keepSynced(true);

        FirebaseRecyclerAdapter<FindUsers, FindUsersViewHolder> adapter = new FirebaseRecyclerAdapter<FindUsers, FindUsersViewHolder>(FindUsers.class, R.layout.list_users, FindUsersViewHolder.class, mDatabase) {
            @Override
            protected void populateViewHolder(final FindUsersViewHolder v, final FindUsers m, int position) {
                v.setName(m.getUsername());
                v.setImage(m.getImage(), getApplicationContext());

                v.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        v.setUID(m.getUid(), getApplicationContext());
                    }
                });
            }
        };

        rvUsers.setAdapter(adapter);

    }

    public static class FindUsersViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public FindUsersViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setUID(String uid, Context context) {
            Utils.toastyInfo(context, Base64Custom.decodeBase64(uid));
        }

        public void setName(String name) {
            TextView tvUsername = (TextView) mView.findViewById(R.id.tvUsername);
            tvUsername.setText(Base64Custom.decodeBase64(name));
        }

        public void setImage(final String image, final Context context) {
            final ImageView ivUser = (CircleImageView) mView.findViewById(R.id.cvImageUser);
            Picasso.with(context).load(Base64Custom.decodeBase64(image)).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.img_profile).into(ivUser, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(context).load(Base64Custom.decodeBase64(image)).placeholder(R.drawable.img_profile).into(ivUser);
                }
            });
        }


    }

}
