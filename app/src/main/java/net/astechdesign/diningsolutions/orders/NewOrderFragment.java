package net.astechdesign.diningsolutions.orders;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import net.astechdesign.diningsolutions.R;


public class NewOrderFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_new_order, null);
        return new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.new_customer_title)
                .setPositiveButton(android.R.string.ok, null)
                .create();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
    }
}