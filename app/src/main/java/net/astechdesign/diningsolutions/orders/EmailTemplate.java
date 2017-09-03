package net.astechdesign.diningsolutions.orders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import net.astechdesign.diningsolutions.admin.Prefs;
import net.astechdesign.diningsolutions.app.model.CurrentCustomer;
import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.model.Distributor;
import net.astechdesign.diningsolutions.model.Order;
import net.astechdesign.diningsolutions.model.OrderItem;
import net.astechdesign.diningsolutions.repositories.OrderRepo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class EmailTemplate {

    private static String INVOICE_NUMBER = "#INVOICE_NUMBER";
    private static String INVOICE_DATE = "#DATE";
    private static String CUSTOMER_NAME = "#CUSTOMER_NAME";
    private static String CUSTOMER_ADDRESS = "#ADDRESS";
    private static String CUSTOMER_POSTCODE = "#POSTCODE";
    private static String CUSTOMER_EMAIL = "#EMAIL";
    private static String DISTRIBUTOR_NUMBER = "#DISTRIBUTOR_NUMBER";
    private static String DISTRIBUTOR_NAME = "#DISTRIBUTOR_NAME";
    private static String DISTRIBUTOR_MOBILE = "#DISTRIBUTOR_MOBILE";
    private static String DSD_EMAIL = "#DSD_EMAIL";

    private Context context;
    private Customer customer;
    private Order order;
    private final String invoiceNumber;

    private EmailTemplate(Context context, Customer customer, Order order) {
        this.context = context;
        this.customer = customer;
        this.order = order;
        this.invoiceNumber = getInvoiceNumber(context);
    }

    @NonNull
    public Intent getEmailIntent() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");

        String distributorEmail = getDistributorEmail(context);
        String dsdEmail = getDSDEmail(context);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{customer.email.address, distributorEmail, dsdEmail});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Dining Solutions Direct - Invoice : " + invoiceNumber);
        intent.putExtra(Intent.EXTRA_TEXT, "Attached please find Invoice No. " + invoiceNumber);

        try {
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(this.createPdf()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return intent;
    }

    public File createPdf() throws FileNotFoundException, DocumentException {

        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            throw new RuntimeException("State: " + state);
        }

        File pdfFolder = new File(context.getExternalFilesDir(null), "invoices");
        if (!pdfFolder.exists() && !pdfFolder.mkdirs()) {
            throw new RuntimeException("Couldn't make dir: " + pdfFolder.getAbsolutePath());
        }

        File myFile = new File(pdfFolder, "temp.pdf");

        OutputStream output = new FileOutputStream(myFile);

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, output);
        document.open();
        buildInvoice(document);
        document.close();

        return myFile;
    }

    private void buildInvoice(Document document) throws DocumentException {
        Distributor distributor = Distributor.get(context);

        document.add(new Paragraph(t01));
        document.add(new Paragraph(t02.replace(INVOICE_NUMBER, invoiceNumber).replace(INVOICE_DATE, order.created.getDisplayDate())));

        document.add(new Paragraph(c01));
        document.add(new Paragraph(c02.replace(CUSTOMER_NAME, customer.name)));
        document.add(new Paragraph(c03.replace(CUSTOMER_ADDRESS, customer.address.toString())));
        document.add(new Paragraph(c04.replace(CUSTOMER_POSTCODE, customer.address.postcode)));
        document.add(new Paragraph(c05.replace(CUSTOMER_EMAIL, customer.email.address)));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(5);
        float width = PageSize.A4.getWidth();
        table.setWidthPercentage(new float[]{0.5f * width, 0.2f * width, 0.1f * width, 0.1f * width, 0.1f * width}, PageSize.A4);

        addCellCentre(table, "Product Purchased");
        addCellCentre(table, "Batch No.");
        addCellCentre(table, "Price");
        addCellCentre(table, "Quantity");
        addCellCentre(table, "Cost");
        table.setHeaderRows(1);

        for (OrderItem item : order.orderItems) {
            addProduct(table, item.name, item.batch, item.price, item.quantity, item.cost());
        }

        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell("TOTAL");
        PdfPCell c1 = new PdfPCell(new Phrase(String.format("%.2f", order.total())));
        c1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        table.addCell(c1);

        document.add(table);

        document.add(new Paragraph(d01));
        document.add(new Paragraph(d02.replace(DISTRIBUTOR_NUMBER, distributor.number)));
        document.add(new Paragraph(d03.replace(DISTRIBUTOR_NAME, distributor.name)));
        document.add(new Paragraph(d04.replace(DISTRIBUTOR_MOBILE, distributor.mobile)));

        document.add(new Paragraph(f01));
        document.add(new Paragraph(f02));
        document.add(new Paragraph(f03));
    }

    String t01 = "DINING SOLUTIONS DIRECT.COM";
    String t02 = "Invoice Number : #INVOICE_NUMBER                       Date : #DATE";

    String c01 = "Customer Details" ;
    String c02 = "Name : #CUSTOMER_NAME";
    String c03 = "Address : #ADDRESS";
    String c04 = "Postcode : #POSTCODE";
    String c05 = "Email : #EMAIL";

    String d01 = "Distributor";
    String d02 = "Number : #DISTRIBUTOR_NUMBER";
    String d03 = "Name : #DISTRIBUTOR_NAME";
    String d04 = "Mobile : #DISTRIBUTOR_MOBILE";

    String f01 = "Unit 11 and 12, Rose Mills Industrial Estate, Hort Bridge, Ilminster, Somerset, TA19 9PS";
    String f02 = "Email : customersupport@diningsolutionsdirect.com";
    String f03 = "Customer Services Number : 0844 884 111";

    private void addProduct(PdfPTable table, String product, String batch, double price, int quantity, double cost) {
        addCellLeft(table, product);
        addCellCentre(table, batch);
        addCellRight(table, String.format("%.2f", price));
        addCellCentre(table, String.format("%s", quantity));
        addCellRight(table, String.format("%.2f", cost));
    }

    private void addCellCentre(PdfPTable table, String text) {
        addCell(table, text, Element.ALIGN_CENTER);
    }

    private void addCellLeft(PdfPTable table, String text) {
        addCell(table, text, Element.ALIGN_LEFT);
    }

    private void addCellRight(PdfPTable table, String text) {
        addCell(table, text, Element.ALIGN_RIGHT);
    }

    private void addCell(PdfPTable table, String text, int align) {
        PdfPCell c1 = new PdfPCell(new Phrase(text));
        c1.setHorizontalAlignment(align);
        table.addCell(c1);
    }

    public static EmailTemplate createIntent(Context context, Customer mCustomer, Order mOrder) {
        return new EmailTemplate(context, mCustomer, mOrder);
    }

    public static void sendEmail(Context context, Order order) {
        try {
            EmailTemplate template = EmailTemplate.createIntent(context, CurrentCustomer.get(), order);
            Intent intent = template.getEmailIntent();
            context.startActivity(intent);
            OrderRepo.get(context).updateInvoiceNumber(order, template.getInvoiceNumber());
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }

    }

    private String getInvoiceNumber() {
        return invoiceNumber;
    }

    public String getInvoiceNumber(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        long invoiceNumber = Long.parseLong(sharedPref.getString(Prefs.INVOICE.toString(), "1"));
        String distributor = sharedPref.getString(Prefs.NUMBER.toString(), "0555");

        SharedPreferences.Editor ed = sharedPref.edit();
        ed.putString("invoice_start_number", Long.toString(invoiceNumber + 1));
        ed.commit();

        return String.format("%s-%06d", distributor, invoiceNumber);
    }

    public String getDistributorEmail(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getString(Prefs.EMAIL.toString(), "");
    }

    public String getDSDEmail(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPref.getString(Prefs.DSD.toString(), "");
    }
}
