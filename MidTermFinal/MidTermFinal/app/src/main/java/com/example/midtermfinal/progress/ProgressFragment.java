package com.example.midtermfinal.progress;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.midtermfinal.R;
import com.example.midtermfinal.adapter.ProgressMgmtAdapter;
import com.example.midtermfinal.data.GroupProgressItems;
import com.example.midtermfinal.firestore.MyFireStore;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

public class ProgressFragment extends Fragment implements ProgressMgmtAdapter.OnItemClickListener {

    private ArrayList<GroupProgressItems> groupProgressItemList;
    private ArrayList<GroupProgressItems> groupProgressItemListBuffer;
    private ArrayList<String> groupPwdList;
    private ArrayList<String> groupPwdListBuffer;
    private ArrayList<String> groupTopicList;
    private ArrayList<String> groupTopicListBuffer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        groupProgressItemList = new ArrayList<>();
        groupProgressItemListBuffer = new ArrayList<>();
        groupPwdList = new ArrayList<>();
        groupTopicList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress, container, false);

        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        CollectionReference collectionReference = firebaseFirestore.collection(MyFireStore.PROGRESS_COLLECTION);

        collectionReference.get()
                .addOnSuccessListener((OnSuccessListener<QuerySnapshot>) queryDocumentSnapshots -> {
                    /* Prerequisite to retrieve data */
                    /* Start from group 1 */
                    int traceGroup = 1;

                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        String groupID = snapshot.getId();

                        Map<String, Object> groupProgressItems = snapshot.getData();
                        GroupProgressItems items = new GroupProgressItems();
                        Log.d("ProgressFragment class", "onCreateView - if, groupProgressItems: " + groupProgressItems);

                        /* Retrieve and assign data of Progress Item according group */
                        items.setGroupID(groupID);

                        /* Members of group retrieve data phase */
                        ArrayList<String> itemContentList = new ArrayList<>();
                        ArrayList<Boolean> itemStatusList = new ArrayList<>();
                        for (int i = 0; i < groupProgressItems.size(); i++) {
                            Map<String, Object> item = (Map<String, Object>) groupProgressItems.get(MyFireStore.ITEM_NO + String.valueOf(i + 1));
                            assert item != null;

                            itemContentList.add(item.get(MyFireStore.ITEM_CONTENT).toString());
                            itemStatusList.add((Boolean) item.get(MyFireStore.ITEM_STATUS));
                        }
                        items.setItemListContent(itemContentList);
                        items.setItemListStatus(itemStatusList);

                        /* add to ArrayList buffer */
                        if (Integer.parseInt(groupID.replace(MyFireStore.GROUP_NO_DOCUMENT, "")) != traceGroup) {
                            groupProgressItemListBuffer.add(items);
//                            Log.d("GroupFragment class", "onCreateView - if, Group Items: " + groupProgressItems);
                        } else {
                            groupProgressItemList.add(items);
                            traceGroup++;
//                            Log.d("GroupFragment class", "onCreateView - else, Group Items: " + groupProgressItems);
                        }

                    }

                    /* Push all data from Buffer to main ArrayList */
                    if (!groupProgressItemListBuffer.isEmpty())
                        groupProgressItemList.addAll(groupProgressItemListBuffer);
//                    for (int i = 0; i < groupInfoArrayList.size(); i++) {
//                        Log.d("GroupFragment class", "onCreateView, Final Result: " + groupInfoArrayList.get(i));
//                    }

                    /* Front-End phase */
                    RecyclerView progressRecyclerView = view.findViewById(R.id.recyclerViewProgress);
                    progressRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    progressRecyclerView.setHasFixedSize(true);

                    /* this because whole class implements 1 onItemClickListener */
                    ProgressMgmtAdapter progressMgmtAdapter = new ProgressMgmtAdapter(getContext(), groupProgressItemList, this::onItemClick);

                    progressRecyclerView.setAdapter(progressMgmtAdapter);
                    progressMgmtAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.d("ProgressFragment class", "Error retrieve data:\n" + e));
        return view;

    }

    @Override
    public void onItemClick(int position) {
//        Toast.makeText(getContext(), "Click at " + position,
//                Toast.LENGTH_SHORT).show();
        /* Verify phase */
        AlertDialog.Builder verifyDialog = new AlertDialog.Builder(getActivity());
        verifyDialog.setTitle("VERIFY PERMISSION");

        final EditText input = new EditText(getActivity());
        input.setHint("Group Password");
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        verifyDialog.setView(input);

        verifyDialog.setPositiveButton("Verify", (vDialog, vWhich) -> {
                    String inputPwd = input.getText().toString();

                    FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
                    firebaseFirestore.collection(MyFireStore.GROUP_INFO_COLLECTION)
                            .document(groupProgressItemList.get(position).getGroupID())
                            .get().addOnSuccessListener(documentSnapshot -> {

                                /* Retrieve Group topic to GroupProgressItem */
                                groupProgressItemList.get(position).setGroupTopic(documentSnapshot.get(MyFireStore.GROUP_TOPIC).toString());
                                if (inputPwd.equals(documentSnapshot.get(MyFireStore.GROUP_PASSWORD))) {
                                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//                                    Log.d("Progress Fragment", "Pass Data to GroupItem: "
//                                            + groupProgressItemList.get(position).getItemListContent());
                                    fragmentTransaction.replace(R.id.frag_main, new ProgressGroupFragment(groupProgressItemList.get(position), position));
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();
                                } else {
                                    Toast.makeText(getContext(), "Verify fail",
                                            Toast.LENGTH_SHORT).show();
                                    vDialog.dismiss();
                                }
                            })
                            .addOnFailureListener(e -> Log.d("ProgressFragment", "onItemClick " + e));
                })
                .setNeutralButton("Cancel", (dialog, which) -> dialog.dismiss());
        verifyDialog.show();
    }
}
