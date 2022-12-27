package com.example.midtermfinal.group;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.midtermfinal.R;
import com.example.midtermfinal.adapter.GroupAdapter;
import com.example.midtermfinal.data.GroupInfo;
import com.example.midtermfinal.firestore.MyFireStore;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class GroupFragment extends Fragment implements GroupAdapter.OnItemClickListener {
    private ArrayList<GroupInfo> groupInfoArrayList;
    private ArrayList<GroupInfo> groupInfoArrayListBuffer;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        groupInfoArrayList = new ArrayList<>();
        groupInfoArrayListBuffer = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group, container, false);
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        CollectionReference collectionReference = firebaseFirestore.collection(MyFireStore.GROUP_INFO_COLLECTION);

        collectionReference.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    /* Prerequisite to retrieve data */
                    /* Start from group 1 */
                    int traceGroup = 1;

                    for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                        String groupID = snapshot.getId();
                        Map<String, Object> groupData = snapshot.getData();
                        GroupInfo groupInfo = new GroupInfo();
                        Log.d("GroupFragment class", "onCreateView - if, groupData: " + groupData);

                        /* Retrieve and assign data of group */
                        groupInfo.setGroupID(groupID);
                        groupInfo.setGroupName(Objects.requireNonNull(groupData.get(MyFireStore.GROUP_NAME)).toString());
                        groupData.remove(MyFireStore.GROUP_NAME);
                        groupInfo.setGroupPassword(Objects.requireNonNull(groupData.get(MyFireStore.GROUP_PASSWORD)).toString());
                        groupData.remove(MyFireStore.GROUP_PASSWORD);
                        groupInfo.setGroupTopic(Objects.requireNonNull(groupData.get(MyFireStore.GROUP_TOPIC)).toString());
                        groupData.remove(MyFireStore.GROUP_TOPIC);

                        /* Members of group retrieve data phase */
                        Map<String, Object> memberInfo = new HashMap<>();
                        ArrayList<String> membersName = new ArrayList<>();
                        ArrayList<String> membersID = new ArrayList<>();
                        ArrayList<Long> membersAbsent = new ArrayList<>();
                        ArrayList<Boolean> membersRole = new ArrayList<>();
                        for (int i = 0; i < groupData.size(); i++) {
                            memberInfo = (Map<String, Object>) groupData.get(MyFireStore.MEMBER_NO + (i + 1));
                            assert memberInfo != null;
                            membersName.add(Objects.requireNonNull(memberInfo.get(MyFireStore.MEMBER_NAME)).toString());
                            membersID.add(Objects.requireNonNull(memberInfo.get(MyFireStore.MEMBER_ID)).toString());
                            membersAbsent.add(((Number) Objects.requireNonNull(memberInfo.get(MyFireStore.MEMBER_ABSENT_COUNT))).longValue());
                            membersRole.add((Boolean) memberInfo.get(MyFireStore.MEMBER_ROLE));
                        }
                        groupInfo.setGroupMembers(membersName);
                        groupInfo.setGroupMembersID(membersID);
                        groupInfo.setGroupMembersAbsent(membersAbsent);
                        groupInfo.setGroupMembersRole(membersRole);

                        if (Integer.parseInt(groupID.replace(MyFireStore.GROUP_NO_DOCUMENT, "")) != traceGroup) {
                            Log.d("GroupFragment class", "onCreateView - if, groupInfo: " + groupInfo);
                            /* add to ArrayList buffer */
                            groupInfoArrayListBuffer.add(groupInfo);
                        } else {
                            Log.d("GroupFragment class", "onCreateView - else, groupInfo: " + groupInfo);
                            /* add to ArrayList buffer */
                            groupInfoArrayList.add(groupInfo);
                            traceGroup++;
                        }

                    }
                    /* Push all data from Buffer to main ArrayList */
                    if (!groupInfoArrayListBuffer.isEmpty())
                        groupInfoArrayList.addAll(groupInfoArrayListBuffer);
//                    for (int i = 0; i < groupInfoArrayList.size(); i++) {
//                        Log.d("GroupFragment class", "onCreateView, Final Result: " + groupInfoArrayList.get(i));
//                    }

                    /* Front-End phase */
                    RecyclerView groupRecyclerView = view.findViewById(R.id.recyclerViewGroup);
                    groupRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    groupRecyclerView.setHasFixedSize(true);

                    /* this because whole class implements 1 onItemClickListener */
                    GroupAdapter groupAdapter = new GroupAdapter(getContext(), groupInfoArrayList, this::onItemClick);

                    groupRecyclerView.setAdapter(groupAdapter);
                    groupAdapter.notifyDataSetChanged();

                })
                .addOnFailureListener(e -> Log.d("GroupFragment class", "Error retrieve data:\n" + e));
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onItemClick(int position) {
//        Log.d("Group Fragment", "Data GroupInfo: " + groupInfoArrayList.size() +
//                "\n Click at position: " + position);

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frag_main, new GroupInfoFragment(groupInfoArrayList.get(position), position));
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

}
