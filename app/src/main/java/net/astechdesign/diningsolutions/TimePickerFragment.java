package net.astechdesign.diningsolutions;


import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.TimePicker;

import net.astechdesign.diningsolutions.model.DSDDate;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    public static final String TIME_PICKER = "time_picker";
    private static final String ARG_TIME = "time";
    public static final String RETURN_TIME = "net.astechdesign.diningsolutions.time";
    public static final int REQUEST_TIME = 1;
    private TimePickerListener mListener;

    public static TimePickerFragment newInstance(TimePickerListener listener, DSDDate time) {
        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setListener(listener);

        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_TIME, time);
        fragment.setArguments(bundle);

        return fragment;
    }

    public interface TimePickerListener {
        void onTimePicked(DSDDate newDate);
    }

    private void setListener(TimePickerListener listener) {
        mListener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DSDDate time = (DSDDate) getArguments().getSerializable(ARG_TIME);
        if (time == null) {
            time = DSDDate.create();
        }
        return new TimePickerDialog(getActivity(), this, time.getHour(), time.getMinute(), false);
    }

    @Override
    public void onTimeSet(TimePicker view, int hour, int minute) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, hour);
        cal.set(Calendar.MINUTE, minute);

        mListener.onTimePicked(DSDDate.create(cal));
    }
}
