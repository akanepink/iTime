package com.example.itime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class EventEditActivity extends AppCompatActivity {
    private ImageButton buttonBack;
    private Button buttonOk;
    private EditText editTextTitle,editTextMemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_edit);

        buttonBack=(ImageButton)this.findViewById(R.id.image_button_back);
        buttonOk=(Button)this.findViewById(R.id.button_ok);
        editTextTitle=(EditText)this.findViewById(R.id.edit_text_title);
        editTextMemo=(EditText)this.findViewById(R.id.edit_text_memo);

        //点击ListView中的哪一个Event，就传递哪一个Event的信息
        editTextTitle.setText(getIntent().getStringExtra("title"));
        editTextMemo.setText(getIntent().getStringExtra("memo"));


        //确认按钮的响应事件
        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.putExtra("title",editTextTitle.getText().toString());
                intent.putExtra("memo",editTextMemo.getText().toString());
                setResult(RESULT_OK,intent);
                EventEditActivity.this.finish();
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
}
