package net.astechdesign.diningsolutions.reports;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.astechdesign.diningsolutions.R;
import net.astechdesign.diningsolutions.reports.dummy.DummyContent;

public class EarningsReportFragment extends Fragment {

    public EarningsReportFragment() {
    }

    public static EarningsReportFragment newInstance() {
        EarningsReportFragment fragment = new EarningsReportFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record_list, container, false);

        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setAdapter(new MyRecordRecyclerViewAdapter(DummyContent.ITEMS));
        }
        return view;
    }

}
