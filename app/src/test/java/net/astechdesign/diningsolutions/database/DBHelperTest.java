package net.astechdesign.diningsolutions.database;

import android.database.sqlite.SQLiteDatabase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class DBHelperTest {

    private DBHelper dbHelper;

    @Before
    public void setUp() throws Exception {
        dbHelper = DBHelper.get(RuntimeEnvironment.application);
    }

    @Test
    public void should() throws Exception {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        assertNotNull(db);
    }
}