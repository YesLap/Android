package br.sendlook.yeslap.model;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.thunder413.datetimeutils.DateTimeUnits;
import com.github.thunder413.datetimeutils.DateTimeUtils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;

import br.sendlook.yeslap.R;
import br.sendlook.yeslap.view.Favorites;
import br.sendlook.yeslap.view.Utils;
import de.hdodenhof.circleimageview.CircleImageView;

public class FavoritesAdapter extends ArrayAdapter<Favorites> {

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

            final CircleImageView cvImageUser = (CircleImageView) view.findViewById(R.id.cvUserImageFav);
            final ImageView ivStatus = (ImageView) view.findViewById(R.id.ivStatus);
            final ImageView ivGhost = (ImageView) view.findViewById(R.id.ivGhost);
            final TextView tvUsername = (TextView) view.findViewById(R.id.tvUsername);

            Favorites favorite = favorites.get(position);

            mDatabase = FirebaseDatabase.getInstance().getReference().child(Utils.USERS).child(favorite.getUid());
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String username = dataSnapshot.child(Utils.USERNAME).getValue(String.class);
                    String image = dataSnapshot.child(Utils.IMAGE_1).getValue(String.class);
                    final String status = dataSnapshot.child(Utils.STATUS).getValue(String.class);
                    String uid = dataSnapshot.child(Utils.UID).getValue(String.class);
                    String lastSeen = dataSnapshot.child(Utils.LAST_SEEN).getValue(String.class);

                    tvUsername.setText(username);

                    Picasso.with(context).load(image).placeholder(R.drawable.img_profile).into(cvImageUser);

                    int diff = DateTimeUtils.getDateDiff(getDateNow(), lastSeen, DateTimeUnits.DAYS);
                    if (diff > 7) {
                        ivStatus.setImageResource(R.drawable.icon_ghost);
                    } else {
                        if (Objects.equals(status, "online")) {
                            ivStatus.setImageResource(R.drawable.on_user);
                        } else {
                            ivStatus.setImageResource(R.drawable.off_user);
                        }
                    }


                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        return view;
    }

    private String getDateNow() {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int yyyy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        return yyyy + "-" + mm + "-" + dd;
    }

}
