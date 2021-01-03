package project.testutil;

import static project.testutil.CharacterList.CHARACTER_A;
import static project.testutil.CharacterList.CHARACTER_B;
import static project.testutil.CharacterList.CHARACTER_C;
import static project.testutil.CharacterList.CHARACTER_D;
import static project.testutil.CharacterList.CHARACTER_E;
import static project.testutil.CharacterList.CHARACTER_F;

import project.model.alloc.Team;

public class TeamList {
    public static final Team TEAM_ZERO_MEMBERS = new Team();
    public static final Team TEAM_A_SINGLE_MEMBER = teamA();
    public static final Team TEAM_B_TWO_MEMBERS = teamB();
    public static final Team TEAM_C_FULL_TEAM = teamC();
    public static final Team TEAM_D_TWO_MEMBERS = teamD(); //different characters from Team B



    private static Team teamA() {
        Team a = new Team();
        a.addMember(CHARACTER_A);
        return a;
    }

    private static Team teamB() {
        Team b = new Team();
        b.addMember(CHARACTER_A);
        b.addMember(CHARACTER_B);
        return b;
    }

    private static Team teamC() {
        Team c = new Team();
        c.addMember(CHARACTER_A);
        c.addMember(CHARACTER_B);
        c.addMember(CHARACTER_C);
        c.addMember(CHARACTER_D);
        c.addMember(CHARACTER_E);
        c.addMember(CHARACTER_F);
        return c;
    }

    private static Team teamD() {
        Team d = new Team();
        d.addMember(CHARACTER_C);
        d.addMember(CHARACTER_D);
        return d;
    }

}
