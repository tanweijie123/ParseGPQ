package project.testutil;

import project.model.Character;

public class CharacterList {
    public static final Character CHARACTER_A = new Character("AAaa123");
    public static final Character CHARACTER_B = new Character("BBbb234").setJob("kanna");
    public static final Character CHARACTER_C = new Character("cCcc09").setJob("evan").setFloor(20);
    public static final Character CHARACTER_D = new Character("DDDd").setFloor(50).setAlias(new String[] {"woke"});
    public static final Character CHARACTER_E = new Character("eeEEeeee0E").setFloor(1).setAlias(new String[] {"wut", "", "mkie"});
    public static final Character CHARACTER_F = new Character("f").setFloor(22).setJob("phantom").setAlias(new String[] {"mesosack", "richguy"});
    public static final Character CHARACTER_G = new Character("gg").setJob("Hayato").setFloor(60).setAlias("");
}
