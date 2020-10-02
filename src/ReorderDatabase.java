import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ReorderDatabase {
    private static List<Character> characterList = new ArrayList<>();
    public static void main(String[] args) throws IOException {
        Database database = new Database("https://www.codepile.net/raw/PrNjYerZ.rdoc");
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
                c = new Character(ign, job, Integer.parseInt(floor), aliasSplit, false);
                characterList.add(c);
            } else { //edited only floor or alias
                if (c.getFloor() != Integer.parseInt(floor))
                    c.setFloor(Integer.parseInt(floor));
                mergeAlias(c.alias, aliasSplit);
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
            if (!existingAlias.stream().anyMatch("search_value"::equalsIgnoreCase) && !s.isBlank())
                existingAlias.add(s);
        }
    }
}