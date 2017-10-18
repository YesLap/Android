package com.sendlook.yeslap.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sendlook.yeslap.R;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatMessageAdapter extends ArrayAdapter<ChatMessage> {

    private ArrayList<ChatMessage> chatMessages;
    private Context context;

    public ChatMessageAdapter(@NonNull Context c, @NonNull ArrayList<ChatMessage> objects) {
        super(c, 0, objects);
        this.chatMessages = objects;
        this.context = c;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;

        if (chatMessages != null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.list_users, parent, false);
            TextView tvUsername = view.findViewById(R.id.tvUsername);
            CircleImageView cvUserImage = view.findViewById(R.id.cvImageUser);

            ChatMessage chatMessage = chatMessages.get(position);
            tvUsername.setText(chatMessage.getName());
            //Recuperar a Foto

        }

        return view;

    }
}
