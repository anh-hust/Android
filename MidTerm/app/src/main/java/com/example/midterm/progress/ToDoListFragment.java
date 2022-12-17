package com.example.midterm.progress;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.ListFragment;

import com.example.midterm.R;
import com.example.midterm.data.GroupData;
import com.example.midterm.data.GroupInfo;
import com.example.midterm.data.ToDoAdapter;
import com.example.midterm.data.ToDoItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * TODO Fragment handler, add more item to TODO
 */

public class ToDoListFragment extends ListFragment {
    private GroupInfo groupProgress;
    private ArrayList<ToDoItem> toDoItemArrayList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Get the group onCLick from previous fragment */
        Bundle bundle = getArguments();
        if (bundle != null) {
            int position = bundle.getInt("GroupProgress_id");

            /* Take data we need of this group */
            groupProgress = new GroupData().groupProgressArrayList().get(position);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.todo_list_fragment, container, false);

        if (groupProgress != null) {
            TextView groupTopic = view.findViewById(R.id.groupProgressTopic);
            groupTopic.setText(groupProgress.getGroupTopic() + "\nJourney to complete");

            /* Float Button handler */
            FloatingActionButton floatBtn = view.findViewById(R.id.addItemFloatBtn);

            /* CLick event handle */
            floatBtn.setOnClickListener(v -> {
                AlertDialog.Builder addItemDialog = new AlertDialog.Builder(v.getContext());
                addItemDialog.setTitle("Add TODO to Journey");

                final EditText input = new EditText(v.getContext());
                addItemDialog.setView(input);

                /* ADD button handle */
                addItemDialog.setPositiveButton("Add", (dialogInterface, i) -> {

                    /* String input read from EditText field */
                    String inputItemContent = input.getText().toString();

                    /* if != null */
                    if (!inputItemContent.isEmpty()) {
                        /* New item create */
                        ToDoItem toDoItem = new ToDoItem();
                        toDoItem.setItemContent(inputItemContent);

                        /* Add new item into item array */
                        toDoItemArrayList.add(toDoItem);

                        /* Pass data to List View and set Adapter through ToDoAdapter */
                        ToDoAdapter toDoAdapter = new ToDoAdapter(getActivity(),
                                R.layout.todo_list_item, toDoItemArrayList);
                        setListAdapter(toDoAdapter);

                        /* Notify add successfully */
                        Toast.makeText(getContext(), "New item is added",
                                Toast.LENGTH_SHORT).show();
                    }

                    /* If String input is empty */
                    else
                        Toast.makeText(getContext(), "New item cannot be null !!!",
                                Toast.LENGTH_SHORT).show();
                });

                /* Cancel Button handle */
                addItemDialog.setNegativeButton("Cancel", (dialogInterface, i) ->
                        dialogInterface.dismiss());

                addItemDialog.show();
            });
        }

        return view;
    }
}
