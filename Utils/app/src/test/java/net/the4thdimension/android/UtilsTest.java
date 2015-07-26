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
        String capitalizedString = Utils.capitalizeString(stringToCap);
        String expected = "This Is Awesome";

        // Log.i(TAG, "formattedSize: " + formattedSize + " expected: " + expected);
        // use this method because float is not precise
        assertEquals("Capitalization failed", expected,
                capitalizedString);
    }

    @Test
    public void testCapitalizeStringForNull() throws Exception {
        String stringToCap = null;
        String capitalizedString = Utils.capitalizeString(stringToCap);
        String expected = null;

        // Log.i(TAG, "formattedSize: " + formattedSize + " expected: " + expected);
        // use this method because float is not precise
        assertEquals("Capitalization failed", expected,
                capitalizedString);
    }

    @Test
    public void testCapitalizeStringForEmpty() throws Exception {
        String stringToCap = "";
        String capitalizedString = Utils.capitalizeString(stringToCap);
        String expected = "";

        // Log.i(TAG, "formattedSize: " + formattedSize + " expected: " + expected);
        // use this method because float is not precise
        assertEquals("Capitalization failed", expected,
                capitalizedString);
    }

    @Test
    public void testIsValidEmailTrue() throws Exception {
        boolean valid = Utils.isValidEmail("jay@twitter.com");
        boolean expected = true;

        // Log.i(TAG, "formattedSize: " + formattedSize + " expected: " + expected);
        // use this method because float is not precise
        assertEquals("Email invalid", expected,
                valid);
    }

    @Test
    public void testIsValidEmailFalse() throws Exception {
        boolean invalid = Utils.isValidEmail("jay@twitter");
        boolean expected = false;

        // Log.i(TAG, "formattedSize: " + formattedSize + " expected: " + expected);
        // use this method because float is not precise
        assertEquals("Email invalid", expected,
                invalid);
    }
}