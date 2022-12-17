package com.example.midterm.progress;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.ListFragment;

import com.example.midterm.R;
import com.example.midterm.data.GroupData;
import com.example.midterm.data.GroupInfo;
import com.example.midterm.data.GroupProgressArrayAdapter;

import java.util.List;

public class GroupProgressFragment extends ListFragment {
    private List<GroupInfo> groupProgressList = new GroupData().groupProgressArrayList();
    private groupProgressCallbacks groupProgressCallbacks;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Pass data and set adapter */
        GroupProgressArrayAdapter groupProgressArrayAdapter = new GroupProgressArrayAdapter(getActivity(),
                R.layout.progress_list_item, groupProgressList);

        setListAdapter(groupProgressArrayAdapter);

    }

    /* inflater allow us to fetch our fragment_main.xml   */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        /* Put fragment_main into container */
        View view = inflater.inflate(R.layout.progress_list_fragment, container, false);

        return view;
    }

    public interface groupProgressCallbacks {
        void onGroupProgressItemSelected(GroupInfo groupProgress, int position);
    }

    @Override
    public void onListItemClick(@NonNull ListView l, @NonNull View v, int position, long id) {

        /* Verify through password for particular group */
        identifyAndNavDialog(position);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.groupProgressCallbacks = (groupProgressCallbacks) context;
    }

    /* Ask password at progress for identifying each Group */
    private void identifyAndNavDialog(int position) {
        AlertDialog.Builder pswdDialog = new AlertDialog.Builder(getActivity());
        pswdDialog.setTitle("Enter Password");

        final EditText input = new EditText(getActivity());
        pswdDialog.setView(input);

        pswdDialog.setPositiveButton("Login", (dialogInterface, i) -> {
            String inputPassword = input.getText().toString();

            /* if password is correct --> redirect to ToDoListFragment */
            if (groupProgressList.get(position).getGroupPassword().equals(inputPassword)) {
                Toast.makeText(getActivity(), "Welcome",
                        Toast.LENGTH_SHORT).show();

                GroupInfo groupProgress = groupProgressList.get(position);
                this.groupProgressCallbacks.onGroupProgressItemSelected(groupProgress, position);

            } else {
                Toast.makeText(getActivity(), "Incorrect password",
                        Toast.LENGTH_SHORT).show();
            }
        });

        pswdDialog.setNegativeButton("Cancel", (dialogInterface, i) ->
                dialogInterface.dismiss());

        pswdDialog.show();
    }
}
