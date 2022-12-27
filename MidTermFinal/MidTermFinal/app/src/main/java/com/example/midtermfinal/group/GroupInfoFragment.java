package com.example.midtermfinal.group;

import static androidx.core.view.ViewCompat.animate;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.midtermfinal.R;
import com.example.midtermfinal.adapter.MemberInfoAdapter;
import com.example.midtermfinal.data.GroupInfo;
import com.example.midtermfinal.dialog.AddMemberDialog;
import com.example.midtermfinal.dialog.ChangePasswordDialog;
import com.example.midtermfinal.dialog.EditGroupDialog;
import com.example.midtermfinal.dialog.EditMemberDialog;
import com.example.midtermfinal.firestore.MyFireStore;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class GroupInfoFragment extends Fragment implements MemberInfoAdapter.OnItemClickListener,
        AddMemberDialog.AddMemberDialogListener,
        EditGroupDialog.EditGroupDialogListener,
        ChangePasswordDialog.ChangePasswordDialogListener,
        EditMemberDialog.EditMemberDialogListener {
    private GroupInfo groupInfo;
    private boolean isFAB_open = false;
    private final FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private TextView groupName;
    private TextView groupTopic;
    private int groupPosition;

    /* For Delete Dialog */
    private boolean verifySuccessful = false;
    private boolean sureDelete = false;

    public GroupInfoFragment(GroupInfo groupInfo, int groupPosition) {
        this.groupInfo = groupInfo;
        this.groupPosition = groupPosition;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group_info, container, false);

        this.groupName = view.findViewById(R.id.groupName);
        this.groupTopic = view.findViewById(R.id.groupTopic);

        this.groupName.setText(getGroupInfo().getGroupName());
        this.groupTopic.setText(getGroupInfo().getGroupTopic());


        /* Front-End phase */
        RecyclerView memberRecyclerView = view.findViewById(R.id.recyclerViewMember);
        memberRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        memberRecyclerView.setHasFixedSize(true);

        /* this because whole class implements 1 onItemClickListener */
//        Log.d("GroupInfoFragment class", "onCreateView data to adapter:\n" +
//                getGroupInfo().getGroupMembers().get(0) + getGroupInfo().getGroupMembers().get(1)
//        );
        MemberInfoAdapter groupAdapter = new MemberInfoAdapter(getContext(), getGroupInfo(), this::onItemClick);

        memberRecyclerView.setAdapter(groupAdapter);
        groupAdapter.notifyDataSetChanged();

        return view;
    }

    /**
     * TODO ViewCreated will handle Button CLick Event
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final OvershootInterpolator interpolator = new OvershootInterpolator();

        /* Init Float Action Button */
        Button backBtn = view.findViewById(R.id.backBtn);
        FloatingActionButton fab = view.findViewById(R.id.faBtn);
        FloatingActionButton addFloatBtn = view.findViewById(R.id.addBtn);
        FloatingActionButton editFloatBtn = view.findViewById(R.id.editBtn);
        FloatingActionButton changePwdFloatBtn = view.findViewById(R.id.changePwdBtn);
        FloatingActionButton deleteFloatBtn = view.findViewById(R.id.deleteBtn);

        /* Float button Animation */
        fab.setOnClickListener(v -> {
            if (!isFAB_open()) {
                animate(fab).
                        setInterpolator(interpolator).
                        setListener(null).
                        rotation(fab.getRotation() + 90f).
                        withLayer().
                        setDuration(300).
                        withStartAction(null).
                        start();
                addFloatBtn.show();
                editFloatBtn.show();
                changePwdFloatBtn.show();
                deleteFloatBtn.show();
                setFAB_open(true);
            } else {
                animate(fab).
                        setInterpolator(interpolator).
                        setListener(null).
                        rotation(fab.getRotation() - 90f).
                        withLayer().
                        setDuration(300).
                        withStartAction(null).
                        start();
                deleteFloatBtn.hide();
                changePwdFloatBtn.hide();
                editFloatBtn.hide();
                addFloatBtn.hide();

                setFAB_open(false);
            }
        });

        /* Add button handler */
        addFloatBtn.setOnClickListener(v -> {
            /**
             * TODO Very notice here and the constructor of this because implements > 2 interfaces
             * */
            /* Verify by using group Password step */
            AlertDialog.Builder verifyDialog = new AlertDialog.Builder(getActivity());
            verifyDialog.setTitle("VERIFY PERMISSION");

            final EditText input = new EditText(getActivity());
            input.setHint("Group Password");
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

            verifyDialog.setView(input);

            verifyDialog.setPositiveButton("Verify", (dialog, which) -> {
                        String inputPwd = input.getText().toString();
                        if (inputPwd.equals(getGroupInfo().getGroupPassword())) {
                            AddMemberDialog addMemberDialog = new AddMemberDialog(this::addMemberClicked);
                            addMemberDialog.show(getActivity().getSupportFragmentManager(), "Add Member Dialog");
                        } else {
                            Toast.makeText(getContext(), "Verify fail",
                                    Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    })
                    .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            verifyDialog.show();
        });

        /* Edit Group button handler */
        /**
         * TODO Very notice here and the constructor of this because implements > 2 interfaces
         * */
        /* Verify by using group password */
        editFloatBtn.setOnClickListener(v -> {
            AlertDialog.Builder verifyDialog = new AlertDialog.Builder(getActivity());
            verifyDialog.setTitle("VERIFY PERMISSION");

            final EditText input = new EditText(getActivity());
            input.setHint("Group Password");
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

            verifyDialog.setView(input);

            verifyDialog.setPositiveButton("Verify", (dialog, which) -> {
                        String inputPwd = input.getText().toString();
                        if (inputPwd.equals(getGroupInfo().getGroupPassword())) {
                            EditGroupDialog editGroupDialog = new EditGroupDialog(this::editGroupClicked, getGroupInfo());
                            editGroupDialog.show(getActivity().getSupportFragmentManager(), "Edit Group Info Dialog");
                        } else {
                            Toast.makeText(getContext(), "Verify fail",
                                    Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    })
                    .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            verifyDialog.show();

        });

        /* Change password button handler */
        changePwdFloatBtn.setOnClickListener(v -> {
            ChangePasswordDialog changePasswordDialog = new ChangePasswordDialog(this::changePwdClicked, getGroupInfo().getGroupPassword());
            changePasswordDialog.show(getActivity().getSupportFragmentManager(), "Change password Dialog");
        });

        /* Delete Button handler */
        deleteFloatBtn.setOnClickListener(v -> {
            AlertDialog.Builder verifyDialog = new AlertDialog.Builder(getActivity());
            verifyDialog.setTitle("VERIFY PERMISSION");

            final EditText input = new EditText(getActivity());
            input.setHint("Group Password");
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

            verifyDialog.setView(input);

            verifyDialog.setPositiveButton("Verify", (dialog, which) -> {
                        String inputPwd = input.getText().toString();
                        if (inputPwd.equals(getGroupInfo().getGroupPassword())) {

                            AlertDialog.Builder confirmDialog = new AlertDialog.Builder(getActivity());
                            confirmDialog.setTitle("CONFIRM DELETE GROUP");


                            confirmDialog.setPositiveButton("Delete", (cDialog, cWhich) -> {
                                this.firebaseFirestore.collection(MyFireStore.GROUP_INFO_COLLECTION)
                                        .document(getGroupInfo().getGroupID())
                                        .delete()
                                        .addOnSuccessListener(unused -> {
                                            Toast.makeText(getActivity(), "Delete successfully",
                                                    Toast.LENGTH_SHORT).show();

                                            FirebaseFirestore pFirebase = FirebaseFirestore.getInstance();

                                            /* Delete group in Progress side */
                                            pFirebase.collection(MyFireStore.PROGRESS_COLLECTION)
                                                    .document(getGroupInfo().getGroupID())
                                                    .delete();

                                            /* Back to Group Fragment */
                                            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                            fragmentTransaction.replace(R.id.frag_main, new GroupFragment());
                                            fragmentTransaction.addToBackStack(null);
                                            fragmentTransaction.commit();
                                        }).addOnFailureListener(e -> Toast.makeText(getActivity(), "Delete Failure",
                                                Toast.LENGTH_SHORT).show());
                            }).setNeutralButton("Cancel", (cDialog, cWhich) -> {
                                cDialog.dismiss();
                            });
                            confirmDialog.show();
                        } else {
                            Toast.makeText(getContext(), "Verify fail",
                                    Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    })
                    .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            verifyDialog.show();
        });

        /* Back button handler */
        backBtn.setOnClickListener(v -> {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frag_main, new GroupFragment());
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

    }

    /**
     * TODO Setter
     */
    public void setFAB_open(boolean FAB_status) {
        isFAB_open = FAB_status;
    }

    public void setGroupInfo(GroupInfo groupInfo) {
        this.groupInfo = groupInfo;
    }

    /**
     * TODO Getter
     */
    public boolean isFAB_open() {
        return this.isFAB_open;
    }

    public GroupInfo getGroupInfo() {
        return this.groupInfo;
    }


    @Override
    public void addMemberClicked(String newMemberName, String newMemberID, boolean newMemberRole, long newMemberAbsent) {

        /* Setup data to update Firebase */
        Map<String, Object> newMember = new HashMap<>();
        newMember.put(MyFireStore.MEMBER_NAME, newMemberName);
        newMember.put(MyFireStore.MEMBER_ID, newMemberID);
        newMember.put(MyFireStore.MEMBER_ROLE, newMemberRole);
        newMember.put(MyFireStore.MEMBER_ABSENT_COUNT, newMemberAbsent);

        /* Follow the format in Firebase */
        Map<String, Object> member = new HashMap<>();
        member.put(MyFireStore.MEMBER_NO + String.valueOf(getGroupInfo().getGroupMembers().size() + 1)
                , newMember);

        this.firebaseFirestore.collection(MyFireStore.GROUP_INFO_COLLECTION)
                .document(getGroupInfo().getGroupID())
                .update(member)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(getContext(), "Add new member successful",
                            Toast.LENGTH_SHORT).show();

                    /* Front-End phase */
                    RecyclerView memberRecyclerView = getView().findViewById(R.id.recyclerViewMember);
                    memberRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    memberRecyclerView.setHasFixedSize(true);

                    /* Set data back and  update for Adapter in RecyclerView */
                    getGroupInfo().getGroupMembers().add(newMemberName);
                    getGroupInfo().getGroupMembersID().add(newMemberID);
                    getGroupInfo().getGroupMembersRole().add(newMemberRole);
                    getGroupInfo().getGroupMembersAbsent().add(newMemberAbsent);
                    /* this because whole class implements 1 onItemClickListener */
                    MemberInfoAdapter groupAdapter = new MemberInfoAdapter(getContext(), getGroupInfo(), this::onItemClick);

                    memberRecyclerView.setAdapter(groupAdapter);
                    groupAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Add member Failure",
                        Toast.LENGTH_SHORT).show());
    }

    @Override
    public void editGroupClicked(String newGroupName, String newGroupTopic) {

        /* Setup data for firebase */
        Map<String, Object> data = new HashMap<>();
        data.put(MyFireStore.GROUP_NAME, newGroupName);
        data.put(MyFireStore.GROUP_TOPIC, newGroupTopic);

        this.firebaseFirestore.collection(MyFireStore.GROUP_INFO_COLLECTION)
                .document(groupInfo.getGroupID())
                .update(data)
                .addOnSuccessListener(unused -> {
                    /* Set changed information back */
                    getGroupInfo().setGroupName(newGroupName);
                    getGroupInfo().setGroupTopic(newGroupTopic);

                    /* Set Front-end */
                    groupName.setText(newGroupName);
                    groupTopic.setText(newGroupTopic);

                    Toast.makeText(getContext(), "Change group info successfully",
                            Toast.LENGTH_SHORT).show();

                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Change group info fail",
                                Toast.LENGTH_SHORT).show();

                    }
                });
    }

    @Override
    public void changePwdClicked(String newPassword) {
        Map<String, Object> data = new HashMap<>();
        data.put(MyFireStore.GROUP_PASSWORD, newPassword);

        this.firebaseFirestore.collection(MyFireStore.GROUP_INFO_COLLECTION)
                .document(getGroupInfo().getGroupID())
                .update(data)
                .addOnSuccessListener(unused -> {
                    /* Set changed information back */
                    getGroupInfo().setGroupPassword(newPassword);

                    Toast.makeText(getContext(), "Change password successfully",
                            Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Change password fail",
                        Toast.LENGTH_SHORT).show());
    }

    @Override
    public void editMemberClicked(boolean whichAction,
                                  int memberPosition,
                                  String newMemberName,
                                  String newMemberID,
                                  boolean newMemberRole,
                                  long newMemberAbsent) {
        if (whichAction == EditMemberDialog.EDIT_ACTION) {
            /* Setup data to update Firebase */
            Map<String, Object> memberEdited = new HashMap<>();
            memberEdited.put(MyFireStore.MEMBER_NAME, newMemberName);
            memberEdited.put(MyFireStore.MEMBER_ID, newMemberID);
            memberEdited.put(MyFireStore.MEMBER_ROLE, newMemberRole);
            memberEdited.put(MyFireStore.MEMBER_ABSENT_COUNT, newMemberAbsent);

            /* Follow the format in Firebase */
            Map<String, Object> member = new HashMap<>();
            member.put(MyFireStore.MEMBER_NO + String.valueOf(memberPosition + 1)
                    , memberEdited);

            this.firebaseFirestore.collection(MyFireStore.GROUP_INFO_COLLECTION)
                    .document(getGroupInfo().getGroupID())
                    .update(member)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(getContext(), "Change member info successful",
                                Toast.LENGTH_SHORT).show();

                        /* Front-End phase */
                        RecyclerView memberRecyclerView = getView().findViewById(R.id.recyclerViewMember);
                        memberRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        memberRecyclerView.setHasFixedSize(true);

                        /* Set data back and  update for Adapter in RecyclerView */
                        getGroupInfo().getGroupMembers().set(memberPosition, newMemberName);
                        getGroupInfo().getGroupMembersID().set(memberPosition, newMemberID);
                        getGroupInfo().getGroupMembersRole().set(memberPosition, newMemberRole);
                        getGroupInfo().getGroupMembersAbsent().set(memberPosition, newMemberAbsent);
                        /* this because whole class implements 1 onItemClickListener */
                        MemberInfoAdapter groupAdapter = new MemberInfoAdapter(getContext(), getGroupInfo(), this::onItemClick);

                        memberRecyclerView.setAdapter(groupAdapter);
                        groupAdapter.notifyDataSetChanged();
                    })
                    .addOnFailureListener(e -> Toast.makeText(getContext(), "Change member info fail",
                            Toast.LENGTH_SHORT).show());
        } else {
            getGroupInfo().getGroupMembers().remove(memberPosition);
            getGroupInfo().getGroupMembersID().remove(memberPosition);
            getGroupInfo().getGroupMembersRole().remove(memberPosition);
            getGroupInfo().getGroupMembersAbsent().remove(memberPosition);

            /* Ready data to push */
            Map<String, Object> groupData = new HashMap<>();
            for (int i = 0; i < getGroupInfo().getGroupMembers().size(); i++) {
                Map<String, Object> member = new HashMap<>();
                member.put(MyFireStore.MEMBER_NAME, getGroupInfo().getGroupMembers().get(i));
                member.put(MyFireStore.MEMBER_ID, getGroupInfo().getGroupMembersID().get(i));
                member.put(MyFireStore.MEMBER_ROLE, getGroupInfo().getGroupMembersRole().get(i));
                member.put(MyFireStore.MEMBER_ABSENT_COUNT, getGroupInfo().getGroupMembersAbsent().get(i));

                groupData.put(MyFireStore.MEMBER_NO + String.valueOf(i + 1), member);
            }

            groupData.put(MyFireStore.GROUP_NAME, getGroupInfo().getGroupName());
            groupData.put(MyFireStore.GROUP_TOPIC, getGroupInfo().getGroupTopic());
            groupData.put(MyFireStore.GROUP_PASSWORD, getGroupInfo().getGroupPassword());

            this.firebaseFirestore.collection(MyFireStore.GROUP_INFO_COLLECTION)
                    .document(getGroupInfo().getGroupID())
                    .set(groupData)
                    .addOnSuccessListener(unused -> {
                        Toast.makeText(getActivity(), "Delete member successfully",
                                Toast.LENGTH_SHORT).show();

                        /* Update FE */
                        RecyclerView memberRecyclerView = getView().findViewById(R.id.recyclerViewMember);
                        memberRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        memberRecyclerView.setHasFixedSize(true);

                        MemberInfoAdapter groupAdapter = new MemberInfoAdapter(getContext(), getGroupInfo(), this::onItemClick);
                        memberRecyclerView.setAdapter(groupAdapter);
                        groupAdapter.notifyDataSetChanged();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getActivity(), "Delete member fail",
                                Toast.LENGTH_SHORT).show();
                    });
        }
    }

    /* Click item to delete or edit Member */
    @Override
    public void onItemClick(int position) {
        /* Verify phase */
        AlertDialog.Builder verifyDialog = new AlertDialog.Builder(getActivity());
        verifyDialog.setTitle("VERIFY PERMISSION");

        final EditText input = new EditText(getActivity());
        input.setHint("Group Password");
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        verifyDialog.setView(input);

        verifyDialog.setPositiveButton("Verify", (vDialog, vWhich) -> {
                    String inputPwd = input.getText().toString();
                    if (inputPwd.equals(getGroupInfo().getGroupPassword())) {

                        EditMemberDialog editMemberDialog = new EditMemberDialog(this::editMemberClicked,
                                position,
                                getGroupInfo().getGroupMembers().get(position),
                                getGroupInfo().getGroupMembersID().get(position),
                                getGroupInfo().getGroupMembersRole().get(position),
                                getGroupInfo().getGroupMembersAbsent().get(position));
                        editMemberDialog.show(getActivity().getSupportFragmentManager(), "Edit Member Dialog");
                    } else {
                        Toast.makeText(getContext(), "Verify fail",
                                Toast.LENGTH_SHORT).show();
                        vDialog.dismiss();
                    }
                })

                .setNeutralButton("Cancel", (dialog, which) -> dialog.dismiss());
        verifyDialog.show();
    }

}
