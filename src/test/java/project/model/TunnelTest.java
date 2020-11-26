package project.model;

import static org.junit.jupiter.api.Assertions.*;
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
import static project.testutil.TeamList.TEAM_D_TWO_MEMBERS;
import static project.testutil.TeamList.TEAM_ZERO_MEMBERS;
import static project.testutil.TunnelList.TUNNEL_EMPTY_ID999;
import static project.testutil.TunnelList.TUNNEL_FULLTEAM_DUP_ID777;
import static project.testutil.TunnelList.TUNNEL_PARTIAL_ID888;

import org.junit.jupiter.api.Test;

class TunnelTest {

    @Test
    public void getters_testcase() {
        Tunnel t0 = TUNNEL_EMPTY_ID999;
        assertEquals(999, t0.getId());
        assertEquals(new Team(), t0.getTeam1());
        assertEquals(new Team(), t0.getTeam2());
        assertEquals(new Team(), t0.getTeam3());

        Tunnel t1 = TUNNEL_PARTIAL_ID888;
        assertEquals(888, t1.getId());
        assertEquals(TEAM_A_SINGLE_MEMBER, t1.getTeam1());
        assertEquals(TEAM_A_SINGLE_MEMBER, t1.getAllTeam()[0]);
        assertEquals(TEAM_B_TWO_MEMBERS, t1.getTeam2());
        assertEquals(TEAM_B_TWO_MEMBERS, t1.getAllTeam()[1]);
        assertEquals(TEAM_C_FULL_TEAM, t1.getTeam3());
        assertEquals(TEAM_C_FULL_TEAM, t1.getAllTeam()[2]);

        assertEquals(3, t1.getAllTeam().length);
    }

    @Test
    public void getLowestTeam_testcase() {
        Tunnel t0 = TUNNEL_EMPTY_ID999;
        assertEquals(TEAM_ZERO_MEMBERS, t0.getLowestTeam());

        Tunnel t1 = TUNNEL_PARTIAL_ID888;
        assertEquals(TEAM_A_SINGLE_MEMBER, t1.getLowestTeam());

        Tunnel t2 = TUNNEL_FULLTEAM_DUP_ID777;
        assertEquals(TEAM_C_FULL_TEAM, t2.getLowestTeam());

        Tunnel t3 = new Tunnel(444, TEAM_B_TWO_MEMBERS, TEAM_D_TWO_MEMBERS, TEAM_C_FULL_TEAM);
        assertEquals(TEAM_B_TWO_MEMBERS, t3.getLowestTeam());

        Tunnel t4 = new Tunnel(333, TEAM_D_TWO_MEMBERS, TEAM_B_TWO_MEMBERS, TEAM_C_FULL_TEAM);
        assertEquals(TEAM_D_TWO_MEMBERS, t4.getLowestTeam());
    }

    @Test
    public void sortByFloor_sortInnerTeam_success() {
        //a general test case to check integration. actual sortByFloor unit test is in Team

        //blank team
        Team expectedT1 = new Team();
        Team actualT1 = new Team();

        //partial team
        Team expectedT2 = new Team();
        expectedT2.addMember(CHARACTER_G);
        expectedT2.addMember(CHARACTER_F);
        Team actualT2 = new Team();
        actualT2.addMember(CHARACTER_F);
        actualT2.addMember(CHARACTER_G);

        Team expectedT3 = new Team();
        expectedT3.addMember(CHARACTER_G);
        expectedT3.addMember(CHARACTER_D);
        expectedT3.addMember(CHARACTER_F);
        expectedT3.addMember(CHARACTER_C);
        expectedT3.addMember(CHARACTER_A);
        expectedT3.addMember(CHARACTER_B);


        Team actualT3 = new Team();
        actualT3.addMember(CHARACTER_A);
        actualT3.addMember(CHARACTER_B);
        actualT3.addMember(CHARACTER_C);
        actualT3.addMember(CHARACTER_D);
        actualT3.addMember(CHARACTER_F);
        actualT3.addMember(CHARACTER_G);

        Tunnel tunnelExpected = new Tunnel(1234, expectedT1, expectedT2, expectedT3);
        Tunnel tunnelActual = new Tunnel(1234, actualT1, actualT2, actualT3);
        tunnelActual.sortByFloor();

        assertEquals(tunnelExpected, tunnelActual);
    }

    @Test
    public void isFull_allEmpty_true() {
        Tunnel t0 = new Tunnel(999, TEAM_ZERO_MEMBERS, TEAM_ZERO_MEMBERS, TEAM_ZERO_MEMBERS);
        assertFalse(t0.isFull());
    }

    @Test
    public void isFull_oneFull_true() {
        Tunnel t0 = new Tunnel(999, TEAM_C_FULL_TEAM, TEAM_A_SINGLE_MEMBER, TEAM_B_TWO_MEMBERS);
        assertFalse(t0.isFull());
    }

    @Test
    public void isFull_twoFull_true() {
        Tunnel t0 = new Tunnel(999, TEAM_B_TWO_MEMBERS, TEAM_C_FULL_TEAM, TEAM_C_FULL_TEAM);
        assertFalse(t0.isFull());
    }

    @Test
    public void isFull_allFull_true() {
        Tunnel t0 = new Tunnel(999, TEAM_C_FULL_TEAM, TEAM_C_FULL_TEAM, TEAM_C_FULL_TEAM);
        assertTrue(t0.isFull());
    }

    @Test
    public void toString_testcase() {
        assertEquals(
                "Team 1:\n[LEADER] \nTeam2:\n[LEADER] \nTeam3:\n[LEADER] ",
                TUNNEL_EMPTY_ID999.toString());

        assertEquals(
                "Team 1:\n[LEADER] AAaa123[0]\n\nTeam2:\n[LEADER] AAaa123[0]\nBBbb234[0]\n\nTeam3:\n" +
                "[LEADER] AAaa123[0]\nBBbb234[0]\ncCcc09[20]\nDDDd[50]\neeEEeeee0E[1]\nf[22]\n",
                TUNNEL_PARTIAL_ID888.toString());

        assertEquals(
                "Team 1:\n[LEADER] AAaa123[0]\nBBbb234[0]\ncCcc09[20]\nDDDd[50]\neeEEeeee0E[1]\nf[22]\n\n" +
                "Team2:\n[LEADER] AAaa123[0]\nBBbb234[0]\ncCcc09[20]\nDDDd[50]\neeEEeeee0E[1]\nf[22]\n\n" +
                "Team3:\n[LEADER] AAaa123[0]\nBBbb234[0]\ncCcc09[20]\nDDDd[50]\neeEEeeee0E[1]\nf[22]\n",
                TUNNEL_FULLTEAM_DUP_ID777.toString());
    }

    @Test
    public void equals_hashcode_success() {
        Tunnel t0 = new Tunnel(999);

        //self-reference
        assertTrue(t0.hashCode() == t0.hashCode());
        assertTrue(t0.equals(t0));

        //same attribute reference
        assertTrue(t0.hashCode() == TUNNEL_EMPTY_ID999.hashCode());
        assertTrue(t0.equals(TUNNEL_EMPTY_ID999));

        //different attribute reference
        assertFalse(t0.hashCode() == TUNNEL_FULLTEAM_DUP_ID777.hashCode());
        assertFalse(t0.equals(TUNNEL_FULLTEAM_DUP_ID777));

        Tunnel t1 = new Tunnel(999);

        t0.getTeam1().addMember(CHARACTER_G);
        t1.getTeam1().addMember(CHARACTER_G);

        assertTrue(t0.equals(t1));
    }

    //remaining test case: compact() 
}