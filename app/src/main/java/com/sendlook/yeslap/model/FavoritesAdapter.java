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

public class FavoritesAdapter extends ArrayAdapter<Favorites>{

    private Context context;
    private ArrayList<Favorites> favorites;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public FavoritesAdapter(@NonNull Context c, @NonNull ArrayList<Favorites> objects) {
        super(c, 0, objects);
        this.context = c;
        this.favorites = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;
        mAuth = FirebaseAuth.getInstance();

        if (favorites != null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_favorites, parent, false);

            Favorites favorite =  favorites.get(position);

            String uid = favorite.getUid();
            Utils.toastyError(context, uid);

            final CircleImageView cvImageUser = (CircleImageView) view.findViewById(R.id.cvImageUser);
            final ImageView ivStatus = (ImageView) view.findViewById(R.id.ivStatus);
            final TextView tvUsername = (TextView) view.findViewById(R.id.tvUsername);

            mDatabase = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String username = dataSnapshot.child("username").getValue(String.class);
                    String image = dataSnapshot.child("image1").getValue(String.class);
                    String status = dataSnapshot.child("status").getValue(String.class);

                    tvUsername.setText(username);

                    if (Objects.equals(status, "online")) {
                        ivStatus.setImageResource(R.drawable.on_user);
                    } else {
                        ivStatus.setImageResource(R.drawable.off_user);
                    }

                    if (Objects.equals(image, "") || image == null) {
                        cvImageUser.setImageResource(R.drawable.img_profile);
                    } else {
                        Picasso.with(context).load(image).placeholder(R.drawable.img_profile).into(cvImageUser);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        return view;
    }
}
