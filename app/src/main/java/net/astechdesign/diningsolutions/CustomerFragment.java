package net.astechdesign.diningsolutions;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.repositories.CustomerRepo;


/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerFragment extends Fragment implements View.OnClickListener {


    private Customer mCustomer;
    private EditText mNameField;
    private Button mDateButton;
    private CheckBox mCurrentCheckBox;
    private Button mOrderButton;

    public CustomerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCustomer = CustomerRepo.get(getActivity()).getmCustomers().get(0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_customer, container, false);
        mNameField = (EditText) view.findViewById(R.id.customer_name);
        mNameField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                mCustomer.name = s.toString();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        mOrderButton = (Button)view.findViewById(R.id.orders_button);
        mOrderButton.setOnClickListener(this);

        mDateButton = (Button)view.findViewById(R.id.customer_created);
        java.text.DateFormat dateFormat = DateFormat.getMediumDateFormat(getActivity());
        mDateButton.setText(dateFormat.format(mCustomer.created));
        mDateButton.setEnabled(false);

//        mCurrentCheckBox = (CheckBox) view.findViewById(R.id.customer_current);
//        mCurrentCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                mCustomer.setCurrent(isChecked);
//            }
//        });

        return view;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), OrderListActivity.class);
        intent.putExtra("customer_id", mCustomer.id);
        startActivity(intent);
    }
}
