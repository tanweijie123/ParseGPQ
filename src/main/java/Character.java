import java.util.ArrayList;
import java.util.List;

public class Character {
    private final String ign;
    private String job;
    private int floor;
    public List<String> alias;
    public boolean isNew;
    public boolean isModified;

    public Character(String ign) {
        this.ign = ign;
        this.job = "";
        this.floor = 0;
        alias = new ArrayList<>();
        isNew = true;
        isModified = false;
    }

    public Character (String ign, String job, int floor, String[] alias, boolean isNew) {
        this.ign = ign.strip();
        this.job = job.strip().toUpperCase();
        this.floor = floor;
        this.alias = new ArrayList<>();
        this.isNew = isNew;
        this.isModified = false;

        if (alias != null) {
            for (String s : alias) {
                if (!s.strip().isBlank())
                    this.alias.add(s.strip());
            }
        }

        this.isNew = false;
    }

    public String getIgn() {
        return this.ign;
    }

    public String getJob() {
        return this.job;
    }

    public void setJob(String job) {
        if (!job.isBlank() && !this.job.equalsIgnoreCase(job)) {
            if (!this.isNew)
                this.isModified = true;
            this.job = job.toUpperCase();
        }
    }

    public int getFloor() {
        return this.floor;
    }

    public void setFloor(int floor) {
        int floorToEdit = Math.max(0, floor);
        if (floorToEdit != 0 && floorToEdit != this.floor) {
            if (!this.isNew)
                this.isModified = true;
            this.floor = floorToEdit;
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
        return String.format("[%s | %s (F%d)] -> %s", this.ign, this.job, this.floor, this.alias.toString());
    }
}
