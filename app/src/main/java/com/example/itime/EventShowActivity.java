package com.example.itime;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.itime.data.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;

public class EventShowActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_EDIT_EVENT = 903;
    public static final int RESULT_CODE_EDIT_OK = 703;
    public static final int RESULT_CODE_DELETE_OK = 704;
    public static final int RESULT_CODE_EDIT_SHOW_OK = 705;
    private FloatingActionButton buttonShowBack,buttonShowFull,buttonShowDelete;
    private FloatingActionButton buttonShowShare,buttonShowEdit;
    private TextView textViewShowTitle,textViewShowDate,textViewShowCountdown;
    private Event eventShow;
    private Calendar nowSystemTime;
    private int position;
    private int colorEdit;
    private ConstraintLayout constraintShowLayout;
    String []arr = {"周日","周一","周二","周三","周四","周五","周六"};


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_show);
        //隐藏标题栏
        getSupportActionBar().hide();
        //状态栏半透明
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        buttonShowBack=this.findViewById(R.id.floating_action_button_back);
        buttonShowFull=this.findViewById(R.id.floating_action_button_full);
        buttonShowDelete=this.findViewById(R.id.floating_action_button_delete);
        buttonShowShare=this.findViewById(R.id.floating_action_button_share);
        buttonShowEdit=this.findViewById(R.id.floating_action_button_edit);
        textViewShowTitle=this.findViewById(R.id.text_view_show_event_title);
        textViewShowDate=this.findViewById(R.id.text_view_show_event_date);
        textViewShowCountdown=this.findViewById(R.id.text_view_show_event_countdown);
        constraintShowLayout=this.findViewById(R.id.constraint_show_layout);

        colorEdit=getIntent().getExtras().getInt("colorBackg");
        nowSystemTime=(Calendar)getIntent().getExtras().getSerializable("nowTime");
        position=getIntent().getIntExtra("position",-1);
        //接收序列化的Event
        eventShow=(Event)getIntent().getExtras().getSerializable("event");
        textViewShowTitle.setText(eventShow.getTitle());
        textViewShowDate.setText(eventShow.calendarToString()+" "
                +eventShow.timeToString()+" "
                +arr[eventShow.getCalendar().get(Calendar.DAY_OF_WEEK)-1]);
        textViewShowCountdown.setText(getTimeDiff());
        constraintShowLayout.setBackground(getResources().getDrawable(eventShow.getBackgId()));

        //开始
        handler.sendEmptyMessage(1);

        buttonShowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventShowActivity.this.finish();
                //停止
                handler.removeCallbacksAndMessages(null);
            }
        });

        buttonShowFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EventShowActivity.this,"全屏模式未完善",Toast.LENGTH_SHORT).show();
            }
        });

        buttonShowShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EventShowActivity.this,"分享功能未完善",Toast.LENGTH_SHORT).show();
            }
        });

        buttonShowDelete.setOnClickListener(new View.OnClickListener() {
            //弹出对话框询问是否删除
            @Override
            public void onClick(View v) {
                final int deletePosition=position;
                new android.app.AlertDialog.Builder(EventShowActivity.this)
                        .setMessage("是否删除该计时")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //传回删除的position
                                Intent intent=new Intent();
                                intent.putExtra("deletePosition",deletePosition);
                                setResult(RESULT_CODE_DELETE_OK,intent);
                                //关闭该activity
                                EventShowActivity.this.finish();
                                //停止
                                handler.removeCallbacksAndMessages(null);
                            }})
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }})
                        .create().show();
        }
        });

        buttonShowEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EventShowActivity.this,EventEditActivity.class);
                Bundle bundle=new Bundle();
                bundle.putInt("colorBackg",colorEdit);
                bundle.putInt("position",position);
                bundle.putSerializable("event", eventShow);//序列化
                intent.putExtras(bundle);//发送数据
                startActivityForResult(intent, REQUEST_CODE_EDIT_EVENT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case REQUEST_CODE_EDIT_EVENT:
                if(resultCode==RESULT_CODE_EDIT_OK)
                {
                    Event newEditEvent=(Event)data.getExtras().getSerializable("newEditEvent");
                    Intent newIntent=new Intent();
                    Bundle newBundle=new Bundle();
                    newBundle.putInt("position",position);
                    newBundle.putSerializable("newEditEvent",newEditEvent);
                    newIntent.putExtras(newBundle);
                    setResult( RESULT_CODE_EDIT_SHOW_OK,newIntent);
                    //关闭该activity
                    EventShowActivity.this.finish();
                    //停止
                    handler.removeCallbacksAndMessages(null);
                }
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
               textViewShowCountdown.setText(getTimeDiff());
                sendEmptyMessageDelayed(1,1000);
            }
        }
    };

    private String getTimeDiff()
    {
        String outputStr="";
        long cTime=eventShow.getCalendar().getTimeInMillis()-Calendar.getInstance().getTimeInMillis();
        long sTime=cTime/1000;//时间差，单位：秒
        long mTime=sTime/60;
        long hTime=mTime/60;
        long dTime=hTime/24;
        if(sTime>=0)
        {
            if(dTime!=0)
                outputStr+=dTime+"天  ";
            if((hTime%24)!=0)
                outputStr+=hTime%24 +"小时  ";
            if((mTime%60)!=0)
                outputStr+= mTime%60 +"分钟  ";
            if((sTime%60)!=0)
                outputStr+=sTime%60 +"秒";
        }

        else {
            if(dTime!=0)
            {
                dTime*=(-1);
                outputStr+=dTime+"天  ";
            }
            if((hTime%24)!=0)
            {
                hTime*=(-1);
                outputStr+=hTime%24 +"小时  ";
            }

            if((mTime%60)!=0)
            {
                mTime*=(-1);
                outputStr+= mTime%60 +"分钟  ";
            }

            if((sTime%60)!=0)
            {
                sTime*=(-1);
                outputStr+=sTime%60 +"秒";
            }

        }
        return outputStr;
    }
}
