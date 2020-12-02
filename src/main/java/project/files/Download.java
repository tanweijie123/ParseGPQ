package project.files;

import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;

import java.io.*;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.List;
import java.util.stream.Collectors;

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

        try {
            outputFile.getParentFile().mkdirs();
            outputFile.createNewFile();
        } catch (IOException e) {
            System.err.println("Unable to create a new folder / file. Do check if your anti-virus is blocking the write access.");
            return false;
        }

        try {
            Connection.Response conn = Jsoup.connect(this.url.toString()).ignoreContentType(true).execute();
            FileOutputStream output = new FileOutputStream(outputFile);
            output.write(conn.bodyAsBytes());
            output.close();
            return true;
        } catch (HttpStatusException e) {
            System.err.println("Error when fetching from URL. Do check if your Google Sheet is unlisted/ public access");
            e.printStackTrace();
        } catch (UnknownHostException e) {
            System.err.println("Unable to get from link or connect to link. This could be due to network issue");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Unable to write into file.");
            e.printStackTrace();
        }
        return false;
    }
}
