package net.astechdesign.diningsolutions.invoices;

import android.os.Environment;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PDFInvoice {

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

        //Create time stamp
        Date date = new Date() ;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);

        File myFile = new File(pdfFolder, timeStamp + ".pdf");

        OutputStream output = new FileOutputStream(myFile);

        Document document = new Document(PageSize.A5);
        PdfWriter.getInstance(document, output);
        document.open();
        //Step 4 Add content
        document.add(new Paragraph("Some String"));
        document.add(new Paragraph("Another String"));

        document.close();

        return myFile;
    }
}
