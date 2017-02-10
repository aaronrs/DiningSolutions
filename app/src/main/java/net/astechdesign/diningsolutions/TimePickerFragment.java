package net.astechdesign.diningsolutions;


import android.app.Activity;
import android.app.Dialog;
import android.app.TimePickerDialog;
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

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    public static final String TIME_PICKER = "time_picker";
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
        return new TimePickerDialog(getActivity(), this, time.getHour(), time.getMinute(), false);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Intent intent = new Intent();
        Calendar cal = GregorianCalendar.getInstance();
        cal.set(Calendar.HOUR, hourOfDay);
        cal.set(Calendar.MINUTE, minute);
        intent.putExtra(RETURN_TIME, DSDDate.create(cal));

        getTargetFragment().onActivityResult(getTargetRequestCode(), TimePickerFragment.REQUEST_TIME, intent);
    }
}
