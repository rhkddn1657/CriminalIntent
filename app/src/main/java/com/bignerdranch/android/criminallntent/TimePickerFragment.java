package com.bignerdranch.android.criminallntent;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by rhkdd on 2016-12-25.
 */

public class TimePickerFragment extends DialogFragment {

    private static final String ARG_Time = "time";
    private TimePicker mTimePicker;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    public static final String EXTRA_DATE = "com.bignerdranch.android.criminalintent.date";

    public static TimePickerFragment newInstance(Date date) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_Time, date);

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Date date = (Date) getArguments().getSerializable(ARG_Time);

        Calendar calendar = Calendar.getInstance(); //Calendar 객체로부터 필요한데이터형태로 변환.
        calendar.setTime(date);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_time, null);
        mTimePicker = (TimePicker) v.findViewById(R.id.dialog_time_picker);

        if (Build.VERSION.SDK_INT >= 23) {
            mTimePicker.setHour(hour);
            mTimePicker.setMinute(minute);
        } else {
            mTimePicker.setCurrentHour(hour);
            mTimePicker.setCurrentMinute(minute);
        }

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.time_picker_title)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (Build.VERSION.SDK_INT >= 23) {
                                    hour = mTimePicker.getHour();
                                    minute = mTimePicker.getMinute();
                                } else {
                                    hour = mTimePicker.getCurrentHour();
                                    minute = mTimePicker.getCurrentMinute();
                                }
                                Date time = new GregorianCalendar(year, month, day,
                                        hour, minute, 0).getTime();
                                setResult(Activity.RESULT_OK, time);
                            }
                        })
                .create();
    }

    private void setResult(int resultCode, Date date) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_DATE, date);
        Log.d("test3","sendResult : getTarget:"+getTargetRequestCode()+"| resultCode:"+resultCode);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);

    }
}
