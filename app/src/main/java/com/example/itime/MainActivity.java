package com.example.itime;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.itime.data.Event;
import com.example.itime.data.EventSaver;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_NEW_EVENT = 901;
    public static final int REQUEST_CODE_SHOW_EVENT = 902;
    public static final int RESULT_CODE_EDIT_OK = 703;
    public static final int RESULT_CODE_DELETE_OK = 704;
    public static final int RESULT_CODE_EDIT_SHOW_OK = 705;
    private ViewPager viewPagerEvents;
    private FloatingActionButton  buttonAdd,buttonMenu;
    private List<Event> listEvents = new ArrayList<>();
    private EventAdapter eventAdapter;
    private Calendar nowSystemTime = Calendar.getInstance();
    EventSaver eventSaver;
    private int colorEdit=-1;
    private static int picNum=2;
    private DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        eventSaver.save();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //隐藏标题栏
        getSupportActionBar().hide();
        //状态栏透明
        if(Build.VERSION.SDK_INT >= 21){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    |View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        viewPagerEvents = this.findViewById(R.id.view_pager_event_show);
        buttonAdd = this.findViewById(R.id.floating_action_button_add);
        buttonMenu = this.findViewById(R.id.floating_action_button_menu);
        navigationView = this.findViewById(R.id.navigation_view);
        drawerLayout = this.findViewById(R.id.drawer_layout);
        navigationView.setItemIconTintList(null);

        eventSaver = new EventSaver(this);
        listEvents = eventSaver.load();
        if (listEvents.size() == 0) {
            init();
        }
        eventAdapter = new EventAdapter(MainActivity.this, R.layout.list_view_item_event, listEvents);
        final ListView listViewEvents = this.findViewById(R.id.list_view_event);
        listViewEvents.setAdapter(eventAdapter);

        buttonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击菜单，跳出侧滑菜单
                if (drawerLayout.isDrawerOpen(navigationView)) {
                    drawerLayout.closeDrawer(navigationView);
                } else {
                    drawerLayout.openDrawer(navigationView);
                }
            }

        });


        View headerView = navigationView.getHeaderView(0);
        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"此功能未完善",Toast.LENGTH_SHORT).show();
            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                //在这里处理item的点击事件
                if(item.getTitle().equals("主题色"))
                {
                    ColorPickerDialog colorPickerDialog=new ColorPickerDialog(MainActivity.this, "主题色", new ColorPickerDialog.OnColorChangedListener() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void colorChanged(int color) {
                            Toast.makeText(MainActivity.this,"ok",Toast.LENGTH_SHORT).show();
                            //按钮颜色
                            buttonAdd.getBackground().setColorFilter(color, PorterDuff.Mode.SRC);
                            colorEdit=color;
                        }
                    });
                    colorPickerDialog.show();
                }
                else {
                    Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_LONG).show();
                }
                //drawerLayout.closeDrawers();
                return true;
            }
        });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, EventEditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("colorBackg",colorEdit);
                bundle.putSerializable("event", null);//序列化
                intent.putExtras(bundle);//发送数据
                startActivityForResult(intent, REQUEST_CODE_NEW_EVENT);
            }
        });

        listViewEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //利用Bundle传递序列化的Event
                Intent intent = new Intent(MainActivity.this, EventShowActivity.class);
                Bundle bundle = new Bundle();

                bundle.putInt("colorBackg",colorEdit);
                bundle.putSerializable("nowTime", nowSystemTime);
                bundle.putInt("position", position);
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
                    String resourceBase="backg_";
                    String resStr=resourceBase+picNum+"_mini";
                    String backgStr=resourceBase+picNum;
                    int resId=getResources().getIdentifier(resStr,"drawable",getPackageName());
                    int backgId=getResources().getIdentifier(backgStr,"drawable",getPackageName());
                    picNum++;
                    if(picNum==4)
                        picNum=1;
                    newEvent.setResourceId(resId);
                    newEvent.setBackgId(backgId);

                    getListEvents().add(newEvent);
                    eventAdapter.notifyDataSetChanged();
                }
                break;
            case REQUEST_CODE_SHOW_EVENT:
                if(resultCode== RESULT_CODE_EDIT_SHOW_OK)
                {
                    Event newEditEvent=(Event)data.getExtras().getSerializable("newEditEvent");
                    int position=data.getExtras().getInt("position",-1);
                    if(position>=0){
                        listEvents.get(position).setTitle(newEditEvent.getTitle());
                        listEvents.get(position).setMemo(newEditEvent.getMemo());
                        listEvents.get(position).setCalendar(newEditEvent.getCalendar());
                        eventAdapter.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this,newEditEvent.timeToString(),Toast.LENGTH_SHORT).show();

                    }
                }
                if(resultCode==RESULT_CODE_DELETE_OK)
                {
                    int deletePosition=data.getIntExtra("deletePosition",0);
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
        Event initEvent=new Event("我的生日","开心",Calendar.getInstance());
        initEvent.setResourceId(R.drawable.backg_1_mini);
        initEvent.setBackgId(R.drawable.backg_1);
        listEvents.add(initEvent);

        /*
        listEvents.add(new Event("likey","memo",R.drawable.backg_2_mini));
        listEvents.add(new Event("numnum","owewdsdmo",R.drawable.backg_3_mini));
        */
    }

    public List<Event> getListEvents(){
        return listEvents;
    }

    public class EventAdapter extends ArrayAdapter<Event> {

        private int resourceId;
        private ImageView imageViewEventCover;

        public EventAdapter(Context context, int resource, List<Event> objects) {
            super(context, resource, objects);
            resourceId = resource;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Event event = getItem(position);//获取当前项的实例
            View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            imageViewEventCover=(view.findViewById(R.id.image_view_event_cover));
            imageViewEventCover.setImageResource(event.getResourceId());

            Bitmap bitmap = setTextToImg(getDateDiff(position),position);
            imageViewEventCover.setImageBitmap(bitmap);
            ((TextView) view.findViewById(R.id.text_view_event_title)).setText(event.getTitle());
            ((TextView) view.findViewById(R.id.text_view_event_date)).setText(event.calendarToString());
            ((TextView) view.findViewById(R.id.text_view_event_memo)).setText(event.getMemo());
            return view;
        }

        /**
         * 文字绘制在图片上，并返回bitmap对象
         */
        private Bitmap setTextToImg(String text,int position) {
            BitmapDrawable icon = (BitmapDrawable) getResources().getDrawable(EventAdapter.this.getItem(position).getResourceId());

            Bitmap bitmap = icon.getBitmap().copy(Bitmap.Config.ARGB_8888, true);
            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            // 抗锯齿
            paint.setAntiAlias(true);
            // 防抖动
            paint.setDither(true);
            paint.setTextSize(100);
            paint.setColor(Color.parseColor("#ffffff"));
            int x=text.length()*2-1;
            canvas.drawText(text, (bitmap.getWidth()/x), (bitmap.getHeight() / 2), paint);

            return bitmap;
        }

        private String getDateDiff(int position)
        {
            long cTime=this.getItem(position).getCalendar().getTimeInMillis()-nowSystemTime.getTimeInMillis();
            long sTime=cTime/1000;//时间差，单位：秒
            /*
            long mTime=sTime/60;
            long hTime=mTime/60;
            long dTime=hTime/24;
             */
            long dTime=cTime/(1000*60*60*24);
            if(sTime>0)
                return "还剩"+dTime +"天";
            else {
                dTime*=(-1);
                return "已经" + dTime + "天";
            }
        }
    }


}
