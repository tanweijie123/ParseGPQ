package project;

import java.io.IOException;
import java.net.UnknownHostException;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;

public class CheckVersion {
    private static String releaseLink = "https://tanwj.link/Download";

    public static void main(String[] args) throws IOException {
        String ver = CheckVersion.class.getPackage().getImplementationVersion();
        System.out.println("Local Version: v" + ver + "\n");

        String releaseVer = "";
        try {
            releaseVer = Jsoup.connect(releaseLink).execute().parse().select("blockquote").select("a").text();
        } catch (UnknownHostException e) {
            //unable to connect to internet
        } catch (HttpStatusException e) {
            //webpage is not available / cannot be found / 404
        } catch (IOException e) {
            //any type of general IO exception
        }

        if (releaseVer.isBlank()) {
            System.out.println("Unable to check for newer version.");
        } else if (!releaseVer.strip().equals(ver.strip())) {
            System.out.printf("Different version detected. GitHub=v%s; Local=v%s\n", releaseVer, ver);
        } else {
            System.out.println("You have the latest version!");
        }
    }
}
