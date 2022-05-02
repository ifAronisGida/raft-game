package raftgame.data;

/**
 * Represents a water cleaner building object.
 * Stores data about water cleaner's status
 */
public class WaterCleaner extends GameObject {
    private int actionsToMakeWater;
    private boolean isWaterReady;

    public WaterCleaner(int posX, int posY) {
        super(posX, posY);
        this.actionsToMakeWater = 25;
        this.isWaterReady = false;
    }

    /**
     * Progresses water cleaner's status
     */
    public void progress() {
        if (actionsToMakeWater > 0) actionsToMakeWater--;
        if (actionsToMakeWater <= 0) isWaterReady = true;
    }

    public boolean isWaterReady() {
        return isWaterReady;
    }

    public int getActionsToMakeWater() {
        return actionsToMakeWater;
    }

    /**
     * Resets water cleaner, called when player used it
     */
    public void reset() {
        isWaterReady = false;
        actionsToMakeWater = 25;
    }
}
