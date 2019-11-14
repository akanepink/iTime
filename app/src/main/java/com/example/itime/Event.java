package com.example.itime;


import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;
import java.util.Calendar;

public class Event {

    private String title;
    private Calendar calendar=Calendar.getInstance();
    private int resourceId;

    public Event(String title, int resourceId) {
        this.title = title;
        this.resourceId=resourceId;


    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }
}
