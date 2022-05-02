package raftgame.data;

import java.util.*;
import raftgame.Util;

/**
 * Represents a barrel object
 */
public class Barrel extends GameObject {
    private List<String> content;

    public Barrel(int posX, int posY) {
        super(posX, posY);
        content = new ArrayList<>();
    }

    /**
     * Creates 5 random string, representing an object
     *
     * @return barrel's content as a List
     */
    public List<String> getContent() {
        Random random = new Random();
        for (int i = 0; i < 5; i++) {
            int randInt = random.nextInt(100);
            if (Util.isBetween(randInt, 0, 24)) content.add("Junk");
            else if (Util.isBetween(randInt, 25, 49)) content.add("Leaf");
            else if (Util.isBetween(randInt, 50, 74)) content.add("Board");
            else if (Util.isBetween(randInt, 75, 99)) content.add("Potato");
        }
        return content;
    }
}
