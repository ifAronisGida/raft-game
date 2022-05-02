package data;

import randomworld.data.*;
import org.junit.jupiter.api.*;


import static org.junit.jupiter.api.Assertions.*;

public class TestWaterCleaner {
    private WaterCleaner waterCleanerInstance;

    @BeforeEach
    public void setUp() {
        waterCleanerInstance = new WaterCleaner(10,10);
    }

    @AfterAll
    public static void tearDown() {
        System.out.println("Tests ran successfully!");
    }

    @Test
    public void testProgress() {
        assertFalse(waterCleanerInstance.isWaterReady());
        for (int i = 0; i < 25; i++) {
            waterCleanerInstance.progress();
        }
        assertTrue(waterCleanerInstance.isWaterReady());
    }


    @Test
    public void testReset() {
        assertEquals(25,waterCleanerInstance.getActionsToMakeWater());
        waterCleanerInstance.progress();
        waterCleanerInstance.progress();
        waterCleanerInstance.progress();
        assertNotEquals(25, waterCleanerInstance.getActionsToMakeWater());
        waterCleanerInstance.reset();
        assertEquals(25,waterCleanerInstance.getActionsToMakeWater());
    }
}
