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
    public void setAlias_emptyAlias_blankAliasReturned() {
        String ign = "avbc12";
        String alias_empty = "";
        String alias_blank = "   ";
        String alias_null = null;
        String[] alias_array_empty = new String[] {};
        String[] alias_array_blank = new String[] {"  "};
        String[] alias_array_null = null;
        List<String> expectedAlias = new ArrayList<>();
        Character c = new Character(ign);

        assertEquals(expectedAlias, c.setAlias(alias_null).getAlias());
        assertEquals(expectedAlias, c.setAlias(alias_empty).getAlias());
        assertEquals(expectedAlias, c.setAlias(alias_blank).getAlias());
        assertEquals(expectedAlias, c.setAlias(alias_null).setAlias(alias_blank).getAlias());

        assertEquals(expectedAlias, c.setAlias(alias_array_null).getAlias());
        assertEquals(expectedAlias, c.setAlias(alias_array_empty).getAlias());
        assertEquals(expectedAlias, c.setAlias(alias_array_blank).getAlias());
        assertEquals(expectedAlias, c.setAlias(alias_array_null).setAlias(alias_array_blank).getAlias());
    }

    @Test
    public void setAlias_appendAlias_existingAlias_success() {
        String ign = "avbc12";
        String alias = "ello";
        String appendAlias = "newone";
        List<String> expectedAlias = Arrays.asList("ello", "newone");

        Character c = new Character(ign).setAlias(alias).setAlias(appendAlias);
        assertEquals(expectedAlias, c.getAlias());
    }

    @Test
    public void setAlias_appendAlias_existingAliasArray_success() {
        String ign = "avbc12";
        String[] alias = new String[] { "ello" };
        String appendAlias = "newone";
        List<String> expectedAlias = Arrays.asList("ello", "newone");

        Character c = new Character(ign).setAlias(alias).setAlias(appendAlias);
        assertEquals(expectedAlias, c.getAlias());
    }

    @Test
    public void setAlias_appendAliasBlank_noChange() {
        String ign = "avbc12";
        String[] alias = new String[] { "ello" };
        String appendAlias = "";
        List<String> expectedAlias = Arrays.asList("ello");

        Character c = new Character(ign).setAlias(alias).setAlias(appendAlias);
        assertEquals(expectedAlias, c.getAlias());
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
    public void setJob_getJob_success() {
        Character c = new Character("SteveJ0bs");
        assertEquals("", c.getJob()); //default blank jobs

        //ep: null, blank, empty
        assertEquals("", c.setJob(null).getJob());
        assertEquals("", c.setJob("").getJob());
        assertEquals("", c.setJob("   ").getJob());

        //ep: any non-blank text
        assertEquals("notNullWithoutSpace".toUpperCase(), c.setJob("notNullWithoutSpace").getJob());
        assertEquals("notNullWith Space".toUpperCase(), c.setJob("notNullWith Space").getJob());
        assertEquals("$pec!@L C#@^ac*ter".toUpperCase(), c.setJob("$pec!@L C#@^ac*ter").getJob()); //restrict in the future
    }

    @Test
    public void setFloor_getFloor_success() {
        Character c = new Character("setFloorTester");

        assertEquals(0, c.getFloor()); //default 0 floor

        //ep: 0 floor
        assertEquals(0, c.setFloor(0).getFloor());

        //ep: negative floor
        assertEquals(0, c.setFloor(-1).getFloor());
        assertEquals(0, c.setFloor(Integer.MIN_VALUE).getFloor());

        //ep: > 0
        assertEquals(22, c.setFloor(22).getFloor());
        assertEquals(50, c.setFloor(50).getFloor());
        assertEquals(Integer.MAX_VALUE, c.setFloor(Integer.MAX_VALUE).getFloor()); //to restrict in the future
    }

    //missing print, export, printMissingFields testcases

    @Test
    public void toString_testcases_success() {
        assertEquals("[AAaa123 |  (F0)] -> []", CHARACTER_A.toString());
        assertEquals("[BBbb234 | KANNA (F0)] -> []", CHARACTER_B.toString());
        assertEquals("[cCcc09 | EVAN (F20)] -> []", CHARACTER_C.toString());
        assertEquals("[DDDd |  (F50)] -> [woke]", CHARACTER_D.toString());
        assertEquals("[eeEEeeee0E |  (F1)] -> [wut, mkie]", CHARACTER_E.toString());
        assertEquals("[f | PHANTOM (F22)] -> [mesosack, richguy]", CHARACTER_F.toString());
        assertEquals("[gg | HAYATO (F60)] -> []", CHARACTER_G.toString());
    }

    @Test
    public void equals_testcases_success() {
        assertTrue(CHARACTER_A.equals(CHARACTER_A));
        assertFalse(CHARACTER_A.equals(CHARACTER_B));
        assertFalse(CHARACTER_B.equals(CHARACTER_A));

        //changed values equality
        assertFalse(CHARACTER_A.equals(CHARACTER_A.setFloor(20)));
        assertFalse(CHARACTER_A.equals(CHARACTER_A.setJob("Woof")));
        assertFalse(CHARACTER_A.equals(CHARACTER_A.setAlias("mehh")));

        //changed and reset back attributes
        assertTrue(CHARACTER_F.equals(CHARACTER_F.setFloor(20).setFloor(22)));
        assertTrue(CHARACTER_F.equals(CHARACTER_F.setJob("Kaiser").setJob("Phantom")));
        assertTrue(CHARACTER_F.equals(CHARACTER_F.setAlias(" ")));
    }

    @Test
    public void hashCode_testcases_success() {
        //this method is a reflection of equals() testcases

        assertTrue(CHARACTER_A.hashCode() == CHARACTER_A.hashCode());
        assertFalse(CHARACTER_A.hashCode() == CHARACTER_B.hashCode());
        assertFalse(CHARACTER_B.hashCode() == CHARACTER_A.hashCode());

        //changed values equality
        assertFalse(CHARACTER_A.hashCode() == CHARACTER_A.setFloor(20).hashCode());
        assertFalse(CHARACTER_A.hashCode() == CHARACTER_A.setJob("Woof").hashCode());
        assertFalse(CHARACTER_A.hashCode() == CHARACTER_A.setAlias("mehh").hashCode());

        //changed and reset back attributes
        assertTrue(CHARACTER_F.hashCode() == CHARACTER_F.setFloor(20).setFloor(22).hashCode());
        assertTrue(CHARACTER_F.hashCode() == CHARACTER_F.setJob("Kaiser").setJob("Phantom").hashCode());
        assertTrue(CHARACTER_F.hashCode() == CHARACTER_F.setAlias(" ").hashCode());
    }

    @Test
    public void toString_equals_hashcode_get_test() {
        //big bang test case
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
