package com.example.midtermfinal.progress;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.midtermfinal.R;
import com.example.midtermfinal.adapter.GroupItemAdapter;
import com.example.midtermfinal.data.GroupProgressItems;
import com.example.midtermfinal.dialog.AddItemDialog;
import com.example.midtermfinal.dialog.EditItemDialog;
import com.example.midtermfinal.firestore.MyFireStore;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.HashMap;
import java.util.Map;

public class ProgressGroupFragment extends Fragment implements GroupItemAdapter.OnItemClickListener,
        AddItemDialog.AddItemDialogListener,
        EditItemDialog.EditItemDialogListener {
    private GroupProgressItems groupProgressItems;
    private int position;

    public ProgressGroupFragment(GroupProgressItems groupProgressItems, int position) {
        setGroupProgressItems(groupProgressItems);
        setPosition(position);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress_list, container, false);

        TextView groupTopic = view.findViewById(R.id.groupTopic);

        groupTopic.setText(getGroupProgressItems().getGroupTopic());

        RecyclerView groupItemRecyclerView = view.findViewById(R.id.recyclerViewProgressList);
        groupItemRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        groupItemRecyclerView.setHasFixedSize(true);

//        Log.d("ProgressGroupFragment", "Data: " + groupProgressItems.getItemListContent());
        GroupItemAdapter groupItemAdapter = new GroupItemAdapter(getContext(), getGroupProgressItems(), this::onItemClick);

        groupItemRecyclerView.setAdapter(groupItemAdapter);
        groupItemAdapter.notifyDataSetChanged();

        return view;
    }

    /* Button Handler */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        ImageButton backBtn = view.findViewById(R.id.backBtn);
        FloatingActionButton addTaskFab = view.findViewById(R.id.addTaskFab);

        /* When push add float btn --> call to AddItem DIalog
         * --> at dialog will retrieve input data of dialog
         * --> call to addItemClicked right below (interface of Dialog implement here)
         *   */
        addTaskFab.setOnClickListener(v -> {
            AddItemDialog addItemDialog = new AddItemDialog(this::addItemClicked);
            addItemDialog.show(requireActivity().getSupportFragmentManager(), "Add Item Dialog");
        });

        backBtn.setOnClickListener(v -> {
            FragmentTransaction fragmentTransaction = requireActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frag_main, new ProgressFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });
    }

    @Override
    public void onItemClick(int position) {
        EditItemDialog editItemDialog = new EditItemDialog(this::editItemClicked,
                position,
                getGroupProgressItems().getItemListContent().get(position),
                getGroupProgressItems().getItemListStatus().get(position));
        editItemDialog.show(getActivity().getSupportFragmentManager(), "Edit Item Dialog");
    }

    @Override
    public void addItemClicked(String newItemContent, boolean newItemStatus) {
        Map<String, Object> item = new HashMap<>();
        item.put(MyFireStore.ITEM_CONTENT, newItemContent);
        item.put(MyFireStore.ITEM_STATUS, newItemStatus);
        Map<String, Object> data = new HashMap<>();
        data.put(MyFireStore.ITEM_NO + String.valueOf(getGroupProgressItems().getItemListContent().size() + 1), item);
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        CollectionReference collectionReference = firebaseFirestore.collection(MyFireStore.PROGRESS_COLLECTION);
        collectionReference.document(getGroupProgressItems().getGroupID())
                .update(data)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(getContext(), "Add Item successfully",
                            Toast.LENGTH_SHORT).show();

                    /* Front-End phase */
                    RecyclerView groupProgressRecyclerView = getView().findViewById(R.id.recyclerViewProgressList);
                    groupProgressRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    groupProgressRecyclerView.setHasFixedSize(true);

                    getGroupProgressItems().getItemListContent().add(newItemContent);
                    getGroupProgressItems().getItemListStatus().add(newItemStatus);

                    GroupItemAdapter groupItemAdapter = new GroupItemAdapter(getContext(), getGroupProgressItems(), this::onItemClick);
                    groupProgressRecyclerView.setAdapter(groupItemAdapter);
                    groupItemAdapter.notifyDataSetChanged();

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Add item fail",
                            Toast.LENGTH_SHORT).show();
                });
    }

    @Override
    public void editItemClicked(boolean whichAction, int itemPosition, String newItemContent, boolean newItemStatus) {
        if (whichAction == GroupProgressItems.COMPLETED) {
            /* Data update */
            Map<String, Object> item = new HashMap<>();
            item.put(MyFireStore.ITEM_CONTENT, newItemContent);
            item.put(MyFireStore.ITEM_STATUS, newItemStatus);

            Map<String, Object> newItem = new HashMap<>();
            newItem.put(MyFireStore.ITEM_NO + String.valueOf(itemPosition + 1), item);

            /* Update to Firebase */
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

            firebaseFirestore.collection(MyFireStore.PROGRESS_COLLECTION)
                    .document(getGroupProgressItems().getGroupID())
                    .update(newItem)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(getContext(), "Edit Item Success",
                                Toast.LENGTH_SHORT).show();

                        /* Update FE */
                        getGroupProgressItems().getItemListContent().set(itemPosition, newItemContent);
                        getGroupProgressItems().getItemListStatus().set(itemPosition, newItemStatus);

                        RecyclerView groupItemRecyclerView = getView().findViewById(R.id.recyclerViewProgressList);
                        groupItemRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        groupItemRecyclerView.setHasFixedSize(true);

                        GroupItemAdapter groupItemAdapter = new GroupItemAdapter(getContext(), getGroupProgressItems(), ProgressGroupFragment.this::onItemClick);

                        groupItemRecyclerView.setAdapter(groupItemAdapter);
                        groupItemAdapter.notifyDataSetChanged();
                    })
                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Edit Item fail",
                            Toast.LENGTH_SHORT).show());
        } else {
            /* Data Update */
            getGroupProgressItems().getItemListContent().remove(itemPosition);
            getGroupProgressItems().getItemListStatus().remove(itemPosition);

            Map<String, Object> groupItem = new HashMap<>();
            for (int i = 0; i < getGroupProgressItems().getItemListContent().size(); i++) {
                Map<String, Object> item = new HashMap<>();
                item.put(MyFireStore.ITEM_CONTENT, getGroupProgressItems().getItemListContent().get(i));
                item.put(MyFireStore.ITEM_STATUS, getGroupProgressItems().getItemListStatus().get(i));

                groupItem.put(MyFireStore.ITEM_NO + String.valueOf(i + 1), item);

                /* Update to Firebase */
                FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

                firebaseFirestore.collection(MyFireStore.PROGRESS_COLLECTION)
                        .document(getGroupProgressItems().getGroupID())
                        .set(groupItem)
                        .addOnSuccessListener(unused -> {
                            Toast.makeText(getContext(), "Delete item success", Toast.LENGTH_SHORT).show();

                            /* Update FE */
                            RecyclerView groupItemRecyclerView = getView().findViewById(R.id.recyclerViewProgressList);
                            groupItemRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            groupItemRecyclerView.setHasFixedSize(true);

                            GroupItemAdapter groupItemAdapter = new GroupItemAdapter(getContext(), getGroupProgressItems(), ProgressGroupFragment.this::onItemClick);

                            groupItemRecyclerView.setAdapter(groupItemAdapter);
                            groupItemAdapter.notifyDataSetChanged();

                        }).addOnFailureListener(e ->
                                Toast.makeText(getActivity(), "Delete item fail",
                                        Toast.LENGTH_SHORT).show());
            }
        }
    }

    /* Setter Getter */
    public GroupProgressItems getGroupProgressItems() {
        return groupProgressItems;
    }

    public void setGroupProgressItems(GroupProgressItems groupProgressItems) {
        this.groupProgressItems = groupProgressItems;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

}
