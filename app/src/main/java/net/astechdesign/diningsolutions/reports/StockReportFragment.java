package net.astechdesign.diningsolutions.reports;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.astechdesign.diningsolutions.R;
import net.astechdesign.diningsolutions.repositories.ReportRepo;

public class StockReportFragment extends Fragment {

    private final ReportRepo reportRepo;

    public StockReportFragment() {
        reportRepo = ReportRepo.get(getContext());
    }

    public static StockReportFragment newInstance() {
        StockReportFragment fragment = new StockReportFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record_list, container, false);

        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setAdapter(new MyRecordRecyclerViewAdapter(reportRepo.getSales()));
        }
        return view;
    }

}
