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
import br.sendlook.yeslap.view.Users;
import br.sendlook.yeslap.view.Utils;

public class UsersAdapter extends BaseAdapter {

    private Context context;
    private List<Users> usersList;

    public UsersAdapter(Context c, List<Users> l) {
        context = c;
        usersList = l;
    }

    @Override
    public int getCount() {
        return usersList.size();
    }

    @Override
    public Users getItem(int position) {
        return usersList.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View v = null;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            v = inflater.inflate(R.layout.list_users, null);
        } else {
            v = convertView;
        }

        Users users = getItem(position);

        ImageView ivStatus = v.findViewById(R.id.ivStatus);
        TextView tvUsername = v.findViewById(R.id.tvUsername);
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

        tvUsername.setText(users.getUsername_user());

        if (Objects.equals(users.getStatus_user(), "online")) {
            ivStatus.setImageResource(R.drawable.on_user);
        } else {
            ivStatus.setImageResource(R.drawable.off_user);
        }

        //SUNDAY
        if (Objects.equals(users.getSun_m(), users.getId_user())) {
            ivMorningSun.setImageResource(R.drawable.iconmorningon);
        }
        if (Objects.equals(users.getSun_a(), users.getId_user())) {
            ivAfternoonSun.setImageResource(R.drawable.iconafternoonon);
        }
        if (Objects.equals(users.getSun_n(), users.getId_user())) {
            ivNightSun.setImageResource(R.drawable.iconnighton);
        }
        //MONDAY
        if (Objects.equals(users.getMon_m(), users.getId_user())) {
            ivMorningMon.setImageResource(R.drawable.iconmorningon);
        }
        if (Objects.equals(users.getMon_a(), users.getId_user())) {
            ivAfternoonMon.setImageResource(R.drawable.iconafternoonon);
        }
        if (Objects.equals(users.getMon_n(), users.getId_user())) {
            ivNightMon.setImageResource(R.drawable.iconnighton);
        }
        //TUESDAY
        if (Objects.equals(users.getTue_m(), users.getId_user())) {
            ivMorningTue.setImageResource(R.drawable.iconmorningon);
        }
        if (Objects.equals(users.getTue_a(), users.getId_user())) {
            ivAfternoonTue.setImageResource(R.drawable.iconafternoonon);
        }
        if (Objects.equals(users.getTue_n(), users.getId_user())) {
            ivNightTue.setImageResource(R.drawable.iconnighton);
        }
        //WEDNESDAY
        if (Objects.equals(users.getWed_m(), users.getId_user())) {
            ivMorningWed.setImageResource(R.drawable.iconmorningon);
        }
        if (Objects.equals(users.getWed_a(), users.getId_user())) {
            ivAfternoonWed.setImageResource(R.drawable.iconafternoonon);
        }
        if (Objects.equals(users.getWed_n(), users.getId_user())) {
            ivNightWed.setImageResource(R.drawable.iconnighton);
        }
        //THUESDAY
        if (Objects.equals(users.getThu_m(), users.getId_user())) {
            ivMorningThu.setImageResource(R.drawable.iconmorningon);
        }
        if (Objects.equals(users.getThu_a(), users.getId_user())) {
            ivAfternoonThu.setImageResource(R.drawable.iconafternoonon);
        }
        if (Objects.equals(users.getThu_n(), users.getId_user())) {
            ivNightThu.setImageResource(R.drawable.iconnighton);
        }
        //FRIDAY
        if (Objects.equals(users.getFri_m(), users.getId_user())) {
            ivMorningFri.setImageResource(R.drawable.iconmorningon);
        }
        if (Objects.equals(users.getFri_a(), users.getId_user())) {
            ivAfternoonFri.setImageResource(R.drawable.iconafternoonon);
        }
        if (Objects.equals(users.getFri_n(), users.getId_user())) {
            ivNightFri.setImageResource(R.drawable.iconnighton);
        }
        //SATURDAY
        if (Objects.equals(users.getSat_m(), users.getId_user())) {
            ivMorningSat.setImageResource(R.drawable.iconmorningon);
        }
        if (Objects.equals(users.getSat_a(), users.getId_user())) {
            ivAfternoonSat.setImageResource(R.drawable.iconafternoonon);
        }
        if (Objects.equals(users.getSat_n(), users.getId_user())) {
            ivNightSat.setImageResource(R.drawable.iconnighton);
        }

        return v;

    }
}
