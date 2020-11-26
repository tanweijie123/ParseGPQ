package project.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static project.testutil.CharacterList.CHARACTER_A;
import static project.testutil.CharacterList.CHARACTER_B;
import static project.testutil.CharacterList.CHARACTER_C;
import static project.testutil.CharacterList.CHARACTER_D;
import static project.testutil.CharacterList.CHARACTER_E;
import static project.testutil.CharacterList.CHARACTER_F;
import static project.testutil.CharacterList.CHARACTER_G;
import static project.testutil.TeamList.TEAM_A_SINGLE_MEMBER;
import static project.testutil.TeamList.TEAM_B_TWO_MEMBERS;
import static project.testutil.TeamList.TEAM_C_FULL_TEAM;
import static project.testutil.TeamList.TEAM_ZERO_MEMBERS;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

class TeamTest {

    @Test
    public void getByIndex_testcases() {
        assertThrows(IndexOutOfBoundsException.class, () -> TEAM_C_FULL_TEAM.get(7));
        assertThrows(IndexOutOfBoundsException.class, () -> TEAM_ZERO_MEMBERS.get(0));
        assertThrows(IndexOutOfBoundsException.class, () -> TEAM_ZERO_MEMBERS.get(-1));

        Team actualTeam = TEAM_A_SINGLE_MEMBER;
        assertEquals(CHARACTER_A, actualTeam.get(0));

        actualTeam = TEAM_B_TWO_MEMBERS;
        assertEquals(CHARACTER_B, actualTeam.get(1));

        actualTeam = TEAM_C_FULL_TEAM;
        assertEquals(CHARACTER_F, actualTeam.get(5));
    }

    @Test
    public void getTeamList_testcases() {
        assertEquals(new ArrayList<Character>(), TEAM_ZERO_MEMBERS.getTeamList());

        assertEquals(Arrays.asList(CHARACTER_A), TEAM_A_SINGLE_MEMBER.getTeamList());

        assertEquals(Arrays.asList(CHARACTER_A, CHARACTER_B), TEAM_B_TWO_MEMBERS.getTeamList());

        assertEquals(Arrays.asList(CHARACTER_A, CHARACTER_B, CHARACTER_C, CHARACTER_D, CHARACTER_E, CHARACTER_F),
                TEAM_C_FULL_TEAM.getTeamList());

    }

    @Test
    public void addMember_teamLessThan6_success() {
        Team t1 = new Team();
        assertTrue(t1.addMember(CHARACTER_C));
        assertEquals("[LEADER] cCcc09[20]\n", t1.toString());

        assertTrue(t1.addMember(CHARACTER_D));
        assertEquals("[LEADER] cCcc09[20]\nDDDd[50]\n", t1.toString());
    }

    @Test
    public void addMember_teamSize6_fail() {
        Team fullTeam = TEAM_C_FULL_TEAM;

        assertEquals(6, fullTeam.size());
        assertFalse(fullTeam.addMember(CHARACTER_G));
    }

    @Test
    public void full_testcases() {
        assertEquals(false, TEAM_ZERO_MEMBERS.isFull());
        assertEquals(false, TEAM_A_SINGLE_MEMBER.isFull());
        assertEquals(false, TEAM_B_TWO_MEMBERS.isFull());
        assertEquals(true, TEAM_C_FULL_TEAM.isFull());
    }

    @Test
    public void getSize_testcases() {
        assertEquals(0, TEAM_ZERO_MEMBERS.size());
        assertEquals(1, TEAM_A_SINGLE_MEMBER.size());
        assertEquals(2, TEAM_B_TWO_MEMBERS.size());
        assertEquals(6, TEAM_C_FULL_TEAM.size());
    }

    @Test
    public void sortByFloor_noMembers_success() {
        Team expectedTeam = new Team();
        Team actualTeam = new Team();
        actualTeam.sortByFloor();

        assertEquals(expectedTeam, TEAM_ZERO_MEMBERS);
    }

    @Test
    public void sortByFloor_oneMember_noChange_success() {
        Team expectedTeam = new Team();
        expectedTeam.addMember(CHARACTER_A);

        Team actualTeam = new Team();
        actualTeam.addMember(CHARACTER_A);
        actualTeam.sortByFloor();

        assertEquals(expectedTeam, actualTeam);
    }

    @Test
    public void sortByFloor_twoMember_noChange_success() {
        // this test case no change because A and B are same floor (0), thus it is sorted by ign.
        Team expectedTeam = new Team();
        expectedTeam.addMember(CHARACTER_A);
        expectedTeam.addMember(CHARACTER_B);

        Team actualTeam = new Team();
        actualTeam.addMember(CHARACTER_A);
        actualTeam.addMember(CHARACTER_B);
        actualTeam.sortByFloor();

        assertEquals(expectedTeam, actualTeam);
    }

    @Test
    public void sortByFloor_twoMember_changes_success() {
        Team expectedTeam = new Team();
        expectedTeam.addMember(CHARACTER_D);
        expectedTeam.addMember(CHARACTER_C);

        Team actualTeam = new Team();
        actualTeam.addMember(CHARACTER_C);
        actualTeam.addMember(CHARACTER_D);
        actualTeam.sortByFloor();

        assertEquals(expectedTeam, actualTeam);
    }

    @Test
    public void sortByFloor_fullTeam_changes_success() {
        Team expectedTeam = new Team();
        //dfcab
        expectedTeam.addMember(CHARACTER_D);
        expectedTeam.addMember(CHARACTER_F);
        expectedTeam.addMember(CHARACTER_C);
        expectedTeam.addMember(CHARACTER_E);
        expectedTeam.addMember(CHARACTER_A);
        expectedTeam.addMember(CHARACTER_B);


        Team actualTeam = new Team();
        actualTeam.addMember(CHARACTER_A);
        actualTeam.addMember(CHARACTER_B);
        actualTeam.addMember(CHARACTER_C);
        actualTeam.addMember(CHARACTER_D);
        actualTeam.addMember(CHARACTER_E);
        actualTeam.addMember(CHARACTER_F);
        actualTeam.sortByFloor();

        assertEquals(expectedTeam, actualTeam);
    }

    @Test
    public void toString_testcases() {
        assertEquals("[LEADER] ", TEAM_ZERO_MEMBERS.toString());
        assertEquals("[LEADER] AAaa123[0]\n", TEAM_A_SINGLE_MEMBER.toString());
        assertEquals("[LEADER] AAaa123[0]\nBBbb234[0]\n", TEAM_B_TWO_MEMBERS.toString());
        assertEquals("[LEADER] AAaa123[0]\nBBbb234[0]\ncCcc09[20]\nDDDd[50]\neeEEeeee0E[1]\nf[22]\n", TEAM_C_FULL_TEAM.toString());
    }

    @Test
    public void equals_hashcode_success() {
        Team t1 = new Team();
        Team t2 = new Team();
        assertTrue(t1.hashCode() == t2.hashCode());
        assertTrue(t1.equals(t2));

        t1.addMember(CHARACTER_A);
        t2.addMember(CHARACTER_A);
        assertTrue(t1.hashCode() == t2.hashCode());
        assertTrue(t1.equals(t2));

        Team t3 = new Team();
        assertTrue(t1.hashCode() != t3.hashCode());
        assertFalse(t1.equals(t3));
    }
}
