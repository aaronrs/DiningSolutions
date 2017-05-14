package net.astechdesign.diningsolutions.customers;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import net.astechdesign.diningsolutions.DatePickerFragment;
import net.astechdesign.diningsolutions.R;
import net.astechdesign.diningsolutions.TimePickerFragment;
import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.model.DSDDate;

public class NextVisitFragment extends DialogFragment implements DatePickerFragment.DatePickerListener {

    public static final String CUSTOMER = "customer";
    public static final String HEADER = "task_header";

    private NextVisitListener mListener;
    private TextView mDateText;
    private TextView mTimeText;

    public interface NextVisitListener {
        void onPositiveClick(DialogInterface dialog, DSDDate date);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        Customer customer = (Customer) arguments.getSerializable(CUSTOMER);
        String header = arguments.getString(HEADER, "Next visit for: " + customer.name);

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_next_visit, null);
        mDateText = (TextView) view.findViewById(R.id.visit_date_button);
        mTimeText = (TextView) view.findViewById(R.id.visit_time_button);

        DSDDate date = customer.visit;
        mDateText.setText(date.getDisplayDate());
        mTimeText.setText(date.getDisplayTime());
        mDateText.setTag(date);
        mTimeText.setTag(DSDDate.create(date.date));
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(header)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            DSDDate date = (DSDDate) mDateText.getTag();
                            date = DSDDate.withTime(date, (DSDDate) mTimeText.getTag());
                            mListener.onPositiveClick(dialog, date);
                    }
                })
                .create();
    }

    @Override
    public void onDatePicked(String mode, DSDDate date) {
        mDateText.setText(date.getDisplayDate());
        mDateText.setTag(date);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == DatePickerFragment.REQUEST_DATE) {
            DSDDate date = (DSDDate) data.getSerializableExtra(DatePickerFragment.RETURN_DATE);
            mDateText.setText(date.getDisplayDate());
            mDateText.setTag(date);
            return;
        }
        if (requestCode == TimePickerFragment.REQUEST_TIME) {
            DSDDate time = (DSDDate) data.getSerializableExtra(TimePickerFragment.RETURN_TIME);
            mTimeText.setText(time.getDisplayTime());
            mTimeText.setTag(time);
            return;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (NextVisitListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement NextVisitListener");
        }
    }
}
