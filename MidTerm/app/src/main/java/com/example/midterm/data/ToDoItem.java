package com.example.midterm.data;

public class ToDoItem {

    private String content;
    private boolean completed;
    public static boolean COMPLETED = true;
    public static boolean NOT_COMPLETED = false;
    public static String CONTENT = "content";
    public static String CONTENT_STATUS = "completed";

    public ToDoItem(String itemContent, boolean completed) {
        this.content = itemContent;
        this.completed = completed;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getItemContent() {
        return content;
    }

    public void setItemContent(String itemContent) {
        this.content = itemContent;
    }
}
