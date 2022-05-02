package data;

import raftgame.data.*;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestHuman {

    private Human humanInstance;

    @BeforeEach
    public void setUp() {
        humanInstance = new Human(10,10);
    }

    @AfterAll
    public static void tearDown() {
        System.out.println("Tests ran successfully!");
    }

    @Test
    public void testAge() {
        humanInstance.age();
        assertEquals(99, humanInstance.getHunger());
        assertEquals(99, humanInstance.getThirst());
    }

    @Test
    public void testIsAlive() {
        assertTrue(humanInstance.isAlive());
        for (int i = 0; i < 100; i++) {
            humanInstance.age();
        }
        assertFalse(humanInstance.isAlive());
    }

    @Test
    public void testGetItemCount() {
        assertEquals(0, humanInstance.getItemCount("Item"));
        humanInstance.addToInventory("Item");
        assertEquals(1,humanInstance.getItemCount("Item"));
    }

    @Test
    public void testAddToInventory() {
        assertEquals(0, humanInstance.getItemCount("Spear"));
        assertEquals(0, humanInstance.getItemCount("Object"));
        assertEquals(0, humanInstance.getItemCount("Whatever"));
        humanInstance.addToInventory("Spear");
        humanInstance.addToInventory("Spear");
        humanInstance.addToInventory("Object");
        assertEquals(2,humanInstance.getItemCount("Spear"));
        assertEquals(1,humanInstance.getItemCount("Object"));
    }


    @Test
    public void testTryToFish() {
        int failCount = 0;
        int successCount = 0;
        for (int i = 0; i < 1000; i++) {
            if (humanInstance.tryToFish()) successCount++;
            else failCount++;
        }

        assertTrue(successCount > 1);
        assertTrue(failCount > 3);
    }

    @Test
    public void testRemoveFromInventory() {
        humanInstance.addToInventory("Spear");
        assertEquals(1, humanInstance.getItemCount("Spear"));
        humanInstance.removeFromInventory("Spear", 1);
        assertEquals(0,humanInstance.getItemCount("Spear"));
    }

    @Test
    public void testEat()  {
        for (int i = 0; i < 80; i++) {
            humanInstance.age();
        }
        assertEquals(20,humanInstance.getHunger());
        humanInstance.eat();
        assertEquals(80,humanInstance.getHunger());
    }

    @Test
    public void testDrink()  {
        for (int i = 0; i < 80; i++) {
            humanInstance.age();
        }
        assertEquals(20,humanInstance.getThirst());
        humanInstance.drink();
        assertEquals(60,humanInstance.getThirst());
    }

    @Test
    public void testSharkAttack() {
        humanInstance.setProtectedFor(5);
        assertEquals(5, humanInstance.getProtectedFor());
        humanInstance.sharkAttack();
        assertEquals(4,humanInstance.getProtectedFor());
        humanInstance.sharkAttack();
        humanInstance.sharkAttack();
        assertEquals(2, humanInstance.getProtectedFor());
    }

}
