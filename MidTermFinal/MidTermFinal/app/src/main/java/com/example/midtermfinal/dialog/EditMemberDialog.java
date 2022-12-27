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

public class EditMemberDialog extends AppCompatDialogFragment {
    private EditText nameEdit;
    private EditText idEdit;
    private NumberPicker absentPicker;
    private CheckBox isLeader;

    private final String currentMemberName;
    private final String currentMemberID;
    private final Boolean currentMemberRole;
    private final Long currentMemberAbsent;
    private final int memberPosition;

    public static final boolean DELETE_ACTION = false;
    public static final boolean EDIT_ACTION = true;

    private final EditMemberDialogListener dialogListener;

    /**
     * TODO Very carefully, in GroupInfoFragment you implements > 2 interface, what you need is
     * 1. Constructor && Listener at final
     * 2. in call new AddMemberDialog(this::editMemberClicked)
     */
    public EditMemberDialog(EditMemberDialogListener editMemberDialogListener,
                            int memberPosition,
                            String currentMemberName,
                            String currentMemberID,
                            boolean currentMemberRole,
                            long currentMemberAbsent) {
        this.memberPosition = memberPosition;
        this.currentMemberAbsent = currentMemberAbsent;
        this.currentMemberID = currentMemberID;
        this.currentMemberName = currentMemberName;
        this.currentMemberRole = currentMemberRole;

        this.dialogListener = editMemberDialogListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit_member, null);

        nameEdit = view.findViewById(R.id.nameEdit);
        idEdit = view.findViewById(R.id.idEdit);
        absentPicker = view.findViewById(R.id.absentPicker);
        isLeader = view.findViewById(R.id.isLeader);

        /* init old Member Info */
        nameEdit.setText(this.currentMemberName);
        idEdit.setText(this.currentMemberID);
        isLeader.setChecked(this.currentMemberRole);

        absentPicker.setMinValue(0);
        absentPicker.setMaxValue(20);
        absentPicker.setValue(((Number) this.currentMemberAbsent).intValue());

        builder.setView(view)
                .setTitle("MEMBER INFO")
                .setNeutralButton("Cancel", (dialog, which) -> dialog.dismiss())
                .setNegativeButton("Delete", (dialog, which) -> {

                    /* Confirm Delete Second Confirm */
                    AlertDialog.Builder confirmDialog = new AlertDialog.Builder(requireActivity());
                    confirmDialog.setTitle("CONFIRM DELETE MEMBER");


                    confirmDialog.setPositiveButton("Delete", (cDialog, cWhich) ->
                                    dialogListener.editMemberClicked(DELETE_ACTION, this.memberPosition,
                                            null, null,
                                            false, 1L))
                            .setNeutralButton("Cancel", (cDialog, cWhich) ->
                                    cDialog.dismiss());
                    confirmDialog.show();
                    /* End second confirm */

                })
                .setPositiveButton("Confirm", (dialog, which) -> {
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
                        dialogListener.editMemberClicked(EDIT_ACTION, this.memberPosition,
                                newMemberName, newMemberID, newMemberRole, newMemberAbsent);
                    }

                });
        builder.create();
        /* End of Edit Member Dialog */


        return builder.create();
    }

    public interface EditMemberDialogListener {
        void editMemberClicked(
                boolean whichAction,
                int memberPosition,
                String currentMemberName,
                String currentMemberID,
                boolean currentMemberRole,
                long currentMemberAbsent);
    }
}

