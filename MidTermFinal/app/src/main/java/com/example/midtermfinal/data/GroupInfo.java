package com.example.midtermfinal.data;

import java.util.ArrayList;

public class GroupInfo {
    public static final boolean GROUP_LEADER = true;
    public static final boolean GROUP_MEMBER = true;
    private String groupID;
    private String groupName;
    private String groupPassword;
    private String groupTopic;

    /* About members */
    private ArrayList<String> groupMembers = new ArrayList<>();
    private ArrayList<String> groupMembersID = new ArrayList<>();
    private ArrayList<Long> groupMembersAbsent = new ArrayList<>();
    private ArrayList<Boolean> groupMembersRole = new ArrayList<>();

    public GroupInfo() {
    }

    /**
     * TODO Setter
     */
    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setGroupPassword(String groupPassword) {
        this.groupPassword = groupPassword;
    }

    public void setGroupTopic(String groupTopic) {
        this.groupTopic = groupTopic;
    }

    public void setGroupMembers(ArrayList<String> groupMembers) {
        this.groupMembers = groupMembers;
    }

    public void setGroupMembersID(ArrayList<String> groupMembersID) {
        this.groupMembersID = groupMembersID;
    }

    public void setGroupMembersAbsent(ArrayList<Long> groupMembersAbsent) {
        this.groupMembersAbsent = groupMembersAbsent;
    }

    public void setGroupMembersRole(ArrayList<Boolean> groupMembersRole) {
        this.groupMembersRole = groupMembersRole;
    }

    /**
     * TODO Getter
     */
    public String getGroupID() {
        return groupID;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupPassword() {
        return groupPassword;
    }

    public String getGroupTopic() {
        return groupTopic;
    }

    public ArrayList<String> getGroupMembers() {
        return groupMembers;
    }

    public ArrayList<String> getGroupMembersID() {
        return groupMembersID;
    }

    public ArrayList<Long> getGroupMembersAbsent() {
        return groupMembersAbsent;
    }

    public ArrayList<Boolean> getGroupMembersRole() {
        return groupMembersRole;
    }
}

