package com.example.itime;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class EventShowActivity extends AppCompatActivity {

    public static final int RESULT_SHOW_OK = 701;
    public static final int REQUEST_CODE_EDIT_BOOK = 903;
    public static final int RESULT_CODE_EDIT_OK = 703;
    public static final int RESULT_CODE_DELETE_OK = 704;
    public static final int RESULT_CODE_EDIT_SHOW_OK = 705;
    private ImageButton buttonShowBack,buttonShowFull,buttonShowDelete;
    private ImageButton buttonShowShare,buttonShowEdit;
    private TextView textViewShowTitle,textViewShowDate,textViewShowCountdown;
    private Event event;
    private int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_show);
        buttonShowBack=(ImageButton)this.findViewById(R.id.image_button_show_back);
        buttonShowFull=(ImageButton)this.findViewById(R.id.image_button_show_full);
        buttonShowDelete=(ImageButton)this.findViewById(R.id.image_button_show_delete);
        buttonShowShare=(ImageButton)this.findViewById(R.id.image_button_show_share);
        buttonShowEdit=(ImageButton)this.findViewById(R.id.image_button_show_edit);
        textViewShowTitle=(TextView)this.findViewById(R.id.text_view_show_event_title);
        textViewShowDate=(TextView)this.findViewById(R.id.text_view_show_event_date);
        textViewShowCountdown=(TextView)this.findViewById(R.id.text_view_show_event_countdown);


        position=getIntent().getIntExtra("position",-1);
        //接收序列化的Event
        event=(Event)getIntent().getExtras().getSerializable("event");
        textViewShowTitle.setText(event.getTitle());
        textViewShowDate.setText(event.calendarToString());

        buttonShowBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventShowActivity.this.finish();
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
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("询问")
                        .setMessage("是否删除该计时")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //传回删除的position
                                Intent intent=new Intent();
                                intent.putExtra("deletePosition",deletePosition);
                                //Toast.makeText(EventShowActivity.this,"+++"+position,Toast.LENGTH_SHORT).show();
                                setResult(RESULT_CODE_DELETE_OK,intent);
                                //关闭该activity
                                EventShowActivity.this.finish();

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
                bundle.putInt("position",position);
                bundle.putSerializable("event", event);//序列化
                intent.putExtras(bundle);//发送数据
                startActivityForResult(intent, REQUEST_CODE_EDIT_BOOK);
                //EventShowActivity.this.finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case REQUEST_CODE_EDIT_BOOK:
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

                }
        }
    }
}
