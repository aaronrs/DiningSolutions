package net.astechdesign.diningsolutions;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.repositories.CustomerRepo;

import java.util.UUID;

public class CustomerFragment extends Fragment implements View.OnClickListener {


    private static final String ARG_CUSTOMER_ID = "customer_id";
    private Customer mCustomer;
    private EditText mNameField;
    private Button mDateButton;
    private Button mOrderButton;

    public static CustomerFragment newInstance(UUID customerId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_CUSTOMER_ID, customerId);
        return CustomerFragment.newInstance(customerId);
    }

    public CustomerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID customerId = (UUID) getActivity().getIntent().getSerializableExtra(CustomerActivity.EXTRA_CUSTOMER_ID);
        mCustomer = CustomerRepo.get(getActivity()).getCustomer(customerId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_customer, container, false);
        mNameField = (EditText) view.findViewById(R.id.customer_name);
        mOrderButton = (Button)view.findViewById(R.id.orders_button);
        mOrderButton.setOnClickListener(this);

        mDateButton = (Button)view.findViewById(R.id.customer_created);
        java.text.DateFormat dateFormat = DateFormat.getMediumDateFormat(getActivity());
        mDateButton.setText(dateFormat.format(mCustomer.created));
        mDateButton.setEnabled(false);

        updateUi(view);
        return view;
    }

    private void updateUi(View view) {
        setText(view, R.id.customer_name, mCustomer.name);
        setText(view, R.id.phone, mCustomer.phone.number);
        setText(view, R.id.email, mCustomer.email.address);
        setText(view, R.id.house_name, mCustomer.address.number + ", " + mCustomer.address.name);
        setText(view, R.id.street, mCustomer.address.line1);
        setText(view, R.id.town, mCustomer.address.town);
        setText(view, R.id.county, mCustomer.address.county);
        setText(view, R.id.postcode, mCustomer.address.postcode);
    }
    private void setText(View view, int viewId, String text) {
        ((TextView) view.findViewById(viewId)).setText(text);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), OrderListActivity.class);
        intent.putExtra("customer_id", mCustomer.id);
        startActivity(intent);
    }
}
