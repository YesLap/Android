package com.sendlook.yeslap.model;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sendlook.yeslap.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MesageAdapter extends ArrayAdapter<Message>{

    private Context context;
    private ArrayList<Message> message;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;


    public MesageAdapter(@NonNull Context c, @NonNull ArrayList<Message> objects) {
        super(c, 0, objects);
        this.context = c;
        this.message = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;
        mAuth = FirebaseAuth.getInstance();

        if (message != null) {
            String uid = mAuth.getCurrentUser().getUid();

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            Message messages = message.get(position);

            if (uid.equals(messages.getUid())) {
                view = inflater.inflate(R.layout.message_right, parent, false);
            } else {
                view = inflater.inflate(R.layout.message_left, parent, false);
            }

            TextView tvMessage = (TextView) view.findViewById(R.id.tvMessage);
            tvMessage.setText(messages.getMessage());

            final ImageView ivStatus = view.findViewById(R.id.ivStatus);
            final CircleImageView cvUserImage = view.findViewById(R.id.cvImageUser);
            try {
                mDatabase = FirebaseDatabase.getInstance().getReference().child(Utils.USERS).child(messages.getUid());
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String status = dataSnapshot.child(Utils.STATUS).getValue(String.class);
                        String image = dataSnapshot.child(Utils.IMAGE_1).getValue(String.class);
                        if (Objects.equals(status, "online")) {
                            ivStatus.setImageResource(R.drawable.on_user);
                        } else {
                            ivStatus.setImageResource(R.drawable.off_user);
                        }

                        if (Objects.equals(image, "") || image == null) {
                            cvUserImage.setImageResource(R.drawable.img_profile);
                        } else {
                            Picasso.with(context).load(image).placeholder(R.drawable.img_profile).into(cvUserImage);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            } catch (Exception e) {
                Utils.toastyError(context, e.getMessage());
            }

            /**
             final CircleImageView cvUserImage = view.findViewById(R.id.cvImageUser);
             mDatabase = FirebaseDatabase.getInstance().getReference().child(Utils.USERS).child(messages.getUid());
             mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
            String image = dataSnapshot.child(Utils.IMAGE_1).getValue(String.class);
            if (image != null | image != "") {
            Picasso.with(context).load(image).placeholder(R.drawable.img_profile).into(cvUserImage);
            }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
            });*/

        }

        return view;
    }
}


