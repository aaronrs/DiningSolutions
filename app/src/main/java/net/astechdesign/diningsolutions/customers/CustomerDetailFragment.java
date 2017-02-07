package net.astechdesign.diningsolutions.customers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.astechdesign.diningsolutions.DatePickerFragment;
import net.astechdesign.diningsolutions.R;
import net.astechdesign.diningsolutions.TimePickerFragment;
import net.astechdesign.diningsolutions.model.Address;
import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.repositories.TaskRepo;

import java.util.Calendar;

public class CustomerDetailFragment extends Fragment {

    public static final String ARG_CUSTOMER = "customer";

    private Customer mItem;
    private TaskRepo taskRepo;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CustomerDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        taskRepo = TaskRepo.get(getContext());

        if (getArguments().containsKey(ARG_CUSTOMER)) {
            mItem = (Customer)getArguments().getSerializable(ARG_CUSTOMER);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle("Customers");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.customer_detail, container, false);

        if (mItem != null) {
            rootView.findViewById(R.id.customer_order_btn).setTag(mItem.getDbId());
            rootView.findViewById(R.id.next_visit).setTag(mItem);
            setText(rootView, R.id.customer_name, mItem.name);
            setText(rootView, R.id.customer_email, mItem.email.address);
            setText(rootView, R.id.customer_phone, mItem.phone.number);
            Address address = mItem.address;
            if (address != null) {
                setText(rootView, R.id.address_name, address.name);
                setText(rootView, R.id.address_line1, address.line1);
                setText(rootView, R.id.address_line2, address.line2);
                setText(rootView, R.id.address_town, address.town);
                setText(rootView, R.id.address_county, address.county);
                setText(rootView, R.id.address_postcode, address.postcode);
            }
        }

        return rootView;
    }

    private void setText(View rootView, int id, String value) {
        TextView view = (TextView) rootView.findViewById(id);
        if (value.trim().isEmpty()) {
            view.setVisibility(View.GONE);
        } else {
            view.setText(value);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == DatePickerFragment.REQUEST_DATE) {
            Calendar cal = (Calendar) data.getSerializableExtra(DatePickerFragment.RETURN_DATE);
            taskRepo.addVisitDate(mItem, cal);
            return;
        }
        if (requestCode == TimePickerFragment.REQUEST_TIME) {
            Calendar cal = (Calendar) data.getSerializableExtra(TimePickerFragment.RETURN_TIME);
            taskRepo.addVisitDate(mItem, cal);
            return;
        }
    }

}
