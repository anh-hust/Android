package com.example.midtermfinal.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.midtermfinal.R;


public class ChangePasswordDialog extends AppCompatDialogFragment {
    private EditText oldPasswordEdit;
    private EditText newPasswordEdit;

    private final String oldPassword;
    private final ChangePasswordDialogListener dialogListener;

    /**
     * TODO Very carefully, in GroupInfoFragment you implements > 2 interface, what you need is
     * 1. Constructor && Listener at final
     * 2. in call new AddMemberDialog(this::addMemberClicked)
     */
    public ChangePasswordDialog(ChangePasswordDialogListener changePasswordDialogListener, String oldPassword) {
        this.oldPassword = oldPassword;
        this.dialogListener = changePasswordDialogListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_change_pwd, null);

        oldPasswordEdit = view.findViewById(R.id.oldPwdEdit);
        newPasswordEdit = view.findViewById(R.id.newPwdEdit);

        builder.setView(view)
                .setTitle("CHANGE GROUP's PASSWORD")
                .setPositiveButton("Confirm", (dialog, which) -> {
                    String oldPwd = oldPasswordEdit.getText().toString();
                    String newPassword = newPasswordEdit.getText().toString();

                    if (!oldPwd.equals(this.oldPassword))
                        Toast.makeText(getContext(), "Verify fail",
                                Toast.LENGTH_SHORT).show();
                    else if (newPassword.isEmpty())
                        Toast.makeText(getContext(), "New password cannot be null",
                                Toast.LENGTH_SHORT).show();
                    else {
                        dialogListener.changePwdClicked(newPassword);
                    }
                })
                .setNeutralButton("Cancel", (dialog, which) -> dialog.dismiss());

        return builder.create();
    }

    public interface ChangePasswordDialogListener {
        void changePwdClicked(String newPassword);
    }
}
