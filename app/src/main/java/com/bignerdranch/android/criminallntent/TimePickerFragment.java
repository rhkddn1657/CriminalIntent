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

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_time, null);
        mTimePicker = (TimePicker) v.findViewById(R.id.dialog_time_picker);


        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.time_picker_title)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(date);
                                int year = calendar.get(Calendar.YEAR);
                                int month = calendar.get(Calendar.MONTH);
                                int day = calendar.get(Calendar.DAY_OF_MONTH);
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
