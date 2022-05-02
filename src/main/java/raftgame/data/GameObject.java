package raftgame.data;

/**
 * An abstract class used to derive all game objects from.
 * Mainly used to store object's position.
 * All objects derived from this class have a unique id during runtime used for identifying objects
 */

public abstract class GameObject {
    private int posX;
    private int posY;
    private int objectId;
    private static int counter = 0;

    public GameObject(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        setObjectId(++counter);
    }

    public int getPosX() {
        return posX;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public int getObjectId() {
        return objectId;
    }

    public void setObjectId(int objectId) {
        this.objectId = objectId;
    }
}
