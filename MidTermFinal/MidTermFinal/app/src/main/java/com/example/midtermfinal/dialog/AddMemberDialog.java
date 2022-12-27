package com.example.midtermfinal.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.midtermfinal.R;


public class AddMemberDialog extends AppCompatDialogFragment {
    private EditText nameEdit;
    private EditText idEdit;
    private NumberPicker absentPicker;
    private CheckBox isLeader;

    private final AddMemberDialogListener dialogListener;

    /**
     * TODO Very carefully, in GroupInfoFragment you implements > 2 interface, what you need is
     * 1. Constructor && Listener at final
     * 2. in call new AddMemberDialog(this::addMemberClicked)
     */
    public AddMemberDialog(AddMemberDialogListener dialogListener) {
        this.dialogListener = dialogListener;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_member, null);

        nameEdit = view.findViewById(R.id.nameEdit);
        idEdit = view.findViewById(R.id.idEdit);
        absentPicker = view.findViewById(R.id.absentPicker);
        isLeader = view.findViewById(R.id.isLeader);

        absentPicker.setMinValue(0);
        absentPicker.setMaxValue(20);


        builder.setView(view)
                .setTitle("ADD NEW MEMBER")
                .setNeutralButton("Cancel", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("Add", (dialog, which) -> {
                    String newMemberName = nameEdit.getText().toString();
                    String newMemberID = idEdit.getText().toString();
                    boolean newMemberRole = isLeader.isChecked();
                    long newMemberAbsent = (long) absentPicker.getValue();

                    if (newMemberName.isEmpty() || newMemberID.isEmpty()) {
                        Toast.makeText(getContext(), "Member Name and ID cannot be null",
                                Toast.LENGTH_LONG).show();
                    } else {
//                        Log.d("AddMemberDialog class", "Add CLick Event - Data:\n"
//                                + newMemberName + newMemberID + newMemberRole + newMemberAbsent);
                        dialogListener.addMemberClicked(newMemberName, newMemberID, newMemberRole, newMemberAbsent);
                    }
                });

        return builder.create();
    }

    public interface AddMemberDialogListener {
        void addMemberClicked(String newMemberName, String newMemberID, boolean newMemberRole, long newMemberAbsent);
    }

}
