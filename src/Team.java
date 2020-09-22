import java.util.ArrayList;
import java.util.List;

public class Team {
    public List<Character> teamList;

    public Team() {
        teamList = new ArrayList();
    }

    public boolean addMember(Character c) {
        if (teamList.size() < 6) {
            teamList.add(c);
            return true;
        } else
            return false;
    }

    public boolean isFull() {
        return teamList.size() == 6;
    }

    public int size() {
        return teamList.size();
    }

    /**
     * Sort from highest to lowest floor for this team
     */
    public void sortByFloor() {
        teamList.sort( (Character x, Character y) -> {
            return Integer.compare(y.floor,x.floor);
        });
    }

    @Override
    public String toString() {
        String ret = "[LEADER] ";

        for (int i = 0; i < teamList.size(); i++) {
            ret += teamList.get(i).ign + "[" + teamList.get(i).floor + "]\n";
        }
        return ret;
    }
}
