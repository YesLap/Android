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
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.TimeZone;

import br.sendlook.yeslap.R;
import br.sendlook.yeslap.view.Message;
import br.sendlook.yeslap.view.Utils;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;

public class MessagesAdapter extends BaseAdapter {

    private Context context;
    private List<Message> messageList;

    public MessagesAdapter(Context c, List<Message> l) {
        context = c;
        messageList = l;
    }

    @Override
    public int getCount() {
        return messageList.size();
    }

    @Override
    public Message getItem(int i) {
        return messageList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = null;

        Message message = getItem(i);

        SharedPreferences preferences = context.getSharedPreferences(Utils.PREF_NAME, MODE_PRIVATE);
        String id_sender = preferences.getString(Utils.ID_USER, "");

        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            if (Objects.equals(message.getIdSender(), id_sender)) {
                v = inflater.inflate(R.layout.message_right, null);
            } else {
                v = inflater.inflate(R.layout.message_left, null);
            }
        } else {
            v = view;
        }

        TextView tvMessage = v.findViewById(R.id.tvMessage);
        tvMessage.setText(message.getMessage());

        /*METHOD TO LOAD THE USER STATUS OF PHOTO SIDE
        final ImageView ivStatus = v.findViewById(R.id.ivStatus);
        Ion.with(context)
                .load(Utils.URL_LOAD_STATUS_USER)
                .setBodyParameter(Utils.ID_RECEIVER_APP, message.getIdReceiver())
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        String returnApp = result.get(Utils.STATUS_USER).getAsString();
                        switch (returnApp) {
                            case Utils.CODE_SUCCESS:
                                if (Objects.equals(result.get(Utils.STATUS).getAsString(), "online")) {
                                    ivStatus.setImageResource(R.drawable.on_user);
                                } else  if (Objects.equals(result.get(Utils.STATUS).getAsString(), "offline")) {
                                    ivStatus.setImageResource(R.drawable.off_user);
                                }
                                break;
                            case Utils.CODE_ERROR:
                                break;
                        }
                    }
                });*/

        //todo: Pegar a imagem dos usuarios
        final CircleImageView cvUserImage = v.findViewById(R.id.cvImageUser);

        /*METHOD TO DELETE MESSAGE
        int diff = DateTimeUtils.getDateDiff(message.getDate_message(), getDateNow(), DateTimeUnits.DAYS);
        if (diff >= 1) {
            Ion.with(context)
                    .load(Utils.URL_DELETE_MESSAGE)
                    .setBodyParameter(Utils.ID_MESSAGES, message.getIdMessage())
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            String returnApp = result.get(Utils.MESSAGES).getAsString();
                            switch (returnApp) {
                                case Utils.CODE_SUCCESS:
                                    break;
                                case Utils.CODE_ERROR:
                                    Utils.toastyError(context, e.getMessage());
                                    break;
                            }
                        }
                    });
        }*/

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