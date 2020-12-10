package project.logic;

import project.model.Character;

import java.util.HashMap;

public class Database {
    private static HashMap<String, Character> aliasMap = new HashMap<>();

    public static Character put(String s, Character c) {
        return aliasMap.put(s.toLowerCase(), c);
    }

    public static Character get(String s) {
        return aliasMap.get(s.toLowerCase());
    }

    public static boolean containsKey(String s) {
        return aliasMap.containsKey(s.toLowerCase());
    }
}
