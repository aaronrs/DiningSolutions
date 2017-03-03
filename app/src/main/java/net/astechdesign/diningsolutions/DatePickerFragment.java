package net.astechdesign.diningsolutions;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.widget.DatePicker;

import net.astechdesign.diningsolutions.model.DSDDate;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    public static final String DATE_PICKER = "delivery_date_picker";
    private static final String ARG_DATE = "date";
    public static final String RETURN_DATE = "net.astechdesign.diningsolutions.date";
    public static final int REQUEST_DATE = 0;
    private Fragment targetFragment;

    public static DatePickerFragment newInstance(Fragment targetFragment, DSDDate date) {
        DatePickerFragment fragment = newInstance(date);
        fragment.setTarget(targetFragment);
        return fragment;
    }

    public static DatePickerFragment newInstance(DSDDate date) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(ARG_DATE, date);

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private void setTarget(Fragment targetFragment) {
        this.targetFragment = targetFragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        DSDDate date = (DSDDate) getArguments().getSerializable(ARG_DATE);
        return new DatePickerDialog(getActivity(), this, date.getYear(), date.getMonth(), date.getDay());
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Intent intent = new Intent();
        Calendar cal = GregorianCalendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        intent.putExtra(RETURN_DATE, DSDDate.create(cal));

        if (targetFragment != null) {
            targetFragment.onActivityResult(DatePickerFragment.REQUEST_DATE, Activity.RESULT_OK, intent);
        }
    }
}
