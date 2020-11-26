package project;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import project.files.Download;
import project.util.Storage;

public class DownloadGoogleSheet {
    private static final String GOOGLE_SHEET_LINK = "https://docs.google.com/spreadsheets/d/13LzQZoSwDy6i2nvIQTHNePGV7q9cGgCqnhHPux3IVXg/export?format=xlsx";
    private static final String FILE_LOCATION = "data/form.xlsx";

    public static void main(String[] args) {

        try {
            Download download = new Download(new URL(GOOGLE_SHEET_LINK));
            //List<String> checkIfRedirectToHTML = download.downloadAsListOfString();
            boolean success = download.downloadAsFile(new File(FILE_LOCATION));
            if (success) {
                System.out.println("Google Sheet has been downloaded and stored in " + FILE_LOCATION);
            } else {
                System.out.println("Download Google Sheet failed.");
            }
        } catch (MalformedURLException e) {
            System.err.println("URL given for GOOGLE_SHEET_LINK is erroneous.");
        }
    }
}
