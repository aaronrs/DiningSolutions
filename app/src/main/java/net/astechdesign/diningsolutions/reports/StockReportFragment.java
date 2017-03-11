package net.astechdesign.diningsolutions.reports;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.astechdesign.diningsolutions.R;

public class StockReportFragment extends Fragment {

    public StockReportFragment() {
    }

    public static StockReportFragment newInstance() {
        StockReportFragment fragment = new StockReportFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reports, container, false);
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText("Stock!");
        return rootView;
    }
}
