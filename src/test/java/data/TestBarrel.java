package data;

import randomworld.data.Barrel;
import org.junit.jupiter.api.*;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class TestBarrel {
    private Barrel barrelInstance;

    @BeforeEach
    public void setUp() {
        barrelInstance = new Barrel(10, 10);
    }

    @AfterAll
    public static void tearDown() {
        System.out.println("Test run successfully!");
    }

    @Test
    public void testGetContent() {

        assertEquals(5, barrelInstance.getContent().size());

        String[] possibleContent = {"Junk", "Leaf", "Board", "Potato"};
        for (String content : barrelInstance.getContent()) {
            assertTrue(Arrays.stream(possibleContent).toList().contains(content));
        }

    }
}