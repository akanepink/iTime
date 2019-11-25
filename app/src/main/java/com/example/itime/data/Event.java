package com.example.itime.data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Event implements Serializable {

    private String title;
    private String memo;
    private Calendar calendar=Calendar.getInstance();
    private int resourceId;
    private int backgId;

    public Event(String title, String memo, Calendar calendar) {
        this.title = title;
        this.memo = memo;
        this.calendar = calendar;
    }
    public Event(String title, String memo, Calendar calendar, int resourceId,int backgId) {
        this.title = title;
        this.memo = memo;
        this.calendar = calendar;
        this.resourceId = resourceId;
        this.backgId=backgId;
    }
    public int getBackgId() {
        return backgId;
    }

    public void setBackgId(int backgId) {
        this.backgId = backgId;
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
            SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");// 设置你想要的格式
            String dateStr = df.format(calendar.getTime());
            return dateStr;
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }

    public String timeToString()  {
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
