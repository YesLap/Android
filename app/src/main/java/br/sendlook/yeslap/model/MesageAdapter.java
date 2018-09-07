package br.sendlook.yeslap.model;


import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
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
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;

import br.sendlook.yeslap.R;
import br.sendlook.yeslap.view.Message;
import br.sendlook.yeslap.view.Utils;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

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
        //Log.d("QWERTY", "Sizer Message:" + String.valueOf(message.size()));
        if (message != null) {
            final Message messages = message.get(position);

            SharedPreferences preferences = context.getSharedPreferences(Utils.PREF_NAME, MODE_PRIVATE);
            String id = preferences.getString(Utils.ID_USER, "");

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
            //Log.e("QWERTY", messages.getId_sender());
            //Log.e("QWERTY", id);
            if (id.equals(messages.getId_sender())) {
                view = inflater.inflate(R.layout.message_right, parent, false);
            } else {
                view = inflater.inflate(R.layout.message_left, parent, false);
            }

            TextView tvMessage = (TextView) view.findViewById(R.id.tvMessage);
            tvMessage.setText(messages.getMessage());

            final ImageView ivStatus = view.findViewById(R.id.ivStatus);
            final CircleImageView cvUserImage = view.findViewById(R.id.cvImageUser);
            try {
                mDatabase = FirebaseDatabase.getInstance().getReference().child(Utils.USERS).child(messages.getId_sender());
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        Ion.with(context)
                                .load(Utils.URL_GET_USER_DATA)
                                .setBodyParameter(Utils.ID_USER_APP, messages.getId_sender())
                                .asJsonObject()
                                .setCallback(new FutureCallback<JsonObject>() {
                                    @Override
                                    public void onCompleted(Exception e, JsonObject result) {

                                        String image = result.get(Utils.IMAGE_USER_1).getAsString();
                                        String status = result.get(Utils.STATUS_USER).getAsString();

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
                                });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            } catch (Exception e) {
                Utils.toastyError(context, e.getMessage());
            }

            //Check de time of message and delete
            final DatabaseReference database = FirebaseDatabase.getInstance().getReference().child(Utils.MESSAGES).child(messages.getId_sender()).child(messages.getId_receiver()).child(messages.getKey());
            final DatabaseReference database1 = FirebaseDatabase.getInstance().getReference().child(Utils.MESSAGES).child(messages.getId_receiver()).child(messages.getId_sender()).child(messages.getKey());
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
        return yyyy + "-" + mm + "-" + dd ;
    }

}


