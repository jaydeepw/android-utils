package net.the4thdimension.android;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by jaydeepw on 12/5/16.
 */

public class FileUtils {

    /**
     * List the files in the parameter directory.
     * @param directoryName
     * @param files
     */
    public static void listFiles(String directoryName, ArrayList<File> files) {
        File directory = new File(directoryName);
        listFiles(directory, files);

/*        // get all the files from a directory
        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isFile()) {
                files.add(file);
            } else if (file.isDirectory()) {
                listFiles(file.getAbsolutePath(), files);
            }
        }*/
    }

    /**
     * List the files in the parameter directory.
     * @param files
     */
    public static void listFiles(File directory, ArrayList<File> files) {

        // get all the files from a directory
        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isFile()) {
                files.add(file);
            } else if (file.isDirectory()) {
                listFiles(file.getAbsolutePath(), files);
            }
        }   // end for
    }
}
