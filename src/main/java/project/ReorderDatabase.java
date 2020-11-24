package project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import project.model.Character;
import project.util.Database;
import project.util.Storage;

public class ReorderDatabase {
    private static List<Character> characterList = new ArrayList<>();
    public static void main(String[] args) throws IOException {
        Database database = new Database("https://pastebin.com/raw/46Z9CNEY");
        List<String> data = database.load();

        data.stream().forEach(x -> {
            if (!Pattern.matches(Main.DATABASE_REGEX, x)) {
                System.out.println("Does not match regex: " + x);
                return;
            }

            String ign = x.substring(0, x.indexOf("{"));
            String job = x.substring(x.indexOf("{") + 1, x.indexOf(","));
            String floor = x.substring(x.indexOf(",") + 1, x.indexOf("}"));

            x = x.substring(x.indexOf("[") + 1, x.indexOf("]"));
            String[] aliasSplit = x.split(",");

            //check if ign exist within list
            Character c = getByIgn(ign);
            if (c == null) {
                c = new Character(ign).setJob(job).setFloor(Integer.parseInt(floor)).setAlias(aliasSplit);
                characterList.add(c);
            } else { //edited only floor or alias
                if (c.getFloor() != Integer.parseInt(floor))
                    c.setFloor(Integer.parseInt(floor));
                mergeAlias(c.getAlias(), aliasSplit);
            }
        });

        Storage databaseStorage = new Storage("data/reOrderDatabase.txt");

        try {
            databaseStorage.saveToFile(characterList.stream().sorted(Comparator.comparing(x -> x.getIgn())).map(x -> x.export()).collect(Collectors.toList()));
        } catch (IOException e) {
            System.err.println("Unable to save to file...");
        }
    }

    private static Character getByIgn(String ign) {
        return characterList.stream().filter(x -> x.getIgn().equals(ign)).findFirst().orElse(null);
    }

    private static void mergeAlias(List<String> existingAlias, String[] newAlias) {
        for (String s : newAlias) {
            s = s.strip();
            if(checkUniqueIgnoreCase(existingAlias, s))
                existingAlias.add(s);
        }
    }

    private static boolean checkUniqueIgnoreCase(List<String> existingAlias, String s) {
        Optional<String> result = existingAlias.stream().filter(x -> x.equalsIgnoreCase(s)).findFirst();
        return result.isEmpty();
    }
}
