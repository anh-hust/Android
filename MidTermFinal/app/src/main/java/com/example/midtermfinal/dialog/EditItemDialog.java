package com.example.midtermfinal.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.midtermfinal.R;

public class EditItemDialog extends AppCompatDialogFragment {
    private EditText itemEdit;
    private CheckBox itemCheck;

    private String currentItem;
    private boolean currentItemStatus;
    private final int itemPosition;

    private final EditItemDialogListener dialogListener;

    public static final boolean DELETE_ACTION = false;
    public static final boolean EDIT_ACTION = true;

    public EditItemDialog(EditItemDialogListener editItemDialogListener, int itemPosition,
                          String currentItem, boolean currentItemStatus) {
        this.currentItem = currentItem;
        this.currentItemStatus = currentItemStatus;
        this.itemPosition = itemPosition;

        this.dialogListener = editItemDialogListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit_item, null);

        itemEdit = view.findViewById(R.id.itemEdit);
        itemCheck = view.findViewById(R.id.itemCheck);

        /* init old Member Info */
        itemEdit.setText(currentItem);
        itemCheck.setChecked(currentItemStatus);

        builder.setView(view)
                .setTitle("TODO ITEM")
                .setNeutralButton("Cancel", (dialog, which) -> dialog.dismiss())
                .setNegativeButton("Delete", (dialog, which) -> {

                    /* Confirm Delete Second Confirm */
                    AlertDialog.Builder confirmDialog = new AlertDialog.Builder(requireActivity());
                    confirmDialog.setTitle("CONFIRM DELETE ITEM");


                    confirmDialog.setPositiveButton("Delete", (cDialog, cWhich) ->
                                    dialogListener.editItemClicked(DELETE_ACTION, this.itemPosition,
                                            null, false
                                    ))
                            .setNeutralButton("Cancel", (cDialog, cWhich) ->
                                    cDialog.dismiss());
                    confirmDialog.show();
                    /* End second confirm */

                })
                .setPositiveButton("Confirm", (dialog, which) -> {
                    String newItemContent = itemEdit.getText().toString();
                    boolean newItemStatus = itemCheck.isChecked();

                    if (newItemContent.isEmpty()) {
                        Toast.makeText(getContext(), "Item content cannot be null",
                                Toast.LENGTH_LONG).show();
                    } else {
//                        Log.d("AddMemberDialog class", "Add CLick Event - Data:\n"
//                                + newMemberName + newMemberID + newMemberRole + newMemberAbsent);
                        dialogListener.editItemClicked(EDIT_ACTION, this.itemPosition,
                                newItemContent, newItemStatus);
                    }

                });
        builder.create();
        /* End of Edit Member Dialog */


        return builder.create();

    }

    public interface EditItemDialogListener {
        void editItemClicked(
                boolean whichAction,
                int itemPosition,
                String newItemContent,
                boolean newItemStatus);
    }
}
