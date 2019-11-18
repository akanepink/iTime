package com.example.itime;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class EventShowActivity extends AppCompatActivity {

    private ImageButton buttonShowBack,buttonShowFull,buttonShowDelete;
    private ImageButton buttonShowShare,buttonShowEdit;
    private TextView textViewShowTitle,textViewShowDate,textViewShowCountdown;
    private Event event;
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



    }
}
