package data;

import raftgame.data.*;
import org.junit.jupiter.api.*;


import static org.junit.jupiter.api.Assertions.*;

public class TestStove {
    private Stove stoveInstance;

    @BeforeEach
    public void setUp() {
        stoveInstance = new Stove(10,10);
    }

    @AfterAll
    public static void tearDown() {
        System.out.println("Tests ran successfully!");
    }

    @Test
    public void testStartCooking() {
        stoveInstance.startCooking("Food");
        assertTrue(stoveInstance.isCooking());
        assertTrue(stoveInstance.getFood().equals("Food"));
    }

    @Test
    public void testProgress() {
        stoveInstance.startCooking("Food");
        stoveInstance.progress();
        stoveInstance.progress();
        stoveInstance.progress();
        assertTrue(stoveInstance.isCooking());
        assertEquals(22, stoveInstance.getActionsToCook());
    }

    @Test
    public void testReset() {
        stoveInstance.startCooking("Food");
        stoveInstance.progress();
        stoveInstance.progress();
        stoveInstance.progress();
        assertTrue(stoveInstance.isCooking());
        stoveInstance.reset();
        assertFalse(stoveInstance.isCooking());
    }
}
