package com.example.midterm.data;

import java.util.ArrayList;

/**
 * TODO: All data about group here. Wanna add more data, no problem
 * */

public class GroupData {
    private String[] groupNamePool = {"Group 1", "Group 2", "Group 3", "Group 4",
            "Group 5", "Group 6", "Group 7", "Group 8", "Group 9", "Group 10"};

    private String[] groupPasswordPool = {"1", "2", "3", "4", "5",
            "6", "7", "8", "9", "10"};

    private String[] groupTopicPool = {"AQI DashBoard", "SmartHome App", "Music App",
            "Food Order App", "Hiring Bicycle App", "Food Order App", "Unknown", "Story App",
            "Business Management App", "Cinema Book App"};

    private String[][] groupMemberPool = {
            {"Bui Tuan Anh - Cap", "Le Dang Khoa", "Nguyen Quang Minh"},
            {"Vo Kieu Trinh", "Ngo Quang Hieu - Cap"},
            {"Nguyen Van Tuan", "Dong Van Duy - Cap", "Pham Thi Thanh Huyen"},
            {"Bui Hong Viet - Cap", "Nguyen Tuan Anh", "Duong Duc Long Vu"},
            {"Tran Van Phu", "Nguyen Quoc Tuan - Cap", "Nguyen Manh Cuong"},
            {"Dinh Huu Duc Chien - Cap", "Nguyen Van Quyen"},
            {"Phan Tien Bao Duy", "Bui Van Dat - Cap", "Vu Thi Thanh"},
            {"Le Huy Hoang - Cap", "Vu Ba Hung", "Bui Truong An"},
            {"Nguyen Pham Nhat Long", "Nguyen Duy Anh Tuan"},
            {"Nguyen Viet Hoang - Cap", "Nguyen Van Tinh", "Phan Anh Dung"}};

    private String[][] groupMemberIDPool = {
            {"20182328", "20182609", "20182686"},
            {"20182833", "20182514"},
            {"20182865", "20182461", "20182593"},
            {"20182882", "20182353", "20182893"},
            {"20182722", "20182864", "20182401"},
            {"20182508", "20182749"},
            {"20182466", "20182410", "20182786"},
            {"20182801", "20182567", "20182324"},
            {"20172672", "20172888"},
            {"20192874", "20193135", "20182445"}};


    /**
     * TODO return an ArrayList contains information about Group Name and corresponding image file name
     * TODO Data return all 're fetched from data pool above
     * */
    public ArrayList<GroupInfo> groupIntroArrayList() {
        ArrayList<GroupInfo> groupInfoList = new ArrayList<>();

        for (int i = 0; i < groupNamePool.length; i++) {
            GroupInfo groupIntro = new GroupInfo(groupNamePool[i],
                    groupNamePool[i].replace(" ", "").toLowerCase());
            /* replace: Student 1 --> student1 as name of image in drawable */

            groupInfoList.add(groupIntro);
        }

        return groupInfoList;
    }

    /**
     * TODO return an ArrayList contains information about Group Name, Group Password and Group Topic
     * */
    public ArrayList<GroupInfo> groupProgressArrayList() {
        ArrayList<GroupInfo> groupProgressList = new ArrayList<>();

        for (int i = 0; i < groupNamePool.length; i++) {
            GroupInfo groupProgress = new GroupInfo(groupNamePool[i],
                    groupPasswordPool[i], groupTopicPool[i]);

            groupProgressList.add(groupProgress);
        }

        return groupProgressList;
    }

    /**
     * TODO return an ArrayList contains information which served Group Detail
     * */
    public ArrayList<GroupInfo> groupDetailArrayList() {
        ArrayList<GroupInfo> groupDetailArrayList = new ArrayList<>();

        for (int i = 0; i < groupNamePool.length; i++) {
            GroupInfo groupDetail = new GroupInfo(groupNamePool[i],
                    groupNamePool[i].replace(" ", "").toLowerCase(),
                    groupTopicPool[i], groupMemberPool[i], groupMemberIDPool[i]);

            groupDetailArrayList.add(groupDetail);
        }

        return groupDetailArrayList;
    }
}

