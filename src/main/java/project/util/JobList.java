package project.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JobList {
    private static final List<String> jobList = Arrays.asList(
            "ANIMA: Ho Young",
            "CHILD OF GOD: Zero",
            "CYGNUS: Flame Wizard",
            "CYGNUS: Soul Master",
            "CYGNUS: Mihile",
            "CYGNUS: Night Walker",
            "CYGNUS: Striker",
            "CYGNUS: Wind Breaker",
            "EXPLORER: Bow Master",
            "EXPLORER: Marksman",
            "EXPLORER: Pathfinder",
            "EXPLORER: Bishop",
            "EXPLORER: Fire Poison",
            "EXPLORER: Ice Lightning",
            "EXPLORER: Viper",
            "EXPLORER: Captain",
            "EXPLORER: Cannon Master",
            "EXPLORER: Dual Blade",
            "EXPLORER: Zen",
            "EXPLORER: Night Lord",
            "EXPLORER: Shadower",
            "EXPLORER: Dark Knight",
            "EXPLORER: Hero",
            "EXPLORER: Paladin",
            "FLORA: Adele",
            "FLORA: Ark",
            "FLORA: Illium",
            "HERO: Aran",
            "HERO: Evan",
            "HERO: Luminous",
            "HERO: Mercedes",
            "HERO: Phantom",
            "HERO: Eunwol",
            "NOVA : Cadena",
            "NOVA: Angelic Buster",
            "NOVA: Kaiser",
            "RESISTANCE: Battle Mage",
            "RESISTANCE: Blaster",
            "RESISTANCE: Citizen",
            "RESISTANCE: Demon Avenger",
            "RESISTANCE: Demon Slayer",
            "RESISTANCE: Mechanic",
            "RESISTANCE: Wild Hunter",
            "RESISTANCE: Xenon",
            "SENGOKU: Hayato",
            "SENGOKU: Kanna",
            "SPECIAL: Kinesis"
    );

    public static List<String> getJobList() {
        return new ArrayList<>(jobList);
    }
}
