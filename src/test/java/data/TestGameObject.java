package data;

import raftgame.data.*;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestGameObject {

    private List<GameObject> gameObjectList;

    @BeforeEach
    public void setUp() {
        gameObjectList = new ArrayList<>();
        gameObjectList.add(new Board(10,12));
        gameObjectList.add(new Junk(10,12));
        gameObjectList.add(new Leaf(10,12));
        gameObjectList.add(new Net(10,12));
    }

    @AfterAll
    public static void tearDown() {
        System.out.println("Tests ran successfully!");
    }

    @Test
    public void testId() {
        List<Integer> idList = new ArrayList<>();
        for (GameObject object: gameObjectList) {
            //test if id exists
            assertNotNull(object.getObjectId());

            //test if id is unique
            assertFalse(idList.contains(object.getObjectId()));
            idList.add(object.getObjectId());

        }
    }

    @Test
    public void testPosition() {
        for (GameObject object: gameObjectList) {
            assertEquals(10, object.getPosX());
            assertEquals(12, object.getPosY());
        }
    }
}
