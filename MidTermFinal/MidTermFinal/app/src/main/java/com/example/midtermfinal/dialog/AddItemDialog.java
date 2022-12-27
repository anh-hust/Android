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

public class AddItemDialog extends AppCompatDialogFragment {
    private EditText itemEdit;
    private CheckBox itemCheck;

    private final AddItemDialogListener addItemDialogListener;

    /**
     * TODO Very carefully, in ProgressGroupFragment you implements > 2 interface, what you need is
     * 1. Constructor && Listener at final
     * 2. in call new AddMemberDialog(this::addItemClicked)
     */
    public AddItemDialog(AddItemDialogListener dialogListener) {
        this.addItemDialogListener = dialogListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_add_item, null);

        itemEdit = view.findViewById(R.id.itemEdit);
        itemCheck = view.findViewById(R.id.itemCheck);

        builder.setView(view)
                .setTitle("ADD NEW ITEM")
                .setNeutralButton("Cancel", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("Add", (dialog, which) -> {
                    String newItemContent = itemEdit.getText().toString();
                    boolean newItemStatus = itemCheck.isChecked();
                    if (newItemContent.isEmpty())
                        Toast.makeText(getContext(), "New item cannot be empty",
                                Toast.LENGTH_SHORT).show();
                    else {
                        addItemDialogListener.addItemClicked(newItemContent, newItemStatus);
                    }
                });
        return builder.create();
    }


    public interface AddItemDialogListener {
        void addItemClicked(String itemContent, boolean itemStatus);
    }
}
