package net.astechdesign.diningsolutions.orders;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import net.astechdesign.diningsolutions.R;


public class EditEntryFragment extends DialogFragment {

    private EditEntryAddListener mListener;
    private String mTextTitle;
    private String mValue;
    private String mField;

    public interface EditEntryAddListener {
        void onEditFieldPositiveClick(DialogInterface dialog, String field, String value);
        void onEditFieldNegativeClick(String field);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_edit_entry, null);
        String title = this.mTextTitle;
        if (mValue != null) {
            ((TextView) view.findViewById(R.id.value)).setText(mValue);
            title = "Edit " + this.mTextTitle;
        }
        final TextView valueView = (TextView) view.findViewById(R.id.value);
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(title)
                .setNegativeButton(android.R.string.cancel,  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onEditFieldNegativeClick(mField);
                    }
                })
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String value = valueView.getText().toString();
                        mListener.onEditFieldPositiveClick(dialog, mField, value);
                    }
                })
                .create();
    }

    public void value(EditEntryAddListener listener, String title, String field, String value) {
        this.mListener = listener;
        this.mTextTitle = title;
        this.mField = field;
        this.mValue = value;
    }

}
