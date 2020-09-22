import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class Main {
    public static HashMap<String, Character> aliasMap = new HashMap<>();
    public static String date = "";

    public static void main(String[] args) throws IOException {

        try {
            //codepile link: https://www.codepile.net/pile/PrNjYerZ
            Database database = new Database("https://www.codepile.net/raw/PrNjYerZ.rdoc");
            loadDatabase(database.load());
        } catch (IOException e) {
            System.err.println("Unable to load from codepile...");
        }

        Storage participants = new Storage("data/gpq.txt");
        List<Character> participantList = loadParticipants(participants);

        participantList.sort(Comparator.comparingInt((Character x) -> x.floor).reversed());
        printAllParticipants(participantList);

        //Start assigning
        Storage assign = new Storage("data/assign.txt");
        List<Tunnel> tunnelList = assignHard(assign.load(), participantList);

        int tunnelID = 1;
        while (participantList.size() > 0) {
            assignTunnel2(tunnelList, participantList, tunnelID);
            tunnelID++;
        }

        printTunnelDetails(tunnelList);
    }

    public static void loadDatabase(List<String> databaseList) throws IOException {

        String regex = "[A-Za-z0-9]+\\{[A-Za-z0-9]*,\\d\\d?\\}=\\[[A-Za-z0-9, ]*\\]";
        for (int i = 0; i < databaseList.size(); i++) {
            String line = databaseList.get(i);

            if (!Pattern.matches(regex, line)) {
                System.out.println("Does not match regex: " + line);
                continue;
            }

            String ign = line.substring(0, line.indexOf("{"));
            String job = line.substring(line.indexOf("{") + 1, line.indexOf(","));
            String floor = line.substring(line.indexOf(",") + 1, line.indexOf("}"));

            line = line.substring(line.indexOf("[") + 1, line.indexOf("]"));
            String[] aliasSplit = line.split(",");

            Character c = new Character(ign, job, Integer.parseInt(floor), aliasSplit);

            //TODO: check for duplicate ign in aliasMap
            aliasMap.put(ign, c);

            for (String s : aliasSplit) {
                aliasMap.put(s.toLowerCase(), c); //ALL ALIAS matches CASE INSENSITIVE , ALL LOWER CASE
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
                Character c = new Character();

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

                    c.ign = split[0].strip();
                    if (aliasMap.containsKey(c.ign)) {
                        c = aliasMap.get(c.ign);
                    } else if (aliasMap.containsKey(c.ign.toLowerCase())) { //Matched ALIAS
                        c = aliasMap.get(c.ign.toLowerCase());
                    } else {
                        aliasMap.put(c.ign, c);
                    }
                }

                if (split.length > 1) { //have name + (job or floor)
                    if (c.job.equals("")) {
                        c.job = split[1].strip();
                    }
                }

                if (split.length > 2) {
                    try {
                        if (split[2].contains("(")) {
                            split[2] = split[2].substring(0, split[2].indexOf("("));
                        }
                        int flr = Integer.parseInt(split[2].trim());
                        if (flr != 0) {
                            c.floor = flr;
                        }
                    } catch (NumberFormatException e) {
                    }
                }

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
            if (aliasMap.containsKey(ign)) {
                c = aliasMap.get(ign);
            } else if (aliasMap.containsKey(ign.toLowerCase())) {
                c = aliasMap.get(ign.toLowerCase());
            }

            if (participantList.contains(c)) {
                try {
                    int tunnel = Integer.parseInt(s.substring(s.indexOf(">") + 1, s.indexOf("_")).strip());
                    int team = Integer.parseInt(s.substring(s.indexOf("_") + 1).strip());

                    Tunnel t = getTunnelOrCreateNew(tunnelList, tunnel);

                    switch (team) {
                        case 1:
                            if (t.team1 == null)
                                t.team1 = new Team();

                            t.team1.addMember(c);
                            break;

                        case 2:
                            if (t.team2 == null)
                                t.team2 = new Team();

                            t.team2.addMember(c);
                            break;
                        case 3:
                            if (t.team3 == null)
                                t.team3 = new Team();

                            t.team3.addMember(c);
                            break;
                    }

                    participantList.remove(c);

                } catch (NumberFormatException e) {
                    System.err.printf("%s is invalid format.\n", s);
                } //Do nothing if parse failed
            }
        }

        //tunnelList.sort(Comparator.comparingInt(x -> x.id));
        return tunnelList;
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

                    if (j != 0) {
                        System.out.printf(" ||%-25.25s||%-25.25s||%-25.25s||\n",
                                " " + first,
                                " " + second,
                                " " + third);
                    } else {
                        System.out.printf(" ||%-25.25s||%-25.25s||%-25.25s||\n",
                                " <LDR>" + first,
                                " <LDR>" + second,
                                " <LDR>" + third);
                    }
                } catch (IndexOutOfBoundsException e) {
                    break;
                }
            }
            System.out.println("================================== END OF TUNNEL " + t.id + " ==================================\n");
        }
    }

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

}
