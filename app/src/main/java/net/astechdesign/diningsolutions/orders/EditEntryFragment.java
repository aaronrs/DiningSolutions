package net.astechdesign.diningsolutions.orders;

import android.app.Dialog;
import android.content.Context;
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
    private String textTitle;
    private String value;
    private String field;

    public interface EditEntryAddListener {
        void onEditFieldPositiveClick(DialogInterface dialog, String field, String value);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_edit_entry, null);
        String title = this.textTitle;
        if (value != null) {
            ((TextView) view.findViewById(R.id.value)).setText(value);
            title = "Edit " + this.textTitle;
        }
        final TextView valueView = (TextView) view.findViewById(R.id.value);
        final String editTitle = title;
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(title)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String value = valueView.getText().toString();
                        mListener.onEditFieldPositiveClick(dialog, field, value);
                    }
                })
                .create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (EditEntryAddListener) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement EditEntryAddListener");
        }
    }

    public void value(String title, String field, String value) {
        this.textTitle = title;
        this.field = field;
        this.value = value;
    }

}
