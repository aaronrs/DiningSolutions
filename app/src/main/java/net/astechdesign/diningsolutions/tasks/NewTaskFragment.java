package net.astechdesign.diningsolutions.tasks;

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
import android.widget.EditText;
import android.widget.TextView;

import net.astechdesign.diningsolutions.DatePickerFragment;
import net.astechdesign.diningsolutions.R;
import net.astechdesign.diningsolutions.TimePickerFragment;
import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.model.DSDDate;

public class NewTaskFragment extends DialogFragment implements DatePickerFragment.DatePickerListener, TimePickerFragment.TimePickerListener {

    public static final String ADD_TASK = "add_task";
    public static final String CUSTOMER = "customer";
    public static final String HEADER = "task_header";
    public static final String TITLE = "task_title";

    private NewTaskListener mListener;
    private TextView mDateText;
    private TextView mTimeText;
    private EditText mTitleText;
    private TextView mDescText;

    public interface NewTaskListener {
        void onNewTaskPositiveClick(DialogInterface dialog, DSDDate date, String title, String description);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        String header = arguments.getString(HEADER, "New Task");
        String title = arguments.getString(TITLE, null);
        Customer customer = null;
        if (arguments.containsKey(CUSTOMER)) {
            customer = (Customer) arguments.getSerializable(CUSTOMER);
        }
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_new_task, null);
        mDateText = (TextView) view.findViewById(R.id.task_date_button);
        mTimeText = (TextView) view.findViewById(R.id.task_time_button);
        mTitleText = (EditText) view.findViewById(R.id.task_title);
        mDescText = (TextView) view.findViewById(R.id.task_description);

        if (title != null) {
            mTitleText.setText(title);
            mTitleText.setFocusable(false);
        }

        DSDDate date = customer == null ? DSDDate.create() : customer.visit;
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
                        String title = mTitleText.getText().toString().trim();
                        String description = mDescText.getText().toString().trim();
                        if (title.length() > 0 || description.length() > 0) {
                            DSDDate date = (DSDDate) mDateText.getTag();
                            date = DSDDate.withTime(date, (DSDDate) mTimeText.getTag());
                            mListener.onNewTaskPositiveClick(dialog, date, title, description);
                        }
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
    public void onTimePicked(DSDDate time) {
        mTimeText.setText(time.getDisplayTime());
        mTimeText.setTag(time);
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
            mListener = (NewTaskListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement NewTaskListener");
        }
    }
}
