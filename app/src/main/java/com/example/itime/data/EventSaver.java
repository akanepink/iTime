package com.example.itime.data;

import android.content.Context;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class EventSaver {
    private Context context;
    private ArrayList<Event> events=new ArrayList<>();

    public EventSaver(Context context) {
        this.context = context;
    }

    public ArrayList<Event> getBooks() {
        return events;
    }

    public void save(){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(context.openFileOutput("Serializable.txt", Context.MODE_PRIVATE));
            outputStream.writeObject(events);
            outputStream.close();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public ArrayList<Event> load(){
        try{
            ObjectInputStream inputStream = new ObjectInputStream(context.openFileInput("Serializable.txt"));
            events=(ArrayList<Event>) inputStream.readObject();
            inputStream.close();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return events;
    }
}
