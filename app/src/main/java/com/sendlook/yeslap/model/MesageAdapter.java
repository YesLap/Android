package com.sendlook.yeslap.model;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.sendlook.yeslap.R;

import java.util.ArrayList;
import java.util.List;

public class MesageAdapter extends ArrayAdapter<Message>{

    private Context context;
    private ArrayList<Message> message;
    private FirebaseAuth mAuth;


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

        }

        return view;
    }
}


