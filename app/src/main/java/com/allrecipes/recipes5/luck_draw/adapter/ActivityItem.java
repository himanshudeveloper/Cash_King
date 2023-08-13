package com.allrecipes.recipes5.luck_draw.adapter;

public class ActivityItem {
    private int id;
    private String title, message, date;
    public ActivityItem (String title, String message, String date) {
        this.title = title;
        this.date = date;
        this.message = message;

    }
    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }




}