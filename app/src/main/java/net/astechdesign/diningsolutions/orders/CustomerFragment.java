package net.astechdesign.diningsolutions.orders;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.astechdesign.diningsolutions.DatePickerFragment;
import net.astechdesign.diningsolutions.R;
import net.astechdesign.diningsolutions.TimePickerFragment;
import net.astechdesign.diningsolutions.database.tables.CustomerTable;
import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.model.DSDDate;
import net.astechdesign.diningsolutions.repositories.CustomerRepo;

import java.util.UUID;

import static net.astechdesign.diningsolutions.orders.OrderActivity.EDIT_ENTRY;

public class CustomerFragment extends Fragment implements EditEntryFragment.EditEntryAddListener,
        DatePickerFragment.DatePickerListener,
        TimePickerFragment.TimePickerListener {

    Customer mCustomer;
    private View view;
    private TextView mTextName;
    private TextView mTextPhone;
    private TextView mTextEmail;
    private TextView mTextAddressLine;
    private TextView mTextAddressTown;
    private TextView mTextAddressCounty;
    private TextView mTextAddressPostcode;
    private TextView mTextVisit;
    private TextView mTextVisitTime;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCustomer = ((OrderActivity) getActivity()).getCustomer();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.order_customer_details, container);

        initialiseFields();
        displayCustomer();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        displayCustomer();
    }

    @Override
    public void onEditFieldPositiveClick(DialogInterface dialog, String field, String value) {
        CustomerRepo customerRepo = CustomerRepo.get(getActivity());
        UUID customerId;
        if (mCustomer.getId() == null) {
            if (CustomerTable.CUSTOMER_NAME.equals(field) && value == null) {
                getActivity().finish();
            }
            mCustomer = Customer.create(value);
            customerId = customerRepo.addOrUpdate(mCustomer);
        } else {
            customerRepo.update(mCustomer, field, value);
            customerId = mCustomer.getId();
        }
        mCustomer = customerRepo.get(customerId);
        displayCustomer();
    }

    @Override
    public void onEditFieldNegativeClick(String field) {
        if (mCustomer.getId() == null && CustomerTable.CUSTOMER_NAME.equals(field)) {
            getActivity().finish();
        }
    }

    @Override
    public void onDatePicked(DSDDate newDate) {
        updateVisitDate(newDate, mCustomer.visit);
    }

    @Override
    public void onTimePicked(DSDDate newDate) {
        updateVisitDate(mCustomer.visit, newDate);
    }

    private void updateVisitDate(DSDDate visit, DSDDate newDate) {
        CustomerRepo customerRepo = CustomerRepo.get(getActivity());
        customerRepo.update(mCustomer, CustomerTable.VISIT_DATE, DSDDate.withTime(visit, newDate).dbFormat());
        mCustomer = customerRepo.get(mCustomer.getId());
        displayCustomer();
    }

    private void initialiseFields() {
        mTextName = setupField(R.id.customer_name, "Customer Name", CustomerTable.CUSTOMER_NAME);
        mTextPhone = setupField(R.id.customer_phone, "Customer Phone", CustomerTable.CUSTOMER_PHONE);
        mTextEmail = setupField(R.id.customer_email, "Customer Email", CustomerTable.CUSTOMER_EMAIL);
        mTextAddressLine = setupField(R.id.address_line, "Address", CustomerTable.ADDRESS_LINE);
        mTextAddressTown = setupField(R.id.address_town, "Town", CustomerTable.ADDRESS_TOWN);
        mTextAddressCounty = setupField(R.id.address_county, "County", CustomerTable.ADDRESS_COUNTY);
        mTextAddressPostcode = setupField(R.id.address_postcode, "Postcode", CustomerTable.ADDRESS_POSTCODE);

        mTextVisit = (TextView) view.findViewById(R.id.order_detail_visit);
        mTextVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment dialog = DatePickerFragment.newInstance(CustomerFragment.this, (DSDDate) view.getTag());
                dialog.show(getActivity().getSupportFragmentManager(), "date_picker");
            }
        });
        mTextVisitTime = (TextView) view.findViewById(R.id.order_detail_visit_time);
        mTextVisitTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerFragment dialog = TimePickerFragment.newInstance(CustomerFragment.this, (DSDDate) view.getTag());
                dialog.show(getActivity().getSupportFragmentManager(), "time_picker");
            }
        });
        if (mCustomer.equals(Customer.newCustomer)) {
            mTextVisit.setTag(DSDDate.create());
            mTextVisitTime.setTag(DSDDate.create());
        } else {
            mTextVisit.setTag(mCustomer.visit);
            mTextVisitTime.setTag(mCustomer.visit);
        }
    }

    private TextView setupField(int viewId, String name, String field) {
        TextView fieldView = (TextView) view.findViewById(viewId);
        fieldView.setOnClickListener(getFieldListener(name, field));
        return fieldView;
    }

    private View.OnClickListener getFieldListener(final String name, final String field) {
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                EditEntryFragment fragment = new EditEntryFragment();
                fragment.value(CustomerFragment.this, name, field, ((TextView) v).getText().toString());
                fragment.show(fm, EDIT_ENTRY);
            }
        };
        return listener;
    }

    private void displayCustomer() {
        if (!mCustomer.equals(Customer.newCustomer)) {
            mTextName.setText(mCustomer.name);
            mTextPhone.setText(mCustomer.phone == null ? null : mCustomer.phone.number);
            mTextEmail.setText(mCustomer.email == null ? null : mCustomer.email.address);
            mTextAddressLine.setText(mCustomer.address == null ? null : mCustomer.address.line);
            mTextAddressTown.setText(mCustomer.address == null ? null : mCustomer.address.town);
            mTextAddressCounty.setText(mCustomer.address == null ? null : mCustomer.address.county);
            mTextAddressPostcode.setText(mCustomer.address == null ? null : mCustomer.address.postcode);
            if (!mCustomer.visit.equals(DSDDate.EMPTY_DATE)) {
                mTextVisit.setText(mCustomer.visit.getDisplayDate());
                mTextVisit.setTag(mCustomer.visit);
                mTextVisitTime.setText(mCustomer.visit.getDisplayTime());
                mTextVisitTime.setTag(mCustomer.visit);
            }
        } else {
            mTextName.performClick();
        }
    }
}
