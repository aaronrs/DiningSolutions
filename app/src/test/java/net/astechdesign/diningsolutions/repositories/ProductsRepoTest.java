package net.astechdesign.diningsolutions.repositories;

import android.content.Context;
import android.content.res.AssetManager;

import net.astechdesign.diningsolutions.model.Product;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

public class ProductsRepoTest {

    @Rule
    public MockitoRule rule = MockitoJUnit.rule();

    @Mock private Context context;
    @Mock private AssetManager assetManager;


    @Before
    public void setUp() throws Exception {
        InputStream is = new ByteArrayInputStream(products.getBytes());

        when(context.getAssets()).thenReturn(assetManager);
        when(assetManager.open("db/products.csv")).thenReturn(is);
    }

    @Test
    public void shouldReturnListOfProducts() throws Exception {
        List<Product> productsList = ProductsRepo.get(context);
        assertThat(productsList.size(), is(4));
    }

    private String products = "1322|shellfish|NEW ZEALAND GREEN LIPPED MUSSELS|30.00\n" +
            "1323|hors-doeuvres|PETIT CROLINES|30.00\n" +
            "1327|fish|ARBROATH SMOKIES|30.00\n" +
            "3425|meat-specialities|OUTDOOR REARED STUFFED PORK CHOPS|30.00";
}