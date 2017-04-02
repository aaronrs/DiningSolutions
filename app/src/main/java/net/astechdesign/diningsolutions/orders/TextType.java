package net.astechdesign.diningsolutions.orders;

enum TextType {
    TEXT_NAME("Customer Name"),
    TEXT_PHONE("Phone Number"),
    TEXT_EMAIL("Email Address"),
    TEXT_ADDRESS("Address");

    public final String title;

    TextType(String title) {
        this.title = title;
    }
}
