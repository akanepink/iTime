package com.example.itime;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class CustomTimePickerDialogFragment extends DialogFragment implements TimePicker.OnTimeChangedListener,View.OnClickListener{
    public static final String CURRENT_DATE = "datepicker_current_date";
    Calendar currentDate;

    TimePicker timePicker;
    TextView backTimeButton;
    TextView ensureTimeButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
        Bundle bundle = getArguments();
        currentDate = (Calendar) bundle.getSerializable(CURRENT_DATE);

    }


    @Override
    public View onCreateView(LayoutInflater inflater,@NonNull ViewGroup container,@NonNull Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_custom_time_picker_dialog, container, false);
        if (inflater == null) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setDimAmount(0.8f);
        View view  = inflater.inflate(R.layout.fragment_custom_time_picker_dialog,container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (view != null) {
            timePicker = view.findViewById(R.id.time_picker_view);
            timePicker.setDescendantFocusability(TimePicker.FOCUS_BLOCK_DESCENDANTS);//啥意思？？？？？
            timePicker.setIs24HourView(true);

            backTimeButton = view.findViewById(R.id.button_time_back);
            backTimeButton.setOnClickListener(this);
            ensureTimeButton = view.findViewById(R.id.button_time_ensure);
            ensureTimeButton.setOnClickListener(this);
            ensureTimeButton.setVisibility(View.VISIBLE);

            ViewGroup mContainer = (ViewGroup) timePicker.getChildAt(0);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_time_back:
                dismiss();
                break;
            case R.id.button_time_ensure:
                returnSelectedTimeUnderLOLLIPOP();
                break;
            default:
                break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void returnSelectedTimeUnderLOLLIPOP() {
        //bug3:5.0上超过可选区间的日期依然能选中,所以要手动校验.5.1上已解决，但是为了与5.0保持一致，也采用确定菜单返回日期
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            Calendar selectedTime = Calendar.getInstance();

            Resources systemResources = Resources.getSystem();
            int hourNumberPickerId = systemResources.getIdentifier("hour", "id", "android");
            int minuteNumberPickerId = systemResources.getIdentifier("minute", "id", "android");
            NumberPicker hourNumberPicker = (NumberPicker) timePicker.findViewById(hourNumberPickerId);

            NumberPicker minuteNumberPicker = (NumberPicker) timePicker.findViewById(minuteNumberPickerId);

            selectedTime.set(Calendar.YEAR,Calendar.MONTH,Calendar.DAY_OF_MONTH,timePicker.getHour(), timePicker.getMinute());
            selectedTime.set(Calendar.MILLISECOND,0);
        }
            if (onSelectedTimeListener != null) {
                onSelectedTimeListener.onSelectedTime(timePicker.getHour(), timePicker.getMinute());


        }

        dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        onSelectedTimeListener = null;
    }

    public interface OnSelectedTimeListener {
        void onSelectedTime(int hour, int minite);
    }

    OnSelectedTimeListener onSelectedTimeListener;

    public void setOnSelectedTimeListener(CustomTimePickerDialogFragment.OnSelectedTimeListener onSelectedTimeListener) {
        this.onSelectedTimeListener = onSelectedTimeListener;
    }


    @Override
    public void onTimeChanged(TimePicker view, int hour, int minite) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.M){ //LOLLIPOP上，这个回调无效，排除将来可能的干扰
            return;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) { //5.0以下，必须采用滚轮模式，所以需借助确定菜单回传选定值
            return;
        }
        if (onSelectedTimeListener != null) {
            onSelectedTimeListener.onSelectedTime(hour,minite);
        }
        dismiss();
    }
}