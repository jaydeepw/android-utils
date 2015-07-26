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

    @Test
    public void testCapitalizeString() throws Exception {
        String stringToCap = "this is awesome";
        String formattedSize = Utils.capitalizeString(stringToCap);
        String expected = "This Is Awesome";

        // Log.i(TAG, "formattedSize: " + formattedSize + " expected: " + expected);
        // use this method because float is not precise
        assertEquals("Capitalization failed", expected,
                formattedSize);
    }

    @Test
    public void testCapitalizeStringForNull() throws Exception {
        String stringToCap = null;
        String formattedSize = Utils.capitalizeString(stringToCap);
        String expected = null;

        // Log.i(TAG, "formattedSize: " + formattedSize + " expected: " + expected);
        // use this method because float is not precise
        assertEquals("Capitalization failed", expected,
                formattedSize);
    }

    @Test
    public void testCapitalizeStringForEmpty() throws Exception {
        String stringToCap = "";
        String formattedSize = Utils.capitalizeString(stringToCap);
        String expected = "";

        // Log.i(TAG, "formattedSize: " + formattedSize + " expected: " + expected);
        // use this method because float is not precise
        assertEquals("Capitalization failed", expected,
                formattedSize);
    }
}