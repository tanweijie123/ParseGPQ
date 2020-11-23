package project;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import project.util.Storage;

public class DownloadGoogleSheet {
    private static final String GOOGLE_SHEET_LINK = "https://docs.google.com/spreadsheets/d/13LzQZoSwDy6i2nvIQTHNePGV7q9cGgCqnhHPux3IVXg/export?format=xlsx";

    public static void downloadSheet() {
        try {
            BufferedInputStream inStream = new BufferedInputStream(new URL(GOOGLE_SHEET_LINK).openStream());
            FileOutputStream file = new FileOutputStream("data/form.xlsx");
            file.write(inStream.readAllBytes());
            file.flush();
            file.close();
            inStream.close();

            List<String> content = new Storage("data/form.xlsx").load();
            if (content.size() > 0 && content.get(0).equals("<!DOCTYPE html>")) {
                Files.deleteIfExists(Paths.get("data/form.xlsx"));
                throw new InvalidObjectException("Invalid download");
            }
        } catch (MalformedURLException e) {
            System.err.println("URL given is invalid. Please check again!");
        } catch (InvalidObjectException e) {
            System.err.println("Download excel file failed. " +
                    "Please check if Google Sheet exists and its permission is public.");
        } catch (IOException e) {
            System.err.println("Unable to get from link or connect to link");
        }
    }

    public static void main(String[] args) {
        downloadSheet();
    }
}
