package project.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CharacterTest {
    @Test
    public void constructor_onlyIgn_success() {
        Character c = new Character("iUnder21");
        assertEquals("[iUnder21 |  (F0)] -> []", c.toString());
    }
}