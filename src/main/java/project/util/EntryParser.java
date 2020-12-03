package project.util;

public class EntryParser {
    public static String parseIgn(String s) {
        String[] split = s.split("/");

        //get rid of number<delimeter>ign
        if (split[0].contains(".")) {
            split[0] = split[0].substring(split[0].indexOf(".") + 1);
        } else if (split[0].contains(")")) {
            split[0] = split[0].substring(split[0].indexOf(")") + 1);
        } else if (split[0].contains(" ")) {
            split[0] = split[0].substring(split[0].indexOf(" ") + 1);
        }

        //get rid of appended comments behind
        if (split[0].contains("(")) {
            split[0] = split[0].substring(0, split[0].indexOf("(") - 1);
        }

        // else: the whole split[0] considered as IGN.

        return split[0].strip();
    }

    public static boolean tryParseFloor(String s) {
        try {
            Integer.parseInt(s.strip());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static int parseFloor(String s) {
        return Integer.parseInt(s.strip());
    }
}
