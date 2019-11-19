package com.example.itime;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

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
   // private int year,month,day;
    public static final int RESULT_CODE_EDIT_OK = 703;
    private boolean dateChangeFlag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);

        buttonBack=(ImageButton)this.findViewById(R.id.image_button_back);
        buttonOk=(ImageButton)this.findViewById(R.id.button_ok);
        buttonDate=(Button)this.findViewById(R.id.button_choose_date);
        buttonRepeat=(Button)this.findViewById(R.id.button_choose_repeat);
        buttonPic=(Button)this.findViewById(R.id.button_choose_picture);
        buttonLabel=(Button)this.findViewById(R.id.button_choose_label);
        editTextTitle=(EditText)this.findViewById(R.id.edit_text_title);
        editTextMemo=(EditText)this.findViewById(R.id.edit_text_memo);


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
                    {receiveEvent.getCalendar().set(year,month,day);}
                    receiveEvent.setTitle(editTextTitle.getText().toString());
                    receiveEvent.setMemo(editTextMemo.getText().toString());

                    Intent newIntent = new Intent();
                    Bundle newBundle = new Bundle();
                    //newBundle.putInt("position",position);
                    newBundle.putSerializable("newEditEvent", receiveEvent);

                    newIntent.putExtras(newBundle);
                    setResult(RESULT_CODE_EDIT_OK, newIntent);
                    EventEditActivity.this.finish();
                }else {
                    Intent intent=new Intent();
                    Bundle newBundle=new Bundle();

                    Calendar newCalendar=Calendar.getInstance();
                    newCalendar.set(year,month,day);
                    Event newEvent=new Event(editTextTitle.getText().toString(),editTextMemo.getText().toString(),newCalendar);

                    newBundle.putSerializable("newEvent",newEvent);
                    intent.putExtras(newBundle);
                    setResult(RESULT_OK, intent);
                    EventEditActivity.this.finish();
                }
            }
        });


        //返回按钮的响应事件
        //总是返回到show的layout界面       如果是新增 则返回到主界面
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
                showTimePickDialog1();
                showDatePickDialog1();
                break;
            default:
                break;
        }
    }

    private void showDatePickDialog(){
        Calendar nowCalendar=Calendar.getInstance();
        new DatePickerDialog(EventEditActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Toast.makeText(EventEditActivity.this,"Year:" + year + " Month:" + month + " Day:" + dayOfMonth,Toast.LENGTH_SHORT).show();
            }
        }
                , nowCalendar.get(Calendar.YEAR)
                , nowCalendar.get(Calendar.MONTH)
                , nowCalendar.get(Calendar.DAY_OF_MONTH)).show();

    };

    private void showDatePickDialog1() {

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

            fragment.setArguments(bundle);
            fragment.show(getSupportFragmentManager(), CustomDatePickerDialogFragment.class.getSimpleName());
        }
        else{
            Bundle bundle = new Bundle();
            Calendar currentDate = receiveEvent.getCalendar();
            //Toast.makeText(EventEditActivity.this,currentDate.get(Calendar.YEAR)+":"+currentDate.MONTH+":"+currentDate.DAY_OF_MONTH,Toast.LENGTH_SHORT).show();
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

    private void showTimePickDialog1() {

        CustomTimePickerDialogFragment fragment = new CustomTimePickerDialogFragment();
        fragment.setOnSelectedTimeListener(this);//注册选择日期的监听器

        Bundle bundle = new Bundle();
        Calendar currentDate = Calendar.getInstance();
        currentDate.setTimeInMillis(System.currentTimeMillis());

        bundle.putSerializable(CustomTimePickerDialogFragment.CURRENT_DATE,currentDate);

        fragment.setArguments(bundle);
        fragment.show(getSupportFragmentManager(),CustomTimePickerDialogFragment.class.getSimpleName());
    }

    private void showTimePickDialog(){
        Calendar nowCalendar=Calendar.getInstance();
        new TimePickerDialog(EventEditActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Toast.makeText(EventEditActivity.this,"Hour:"+hourOfDay+"Minute:"+minute,Toast.LENGTH_SHORT).show();
            }
        },nowCalendar.get(Calendar.HOUR_OF_DAY),nowCalendar.get(Calendar.MINUTE),true).show();
    };

    @Override
    public void onSelectedDate(int year, int monthOfYear, int dayOfMonth) {

        this.year=year;
        month=monthOfYear;
        day=dayOfMonth;
        Toast.makeText(EventEditActivity.this,year+"年"+(monthOfYear+1)+"月"+dayOfMonth+"日",Toast.LENGTH_SHORT).show();
        //updateDate.set(this.year,month,day);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // 指定一个日期
        try {
            Date date = dateFormat.parse(String.valueOf(this.year) + "-" + String.valueOf(month) + "-" + String.valueOf(day));
            updateDate.setTime(date);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        // 对 calendar 设置为 date 所定的日期

    }


    @Override
    public void onSelectedTime(int hour, int minite) {

        this.hour=hour;
        this.minute=minite;

        Toast.makeText(EventEditActivity.this,hour+" : "+minute,Toast.LENGTH_SHORT).show();
        //updateDate.set(this.year,month,day);

        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:ss");
        // 指定一个日期
        try {
            Date date = dateFormat.parse(String.valueOf(this.hour) + ":" + String.valueOf(this.minute) );
            updateDate.setTime(date);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        // 对 calendar 设置为 date 所定的日期
    }
}
