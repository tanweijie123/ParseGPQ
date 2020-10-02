import java.util.List;

public class Tunnel {
    public int id;
    public Team team1;
    public Team team2;
    public Team team3;

    public Tunnel(int id) {
        this.id = id;
        this.team1 = new Team();
        this.team2 = new Team();
        this.team3 = new Team();
    }

    public Tunnel(int id, Team t1, Team t2, Team t3) {
        this.id = id;
        this.team1 = t1;
        this.team2 = t2;
        this.team3 = t3;
    }

    /**
     * Gets the team with the lowest number of participants
     */
    public Team getLowestTeam() {
        int min = Math.min(Math.min(team1.size(), team2.size()), team3.size());
        if (min == team1.size()) {
            return team1;
        } else if (min == team2.size()) {
            return team2;
        } else {
            return team3;
        }
    }

    /**
     * Sort from highest to lowest floor for every team
     */
    public void sortByFloor() {
        team1.sortByFloor();
        team2.sortByFloor();
        team3.sortByFloor();
    }

    public boolean isFull() {
        return team1.isFull() && team2.isFull() && team3.isFull();
    }

    /**
     * Reorganise the tunnel (compact) if every team is not full.
     * Tries to shrink as many half parties as possible.
     */
    public void compact() {
        if (!isFull()) {
            int capacity = team1.size() + team2.size() + team3.size();
            int numTeams = (int) Math.ceil(capacity / 6.0);

            assert(numTeams <= 3 && numTeams >= 0);

            if (numTeams == 2) { //can be shrink into 1 or 2 parties
                List<Character> list = team3.teamList;
                while (list.size() > 0) {
                    if (team1.size () > team2.size())
                        team2.addMember(list.remove(0));
                    else
                        team1.addMember(list.remove(0));
                }
            } else if (numTeams == 1) {
                List<Character> list = team3.teamList;
                while (list.size() > 0) {
                    team1.addMember(list.remove(0));
                }
                list = team2.teamList;
                while (list.size() > 0) {
                    team1.addMember(list.remove(0));
                }
            }

        }
    }

    @Override
    public String toString() {
       return String.format("Team 1:\n%s\nTeam2:\n%s\nTeam3:\n%s", team1.toString(), team2.toString(), team3.toString());
    }
}
