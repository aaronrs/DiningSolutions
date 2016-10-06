package net.astechdesign.diningsolutions.model;

import java.util.Date;

public class Invoice {
    public final InvoiceNumber number;
    public final Date date;
    public final Customer customer;
    public final Distributor distributor;
    public final Order order;

    public Invoice(InvoiceNumber number, Date date, Customer customer, Distributor distributor, Order order) {
        this.number = number;
        this.date = date;
        this.customer = customer;
        this.distributor = distributor;
        this.order = order;
    }
}
