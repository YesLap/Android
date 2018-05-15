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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

import br.sendlook.yeslap.R;
import br.sendlook.yeslap.view.ChatMessage;
import br.sendlook.yeslap.view.Utils;
import de.hdodenhof.circleimageview.CircleImageView;

public class ChatMessageAdapter extends ArrayAdapter<ChatMessage> {

    private ArrayList<ChatMessage> chatMessages;
    private Context context;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    public ChatMessageAdapter(@NonNull Context c, @NonNull ArrayList<ChatMessage> objects) {
        super(c, 0, objects);
        this.chatMessages = objects;
        this.context = c;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = null;

        mAuth = FirebaseAuth.getInstance();

        //if (chatMessages != null || !Objects.equals(chatMessages.get(position).getMessage(), "")) {
        if (chatMessages != null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            view = inflater.inflate(R.layout.list_users, parent, false);
            TextView tvUsername = view.findViewById(R.id.tvUsername);
            final ImageView ivStatus = view.findViewById(R.id.ivStatus);
            final CircleImageView cvUserImage = view.findViewById(R.id.cvImageUser);

            ChatMessage chatMessage = chatMessages.get(position);
            tvUsername.setText(chatMessage.getName());
            try {
                mDatabase = FirebaseDatabase.getInstance().getReference().child(Utils.USERS).child(chatMessage.getUid());
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

        }

        return view;

    }
}
