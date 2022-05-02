package raftgame.data;

/**
 * Represents a shark enemy character object
 * Stores if shark is attacking
 */
public class Shark extends GameObject {

    private boolean isAttacking;

    public Shark(int posX, int posY) {
        super(posX, posY);
        isAttacking = false;
    }

    public boolean isAttacking() {
        return isAttacking;
    }

    public void setAttacking(boolean attacking) {
        isAttacking = attacking;
    }
}
