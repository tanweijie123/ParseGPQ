package project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

import project.model.Character;
import project.model.Team;
import project.model.Tunnel;
import project.util.Database;
import project.util.Storage;

public class Main {
    public static final String DATABASE_REGEX = "[A-Za-z0-9]+\\{[A-Z_]*,\\d\\d?\\}=\\[[A-Za-z0-9, .]*\\]";
    public static HashMap<String, Character> aliasMap = new HashMap<>();
    public static String date = "";
    private static String databaseMods = "";

    public static void main(String[] args) throws IOException {

        try {
            //codepile link: https://www.codepile.net/pile/PrNjYerZ
            //pastebin link: https://pastebin.com/raw/46Z9CNEY
            Database database = new Database("https://pastebin.com/raw/46Z9CNEY");
            loadDatabase(database.load());
        } catch (IOException e) {
            System.err.println("Unable to load from codepile...");
        }

        Storage participants = new Storage("data/gpq.txt");
        List<Character> participantList = loadParticipants(participants);

        participantList.sort(Comparator.comparingInt((Character x) -> x.getFloor()).reversed());
        printAllParticipants(participantList);

        //Start assigning
        Storage assign = new Storage("data/assign.txt");
        List<Tunnel> tunnelList = assignHard(assign.load(), participantList);

        int tunnelID = 1;
        while (participantList.size() > 0) {
            assignTunnel2(tunnelList, participantList, tunnelID);
            tunnelID++;
        }

        compactEveryTunnel(tunnelList);
        printTunnelDetails(tunnelList);
        //printNewMembers();
        //printModifiedMembers();
        exportTunnel(tunnelList);

    }

    public static void loadDatabase(List<String> databaseList) throws IOException {


        for (int i = 0; i < databaseList.size(); i++) {
            String line = databaseList.get(i);

            if (!Pattern.matches(DATABASE_REGEX, line)) {
                System.out.println("Does not match regex: " + line);
                continue;
            }

            String ign = line.substring(0, line.indexOf("{"));
            String job = line.substring(line.indexOf("{") + 1, line.indexOf(","));
            String floor = line.substring(line.indexOf(",") + 1, line.indexOf("}"));

            line = line.substring(line.indexOf("[") + 1, line.indexOf("]"));
            String[] aliasSplit = line.split(",");

            Character c = new Character(ign).setJob(job).setFloor(Integer.parseInt(floor)).setAlias(aliasSplit);

            //TODO: check for duplicate ign in aliasMap
            aliasMap.put(ign, c);

            for (String s : aliasSplit) {
                aliasMap.put(s.toLowerCase().strip(), c); //ALL ALIAS matches CASE INSENSITIVE , ALL LOWER CASE
            }
        }
    }

    public static List<Character> loadParticipants(Storage participants) throws IOException {
        List<String> inputList = participants.load();
        List<Character> charList = new ArrayList<>();

        for (int i = 0; i < inputList.size(); i++) {
            String current = inputList.get(i);

            //Check for date
            if (current.contains("Date>")) {
                date = current.substring(current.indexOf("Date>") + 5);
            } else {

                String[] split = current.split("/");
                Character c = null;

                if (split.length > 0) { //at least have name
                    split[0] = split[0].strip();

                    //TODO: check if before delimiter is an integer
                    if (split[0].contains(".")) {
                        split[0] = split[0].substring(split[0].indexOf(".") + 1);
                    } else if (split[0].contains(")")) {
                        split[0] = split[0].substring(split[0].indexOf(")") + 1);
                    } else if (split[0].contains(" ")) {
                        split[0] = split[0].substring(split[0].indexOf(" ") + 1);
                    } // else: the whole split[0] considered as IGN.

                    String ign = split[0].strip();
                    if (aliasMap.containsKey(ign)) {
                        c = aliasMap.get(ign);
                    } else if (aliasMap.containsKey(ign.toLowerCase())) { //Matched ALIAS
                        c = aliasMap.get(ign.toLowerCase());
                    } else {
                        c = new Character(ign);
                        aliasMap.put(ign, c);
                    }
                }

                if (split.length > 1) { //have name + (job or floor)
                    trySetFloor(c, split[1]);
                    if (c.getFloor() == 0) {
                        if (c.getJob().equals("")) {
                            c.setJob(split[1].strip());
                        }
                    }
                }

                if (split.length > 2) {
                    if (split[2].contains("(")) {
                        split[2] = split[2].substring(0, split[2].indexOf("("));
                    }
                    trySetFloor(c, split[2]);
                }
                if (!charList.contains(c)) //TODO: change into TreeSet?
                    charList.add(c);
            }
        }

        return charList;
    }

    public static void printAllParticipants(List<Character> participantList) {
        System.out.println("================================== ALL PARTICIPANTS ================================");
        for (int i = 1; i <= participantList.size(); i++) {
            Character c = participantList.get(i-1);
            System.out.printf("%d: %s\n", i, c);
        }
        System.out.println("================================= END OF PARTICIPANTS ===============================\n\n");
    }

    public static List<Tunnel> assignHard(List<String> assignList, List<Character> participantList) {
        List<Tunnel> tunnelList = new ArrayList<>();

        for (String s : assignList) {
            String ign = s.substring(0, s.indexOf(">"));
            Character c = null;

            if (ign.contains("*")) { //Job assignment
                String[] split = ign.split("\\*");
                c = participantList.stream().filter(x -> split[1].equalsIgnoreCase(x.getJob()))
                        .sorted((x, y) -> Integer.compare(y.getFloor(),x.getFloor())).findFirst().orElse(null);
            } else {
                if (aliasMap.containsKey(ign)) {
                    c = aliasMap.get(ign);
                } else if (aliasMap.containsKey(ign.toLowerCase())) {
                    c = aliasMap.get(ign.toLowerCase());
                }
            }

            boolean success = false;
            if (participantList.contains(c)) {
                try {
                    int tunnel = Integer.parseInt(s.substring(s.indexOf(">") + 1, s.indexOf("_")).strip());
                    int team = Integer.parseInt(s.substring(s.indexOf("_") + 1).strip());

                    Tunnel t = getTunnelOrCreateNew(tunnelList, tunnel);

                    switch (team) {
                        case 1:
                            if (t.team1 == null)
                                t.team1 = new Team();

                            success = t.team1.addMember(c);
                            break;

                        case 2:
                            if (t.team2 == null)
                                t.team2 = new Team();

                            success = t.team2.addMember(c);
                            break;
                        case 3:
                            if (t.team3 == null)
                                t.team3 = new Team();

                            success = t.team3.addMember(c);
                            break;
                    }
                } catch (NumberFormatException e) {
                    System.err.printf("%s is invalid format.\n", s);
                } //Do nothing if parse failed
            }

            if (success)
                participantList.remove(c);
            else {
                System.out.println("Unable to assign: " + s);
            }
        }

        return tunnelList;
    }

    public static void compactEveryTunnel(List<Tunnel> tunnelList) {
        tunnelList.stream().forEach(Tunnel::compact);
    }

    public static void assignTunnel2(List<Tunnel> tunnelList, List<Character> charList, int tunnelID) {
        Tunnel t = getTunnelOrCreateNew(tunnelList, tunnelID);

        while (!t.isFull() && charList.size() > 0) {
            Team team = t.getLowestTeam();
            team.addMember(charList.remove(0));
        }
    }

    public static void printTunnelDetails(List<Tunnel> tunnelList) {
        tunnelList.sort(Comparator.comparingInt(x -> x.id));
        System.out.println("===================================== ASSIGNING ====================================");

        if (!date.isBlank())
            System.out.println("For Date: " + date);

        for (int i = 1; i <= tunnelList.size(); i++) {
            Tunnel t = tunnelList.get(i-1);
            t.sortByFloor();

            System.out.println("===================================== TUNNEL " + t.id + " =====================================");

            System.out.printf(" ||%-25.25s||%-25.25s||%-25.25s||\n", " Team 1:", " Team 2:", " Team 3:");
            for (int j = 0; j < 6; j++) {
                try {
                    String first = "~~~~~EMPTY~~~~~", second = "~~~~~EMPTY~~~~~", third = "~~~~~EMPTY~~~~~";
                    if (t.team1.teamList.size() > j)
                        first = t.team1.teamList.get(j).print();
                    if (t.team2.teamList.size() > j)
                        second = t.team2.teamList.get(j).print();
                    if (t.team3.teamList.size() > j)
                        third = t.team3.teamList.get(j).print();

                    System.out.printf(" ||%-25.25s||%-25.25s||%-25.25s||\n",
                            " " + first,
                            " " + second,
                            " " + third);
                } catch (IndexOutOfBoundsException e) { } //Not possible to happen
            }
            System.out.println("================================== END OF TUNNEL " + t.id + " ==================================\n");
        }
    }

    private static void exportTunnel(List<Tunnel> tunnelList) {
        tunnelList.sort(Comparator.comparingInt(x -> x.id));
        List<String> export = new ArrayList<>();

        if (!date.isBlank())
            export.add("Date: " + date);

        for (int i = 0; i < tunnelList.size(); i++) {
            Tunnel t = tunnelList.get(i);
            t.sortByFloor();

            //Print header
            export.add(",,,Tunnel " + t.id + ",,,");
            export.add(",Team1,Floor,Team2,Floor,Team3,Floor");

            for (int j = 0; j < 6; j++) {
                String join = ",";

                if (t.team1.teamList.size() > j) {
                    join += t.team1.teamList.get(j).getIgn() + "," + t.team1.teamList.get(j).getFloor() + ",";
                } else {
                    join += ",,";
                }

                if (t.team2.teamList.size() > j) {
                    join += t.team2.teamList.get(j).getIgn() + "," + t.team2.teamList.get(j).getFloor() + ",";
                } else {
                    join += ",,";
                }

                if (t.team3.teamList.size() > j) {
                    join += t.team3.teamList.get(j).getIgn() + "," + t.team3.teamList.get(j).getFloor() + ",";
                } else {
                    join += ",,";
                }

                export.add(join);
            }
            export.add("");
            export.add("");
        }


        //---------------------------------------------------------------------------------------------

        ExportExcel excelExport = new ExportExcel("data/output.xlsx", (!date.isBlank()));
        String[][] arr = export.stream().map(x -> x.split(",")).toArray(String[][]::new);
        excelExport.export(arr, databaseMods);
    }

    /*
    private static void printNewMembers() {
        List<String> printOut = new ArrayList<>();
        printOut.add("Consider adding these (if correct info) to the database ~");
        aliasMap.values().stream()
                .distinct()
                .filter(x -> x.isNew)
                .filter(x -> x.getFloor() != 0 && !x.getJob().isBlank())
                .map(x -> x.export())
                .forEach(printOut::add);

        String out = appendLines("", printOut);
        System.out.println(out);
        databaseMods += out;
    }

    private static void printModifiedMembers() {
        List<String> printOut = new ArrayList<>();
        printOut.add("Consider modifying these (if correct info) to the database ~");
        aliasMap.values().stream()
                .distinct()
                .filter(x -> x.isModified)
                .filter(x -> x.getFloor() != 0 && !x.getJob().isBlank())
                .map(x -> x.export())
                .forEach(printOut::add);

        String out = appendLines("", printOut);
        System.out.println(out);
        databaseMods += out;
    }
    */

    /** HELPER METHODS */
    private static Tunnel getTunnelOrCreateNew(List<Tunnel> tunnelList, int tunnelID) {
        return tunnelList.stream()
                .filter(x -> x.id == tunnelID)
                .findFirst()
                .orElseGet(() -> {
                    Tunnel tu = new Tunnel(tunnelID);
                    tunnelList.add(tu);
                    return tu;
                });
    }

    private static void trySetFloor(Character c, String floor) {
        c.setFloor(getNumberFromStr(floor));
    }

    /**
     * Tries to parse a string into an integer.
     * @param number The string which contains the number.
     * @return the number in the string. If it is not a number, -1 will be returned.
     */
    private static int getNumberFromStr(String number) {
        try {
            int num = Integer.parseInt(number.trim());
            return num;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static String appendLines(String mainString, List<String> toAppend) {
        if (mainString == null)
            mainString = "";

        for (String s : toAppend) {
            mainString += s + "\n";
        }

        return mainString;
    }

}
