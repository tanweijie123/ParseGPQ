package project.files;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InvalidObjectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import project.util.Storage;

public class Download {
    private URL url;

    public Download(URL url) {
        this.url = url;
    }

    public List<String> downloadAsListOfString() throws IOException {
        BufferedInputStream in = new BufferedInputStream(url.openStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        return reader.lines()
                .filter(x -> !(x.startsWith("//") || x.isBlank()))
                .collect(Collectors.toList());
    }

    public boolean downloadAsFile(File outputFile) {
        if (outputFile.isDirectory())
            throw new IllegalArgumentException("File given is a directory. Expected file.");

        BufferedInputStream inStream = null;
        FileOutputStream file = null;
        try {
            outputFile.mkdirs();
            inStream = new BufferedInputStream(url.openStream());
            file = new FileOutputStream(outputFile);
            file.write(inStream.readAllBytes());
            file.flush();
            file.close();
            inStream.close();
            return true;
        } catch (SecurityException e) {
            System.err.println("Unable to create your file due to security.");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Unable to get from link or connect to link. This could be due to network issue, " +
                    "or your Google Sheet is not open for unlisted download.");
        } finally {
            try {
                file.close();
                inStream.close();
            } catch (NullPointerException | IOException e) { }
        }
        return false;
    }
}
