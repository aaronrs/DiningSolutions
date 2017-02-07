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
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment {

    public static final String DATE_PICKER = "delivery_date_picker";
    private static final String TASK_DATE = "date";
    public static final String RETURN_DATE = "net.astechdesign.diningsolutions.date";
    public static final int REQUEST_DATE = 0;

    private DatePicker mDatePicker;

    public static DatePickerFragment newInstance(Calendar date) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(TASK_DATE, date);

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar cal = (Calendar) getArguments().getSerializable(TASK_DATE);


        View view = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_date, null);

        mDatePicker = (DatePicker) view.findViewById(R.id.dialog_date_picker);
        mDatePicker.init(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH), null);

        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Calendar cal = Calendar.getInstance();
                        cal.set(mDatePicker.getYear(), mDatePicker.getMonth(), mDatePicker.getDayOfMonth());
                        sendResult(Activity.RESULT_OK, cal);
                    }
                })
                .create();
    }

    private void sendResult(int resultCode, Calendar cal) {
        if (getTargetFragment() == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(RETURN_DATE, cal);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
    }
}
