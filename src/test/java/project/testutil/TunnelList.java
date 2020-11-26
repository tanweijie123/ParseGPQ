package project.testutil;

import static project.testutil.TeamList.TEAM_A_SINGLE_MEMBER;
import static project.testutil.TeamList.TEAM_B_TWO_MEMBERS;
import static project.testutil.TeamList.TEAM_C_FULL_TEAM;

import project.model.Tunnel;

public class TunnelList {
    public static final Tunnel TUNNEL_EMPTY_ID999 = new Tunnel(999);
    public static final Tunnel TUNNEL_PARTIAL_ID888
            = new Tunnel(888, TEAM_A_SINGLE_MEMBER, TEAM_B_TWO_MEMBERS, TEAM_C_FULL_TEAM);
    public static final Tunnel TUNNEL_FULLTEAM_DUP_ID777
            = new Tunnel(777, TEAM_C_FULL_TEAM, TEAM_C_FULL_TEAM, TEAM_C_FULL_TEAM);
}
