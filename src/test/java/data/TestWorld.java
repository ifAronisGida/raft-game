package data;

import randomworld.data.*;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestWorld {

    private World worldInstance;


    @BeforeEach
    public void setUp() {
        worldInstance = new World(100, 100, 5, true, 0);
    }

    @AfterAll
    public static void tearDown() {
        System.out.println("Tests ran successfully!");
    }

    @Test
    public void testGenerate() {
        //generate is called in constructor so we don't call anything here

        assertNotNull(worldInstance);
        assertNotNull(worldInstance.getFields());
        assertEquals(100, worldInstance.getHeight());
        assertNotNull(worldInstance.getFields());
    }

    @Test
    public void testSpawnPlayer() {
        //generate calls spawn player so we don't call anything here

        assertNotNull(worldInstance.getPlayer());
    }

    @Test
    public void testSpawnShark() {
        //generate calls spawn player so we don't call anything here

        assertNotNull(worldInstance.getEnemy());

    }

    @Test
    public void testSpawnObjects() {
        worldInstance.spawnObjects(100000);
        assertTrue(0 < worldInstance.getMaterialList().size());
    }

    @Test
    public void testCreateRandomObject() {
        GameObject object = worldInstance.createRandomObject();
        assertNotNull(object);
    }

    @Test
    public void testMovePlayer() {
        assertEquals(20, worldInstance.getPlayerPosX());
        assertEquals(15, worldInstance.getPlayerPosY());
        assertTrue(worldInstance.movePlayer(21, 15));

        assertEquals(21, worldInstance.getPlayerPosX());
        assertFalse(worldInstance.movePlayer(25, 25));
    }

    @Test
    public void testMoveShark() {
        //test neutral moving
        assertEquals("The shark is swimming peacefully..\n", worldInstance.moveShark());

        //move player to water
        worldInstance.movePlayer(21, 15);
        worldInstance.movePlayer(22, 15);
        assertEquals("The shark is moving towards you!!\n", worldInstance.moveShark());

        //shark reaches player
        for (int i = 0; i < 12; i++) {
            System.out.println(worldInstance.moveShark());
        }
        assertTrue(worldInstance.isGameOver());
    }

    @Test
    public void testMoveObjects() {
        //spawn objects
        worldInstance.spawnObjects(100);
        int posY = worldInstance.getMaterialList().get(0).getPosY();
        System.out.println(posY);

        //move objects a bit
        worldInstance.moveObjects();
        worldInstance.moveObjects();
        worldInstance.moveObjects();
        int newPosY = worldInstance.getMaterialList().get(0).getPosY();
        System.out.println(newPosY);

        //check if Y position changed
        assertNotEquals(posY, newPosY);
    }

    @Test
    public void testClickObject() {
        for (int i = 0; i < 100; i++) {
            worldInstance.spawnObjects(1);
            worldInstance.moveObjects();
            String clickMessage = worldInstance.clickObject(i);
            System.out.println(clickMessage);
            assertNotNull(clickMessage);
        }
    }

    @Test
    public void testPickupObject() {

        assertEquals(0, worldInstance.getPlayer().getItemCount("Junk"));
        assertEquals(0, worldInstance.getPlayer().getItemCount("Board"));
        assertEquals(0, worldInstance.getPlayer().getItemCount("Leaf"));
        for (int i = 0; i < 100; i++) {
            worldInstance.spawnObjects(10);
            String pickupMessage = worldInstance.pickupMaterial(worldInstance.getMaterialList().get(i));
            System.out.println(pickupMessage);
        }

        assertTrue(0 < worldInstance.getPlayer().getItemCount("Junk"));
        assertTrue(0 < worldInstance.getPlayer().getItemCount("Board"));
        assertTrue(0 < worldInstance.getPlayer().getItemCount("Leaf"));

    }

    @Test
    public void testBuildOptions() {
        assertEquals(0, worldInstance.getBuildOptions().size());

        //init world with more starting material
        worldInstance = new World(10, 10, 100, false, 1000);
        assertTrue(0 < worldInstance.getBuildOptions().size());
    }

    @Test
    public void testFish() {
        String fishMessage = worldInstance.fish(10, 10);
        assertEquals("You can only fish in neighboring waters!\n", fishMessage);

        fishMessage = worldInstance.fish(19, 15);
        List<String> possibleMessages = new ArrayList<>();
        possibleMessages.add("Successfully caught a fish!\n");
        possibleMessages.add("Better luck next time!\n");
        assertTrue(possibleMessages.contains(fishMessage));
    }

    @Test
    public void testSpawnBuilding() {
        worldInstance = new World(100, 100, 100, false, 1000);
        int originalBoardCount = worldInstance.getPlayer().getItemCount("Board");
        worldInstance.spawnBuilding(10, 10, "Raft");
        assertNotEquals(originalBoardCount, worldInstance.getPlayer().getItemCount("Board"));

        int originalBuildingCount = worldInstance.getBuildings().size();
        worldInstance.spawnBuilding(10, 10, "Net");
        assertNotEquals(originalBuildingCount, worldInstance.getBuildings().size());

    }

    @Test
    public void testBuild() {
        worldInstance = new World(100, 100, 100, false, 1000);
        String buildMessage = worldInstance.build(10, 10, "Raft");
        assertEquals("You can only build next to you!\n", buildMessage);

        buildMessage = worldInstance.build(19, 15, "Raft");
        assertEquals("Successfully built Raft!\n", buildMessage);

    }
}
