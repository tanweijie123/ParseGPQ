package project.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.function.Executable;

class CharacterTest {
    @Test
    public void constructor_onlyIgnConstructor_success() {
        Character c = new Character("iUnder21");
        assertEquals("[iUnder21 |  (F0)] -> []", c.toString());
    }

    @Test
    public void constructor_onlyIgnConstructor_invalidIgn_exception() {
        Executable e = () -> new Character("Illw123.");
        assertThrows(IllegalArgumentException.class, e);
    }

    @Test
    public void constructor_onlyIgnConstructor_blank_exception() {
        Executable e = () -> new Character("");
        assertThrows(IllegalArgumentException.class, e);
    }

    @Test
    public void constructor_onlyIgnConstructor_emptySpace_exception() {
        Executable e = () -> new Character("    ");
        assertThrows(IllegalArgumentException.class, e);
    }

    @Test
    public void isSameIgn_sameIgn_success() {
        Character c1 = new Character("sameIgn098");
        Character c2 = new Character("sameIgn098");
        assertTrue(Character.isSameIgn(c1, c2));
    }

    @Test
    public void isSameIgn_differentIgn_fail() {
        Character c1 = new Character("sameIgn098");
        Character c2 = new Character("sameIgn097");
        assertFalse(Character.isSameIgn(c1, c2));
    }

    @Test
    public void setAlias_appendAlias_noExistingAlias_success() {
        String ign = "avbc12";
        String[] alias = new String[] { "" };
        String appendAlias = "newone";
        List<String> expectedAlias = Arrays.asList("newone");

        Character c = new Character(ign).setAlias(alias).setAlias(appendAlias);
        assertEquals(expectedAlias, c.getAlias());
    }

    @Test
    public void setAlias_appendAlias_existingAliasBlank_success() {
        String ign = "avbc12";
        String[] alias = new String[] { "    " };
        String appendAlias = "newone";
        List<String> expectedAlias = Arrays.asList("newone");

        Character c = new Character(ign).setAlias(alias).setAlias(appendAlias);
        assertEquals(expectedAlias, c.getAlias());
    }

    @Test
    public void setAlias_appendAlias_existingAlias_success() {
        String ign = "avbc12";
        String[] alias = new String[] { "ello" };
        String appendAlias = "newone";
        List<String> expectedAlias = Arrays.asList("ello", "newone");

        Character c = new Character(ign).setAlias(alias).setAlias(appendAlias);
        assertEquals(expectedAlias, c.getAlias());
    }

    @Test
    public void toString_equals_hashcode_get_test() {
        String ign = "fallguy011";
        String job = "adele";
        int floor = 28;
        String[] alias1 = null;
        String[] alias2 = new String[] { "wiseman", "doit" };

        Character c = new Character(ign);
        assertTrue(c.equals(new Character(ign)));
        assertEquals(new Character(ign).hashCode(), c.hashCode());
        assertEquals(ign, c.getIgn());
        assertEquals("[fallguy011 |  (F0)] -> []", c.toString());

        Character c_withJob = c.setJob(job);
        assertTrue(c_withJob.equals(new Character(ign).setJob(job)));
        assertEquals(new Character(ign).setJob(job).hashCode(), c_withJob.hashCode());
        assertEquals(job.toUpperCase(), c_withJob.getJob());
        assertEquals("[fallguy011 | ADELE (F0)] -> []", c_withJob.toString());

        Character c_withFloor = c.setFloor(floor);
        assertTrue(c_withFloor.equals(new Character(ign).setFloor(floor)));
        assertEquals(new Character(ign).setFloor(floor).hashCode(), c_withFloor.hashCode());
        assertEquals(floor, c_withFloor.getFloor());
        assertEquals("[fallguy011 |  (F28)] -> []", c_withFloor.toString());

        Character c_withAlias1 = c.setAlias(alias1);
        assertTrue(c_withAlias1.equals(new Character(ign).setAlias(alias1)));
        assertEquals(new Character(ign).setAlias(alias1).hashCode(), c_withAlias1.hashCode());
        assertEquals(new ArrayList<String>(), c_withAlias1.getAlias());
        assertEquals("[fallguy011 |  (F0)] -> []", c_withAlias1.toString());

        Character c_withAlias2 = c.setAlias(alias2);
        assertTrue(c_withAlias2.equals(new Character(ign).setAlias(alias2)));
        assertEquals(new Character(ign).setAlias(alias2).hashCode(), c_withAlias2.hashCode());
        assertEquals(Arrays.asList(alias2), c_withAlias2.getAlias());
        assertEquals("[fallguy011 |  (F0)] -> [wiseman, doit]", c_withAlias2.toString());

        c = c.setAlias(alias1).setJob(job).setFloor(floor).setAlias(alias2);
        assertTrue(c.equals(new Character(ign).setJob(job).setFloor(floor).setAlias(alias2)));
        assertEquals(new Character(ign).setJob(job).setFloor(floor).setAlias(alias2).hashCode(), c.hashCode());
        assertEquals(ign, c.getIgn());
        assertEquals(job.toUpperCase(), c.getJob());
        assertEquals(floor, c.getFloor());
        assertEquals(Arrays.asList(alias2), c.getAlias());
        assertEquals("[fallguy011 | ADELE (F28)] -> [wiseman, doit]", c.toString());

    }
}