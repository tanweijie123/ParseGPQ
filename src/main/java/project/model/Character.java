package project.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * An immutable Character object which contains the information about a Character
 */
public class Character {
    /**
     * Only accept ign with the specific regex, A-Z, a-z, 0-9
     */
    private static final Pattern IGN_REGEX = Pattern.compile("^[A-Za-z0-9]+$");

    private final String ign;
    private final String job;
    private final int floor;
    private final List<String> alias;

    public Character(String ign) {
        ign = ign.strip();

        if (ign.isBlank() || !IGN_REGEX.matcher(ign).matches())
            throw new IllegalArgumentException("[ERROR]: Given IGN is empty or invalid");

        this.ign = ign;
        this.job = "";
        this.floor = 0;
        this.alias = new ArrayList<>();
    }

    private Character(String ign, String job, int floor, String[] alias) {
        //assumes ign is valid. because check is done in previous constructor

        job = job.strip().toUpperCase();

        if (job.isBlank())
            System.err.printf("[WARNING]: %s does not have a job\n", ign);

        if (floor == 0)
            System.err.printf("[WARNING]: %s does not have a floor\n", ign);

        this.ign = ign;
        this.job = job;
        this.floor = floor;

        if (alias != null) {
            this.alias = Arrays.stream(alias).map(x -> x.strip()).filter(x -> !x.isBlank()).collect(Collectors.toList());
        } else {
            this.alias = new ArrayList<>();
        }
    }

    public String getIgn() {
        return this.ign;
    }

    public String getJob() {
        return this.job;
    }

    public int getFloor() {
        return this.floor;
    }

    public List<String> getAlias() {
        return new ArrayList<>(this.alias);
    }

    public Character setJob(String job) {
        return new Character(this.ign, job, this.floor, this.alias.toArray(new String[0]));
    }

    public Character setFloor(int floor) {
        return new Character(this.ign, this.job, floor, this.alias.toArray(new String[0]));
    }

    public Character setAlias(String s) {
        this.alias.add(s.strip());
        return new Character(this.ign, this.job, this.floor, this.alias.toArray(new String[0]));
    }

    public Character setAlias(String[] s) {
        return new Character(this.ign, this.job, this.floor, s);
    }

    public static boolean isSameIgn(Character c1, Character c2) {
        return c1.ign.equals(c2.ign); //case sensitive IGN
    }

    public String print() {
        return String.format("%s[%d]", this.ign, this.floor);
    }

    /**
     * To be safekeep in database.txt
     * @return output string
     */
    public String export() {
        return String.format("%s{%s,%d}=%s", this.ign, this.job, this.floor, this.alias.toString());
    }

    @Override
    public String toString() {
        return String.format("[%s | %s (F%d)] -> %s", this.ign, this.job, this.floor, this.alias.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Character)) return false;

        Character oth = (Character) o;
        return (oth.ign.equals(this.ign) && oth.job.equals(this.job)
                && oth.floor == this.floor && oth.alias.equals(this.alias));
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.ign, this.job, this.floor, this.alias);
    }
}
