package com.example.midtermfinal.firestore;

import android.util.Log;

import com.example.midtermfinal.data.GroupInfo;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

/**
 * @NOTE TODO Just a class store Macros and "sample code" for FireBase manipulate, because Firebase retrieve is asynchronous
 */
public class MyFireStore {
    /* Macros for collection GROUP_INFO */
    public final static String GROUP_INFO_COLLECTION = "GROUP_INFO";
    public final static String GROUP_NO_DOCUMENT = "Group_";
    public final static String GROUP_NAME = "Group_name";
    public final static String GROUP_PASSWORD = "Group_password";
    public final static String GROUP_TOPIC = "Group_topic";

    public final static String MEMBER_NO = "Member_";
    public final static String MEMBER_ABSENT_COUNT = "absent";
    public final static String MEMBER_ID = "id";
    public final static String MEMBER_NAME = "name";
    public final static String MEMBER_ROLE = "isLeader";

    /* Macros for collection PROGRESS_PROJECT */
    public final static String PROGRESS_COLLECTION = "PROGRESS_PROJECT";
    public final static String ITEM_NO = "Item_";
    public final static String ITEM_CONTENT = "content";
    public final static String ITEM_STATUS = "completed";



    /* Get Group Info */
//    public GroupInfo getGroupInfo(int group_no) {
//        CollectionReference collectionReference = firebaseFirestore.collection(GROUP_INFO_COLLECTION);
//        GroupInfo groupInfo = new GroupInfo();
//
//        collectionReference.document(GROUP_NO_DOCUMENT + group_no)
//                .get()
//                .addOnSuccessListener(documentSnapshot -> {
//                    Map<String, Object> groupData = documentSnapshot.getData();
//                    Log.d("FireStore Class", "getGroupInfo" +
//                            " Group " + group_no + "\n" + groupData);
//
//                    /* Assign data from Firebase to class GroupInfo */
//                    assert groupData != null;
//                    groupInfo.setGroupName(Objects.requireNonNull(groupData.get(GROUP_NAME)).toString());
//                    groupData.remove(GROUP_NAME);
//                    groupInfo.setGroupPassword(Objects.requireNonNull(groupData.get(GROUP_PASSWORD)).toString());
//                    groupData.remove(GROUP_PASSWORD);
//                    groupInfo.setGroupTopic(Objects.requireNonNull(groupData.get(GROUP_TOPIC)).toString());
//                    groupData.remove(GROUP_TOPIC);
//
//                    /* Assign for member info in Group */
//                    int i;
//                    Map<String, Object> memberInfo;
//                    ArrayList<String> members = new ArrayList<>();
//                    ArrayList<String> membersID = new ArrayList<>();
//                    ArrayList<Long> membersAbsent = new ArrayList<>();
//                    for (i = 0; i < groupData.size(); i++) {
//                        memberInfo = (Map<String, Object>) groupData.get(MEMBER_NO + (i + 1));
//                        assert memberInfo != null;
//                        members.add(Objects.requireNonNull(memberInfo.get(MEMBER_NAME)).toString());
//                        membersID.add(Objects.requireNonNull(memberInfo.get(MEMBER_ID)).toString());
//                        membersAbsent.add(((Number) Objects.requireNonNull(memberInfo.get(MEMBER_ABSENT_COUNT))).longValue());
//                    }
//                    groupInfo.setGroupMembers(members);
//                    groupInfo.setGroupMembersID(membersID);
//                    groupInfo.setGroupMembersAbsent(membersAbsent);
//
//                })
//                .addOnFailureListener(e -> Log.d("FireStore Class", "Error getGroupInfo:\n" + e));
//        return groupInfo;
//    }

//    public ToDoItemList getToDoItemList(int group_no) {
//        CollectionReference collectionReference = firebaseFirestore.collection(PROGRESS_COLLECTION);
//        ToDoItemList toDoItemList = new ToDoItemList();
//
//        collectionReference.document(GROUP_NO_DOCUMENT + group_no)
//                .get()
//                .addOnSuccessListener(documentSnapshot -> {
//                    Map<String, Object> toDoItemData = documentSnapshot.getData();
//
//                    assert toDoItemData != null;
//
//                    /* pass each item */
//                    int i;
//                    Map<String, Object> item;
//                    ArrayList<String> contentList = new ArrayList<>();
//                    ArrayList<Boolean> contentStatusList = new ArrayList<>();
//                    for (i = 0; i < toDoItemData.size(); i++) {
//                        item = (Map<String, Object>) toDoItemData.get(ITEM_NO + (i + 1));
//                        assert item != null;
//
//                        contentList.add(Objects.requireNonNull(item.get(ITEM_CONTENT)).toString());
//                        contentStatusList.add((Boolean) (item.get(ITEM_STATUS)));
//                    }
//                    toDoItemList.setContent(contentList);
//                    toDoItemList.setContentStatus(contentStatusList);
//                })
//                .addOnFailureListener(e -> Log.d("FireStore Class", "Error getToDo\n" + e));
//        return toDoItemList;
//    }

}
