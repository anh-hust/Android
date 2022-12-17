package com.example.midterm.data;

import android.content.Context;

import java.util.ArrayList;
import java.util.Collections;

public class GroupInfo {
    private String groupName;
    private String groupPassword;
    private String groupImage;
    private String groupTopic;
    private ArrayList<String> groupMembers = new ArrayList<>();
    private ArrayList<String> groupMembersID = new ArrayList<>();

    /**
     * @FUNCTION: Constructor corresponding with each Navigation
     */

    /* For Group Information */
    public GroupInfo(String groupName, String groupImage) {
        this.groupName = groupName;
        this.groupImage = groupImage;
    }

    /**
     * @FUNCTION constructor
     * */
    /* For Group Detail inside Group Information */
    public GroupInfo(String groupName, String groupImage, String groupTopic,
                     String[] groupMembers, String[] groupMembersID) {
        this.groupName = groupName;
        this.groupImage = groupImage;
        this.groupTopic = groupTopic;

        /* addAll replace for the for loop to add each member */
        Collections.addAll(this.groupMembers, groupMembers);

        Collections.addAll(this.groupMembersID, groupMembersID);
    }


    /* For Group Progress */
    public GroupInfo(String groupName, String groupPassword, String groupTopic) {
        this.groupName = groupName;
        this.groupPassword = groupPassword;
        this.groupTopic = groupTopic;
    }

    /**
     * @FUNCTION: Getter
     */
    public int getGroupImageId(Context context) {
        return context.getResources().getIdentifier(this.groupImage,
                "drawable", context.getPackageName());
    }

    public String getGroupPassword() {
        return groupPassword;
    }

    public String getGroupName() {
        return groupName;
    }

    public String getGroupTopic() {
        return groupTopic;
    }

    public ArrayList<String> getGroupMembers() {
        return this.groupMembers;
    }

    public ArrayList<String> getGroupMembersID() {
        return groupMembersID;
    }

    /**
     * @FUNCTION: Setter
     */

    public void setGroupPassword(String groupPassword) {
        this.groupPassword = groupPassword;
    }

    public void setGroupTopic(String groupTopic) {
        this.groupTopic = groupTopic;
    }


    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupImage() {
        return groupImage;
    }

    public void setGroupImage(String groupImage) {
        this.groupImage = groupImage;
    }


    public void setGroupMembers(ArrayList<String> groupMembers) {
        this.groupMembers = groupMembers;
    }


    public void setGroupMembersID(ArrayList<String> groupMembersID) {
        this.groupMembersID = groupMembersID;
    }
}

