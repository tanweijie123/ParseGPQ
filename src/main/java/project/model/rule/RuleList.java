package project.model.rule;

import project.model.Character;
import project.model.alloc.Team;
import project.model.alloc.Tunnel;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

public class RuleList {
    public static final String[] FULL_RULE_LIST = new String[] {
            "Assign Job Per Party" //[0]
    };

    public static Rule getRule(int idx, String content) {
        switch (idx) {
            case 0: return new AssignJobPerParty(content);
            default: throw new UnsupportedOperationException("Not available option in Rule List");
        }
    }

    /**
     * Function Library
     * This method is used for lookup of Rule function.
     * The reason it is not polymorphic in Rule class is because Rule is being serialised -> which means
     *    public will be able to modify the function.
     */
    public static BiConsumer<List<Character>, List<Tunnel>> getFunction(Rule rule) {
        BiConsumer<List<Character>, List<Tunnel>> code = null;
        if (rule instanceof AssignJobPerParty) {
             code = (participantList, tunnelList) -> {
                List<Character> concernedList = participantList.stream().filter(x -> x.getJob().equals(rule.getContent().toUpperCase())).collect(Collectors.toList());

                for (int i = 0; i < tunnelList.size(); i++) {
                    Team[] team = tunnelList.get(i).getAllTeam();

                    for (int j = 0; j < 3 ; j++) {
                        if (concernedList.size() > 0 && !team[j].isFull()) {
                            Character c = concernedList.remove(0);
                            team[j].addMember(c);
                            participantList.remove(c);
                        }
                    }
                }
            };
        }

        return code;
    }
}
