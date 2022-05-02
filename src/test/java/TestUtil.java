import org.junit.jupiter.api.*;
import raftgame.Util;

import static org.junit.jupiter.api.Assertions.*;


public class TestUtil {

    @Test
    public void testIsBetween() {

        assertEquals(true, Util.isBetween(1,0,1));
        assertEquals(true, Util.isBetween(1,1,1));
        assertEquals(true, Util.isBetween(0,-3123,23));
        assertEquals(true, Util.isBetween(123,1,1000));
        assertEquals(true, Util.isBetween(-45,-125,1));
        assertNotEquals(true, Util.isBetween(150,1,123));
        assertNotEquals(true, Util.isBetween(-41,1,123));
        assertNotEquals(true, Util.isBetween(-1,-5,-50));
        assertNotEquals(true, Util.isBetween(-1500,0,123));
        assertNotEquals(true, Util.isBetween(0,1,1));
    }

}
