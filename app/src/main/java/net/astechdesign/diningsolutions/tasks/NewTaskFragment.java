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
import net.astechdesign.diningsolutions.model.DSDDate;
import net.astechdesign.diningsolutions.model.Task;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.UUID;

public class NewTaskFragment extends DialogFragment {

    public static final String ADD_TASK = "add_task";
    public static final String CUSTOMER = "customer";
    public static final String HEADER = "task_header";
    public static final String TITLE = "task_title";
    public static final String CUSTOMER_ID = "customer_id";

    private NewTaskListener mListener;
    private TextView mDateText;
    private TextView mTimeText;
    private EditText mTitleText;
    private TextView mDescText;

    public interface NewTaskListener {
        void onNewTaskPositiveClick(DialogInterface dialog, Task task);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        String header = "New Task";
        String title = null;
        final UUID customer_id;
        if (arguments != null) {
            header = arguments.getString(HEADER, "New Task");
            title = arguments.getString(TITLE, null);
            customer_id = (UUID) arguments.getSerializable(CUSTOMER_ID);
        } else {
            customer_id = null;
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
        Calendar cal = GregorianCalendar.getInstance();
        mDateText.setText(DSDDate.getDisplayDate(cal));
        mTimeText.setText(DSDDate.getDisplayTime(cal));
        mDateText.setTag(cal);
        mTimeText.setTag(cal);
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(header)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String title = mTitleText.getText().toString().trim();
                        String description = mDescText.getText().toString().trim();
                        if (title.length() > 0 || description.length() > 0) {
                            Task task = new Task(
                                    null,
                                    DSDDate.create((Calendar) mDateText.getTag(), (Calendar) mTimeText.getTag()),
                                    title,
                                    description,
                                    customer_id
                            );
                            mListener.onNewTaskPositiveClick(dialog, task);
                        }
                    }
                })
                .create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == DatePickerFragment.REQUEST_DATE) {
            Calendar cal = (Calendar) data.getSerializableExtra(DatePickerFragment.RETURN_DATE);
            mDateText.setText(DSDDate.getDisplayDate(cal));
            mDateText.setTag(cal);
            return;
        }
        if (requestCode == TimePickerFragment.REQUEST_TIME) {
            Calendar cal = (Calendar) data.getSerializableExtra(TimePickerFragment.RETURN_TIME);
            mTimeText.setText(DSDDate.getDisplayTime(cal));
            mTimeText.setTag(cal);
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
