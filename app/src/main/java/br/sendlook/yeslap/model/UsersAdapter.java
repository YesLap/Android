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

        tvUsername.setText(users.getUsername_user());

        if (Objects.equals(users.getStatus_user(), "online")) {
            ivStatus.setImageResource(R.drawable.on_user);
        } else {
            ivStatus.setImageResource(R.drawable.off_user);
        }

        return v;

    }
}
