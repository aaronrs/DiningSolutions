package net.astechdesign.diningsolutions;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import net.astechdesign.diningsolutions.model.DSDDate;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    public static final String DATE_PICKER = "date_picker";
    private static final String ARG_DATE = "date";
    public static final String RETURN_DATE = "net.astechdesign.diningsolutions.date";
    public static final int REQUEST_DATE = 0;
    private static String sourceMode;
    private DatePickerListener mListener;

    public static DatePickerFragment newInstance(String mode, DatePickerListener listener, DSDDate date) {
        sourceMode = mode;
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setListener(listener);

        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_DATE, date);
        fragment.setArguments(bundle);

        return fragment;
    }

    public interface DatePickerListener {
        void onDatePicked(String mode, DSDDate newDate);
    }

    private void setListener(DatePickerListener listener) {
        mListener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DSDDate date = (DSDDate) getArguments().getSerializable(ARG_DATE);
        if (date == null) {
            date = DSDDate.create();
        }
        return new DatePickerDialog(getActivity(), this, date.getYear(), date.getMonth(), date.getDay());
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);

        mListener.onDatePicked(sourceMode, DSDDate.create(cal));
    }
}
