package net.astechdesign.diningsolutions;


import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import net.astechdesign.diningsolutions.model.DSDDate;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class TimePickerFragment extends DialogFragment {

    private static final String ARG_TIME = "time";
    public static final String RETURN_TIME = "net.astechdesign.diningsolutions.time";
    public static final int REQUEST_TIME = 1;

    private TimePicker mTimePicker;

    public static TimePickerFragment newInstance(DSDDate time) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_TIME, time);

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DSDDate time = (DSDDate) getArguments().getSerializable(ARG_TIME);

        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_time, null);

        mTimePicker = (TimePicker) view.findViewById(R.id.dialog_time_picker);
        mTimePicker.setCurrentHour(time.getHour());
        mTimePicker.setCurrentMinute(time.getMinute());

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.time_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendResult(Activity.RESULT_OK, 12, 0);
                    }
                })
                .create();
    }

    private void sendResult(int resultCode, int hour, int min) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        Calendar cal = GregorianCalendar.getInstance();
        cal.set(Calendar.HOUR, hour);
        cal.set(Calendar.MINUTE, min);
        intent.putExtra(RETURN_TIME, cal);

        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
