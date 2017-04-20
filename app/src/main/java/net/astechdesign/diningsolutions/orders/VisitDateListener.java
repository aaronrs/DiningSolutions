package net.astechdesign.diningsolutions.orders;

import android.content.Context;

import net.astechdesign.diningsolutions.DatePickerFragment;
import net.astechdesign.diningsolutions.TimePickerFragment;
import net.astechdesign.diningsolutions.database.tables.CustomerTable;
import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.model.DSDDate;
import net.astechdesign.diningsolutions.repositories.CustomerRepo;

class VisitDateListener implements DatePickerFragment.DatePickerListener,
        TimePickerFragment.TimePickerListener {
    private Context context;
    private Customer mCustomer;

    public VisitDateListener(Context context, Customer customer) {
        this.context = context;
        this.mCustomer = customer;
    }

    @Override
    public void onDatePicked(DSDDate newDate) {
        CustomerRepo.get(context).update(mCustomer, CustomerTable.VISIT_DATE, DSDDate.withTime(newDate, mCustomer.visit).dbFormat());
    }

    @Override
    public void onTimePicked(DSDDate newDate) {
        CustomerRepo.get(context).update(mCustomer, CustomerTable.VISIT_DATE, DSDDate.withTime(mCustomer.visit, newDate).dbFormat());
    }
}
