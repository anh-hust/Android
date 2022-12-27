package com.example.midtermfinal.data;

import java.util.ArrayList;

public class GroupProgressItems {
    public static final boolean COMPLETED = true;
    public static final boolean NOT_COMPLETED = false;

    private String groupID;
    private String groupTopic;
    private ArrayList<String> itemListContent;
    private ArrayList<Boolean> itemListStatus;

    public GroupProgressItems() {
    }

    public String getGroupTopic() {
        return groupTopic;
    }

    public void setGroupTopic(String groupTopic) {
        this.groupTopic = groupTopic;
    }

    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    public ArrayList<String> getItemListContent() {
        return itemListContent;
    }

    public void setItemListContent(ArrayList<String> itemListContent) {
        this.itemListContent = itemListContent;
    }

    public ArrayList<Boolean> getItemListStatus() {
        return itemListStatus;
    }

    public void setItemListStatus(ArrayList<Boolean> itemListStatus) {
        this.itemListStatus = itemListStatus;
    }
}
