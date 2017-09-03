package net.astechdesign.diningsolutions.orders;

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
import net.astechdesign.diningsolutions.app.CustomerManager;
import net.astechdesign.diningsolutions.app.SourceMode;
import net.astechdesign.diningsolutions.app.model.CurrentCustomer;
import net.astechdesign.diningsolutions.database.tables.CustomerTable;
import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.model.DSDDate;

import java.util.UUID;

import static net.astechdesign.diningsolutions.orders.OrderActivity.EDIT_ENTRY;

public class CustomerFragment extends Fragment implements EditEntryFragment.EditEntryAddListener,
        DatePickerFragment.DatePickerListener,
        TimePickerFragment.TimePickerListener {

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
        Customer customer = CurrentCustomer.get();
        if (customer.getId() == null) {
            if (CustomerTable.CUSTOMER_NAME.equals(field) && value == null) {
                getActivity().finish();
            }
            UUID uuid = CustomerManager.create(value);
            CurrentCustomer.set(uuid);
        } else {
            CustomerManager.update(field, value);
            CurrentCustomer.set(customer);
        }
        customer = CurrentCustomer.get();
        displayCustomer();
    }

    @Override
    public void onEditFieldNegativeClick(String field) {
        Customer customer = CurrentCustomer.get();
        if (customer.getId() == null && CustomerTable.CUSTOMER_NAME.equals(field)) {
            getActivity().finish();
        }
    }

    @Override
    public void onDatePicked(SourceMode mode, DSDDate newDate) {
        CustomerManager.update(CustomerTable.VISIT_DATE, newDate.dbFormat());
    }

    @Override
    public void onTimePicked(DSDDate newDate) {
        DSDDate visit = CurrentCustomer.get().visit;
        CustomerManager.update(CustomerTable.VISIT_DATE, DSDDate.withTime(visit, newDate).dbFormat());
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
                DSDDate visitDate = CurrentCustomer.getVisitDate();
                DatePickerFragment dialog = DatePickerFragment.newInstance(SourceMode.OTHER, CustomerFragment.this, visitDate == null ? DSDDate.create() : visitDate);
                dialog.show(getActivity().getSupportFragmentManager(), "date_picker");
            }
        });
        mTextVisitTime = (TextView) view.findViewById(R.id.order_detail_visit_time);
        mTextVisitTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DSDDate visitDate = CurrentCustomer.getVisitDate();
                TimePickerFragment dialog = TimePickerFragment.newInstance(CustomerFragment.this, visitDate == null ? DSDDate.create() : visitDate);
                dialog.show(getActivity().getSupportFragmentManager(), "time_picker");
            }
        });
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
        mTextName.setText(CurrentCustomer.getName());
        mTextPhone.setText(CurrentCustomer.getPhoneNumber());
        mTextEmail.setText(CurrentCustomer.getEmailAddress());
        mTextAddressLine.setText(CurrentCustomer.getAddressLine());
        mTextAddressTown.setText(CurrentCustomer.getAddressTown());
        mTextAddressCounty.setText(CurrentCustomer.getAddressCounty());
        mTextAddressPostcode.setText(CurrentCustomer.getAddressPostcode());
        mTextVisit.setText(CurrentCustomer.getVisitDate().getDisplayDate());
        mTextVisit.setTag(CurrentCustomer.getVisitDate());
        mTextVisitTime.setText(CurrentCustomer.getVisitDate().getDisplayTime());
        mTextVisitTime.setTag(CurrentCustomer.getVisitDate());
    }
}
