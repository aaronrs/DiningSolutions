package net.astechdesign.diningsolutions;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import net.astechdesign.diningsolutions.model.Address;
import net.astechdesign.diningsolutions.model.Customer;
import net.astechdesign.diningsolutions.model.DSDDate;
import net.astechdesign.diningsolutions.model.Distributor;
import net.astechdesign.diningsolutions.model.Email;
import net.astechdesign.diningsolutions.model.Order;
import net.astechdesign.diningsolutions.model.Phone;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    private static String INVOICE_NUMBER = "#INVOICE_NUMBER";
    private static String INVOICE_DATE = "#DATE";
    private static String CUSTOMER_NAME = "#CUSTOMER_NAME";
    private static String CUSTOMER_ADDRESS = "#ADDRESS";
    private static String CUSTOMER_POSTCODE = "#POSTCODE";
    private static String CUSTOMER_EMAIL = "#EMAIL";
    private static String DISTRIBUTOR_NUMBER = "#DISTRIBUTOR_NUMBER";
    private static String DISTRIBUTOR_NAME = "#DISTRIBUTOR_NAME";
    private static String DISTRIBUTOR_MOBILE = "#DISTRIBUTOR_MOBILE";

    @Test
    public void pdfTest() throws Exception {

        Customer customer = new Customer(null, "Name",
                Email.create("me@gmail.com"),
                Phone.create("09876543"),
                true,
                new DSDDate(),
                "",
                new Address(null, "Potters Barn", "The Street", "", "The Town", "County", "AB1 1AB"));
        Order order = new Order(null, null, new DSDDate(), "1234567890");
        Distributor distributor = new Distributor("5555", "Tom Jones", "0987654321", "tom@gmail.com");

        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, output);
        document.open();
        
        document.add(new Paragraph(t01));
        document.add(new Paragraph(t02.replace(INVOICE_NUMBER, order.invoiceNumber).replace(INVOICE_DATE, order.created.getDisplayDate())));

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

        addProduct(table, "LAMB KOFTA", "6787-8766", 54.44, 2, 108.88);
        addProduct(table, "CHICKEN KEBABS", "6787-8766", 51.90, 1, 51.99);

        table.addCell(" ");
        table.addCell(" ");
        table.addCell(" ");
        table.addCell("TOTAL");
        PdfPCell c1 = new PdfPCell(new Phrase("160.87"));
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

        document.close();

        output.writeTo(new FileOutputStream("test.pdf"));

    }

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
    
    
    @Test
    public void downloadProducts() throws Exception {
        List<String[]> products = new ArrayList();
        String uri = "http://diningsolutionsdirect.co.uk/page/%s/?s=.&post_type=product";
        for (int i = 1; i <= 17; i++) {
            URL pageUri = new URL(String.format(uri, i));
            try (InputStream inputStream = pageUri.openStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                 Writer writer = new StringWriter()) {
                String line;
                int count = 99;
                String[] data = null;
                while ((line = reader.readLine()) != null) {
                    if (line.contains("class=\"product\"") ||
                            line.contains("class=\"product first\"") ||
                            line.contains("class=\"product last\"")
                            ) {
                        count = 0;
                    }
                    if (count < 99) {
//                        if (line.contains("http://diningsolutionsdirect.co.uk/wp-content/uploads/") && line.contains(".jpg"))
//                            System.out.println(count + ":" + line);
                        if (line.contains("<strong class=\"below-thumb\">")) {
                            data = new String[2];
                            data[0] = line.substring(line.indexOf(">")+1, line.indexOf("</"));
                        }
                        if (line.contains("<span class=\"price\"><span class=\"amount\">") && data != null) {
                            data[1] = line.substring(line.indexOf("&pound;")+7, line.indexOf("</"));
                            products.add(data);
                            System.out.println(data[0] + "|" + data[1]);
                        }
                    }
//                    if (count == 5) {
//                        writer.write(line.split("\"")[1]);
//                    }
//                    if (count == 11) {
//                        writer.write(line.split(">")[1].split("<")[0]);
//                    }
//                    if (count == 12) {
//                        writer.write(line);
//                    }
//                    count++;
                }
            }
        }

    }
}