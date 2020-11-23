package project.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

public class Database {
    private URL url;

    public Database(String url) throws MalformedURLException {
        this.url = new URL(url);
    }

    public List<String> load() throws IOException {
        BufferedInputStream in = new BufferedInputStream(url.openStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));

        return reader.lines()
                .filter(x -> !(x.startsWith("//") || x.isBlank()))
                .collect(Collectors.toList());
    }
}
