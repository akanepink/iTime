package com.example.itime;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_NEW_EVENT = 901;
    private ViewPager viewPagerEvents;
    private FloatingActionButton buttonAdd;
    private List<Event> listEvents= new ArrayList<>();
    private EventAdapter eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPagerEvents=(ViewPager)this.findViewById(R.id.view_pager_event_show);
        buttonAdd=(FloatingActionButton)this.findViewById(R.id.floating_action_button_add);

        init();
        eventAdapter=new EventAdapter(MainActivity.this,R.layout.list_view_item_event,listEvents);
        ListView listViewEvents=(ListView)this.findViewById(R.id.list_view_event);
        listViewEvents.setAdapter(eventAdapter);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,EventEditActivity.class);
                startActivityForResult(intent,REQUEST_CODE_NEW_EVENT);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_NEW_EVENT:
                if (resultCode == RESULT_OK) {
                    String title = data.getStringExtra("title");
                    String memo = data.getStringExtra("memo");
                    int year=data.getIntExtra("year",0);
                    int month=data.getIntExtra("month",0)+1;
                    int day=data.getIntExtra("day",0);
                    Calendar calendar=Calendar.getInstance();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date date = dateFormat.parse(String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day));
                        calendar.setTime(date);
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    getListEvents().add(new Event(title,memo,calendar, R.drawable.backg_2_mini));
                    eventAdapter.notifyDataSetChanged();
                }
                break;

        }
    }
    private void init() {
        listEvents.add(new Event("我的生日","开心",R.drawable.backg_1_mini));
    }
    public List<Event> getListEvents(){
        return listEvents;
    }
    public class EventAdapter extends ArrayAdapter<Event> {

        private int resourceId;

        public EventAdapter(Context context, int resource, List<Event> objects) {
            super(context, resource, objects);
            resourceId = resource;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Event event = getItem(position);//获取当前项的实例
            View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            ((ImageView) view.findViewById(R.id.image_view_event_cover)).setImageResource(event.getResourceId());
            ((TextView) view.findViewById(R.id.text_view_event_title)).setText(event.getTitle());
            ((TextView) view.findViewById(R.id.text_view_event_date)).setText(event.calendarToString());
            ((TextView) view.findViewById(R.id.text_view_event_memo)).setText(event.getMemo());
            return view;
        }
    }


    /*
    public static Bitmap CenterSquareScaleBitmaap(Bitmap bitmap, int edgeLength)
    {
        if(null==bitmap||0==edgeLength)
        {
            return null;
        }
        Bitmap result=bitmap;
        int widthOrg=bitmap.getWidth();
        int heightOrg=bitmap.getHeight();
        if(widthOrg>edgeLength&&heightOrg>edgeLength)
        {
            int longerEdge=(int)(edgeLength*Math.max(widthOrg,heightOrg)/Math.min(widthOrg,heightOrg));
            int scaledWidth=widthOrg>heightOrg?longerEdge:edgeLength;
            int scaledHeight=widthOrg>heightOrg?edgeLength:longerEdge;
            Bitmap scaledBitmap;
            try{
                scaledBitmap=Bitmap.createScaledBitmap(bitmap,scaledWidth,scaledHeight,true);
            }catch (Exception e){
                return null;
            }
            int xTopLeft=(scaledWidth-edgeLength)/2;
            int yTopLeft=(scaledHeight-edgeLength)/2;
            try{
                result=Bitmap.createBitmap(scaledBitmap,xTopLeft,yTopLeft,edgeLength,edgeLength);
                scaledBitmap.recycle();
            }catch (Exception e) {
                return null;
            }
        }
        return result;
    }
     */

}
