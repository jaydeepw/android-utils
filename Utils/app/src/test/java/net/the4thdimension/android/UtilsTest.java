package net.the4thdimension.android;

import junit.framework.TestCase;

import org.junit.Test;

/**
 * Created by jay on 25/7/15.
 */
public class UtilsTest extends TestCase {

    @Test
    public void testFormatSizeKb() throws Exception {
        String formattedSize = Utils.formatSize(1024);
        // expected value is 212
        String expected = "1 KB";

        // Log.i(TAG, "formattedSize: " + formattedSize + " expected: " + expected);
        // use this method because float is not precise
        assertEquals("Conversion from bytes to KB", expected,
                formattedSize);
    }

    @Test
    public void testFormatSizeMb() throws Exception {
        String formattedSize = Utils.formatSize(1024*1024);
        String expected = "1 MB";

        // Log.i(TAG, "formattedSize: " + formattedSize + " expected: " + expected);
        // use this method because float is not precise
        assertEquals("Conversion from bytes to MB", expected,
                formattedSize);
    }
}