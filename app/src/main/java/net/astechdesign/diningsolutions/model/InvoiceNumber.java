package net.astechdesign.diningsolutions.model;

public class InvoiceNumber {
    private static int invoiceNumber = 1;
    public final String value;

    private InvoiceNumber(String value) {
        this.value = value;
    }

    public static InvoiceNumber create() {
        int number = invoiceNumber++;
        return new InvoiceNumber(Integer.toString(number));
    }
}
