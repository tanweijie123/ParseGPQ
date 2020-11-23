package project.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This class will handle the retrieving and storing into data file.
 */
public class Storage {
    /** The filepath to store or retrieve from. */
    private File file;

    /**
     * Constructs a storage object with the given file path.
     *
     * @param filePath The filepath to interact with.
     */
    public Storage(String filePath) {
        this.file = new File(filePath);
    }

    /**
     * Creates the file if it does not exist in the system.
     */
    private void createFileIfNotExist() throws IOException {
        assert(!this.file.isDirectory());

        file.getParentFile().mkdirs();
        file.createNewFile();
    }

    /**
     * Reads the content of the file provided and store it in a list.
     *
     * @return The content of the file as a list of string.
     */
    public List<String> load() throws IOException {
        createFileIfNotExist();
        return readAllLines();
    }

    /**
     * Reads all the content from the file provided.
     *
     * @return content of file as a {@code List} of String.
     */
    private List<String> readAllLines() throws FileNotFoundException {
        assert(this.file.canRead());

        List<String> content = new ArrayList<>();
        Scanner readFile = new Scanner(file);
        while(readFile.hasNextLine()) {
            String ln = readFile.nextLine();
            if (!ln.startsWith("//") && !ln.isBlank()) //Treat all // as comment lines
                content.add(ln);
        }
        readFile.close();
        return content;
    }

    /**
     * Saves the list of Strings into the file.
     *
     * @param contents A list of Strings to store into file
     * @return The status of which the saving to file is successful.
     */
    public boolean saveToFile(List<String> contents) throws IOException {
        createFileIfNotExist();
        assert(this.file.canWrite());

        try {
            FileWriter writer = new FileWriter(this.file);
            for (String s : contents) {
                writer.write(s + "\n");
            }
            writer.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
