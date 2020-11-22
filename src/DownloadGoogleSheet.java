import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class DownloadGoogleSheet {
    private static final String GOOGLE_SHEET_LINK = "https://docs.google.com/spreadsheets/d/13LzQZoSwDy6i2nvIQTHNePGV7q9cGgCqnhHPux3IVXg/export?format=xlsx";

    public static void downloadSheet() {
        try {
            BufferedInputStream inStream = new BufferedInputStream(new URL(GOOGLE_SHEET_LINK).openStream());
            FileOutputStream file = new FileOutputStream("data/form.xlsx");
            file.write(inStream.readAllBytes());
            file.close();
        } catch (MalformedURLException e) {
            System.err.println("URL given is invalid. Please check again!");
        } catch (IOException e) {
            System.err.println("Unable to get from link or connect to link");
        }
    }

    public static void main(String[] args) {
        downloadSheet();
    }
}
