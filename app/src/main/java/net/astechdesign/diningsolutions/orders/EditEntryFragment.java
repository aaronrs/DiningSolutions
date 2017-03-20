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
    private String name;
    private String value;

    public interface EditEntryAddListener {
        void onEditPositiveClick(DialogInterface dialog);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_edit_entry, null);
        String title = name;
        if (value != null) {
            ((TextView) view.findViewById(R.id.value)).setText(value);
            title = "Edit " + name;
        }
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(title)
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onEditPositiveClick(dialog);
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

    public void value(String name, String value) {
        this.name = name;
        this.value = value;
    }

}
