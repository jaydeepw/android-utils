package net.the4thdimension.android;

import android.os.Environment;
import android.support.test.runner.AndroidJUnit4;
import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by jaydeepw on 1/2/17.
 */
@RunWith(AndroidJUnit4.class)
@SmallTest
public class FileUtilsInstrumentationTest extends AndroidTestCase {

    @Test
    public void listFilesMultiple() throws Exception {

        File tempFile = new File(Environment.getExternalStorageDirectory().getPath());
        File directory = new File(tempFile, "rajababu");
        boolean isDirCreated = directory.mkdir();
        assertEquals("Directory created", true, isDirCreated);

        File file1 = new File(directory, "f1.txt");
        File file2 = new File(directory, "f2.txt");
        file1.createNewFile();
        file2.createNewFile();

        ArrayList files = new ArrayList();
        FileUtils.listFiles(directory, files);

        assertEquals(2, files.size());

        // clean up
        file1.delete();
        file2.delete();
        directory.delete();
    }

    @Test
    public void listFilesZeroFiles() throws Exception {

        File tempFile = new File(Environment.DIRECTORY_DOWNLOADS);
        File directory = new File(tempFile, "rajababu");
        directory.mkdir();

        ArrayList files = new ArrayList();
        FileUtils.listFiles(directory, files);

        assertEquals(0, files.size());

        // clean up
        directory.delete();
    }

}