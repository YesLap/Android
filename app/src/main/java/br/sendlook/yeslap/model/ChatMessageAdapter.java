package br.sendlook.yeslap.model;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

import br.sendlook.yeslap.R;
import br.sendlook.yeslap.view.ChatMessage;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatMessageAdapter extends BaseAdapter {

    private Context context;
    private List<ChatMessage> chatMessageList;

    public ChatMessageAdapter(Context c, List<ChatMessage> l) {
        context = c;
        chatMessageList = l;
    }

    @Override
    public int getCount() {
        return chatMessageList.size();
    }

    @Override
    public ChatMessage getItem(int i) {
        return chatMessageList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = null;

        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            v = inflater.inflate(R.layout.list_users, null);
        } else {
            v = view;
        }

        ChatMessage chatMessage = getItem(i);

        TextView tvUsername = v.findViewById(R.id.tvUsername);
        final ImageView ivStatus = v.findViewById(R.id.ivStatus);
        final CircleImageView cvUserImage = v.findViewById(R.id.cvImageUser);

        tvUsername.setText(chatMessage.getUsername());

        if (Objects.equals(chatMessage.getStatus(), "online")) {
            ivStatus.setImageResource(R.drawable.on_user);
        } else {
            ivStatus.setImageResource(R.drawable.off_user);
        }

        return v;

    }
}
