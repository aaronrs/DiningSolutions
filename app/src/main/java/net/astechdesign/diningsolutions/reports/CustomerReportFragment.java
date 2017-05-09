package net.astechdesign.diningsolutions.reports;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.astechdesign.diningsolutions.R;

public class CustomerReportFragment extends Fragment {

    public CustomerReportFragment() {
    }

    // TODO: Customize parameter initialization
    public static CustomerReportFragment newInstance() {
        CustomerReportFragment fragment = new CustomerReportFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_record_list, container, false);

        if (view instanceof RecyclerView) {
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setAdapter(new MyRecordRecyclerViewAdapter(null));
        }
        return view;
    }

}
