package net.astechdesign.diningsolutions.admin;

public enum Prefs {
    NAME("distributor_name"),
    NUMBER("distributor_number"),
    MOBILE("distributor_mobile"),
    EMAIL("distributor_email"),
    INVOICE("invoice_start_number"),
    DSD("dsd_email");

    public final String name;

    Prefs(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
