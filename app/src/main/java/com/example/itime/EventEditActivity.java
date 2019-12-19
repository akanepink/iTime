package com.example.itime;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.itime.data.Event;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EventEditActivity extends AppCompatActivity implements View.OnClickListener, CustomDatePickerDialogFragment.OnSelectedDateListener, CustomTimePickerDialogFragment.OnSelectedTimeListener {
    private ImageButton buttonBack,buttonOk;
    private Button buttonDate,buttonRepeat,buttonPic,buttonLabel;
    private EditText editTextTitle,editTextMemo;
    Calendar updateDate;
    int year,month,day;
    int hour,minute;
    private Event receiveEvent=null;
    public static final int RESULT_CODE_EDIT_OK = 703;
    private boolean dateChangeFlag=false;
    private int colorBackg=-1;
    private ConstraintLayout editLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);
        //隐藏标题栏
        getSupportActionBar().hide();
        //状态栏半透明
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        buttonBack=this.findViewById(R.id.image_button_back);
        buttonOk=this.findViewById(R.id.button_ok);
        buttonDate=this.findViewById(R.id.button_choose_date);
        buttonRepeat=this.findViewById(R.id.button_choose_repeat);
        buttonPic=this.findViewById(R.id.button_choose_picture);
        buttonLabel=this.findViewById(R.id.button_choose_label);
        editTextTitle=this.findViewById(R.id.edit_text_title);
        editTextMemo=this.findViewById(R.id.edit_text_memo);

        editLayout=this.findViewById(R.id.constraint_edit_layout);
        colorBackg=getIntent().getExtras().getInt("colorBackg");
        if(colorBackg!=-1)
            editLayout.setBackgroundColor(colorBackg);

        final int position=getIntent().getExtras().getInt("position",-1);
        receiveEvent=(Event)getIntent().getExtras().getSerializable("event");
        if(receiveEvent!=null) {
            //点击ListView中的哪一个Event，就传递哪一个Event的信息
            editTextTitle.setText(receiveEvent.getTitle());
            editTextMemo.setText(receiveEvent.getMemo());
            buttonDate.setText("日期\n"+receiveEvent.calendarToString());
        }

        buttonDate.setOnClickListener(this);

        //确认按钮的响应事件
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(receiveEvent!=null) {
                    if(dateChangeFlag==true)
                    {receiveEvent.getCalendar().set(year,month,day,hour,minute);}
                    receiveEvent.setTitle(editTextTitle.getText().toString());
                    receiveEvent.setMemo(editTextMemo.getText().toString());

                    Intent newIntent = new Intent();
                    Bundle newBundle = new Bundle();
                    newBundle.putSerializable("newEditEvent", receiveEvent);

                    newIntent.putExtras(newBundle);
                    setResult(RESULT_CODE_EDIT_OK, newIntent);
                    EventEditActivity.this.finish();
                }else {
                    Intent intent=new Intent();
                    Bundle newBundle=new Bundle();

                    Calendar newCalendar=Calendar.getInstance();
                    newCalendar.set(year,month,day,hour,minute);
                    Event newEvent=new Event(editTextTitle.getText().toString(),editTextMemo.getText().toString(),newCalendar);

                    newBundle.putSerializable("newEvent",newEvent);
                    intent.putExtras(newBundle);
                    setResult(RESULT_OK, intent);
                    EventEditActivity.this.finish();
                }
            }
        });

        //返回按钮的响应事件
        //如果是新增 则返回到主界面
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventEditActivity.this.finish();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_choose_date:
                showTimePickDialog();
                showDatePickDialog();
                break;
            default:
                break;
        }
    }

    private void showDatePickDialog() {
        CustomDatePickerDialogFragment fragment = new CustomDatePickerDialogFragment();
        fragment.setOnSelectedDateListener(this);//注册选择日期的监听器
        if(receiveEvent==null) {
            Bundle bundle = new Bundle();
            Calendar currentDate = Calendar.getInstance();
            currentDate.setTimeInMillis(System.currentTimeMillis());
            currentDate.set(Calendar.HOUR_OF_DAY, 0);
            currentDate.set(Calendar.MINUTE, 0);
            currentDate.set(Calendar.SECOND, 0);
            currentDate.set(Calendar.MILLISECOND, 0);
            bundle.putSerializable(CustomDatePickerDialogFragment.CURRENT_DATE, currentDate);
            bundle.putInt("color",colorBackg);

            fragment.setArguments(bundle);
            fragment.show(getSupportFragmentManager(), CustomDatePickerDialogFragment.class.getSimpleName());
        }
        else{
            Bundle bundle = new Bundle();
            Calendar currentDate = receiveEvent.getCalendar();
            currentDate.setTimeInMillis(System.currentTimeMillis());
            currentDate.set(Calendar.HOUR_OF_DAY, 0);
            currentDate.set(Calendar.MINUTE, 0);
            currentDate.set(Calendar.SECOND, 0);
            currentDate.set(Calendar.MILLISECOND, 0);
            bundle.putSerializable(CustomDatePickerDialogFragment.CURRENT_DATE, currentDate);

            fragment.setArguments(bundle);
            fragment.show(getSupportFragmentManager(), CustomDatePickerDialogFragment.class.getSimpleName());
        }
        dateChangeFlag=true;
    }

    private void showTimePickDialog() {

        CustomTimePickerDialogFragment fragment = new CustomTimePickerDialogFragment();
        fragment.setOnSelectedTimeListener(this);//注册选择日期的监听器

        Bundle bundle = new Bundle();
        Calendar currentDate = Calendar.getInstance();
        currentDate.setTimeInMillis(System.currentTimeMillis());

        bundle.putSerializable(CustomTimePickerDialogFragment.CURRENT_DATE,currentDate);

        fragment.setArguments(bundle);
        fragment.show(getSupportFragmentManager(),CustomTimePickerDialogFragment.class.getSimpleName());
    }


    @Override
    public void onSelectedDate(int year, int monthOfYear, int dayOfMonth) {

        this.year=year;
        month=monthOfYear;
        day=dayOfMonth;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // 指定一个日期
        try {
            Date date = dateFormat.parse(this.year + "-" + month+ "-" + day);
            updateDate.setTime(date);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        buttonDate.setText("日期\n"+this.year+"年"+(month+1)+"月"+day+"日");
    }


    @Override
    public void onSelectedTime(int hour, int minite) {

        this.hour=hour;
        this.minute=minite;

        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:ss");
        // 指定一个日期
        try {
            Date date = dateFormat.parse(this.hour + ":" + this.minute );
            updateDate.setTime(date);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
