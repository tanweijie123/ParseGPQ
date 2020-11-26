package project;

import java.io.IOException;
import java.net.UnknownHostException;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;

public class CheckVersion {
    private static String releaseLink = "https://github.com/tanweijie123/ParseGPQ/releases/latest/";

    public static void main(String[] args) throws IOException {
        String ver = CheckVersion.class.getPackage().getImplementationVersion();
        System.out.println("Local Version: v" + ver + "\n");

        String versionInGitHub = "";
        try {
            String link = Jsoup.connect(releaseLink).get().location();
            versionInGitHub = link.substring(link.lastIndexOf("/") + 2); // remove /v from version
        } catch (UnknownHostException e) {
            //unable to connect to internet
        } catch (HttpStatusException e) {
            //webpage is not available / cannot be found / 404
        } catch (IOException e) {
            //any type of general IO exception
        }

        if (versionInGitHub.isBlank()) {
            System.out.println("Unable to check for newer version.");
        } else if (versionInGitHub != ver) {
            System.out.printf("Different version detected. GitHub=v%s; Local=v%s\n", versionInGitHub, ver);
        }
    }
}
