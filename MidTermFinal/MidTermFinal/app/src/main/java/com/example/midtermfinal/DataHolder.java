package com.example.midtermfinal;

import com.example.midtermfinal.data.GroupInfo;
import com.example.midtermfinal.data.GroupProgressItems;

import java.util.ArrayList;

public class DataHolder {
    private static DataHolder instance;
    private ArrayList<GroupInfo> groupInfoArrayList = new ArrayList<>();
    private ArrayList<GroupProgressItems> groupProgressItems = new ArrayList<>();

    private DataHolder() {
    }

    /**
     * TODO Setter
     */
    public void setGroupInfoArrayList(ArrayList<GroupInfo> groupInfoArrayList) {
        this.groupInfoArrayList = groupInfoArrayList;
    }

    public void setGroupProgressItems(ArrayList<GroupProgressItems> groupProgressItems) {
        this.groupProgressItems = groupProgressItems;
    }

    /**
     * TODO Getter
     */
    public ArrayList<GroupProgressItems> getGroupProgressItems() {
        return this.groupProgressItems;
    }

    public ArrayList<GroupInfo> getGroupInfoArrayList() {
        return this.groupInfoArrayList;
    }

    public static DataHolder getInstance() {
        if (instance == null)
            instance = new DataHolder();
        return instance;
    }
}
