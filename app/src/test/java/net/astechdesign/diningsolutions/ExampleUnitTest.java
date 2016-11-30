package net.astechdesign.diningsolutions;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
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