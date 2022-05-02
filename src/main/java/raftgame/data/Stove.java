package raftgame.data;

/**
 * Represents a stove building object.
 * Stores data about cooking.
 */

public class Stove extends GameObject {
    private int actionsToCook;
    private boolean isCooking;
    private boolean isCookingReady;
    private String food;

    public Stove(int posX, int posY) {
        super(posX, posY);
        this.actionsToCook = 25;
        this.isCooking = false;
        this.isCookingReady = false;
        this.food = "";
    }

    /**
     * Progresses cooking if stove is cooking.
     */
    public void progress() {
        if (isCooking) {
            if (actionsToCook > 0) actionsToCook--;
            if (actionsToCook <= 0) isCookingReady = true;
        }
    }

    public int getActionsToCook() {
        return actionsToCook;
    }

    public boolean IsCookingReady() {
        return isCookingReady;
    }

    public boolean isCooking() {
        return isCooking;
    }

    /**
     * Resets stove, called when player used it
     */
    public void reset() {
        isCooking = false;
        isCookingReady = false;
        actionsToCook = 25;
    }

    /**
     * Starts cooking a food
     *
     * @param food food's name
     */
    public void startCooking(String food) {
        isCooking = true;
        this.food = food;
    }

    public String getFood() {
        return food;
    }
}
