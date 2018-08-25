package br.sendlook.yeslap.model;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.thunder413.datetimeutils.DateTimeUnits;
import com.github.thunder413.datetimeutils.DateTimeUtils;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

import br.sendlook.yeslap.R;
import br.sendlook.yeslap.view.Favorites;
import br.sendlook.yeslap.view.Utils;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

public class FavoritesAdapter extends BaseAdapter {

    private Context context;
    private List<Favorites> favoritesList;

    public FavoritesAdapter(Context c, List<Favorites> l) {
        context = c;
        favoritesList = l;
    }

    @Override
    public int getCount() {
        return favoritesList.size();
    }

    @Override
    public Favorites getItem(int i) {
        return favoritesList.get(i);
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
            v = inflater.inflate(R.layout.list_favorites, null);
        } else {
            v = view;
        }

        Favorites favorites = getItem(i);

        final CircleImageView cvImageUser = (CircleImageView) v.findViewById(R.id.cvUserImageFav);
        final ImageView ivStatus = (ImageView) v.findViewById(R.id.ivStatus);
        //final ImageView ivGhost = (ImageView) v.findViewById(R.id.ivGhost);
        final TextView tvUsername = (TextView) v.findViewById(R.id.tvUsername);

        //TODO: FALTA FAZER A PARTE DAS IMAGENS DOS PERFIS

        tvUsername.setText(favorites.getUsername_user());

        int diff = DateTimeUtils.getDateDiff(getDateNow(), favorites.getLast_seen(), DateTimeUnits.DAYS);
        if (diff > 7) {
            ivStatus.setImageResource(R.drawable.icon_ghost);
        } else {
            if (Objects.equals(favorites.getStatus_user(), "online")) {
                ivStatus.setImageResource(R.drawable.on_user);
            } else {
                ivStatus.setImageResource(R.drawable.off_user);
            }
        }



        return v;
    }

    private String getDateNow() {
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int yyyy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        return yyyy + "-" + mm + "-" + dd;
    }

}
