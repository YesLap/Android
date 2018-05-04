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

            if (uid.equals(messages.getUidSender())) {
                view = inflater.inflate(R.layout.message_right, parent, false);
            } else {
                view = inflater.inflate(R.layout.message_left, parent, false);
            }

            TextView tvMessage = (TextView) view.findViewById(R.id.tvMessage);
            tvMessage.setText(messages.getMessage());

            final ImageView ivStatus = view.findViewById(R.id.ivStatus);
            final CircleImageView cvUserImage = view.findViewById(R.id.cvImageUser);
            try {
                mDatabase = FirebaseDatabase.getInstance().getReference().child(Utils.USERS).child(messages.getUidSender());
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

            //Check de time of message and delete
            final DatabaseReference database = FirebaseDatabase.getInstance().getReference().child("messages").child(messages.getUidSender()).child(messages.getUidAddressee()).child(messages.getKey());
            final DatabaseReference database1 = FirebaseDatabase.getInstance().getReference().child("messages").child(messages.getUidAddressee()).child(messages.getUidSender()).child(messages.getKey());
            database.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String date = dataSnapshot.child("date").getValue(String.class);
                    if (date != null) {
                        int diff = DateTimeUtils.getDateDiff(getDateTimeNow(), date, DateTimeUnits.DAYS);

                        if (diff >= 1) {
                            database.removeValue();
                            database1.removeValue();
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

    private String getDateTimeNow() {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int yyyy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        int hh = calendar.get(Calendar.HOUR);
        int min = calendar.get(Calendar.MINUTE);
        int seg  =calendar.get(Calendar.SECOND);
        return yyyy + "-" + mm + "-" + dd + " " + hh + ":" + min + ":" + seg ;
    }

}


