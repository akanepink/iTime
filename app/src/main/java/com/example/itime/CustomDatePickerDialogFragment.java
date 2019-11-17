package com.example.itime;


import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;


/**
 * A simple {@link Fragment} subclass.
 */
public class CustomDatePickerDialogFragment extends DialogFragment implements DatePicker.OnDateChangedListener,View.OnClickListener {
    public static final String CURRENT_DATE = "datepicker_current_date";
    Calendar currentDate;

    DatePicker datePicker;
    TextView backButton;
    TextView ensureButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
        Bundle bundle = getArguments();
        currentDate = (Calendar) bundle.getSerializable(CURRENT_DATE);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (inflater == null) {
            return super.onCreateView(inflater, container, savedInstanceState);
        }
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setDimAmount(0.8f);
        View view  = inflater.inflate(R.layout.fragment_custom_date_picker_dialog,container,false);
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int style;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            style = R.style.ZZBDatePickerDialogLStyle;
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            style = R.style.ZZBDatePickerDialogLStyle;
        } else {
            style = getTheme();
        }
        return new Dialog(getActivity(), style);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (view != null) {
            datePicker = view.findViewById(R.id.date_picker_view);
            backButton = view.findViewById(R.id.button_back);
            backButton.setOnClickListener(this);
            ensureButton = view.findViewById(R.id.button_ensure);
            ensureButton.setOnClickListener(this);
            ensureButton.setVisibility(View.VISIBLE);
            //如果只要日历部分，隐藏header
            ViewGroup mContainer = (ViewGroup) datePicker.getChildAt(0);
            View header = mContainer.getChildAt(0);
            header.setVisibility(View.GONE);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_back:
                dismiss();
                break;
            case R.id.button_ensure:
                returnSelectedDateUnderLOLLIPOP();
                break;
            default:
                break;
        }
    }

    private void returnSelectedDateUnderLOLLIPOP() {
        //bug3:5.0上超过可选区间的日期依然能选中,所以要手动校验.5.1上已解决，但是为了与5.0保持一致，也采用确定菜单返回日期
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            Calendar selectedDate = Calendar.getInstance();
            selectedDate.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(),0,0,0);
            selectedDate.set(Calendar.MILLISECOND,0);

        }
        if (onSelectedDateListener != null) {
            onSelectedDateListener.onSelectedDate(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
        }
        dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        onSelectedDateListener = null;
    }

    public interface OnSelectedDateListener {
        void onSelectedDate(int year, int monthOfYear, int dayOfMonth);
    }

    OnSelectedDateListener onSelectedDateListener;

    public void setOnSelectedDateListener(OnSelectedDateListener onSelectedDateListener) {
        this.onSelectedDateListener = onSelectedDateListener;
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.M){ //LOLLIPOP上，这个回调无效，排除将来可能的干扰
            return;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) { //5.0以下，必须采用滚轮模式，所以需借助确定菜单回传选定值
            return;
        }
        if (onSelectedDateListener != null) {
            onSelectedDateListener.onSelectedDate(year, monthOfYear, dayOfMonth);
        }
        dismiss();
    }

}
