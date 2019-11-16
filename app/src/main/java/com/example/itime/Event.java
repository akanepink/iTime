package com.example.itime;


import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Event {

    private String title;
    private String memo;
    private Calendar calendar=Calendar.getInstance();
    private int resourceId;



    public Event(String title, String memo, int resourceId) {
        this.title = title;
        this.memo = memo;
        this.resourceId = resourceId;
    }

    public Event(String title, String memo, Calendar calendar, int resourceId) {
        this.title = title;
        this.memo = memo;
        this.calendar = calendar;
        this.resourceId = resourceId;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
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

    public String calendarToString() {
        try {
            if (calendar == null) {
                return "";
            }
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");// 设置你想要的格式
            String dateStr = df.format(calendar.getTime());
            return dateStr;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public String timeToString() throws Exception {
        try {
            if (calendar == null) {
                return "";
            }
            SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss");// 设置你想要的格式
            String dateStr = df.format(calendar.getTime());
            return dateStr;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";

    }

}
