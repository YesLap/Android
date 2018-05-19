package br.sendlook.yeslap.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import br.sendlook.yeslap.R;

public class DialogNewEmail extends AppCompatDialogFragment {

    private TextInputEditText etCurrentEmail, etCurrentPassword, etNewEmail;
    private DialogNewEmailListener listener;
    private FirebaseUser mUser;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_email_user, null);

        builder.setView(view)
                .setTitle(R.string.change_email)
                .setNegativeButton(getString(R.string.cancels), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
        .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String currentEmail = etCurrentEmail.getText().toString();
                String currentPassword = etCurrentPassword.getText().toString();
                String newEmail = etNewEmail.getText().toString();
                listener.changeEmail(currentEmail, currentPassword, newEmail);
            }
        });

        etCurrentEmail = view.findViewById(R.id.etCurrentEmail);
        etCurrentPassword = view.findViewById(R.id.etCurrentPassword);
        etNewEmail = view.findViewById(R.id.etNewEmail);

        etCurrentEmail.setText(mUser.getEmail());

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (DialogNewEmailListener) context;
        } catch (ClassCastException e) {
            throw  new ClassCastException(context.toString() + "must implement DialogNewEmailListener");
        }
    }

    public interface DialogNewEmailListener {
        void changeEmail(String currentEmail, String currentPassword, String newEmail);
    }

}
