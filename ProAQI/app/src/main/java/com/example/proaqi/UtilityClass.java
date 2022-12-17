package com.example.proaqi;

import java.util.ArrayList;

public class UtilityClass {
    private static UtilityClass instance;

    private ArrayList<Object> list;

    public ArrayList<Object> getList() {
        return list;
    }

    public void setList(ArrayList<Object> list) {
        this.list = list;
    }

    private UtilityClass(){}

    public static UtilityClass getInstance(){
        if(instance == null){
            instance = new UtilityClass();
        }
        return instance;
    }
}
