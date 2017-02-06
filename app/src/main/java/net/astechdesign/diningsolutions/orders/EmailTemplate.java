package net.astechdesign.diningsolutions.orders;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import com.itextpdf.awt.geom.CubicCurve2D;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.model.Distributor;
import net.astechdesign.diningsolutions.model.Order;
import net.astechdesign.diningsolutions.model.OrderItem;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

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
    private static String ORDER_ITEMS = "#ORDER_ITEMS";

    private String template;
    private Customer customer;
    private Order order;
    private Context context;

    public EmailTemplate(Context context, String template, Customer customer, Order order) {
        this.context = context;
        this.template = template;
        this.customer = customer;
        this.order = order;
    }

    public File createPdf() throws FileNotFoundException, DocumentException {

        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            throw new RuntimeException("State: " + state);
        }

        File pdfFolder = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),
                "invoices");
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

        document.add(new Paragraph(l01.replace(INVOICE_NUMBER, order.invoiceNumber)));
        document.add(new Paragraph(l02.replace(INVOICE_DATE, order.created.getDisplayDate())));
        document.add(new Paragraph(l03));
        document.add(new Paragraph(l04.replace(CUSTOMER_NAME, customer.name)));
        document.add(new Paragraph(l05.replace(CUSTOMER_ADDRESS, customer.address.toString())));
        document.add(new Paragraph(l06.replace(CUSTOMER_POSTCODE, customer.address.postcode)));
        document.add(new Paragraph(l07.replace(CUSTOMER_EMAIL, customer.email.address)));
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

        document.add(new Paragraph(l09));
        document.add(new Paragraph(l10.replace(DISTRIBUTOR_NUMBER, distributor.number)));
        document.add(new Paragraph(l11.replace(DISTRIBUTOR_NAME, distributor.name)));
        document.add(new Paragraph(l12.replace(DISTRIBUTOR_MOBILE, distributor.mobile)));
        document.add(new Paragraph(l13));
        document.add(new Paragraph(l14));
        document.add(new Paragraph(l15));

    }

    private String l01 = "DINING SOLUTIONS DIRECT.COM                               Invoice Number : #INVOICE_NUMBER";
    private String l02 = "                                                                    Date : #DATE";
    private String l03 = "Customer Details" ;
    private String l04 = "Name : #CUSTOMER_NAME";
    private String l05 = "Address : #ADDRESS";
    private String l06 = "Postcode : #POSTCODE";
    private String l07 = "Email : #EMAIL";

    private String l09 = "Distributor";
    private String l10 = "Number : #DISTRIBUTOR_NUMBER";
    private String l11 = "Name : #DISTRIBUTOR_NAME";
    private String l12 = "Mobile : #DISTRIBUTOR_MOBILE";
    private String l13 = "Unit 11 and 12, Rose Mills Industrial Estate, Hort Bridge, Ilminster, Somerset, TA19 9PS";
    private String l14 = "Email : customersupport@diningsolutionsdirect.com";
    private String l15 = "Customer Services Number : 0844 884 111";

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
}
