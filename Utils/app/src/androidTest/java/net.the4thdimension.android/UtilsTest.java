package net.the4thdimension.android;

import android.test.AndroidTestCase;

/**
 * Created by jay on 25/7/15.
 */
public class UtilsTest extends AndroidTestCase {

    public void testAppVersionNumber() throws Exception {
        String applicationVersionNumber = Utils.getApplicationVersionNumber(null);
        String expected = null;

        // Log.i(TAG, "formattedSize: " + formattedSize + " expected: " + expected);
        // use this method because float is not precise
        assertEquals("App version invalid", expected, applicationVersionNumber);
    }

    public void testIsValidUrlWithValidUrl() throws Exception {
        boolean isValid = Utils.isValidURL("http://jay.com/");
        boolean expected = true;

        assertEquals("Url invalid", expected, isValid);
    }

    public void testIsValidUrlWithInvalidUrl() throws Exception {
        boolean isValid = Utils.isValidURL("//jay.com/");
        boolean expected = false;

        assertEquals("Url invalid", expected, isValid);
    }

    public void testIsValidUrlWithSchemeOnly() throws Exception {
        boolean isValid = Utils.isValidURL("http://");
        boolean expected = false;

        assertEquals("Url invalid", expected, isValid);
    }

    public void testIsValidUrlWithoutScheme() throws Exception {
        boolean isValid = Utils.isValidURL("www.jay.com");
        boolean expected = false;

        assertEquals("Url invalid", expected, isValid);
    }
}