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

import br.sendlook.yeslap.R;

public class DialogNewPassword extends AppCompatDialogFragment {

    private TextInputEditText etCurrentPassword, etNewPassword;
    private DialogNewPasswordListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_password_user, null);

        builder.setView(view)
                .setTitle(R.string.change_password)
                .setNegativeButton(getString(R.string.cancels), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
        .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String currentPassword = etCurrentPassword.getText().toString();
                String newPassword = etNewPassword.getText().toString();
                listener.changePassword(currentPassword, newPassword);
            }
        });

        etCurrentPassword = view.findViewById(R.id.etCurrentPassword);
        etNewPassword = view.findViewById(R.id.etNewPassword);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (DialogNewPasswordListener) context;
        } catch (ClassCastException e) {
            throw  new ClassCastException(context.toString() + "must implement DialogNewPasswordListener");
        }
    }

    public interface DialogNewPasswordListener {
        void changePassword(String currentPassword, String newPassowrd);
    }

}
