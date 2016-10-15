package net.astechdesign.diningsolutions;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import net.astechdesign.diningsolutions.model.DSDDate;


public class NewTodoFragment extends DialogFragment {

    private Button mDateButton;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_new_todo, null);
        mDateButton = (Button) view.findViewById(R.id.date_button);
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.new_todo_title)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == DatePickerFragment.REQUEST_DATE) {
            DSDDate date = (DSDDate) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mDateButton.setText(date.toString());
        }
    }
}
