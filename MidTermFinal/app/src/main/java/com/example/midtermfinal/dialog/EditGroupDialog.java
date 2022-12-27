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
import com.example.midtermfinal.data.GroupInfo;

public class EditGroupDialog extends AppCompatDialogFragment {
    private EditText groupNameEdit;
    private EditText groupTopicEdit;
    private final GroupInfo groupInfo;

    private final EditGroupDialogListener dialogListener;

    /**
     * TODO Very carefully, in GroupInfoFragment you implements > 2 interface, what you need is
     * 1. Constructor && Listener at final
     * 2. in call new AddMemberDialog(this::editGroupClicked)
     */
    public EditGroupDialog(EditGroupDialogListener dialogListener, GroupInfo groupInfo) {
        this.dialogListener = dialogListener;
        this.groupInfo = groupInfo;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_edit_group, null);

        groupNameEdit = view.findViewById(R.id.groupNameEdit);
        groupTopicEdit = view.findViewById(R.id.groupTopicEdit);

        groupTopicEdit.setText(groupInfo.getGroupTopic());
        groupNameEdit.setText(groupInfo.getGroupName());

        builder.setView(view)
                .setTitle("CHANGE GROUP INFO")
                .setPositiveButton("Confirm", (dialog, which) -> {
                            String newGroupName = groupNameEdit.getText().toString();
                            String newGroupTopic = groupTopicEdit.getText().toString();

                            if (newGroupName.isEmpty() || newGroupTopic.isEmpty())
                                Toast.makeText(getContext(), "Group Name and Topic annot be null",
                                        Toast.LENGTH_LONG).show();
                            else
                                dialogListener.editGroupClicked(newGroupName, newGroupTopic);
                        }
                )
                .setNeutralButton("Cancel", (dialog, which) -> {

                });

        return builder.create();
    }

    public interface EditGroupDialogListener {
        void editGroupClicked(String newGroupName, String newGroupTopic);
    }

}
