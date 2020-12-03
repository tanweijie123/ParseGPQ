package project.files;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;

class ConfigJsonAdapter {

    static boolean saveSettings(Config config) {
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();

        Gson gson = builder.create();
        String exportString = gson.toJson(config);

        try {
            File file = new File("config.json");
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write(exportString);
            writer.flush();
            writer.close();
            return true;
        } catch (IOException e) {
            System.err.println("Unable to write to config.json");
            return false;
        }
    }

    static Optional<Config> loadSettings() {
        try {
            Scanner readFile = new Scanner(new File("config.json"));
            String importString = readFile.useDelimiter("\\Z").next();
            readFile.close();

            Gson gson = new Gson();
            return Optional.of(gson.fromJson(importString, Config.class));
        } catch (FileNotFoundException e) {
            return Optional.empty(); //create default file?
        }
    }
}
