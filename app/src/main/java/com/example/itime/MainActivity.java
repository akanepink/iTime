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
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    public static final int REQUEST_CODE_SHOW_EVENT = 902;
    public static final int RESULT_CODE_EDIT_OK = 703;
    public static final int RESULT_CODE_DELETE_OK = 704;
    public static final int RESULT_CODE_EDIT_SHOW_OK = 705;
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
        final ListView listViewEvents=(ListView)this.findViewById(R.id.list_view_event);
        listViewEvents.setAdapter(eventAdapter);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,EventEditActivity.class);
                Bundle bundle=new Bundle();
                bundle.putSerializable("event", null);//序列化
                intent.putExtras(bundle);//发送数据
                startActivityForResult(intent,REQUEST_CODE_NEW_EVENT);
            }
        });

        listViewEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //利用Bundle传递序列化的Event
                Intent intent=new Intent(MainActivity.this,EventShowActivity.class);
                Bundle bundle=new Bundle();
                bundle.putInt("position",position);
                bundle.putSerializable("event", listEvents.get(position));//序列化
                intent.putExtras(bundle);//发送数据
                startActivityForResult(intent, REQUEST_CODE_SHOW_EVENT);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_NEW_EVENT:
                if (resultCode == RESULT_OK) {
                    //需要修改
                    Event newEvent=(Event)data.getExtras().getSerializable("newEvent");
                    newEvent.setResourceId(R.drawable.backg_2_mini);
                    getListEvents().add(newEvent);
                    eventAdapter.notifyDataSetChanged();
                }
                break;
            case REQUEST_CODE_SHOW_EVENT:
                if(resultCode== RESULT_CODE_EDIT_SHOW_OK)
                {
                    //Toast.makeText(MainActivity.this,"ok",Toast.LENGTH_SHORT).show();//调试用
                    //需要修改
                    Event newEditEvent=(Event)data.getExtras().getSerializable("newEditEvent");
                    int position=data.getExtras().getInt("position",-1);
                    if(position>=0){
                        listEvents.get(position).setTitle(newEditEvent.getTitle());
                        listEvents.get(position).setMemo(newEditEvent.getMemo());
                        listEvents.get(position).setCalendar(newEditEvent.getCalendar());
                        eventAdapter.notifyDataSetChanged();

                    }
                }
                if(resultCode==RESULT_CODE_DELETE_OK)
                {
                    int deletePosition=data.getIntExtra("deletePosition",0);
                    Toast.makeText(MainActivity.this,"+++"+deletePosition,Toast.LENGTH_SHORT).show();
                    if(deletePosition>=0)
                    {
                        listEvents.remove(deletePosition);
                        eventAdapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this,"删除成功",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    private void init() {
        listEvents.add(new Event("我的生日","开心",R.drawable.backg_1_mini));
        listEvents.add(new Event("likey","memo",R.drawable.backg_2_mini));
        listEvents.add(new Event("numnum","owewdsdmo",R.drawable.backg_3_mini));
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
}
