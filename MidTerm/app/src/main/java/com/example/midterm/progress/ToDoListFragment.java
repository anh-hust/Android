package com.example.midterm.progress;

import static androidx.core.view.ViewCompat.animate;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.ListFragment;

import com.example.midterm.R;
import com.example.midterm.adapter.ToDoAdapter;
import com.example.midterm.data.GroupData;
import com.example.midterm.data.GroupInfo;
import com.example.midterm.data.ToDoItem;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO Fragment handler, add more item to TODO
 */

public class ToDoListFragment extends ListFragment {
    /* Float Button handler */
    private boolean isFabMenuOpen = false;
    final OvershootInterpolator interpolator = new OvershootInterpolator();
    private FloatingActionButton addFloatBtn;
    private FloatingActionButton saveFloatBtn;

    private TextView addText;
    private TextView saveText;

    private ListView listView;

    /* Data */
    private GroupInfo groupProgress;
    private ArrayList<ToDoItem> toDoItemArrayList = new ArrayList<>();

    /* Firebase initialize require */
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference document;
    private Map<String, Object> data = new HashMap<>();

    private int countItem = 0;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* Get the group onCLick from previous fragment */
        Bundle bundle = getArguments();
        if (bundle != null) {
            int position = bundle.getInt("GroupProgress_id");

            /* Take data we need of this group */
            groupProgress = new GroupData().groupProgressArrayList().get(position);

            /* Redirect to the Document of the right Group base on position, pull all data */
            document = db.collection(GroupProgressActivity.TODO_COLLECTION).document("Group_" + String.valueOf(position + 1));
            Log.d("Document Retrieve", "Document: " + "Group_" + String.valueOf(position + 1));

            document.get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            /* Pull and display data phase */
                            data = documentSnapshot.getData();

                            assert data != null;
                            countItem = data.size();
                            Log.d("Document Retrieve", "Number of items: " + String.valueOf(countItem));
                            Log.d("Document Retrieve", "List Items: " + String.valueOf(data));
                            /* Do not get the completedCount */
                            for (int i = 0; i < countItem - 2; i++) {
                                Map<String, Object> item = (Map<String, Object>) data.get("Item_" + String.valueOf(i + 1));
                                Log.d("Document Retrieve", "Item: " + String.valueOf(item));

                                String itemContent = String.valueOf(item.get(ToDoItem.CONTENT));
                                boolean completed = Boolean.parseBoolean(String.valueOf(item.get(ToDoItem.CONTENT_STATUS)));
                                ToDoItem toDoItem = new ToDoItem(itemContent, completed);
                                toDoItemArrayList.add(toDoItem);
                            }

                            /* Pass data to List View and set Adapter through ToDoAdapter */
                            ToDoAdapter toDoAdapter = new ToDoAdapter(getActivity(),
                                    R.layout.todo_list_item, toDoItemArrayList);
                            setListAdapter(toDoAdapter);

                        } else
                            Toast.makeText(getActivity(), "No data exist on this link",
                                    Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getActivity(), "Something error",
                                Toast.LENGTH_SHORT).show();
                        Log.d("FireBase Error", "Error" + String.valueOf(e));
                    });
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
            FloatingActionButton floatMotherBtn = view.findViewById(R.id.floatMotherBtn);
            addFloatBtn = view.findViewById(R.id.addBtn);
            saveFloatBtn = view.findViewById(R.id.saveBtn);
            addText = view.findViewById(R.id.addText);
            saveText = view.findViewById(R.id.saveText);

            addFloatBtn.setVisibility(View.GONE);
            saveFloatBtn.setVisibility(View.GONE);
            addText.setVisibility(View.GONE);
            saveText.setVisibility(View.GONE);

            /* Float Button handle click event */
            floatMotherBtn.setOnClickListener(v -> {
                if (!isFabMenuOpen) {
                    animate(floatMotherBtn).
                            setInterpolator(interpolator).
                            setListener(null).
                            rotation(floatMotherBtn.getRotation() + 45f).
                            withLayer().
                            setDuration(300).
                            withStartAction(null).
                            start();
                    addFloatBtn.show();
                    saveFloatBtn.show();
                    addText.setVisibility(View.VISIBLE);
                    saveText.setVisibility(View.VISIBLE);


                    isFabMenuOpen = true;
                } else {
                    animate(floatMotherBtn).
                            setInterpolator(interpolator).
                            setListener(null).
                            rotation(floatMotherBtn.getRotation() + 45f).
                            withLayer().
                            setDuration(300).
                            withStartAction(null).
                            start();
                    addFloatBtn.hide();
                    saveFloatBtn.hide();
                    addText.setVisibility(View.GONE);
                    saveText.setVisibility(View.GONE);


                    isFabMenuOpen = false;
                }
            });

            /* Click Save button handler */
            saveFloatBtn.setOnClickListener(v -> {
                /* Clear Map<> data to start new Update to Firebase */
                data.clear();
                listView = getListView();
                int count = listView.getCount();
                int completedCount = 0;
                Log.d("Save Button Handle", "Get List View: " + String.valueOf(listView));
                Log.d("Save Button Handle", "Get List View Count: " + String.valueOf(count));
                for (int i = 0; i < count; i++) {
                    ViewGroup viewGroup = (ViewGroup) listView.getChildAt(i);
                    CheckBox checkBox = (CheckBox) viewGroup.findViewById(R.id.itemCheckBox);

                    /* Adjust toItemArrayList global */
                    toDoItemArrayList.get(i).setItemContent(String.valueOf(checkBox.getText()));
                    toDoItemArrayList.get(i).setCompleted(checkBox.isChecked());

                    /* Prerequisite for push data to Firebase */
                    Map<String, Object> item = new HashMap<>();
                    item.put(ToDoItem.CONTENT, checkBox.getText());
                    item.put(ToDoItem.CONTENT_STATUS, checkBox.isChecked());
                    if (checkBox.isChecked())
                        completedCount += 1;

                    data.put("Item_" + String.valueOf(i + 1), item);

                    /* Trace Log */
                    Log.d("Save Button Handler", "View Group: " + String.valueOf(viewGroup));
                    Log.d("Save Button Handler", "Check Box: " + String.valueOf(checkBox));
                    Log.d("Save Button Handler", "Map: " + String.valueOf(item));
                }
                data.put(GroupInfo.TOTAL_ITEMS, count);
                data.put(GroupInfo.COMPLETED_ITEMS, completedCount);
                document.set(data)
                        .addOnSuccessListener(unused -> {
                            Toast.makeText(getActivity(), "Save Success !!",
                                    Toast.LENGTH_SHORT).show();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(getActivity(), "Something's wrong !!",
                                    Toast.LENGTH_SHORT).show();
                            Log.d("Save Button Handler Error", "Error: " + String.valueOf(e));
                        });
            });

            /* CLick add button event handle */
            addFloatBtn.setOnClickListener(v -> {
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
                        /* Clear ArrayList to start load data onScreen (in case do not push save first) */
                        toDoItemArrayList = new ArrayList<>();
                        listView = getListView();
                        int count = listView.getCount();

                        /* Load data from onScreen data */
                        for (int position = 0; position < count; position++) {
                            ViewGroup viewGroup = (ViewGroup) listView.getChildAt(position);
                            CheckBox checkBox = (CheckBox) viewGroup.findViewById(R.id.itemCheckBox);

                            toDoItemArrayList.add(new ToDoItem(String.valueOf(checkBox.getText()), checkBox.isChecked()));

                        }
                        /* Add new item from input EditText at last */
                        toDoItemArrayList.add(new ToDoItem(inputItemContent, ToDoItem.NOT_COMPLETED));

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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView = getListView();
        /* Long click as DELETE item handle */
        listView.setOnItemLongClickListener((parent, view1, position, id) -> {
            Toast.makeText(getActivity(), "Delete Item",
                    Toast.LENGTH_SHORT).show();
            AlertDialog.Builder addItemDialog = new AlertDialog.Builder(view1.getContext());
            addItemDialog.setTitle("Confirm DELETE");

            /* DELETE button handle */
            addItemDialog.setPositiveButton("Delete", (dialogInterface, i) -> {
                toDoItemArrayList.remove(position);
                /* Pass data to List View and set Adapter through ToDoAdapter */
                ToDoAdapter toDoAdapter = new ToDoAdapter(getActivity(),
                        R.layout.todo_list_item, toDoItemArrayList);
                setListAdapter(toDoAdapter);

                /* Notify add successfully */
                Toast.makeText(getContext(), "Item is gone",
                        Toast.LENGTH_SHORT).show();
            });

            /* Cancel Button handle */
            addItemDialog.setNegativeButton("Cancel", (dialogInterface, i) ->
                    dialogInterface.dismiss());

            addItemDialog.show();
            return true;
        });
    }
}
