package project.logic;

import project.files.ExportExcel;
import project.files.Config;
import project.model.Character;
import project.model.rule.Rule;
import project.model.Team;
import project.model.Tunnel;
import project.model.rule.RuleList;
import project.util.EntryParser;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Assignment {
    private static List<Character> participantList = new ArrayList<>();
    private static List<Tunnel> tunnelList = new ArrayList<>();

    public static void loadParticipantList(List<String> list) {
        participantList = list.stream()
                .filter(x -> !x.isBlank())
                .map(x -> parseStringToCharacter(x))
                .sorted(Comparator.comparingInt((Character x) -> x.getFloor()).reversed())
                .distinct()
                .collect(Collectors.toList());
    }

    public static void loadTunnel() {
        int numParty = (int) Math.ceil(participantList.size() / 6.0);
        int numTunnel = (int) Math.ceil(numParty / 3.0);

        for (int i = 1; i <= numTunnel; i++) {
            Tunnel t = new Tunnel(i);
            tunnelList.add(t);
        }
    }

    public static void compactEveryTunnel() {
        tunnelList.stream().forEach(Tunnel::compact);
    }

    /** PRINTING METHODS */

    public static void printAllParticipants(List<Character> participantList) {
        System.out.println("================================== ALL PARTICIPANTS ================================");
        for (int i = 1; i <= participantList.size(); i++) {
            Character c = participantList.get(i-1);
            System.out.printf("%d: %s\n", i, c);
        }
        System.out.println("================================= END OF PARTICIPANTS ===============================\n\n");
    }

    public static void printAllParticipants() { //todo: merge with above after deleting main
        printAllParticipants(participantList);
    }

    public static void printTunnelDetails() {
        tunnelList.sort(Comparator.comparingInt(x -> x.getId()));
        System.out.println("===================================== ASSIGNING ====================================");
        System.out.println("Generated at: " + DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").format(LocalDateTime.now()));

        for (int i = 1; i <= tunnelList.size(); i++) {
            Tunnel t = tunnelList.get(i-1);
            t.sortByFloor();

            System.out.println("===================================== TUNNEL " + t.getId() + " =====================================");

            System.out.printf(" ||%-25.25s||%-25.25s||%-25.25s||\n", " Team 1:", " Team 2:", " Team 3:");
            for (int j = 0; j < 6; j++) {
                try {
                    String first = "~~~~~EMPTY~~~~~", second = "~~~~~EMPTY~~~~~", third = "~~~~~EMPTY~~~~~";
                    if (t.getTeam1().size() > j)
                        first = t.getTeam1().get(j).print();
                    if (t.getTeam2().size() > j)
                        second = t.getTeam2().get(j).print();
                    if (t.getTeam3().size() > j)
                        third = t.getTeam3().get(j).print();

                    System.out.printf(" ||%-25.25s||%-25.25s||%-25.25s||\n",
                            " " + first,
                            " " + second,
                            " " + third);
                } catch (IndexOutOfBoundsException e) { } //Not possible to happen
            }
            System.out.println("================================== END OF TUNNEL " + t.getId() + " ==================================\n");
        }
    }

    public static void exportToExcel() {
        tunnelList.sort(Comparator.comparingInt(x -> x.getId()));
        List<String> export = new ArrayList<>();

        export.add(",,Generated at: " + DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").format(LocalDateTime.now()));

        for (int i = 0; i < tunnelList.size(); i++) {
            Tunnel t = tunnelList.get(i);
            t.sortByFloor();

            //Print header
            export.add(",,,Tunnel " + t.getId() + ",,,");
            export.add(",Team1,Floor,Team2,Floor,Team3,Floor");

            for (int j = 0; j < 6; j++) {
                String join = ",";

                if (t.getTeam1().size() > j) {
                    join += t.getTeam1().get(j).getIgn() + "," + t.getTeam1().get(j).getFloor() + ",";
                } else {
                    join += ",,";
                }

                if (t.getTeam2().size() > j) {
                    join += t.getTeam2().get(j).getIgn() + "," + t.getTeam2().get(j).getFloor() + ",";
                } else {
                    join += ",,";
                }

                if (t.getTeam3().size() > j) {
                    join += t.getTeam3().get(j).getIgn() + "," + t.getTeam3().get(j).getFloor() + ",";
                } else {
                    join += ",,";
                }

                export.add(join);
            }
            export.add("");
            export.add("");
        }


        //---------------------------------------------------------------------------------------------

        ExportExcel excelExport = new ExportExcel(Config.getConfig().getExportExcelPath(), true);
        String[][] arr = export.stream().map(x -> x.split(",")).toArray(String[][]::new);
        excelExport.export(arr, "");
    }


    /** ASSIGNMENT METHODS */


    /**
     * Default assignment is specified as:
     *    - Characters are added into tunnel from the highest floor to lowest floor
     */
    public static void startDefaultAssignment() {
        int tunnelID = 1;
        while (participantList.size() > 0) {
            assignTunnel(tunnelList, participantList, tunnelID);
            tunnelID++;
        }
    }

    public static void runRule(Rule rule) {
        System.out.println(rule.toString());
        RuleList.getFunction(rule).accept(participantList, tunnelList);
    }

    /** HELPER METHODS */
    private static Character parseStringToCharacter(String s) {
        if (s.contains("("))
            s = s.substring(0, s.indexOf("(") - 1);


        //try entry parser. those green from before will be removed

        String name = EntryParser.parseIgn(s);
        if (Database.containsKey(name)) { //check against database
            return Database.get(name);
        }

        //if it contains "/", it could be full specification
        if (s.contains("/")) {
            String[] split = s.split("/");
            Character c = new Character(name);

            if (split.length > 1) { //have name + (job or floor)
                if (EntryParser.tryParseFloor(split[1])) {
                    //second value is floor
                    c = c.setFloor(EntryParser.parseFloor(split[1]));

                } else {
                    c = c.setJob(split[1]);
                }
            }

            if (split.length > 2) {
                if (EntryParser.tryParseFloor(split[2])) {
                    c = c.setFloor(EntryParser.parseFloor(split[2]));
                }
            }

            return c;
        }

        //else, set the entire input as ign, default all values
        return new Character(name);
    }

    private static void assignTunnel(List<Tunnel> tunnelList, List<Character> charList, int tunnelID) {
        Tunnel t = tunnelList.get(tunnelID - 1);

        while (!t.isFull() && charList.size() > 0) {
            Team team = t.getLowestTeam();
            team.addMember(charList.remove(0));
        }
    }
}
