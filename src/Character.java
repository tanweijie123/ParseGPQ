import java.util.ArrayList;
import java.util.List;

public class Character {
    public String ign;
    public String job;
    public int floor;
    public List<String> alias;

    public Character() {
        this.ign = "";
        this.job = "";
        this.floor = 0;
        alias = new ArrayList<>();
    }

    public Character (String ign, String job, int floor, String[] alias) {
        this.ign = ign;
        this.job = job;
        this.floor = floor;
        this.alias = new ArrayList<>();

        if (alias != null) {
            for (String s : alias) {
                if (!s.strip().isBlank())
                    this.alias.add(s.strip());
            }
        }
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
        return String.format("[%s | %s] -> FLOOR %d", this.ign, this.job, this.floor);
    }
}
