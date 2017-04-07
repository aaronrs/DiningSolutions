package net.astechdesign.diningsolutions.orders;

import net.astechdesign.diningsolutions.DatePickerFragment;
import net.astechdesign.diningsolutions.TimePickerFragment;
import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.model.DSDDate;

class VisitDateListener implements DatePickerFragment.DatePickerListener,
        TimePickerFragment.TimePickerListener {
    private OrderActivity activity;
    private DSDDate date;
    private Customer mCustomer;

    public VisitDateListener(Customer customer, DSDDate date) {
        this.mCustomer = customer;
        this.date = date;
    }

    @Override
    public void onDatePicked(DSDDate newDate) {
        activity.updateCustomer(DSDDate.withTime(newDate, this.date));
    }

    @Override
    public void onTimePicked(DSDDate newDate) {
        activity.updateCustomer(DSDDate.withTime(this.date, newDate));
    }
}
