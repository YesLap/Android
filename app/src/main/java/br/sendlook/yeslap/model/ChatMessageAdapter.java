package br.sendlook.yeslap.model;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
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
        ImageView ivStatus = v.findViewById(R.id.ivStatus);
        CircleImageView cvUserImage = v.findViewById(R.id.cvImageUser);
        ImageView ivMorningSun = v.findViewById(R.id.ivMorningSun);
        ImageView ivAfternoonSun = v.findViewById(R.id.ivAfternoonSun);
        ImageView ivNightSun = v.findViewById(R.id.ivNightSun);
        ImageView ivMorningMon = v.findViewById(R.id.ivMorningMon);
        ImageView ivAfternoonMon = v.findViewById(R.id.ivAfternoonMon);
        ImageView ivNightMon = v.findViewById(R.id.ivNightMon);
        ImageView ivMorningTue = v.findViewById(R.id.ivMorningTue);
        ImageView ivAfternoonTue = v.findViewById(R.id.ivAfternoonTue);
        ImageView ivNightTue = v.findViewById(R.id.ivNightTue);
        ImageView ivMorningWed = v.findViewById(R.id.ivMorningWed);
        ImageView ivAfternoonWed = v.findViewById(R.id.ivAfternoonWed);
        ImageView ivNightWed = v.findViewById(R.id.ivNightWed);
        ImageView ivMorningThu = v.findViewById(R.id.ivMorningThu);
        ImageView ivAfternoonThu = v.findViewById(R.id.ivAfternoonThu);
        ImageView ivNightThu = v.findViewById(R.id.ivNightThu);
        ImageView ivMorningFri = v.findViewById(R.id.ivMorningFri);
        ImageView ivAfternoonFri = v.findViewById(R.id.ivAfternoonFri);
        ImageView ivNightFri = v.findViewById(R.id.ivNightFri);
        ImageView ivMorningSat = v.findViewById(R.id.ivMorningSat);
        ImageView ivAfternoonSat = v.findViewById(R.id.ivAfternoonSat);
        ImageView ivNightSat = v.findViewById(R.id.ivNightSat);

        tvUsername.setText(chatMessage.getUsername());

        if (Objects.equals(chatMessage.getStatus(), "online")) {
            ivStatus.setImageResource(R.drawable.on_user);
        } else {
            ivStatus.setImageResource(R.drawable.off_user);
        }

        //SUNDAY
        if (Objects.equals(chatMessage.getSun_m(), chatMessage.getId_receiver())) {
            ivMorningSun.setImageResource(R.drawable.iconmorningon);
        }
        if (Objects.equals(chatMessage.getSun_a(), chatMessage.getId_receiver())) {
            ivAfternoonSun.setImageResource(R.drawable.iconafternoonon);
        }
        if (Objects.equals(chatMessage.getSun_n(), chatMessage.getId_receiver())) {
            ivNightSun.setImageResource(R.drawable.iconnighton);
        }
        //MONDAY
        if (Objects.equals(chatMessage.getMon_m(), chatMessage.getId_receiver())) {
            ivMorningMon.setImageResource(R.drawable.iconmorningon);
        }
        if (Objects.equals(chatMessage.getMon_a(), chatMessage.getId_receiver())) {
            ivAfternoonMon.setImageResource(R.drawable.iconafternoonon);
        }
        if (Objects.equals(chatMessage.getMon_n(), chatMessage.getId_receiver())) {
            ivNightMon.setImageResource(R.drawable.iconnighton);
        }
        //TUESDAY
        if (Objects.equals(chatMessage.getTue_m(), chatMessage.getId_receiver())) {
            ivMorningTue.setImageResource(R.drawable.iconmorningon);
        }
        if (Objects.equals(chatMessage.getTue_a(), chatMessage.getId_receiver())) {
            ivAfternoonTue.setImageResource(R.drawable.iconafternoonon);
        }
        if (Objects.equals(chatMessage.getTue_n(), chatMessage.getId_receiver())) {
            ivNightTue.setImageResource(R.drawable.iconnighton);
        }
        //WEDNESDAY
        if (Objects.equals(chatMessage.getWed_m(), chatMessage.getId_receiver())) {
            ivMorningWed.setImageResource(R.drawable.iconmorningon);
        }
        if (Objects.equals(chatMessage.getWed_a(), chatMessage.getId_receiver())) {
            ivAfternoonWed.setImageResource(R.drawable.iconafternoonon);
        }
        if (Objects.equals(chatMessage.getWed_n(), chatMessage.getId_receiver())) {
            ivNightWed.setImageResource(R.drawable.iconnighton);
        }
        //THUESDAY
        if (Objects.equals(chatMessage.getThu_m(), chatMessage.getId_receiver())) {
            ivMorningThu.setImageResource(R.drawable.iconmorningon);
        }
        if (Objects.equals(chatMessage.getThu_a(), chatMessage.getId_receiver())) {
            ivAfternoonThu.setImageResource(R.drawable.iconafternoonon);
        }
        if (Objects.equals(chatMessage.getThu_n(), chatMessage.getId_receiver())) {
            ivNightThu.setImageResource(R.drawable.iconnighton);
        }
        //FRIDAY
        if (Objects.equals(chatMessage.getFri_m(), chatMessage.getId_receiver())) {
            ivMorningFri.setImageResource(R.drawable.iconmorningon);
        }
        if (Objects.equals(chatMessage.getFri_a(), chatMessage.getId_receiver())) {
            ivAfternoonFri.setImageResource(R.drawable.iconafternoonon);
        }
        if (Objects.equals(chatMessage.getFri_n(), chatMessage.getId_receiver())) {
            ivNightFri.setImageResource(R.drawable.iconnighton);
        }
        //SATURDAY
        if (Objects.equals(chatMessage.getSat_m(), chatMessage.getId_receiver())) {
            ivMorningSat.setImageResource(R.drawable.iconmorningon);
        }
        if (Objects.equals(chatMessage.getSat_a(), chatMessage.getId_receiver())) {
            ivAfternoonSat.setImageResource(R.drawable.iconafternoonon);
        }
        if (Objects.equals(chatMessage.getSat_n(), chatMessage.getId_receiver())) {
            ivNightSat.setImageResource(R.drawable.iconnighton);
        }

        return v;

    }
}
