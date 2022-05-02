package raftgame.data;

import java.util.*;

/**
 * Represents a player character object.
 * Used to store hunger, thirst and inventory content
 */
public class Human extends GameObject{
    private int hunger = 100;
    private int thirst = 100;
    private Map<String, Integer> inventory;
    private int protectedFor;

    public int getHunger() {
        return hunger;
    }

    public int getThirst() {
        return thirst;
    }

    public Human(int posX, int posY) {
        super(posX, posY);
        inventory = new HashMap<>();
    }

    /**
     * Decrements player character's health
     */
    public void age() {
        hunger = hunger - 1;
        thirst = thirst - 1;
    }

    /**
     * Checks if player character's health is above 0
     *
     * @return boolean if character is alive or not
     */
    public boolean isAlive() {
        return (hunger > 1 && thirst > 1);
    }

    /**
     * Adds an item to inventory.
     *
     * @param objectName item's name to check
     */
    public void addToInventory(String objectName) {
        if (inventory.containsKey(objectName)) {
            inventory.put(objectName, inventory.get(objectName) + 1);
        } else {
            inventory.put(objectName, 1);
        }
        if (objectName.equals("Spear")) protectedFor = 5;
    }

    /**
     * Gets item count from inventory
     *
     * @param itemName item's name to check
     * @return number of occurrence of item
     */
    public int getItemCount(String itemName) {
        if (inventory.containsKey(itemName)) {
            return inventory.get(itemName);
        } else {
            return 0;
        }
    }

    /**
     * Simulates player fishing.
     * Fishing succeeds only 25% of time
     *
     * @return boolean if fishing was a success
     */
    public boolean tryToFish() {
        Random random = new Random();
        int randInt = random.nextInt(100);
        if (randInt <= 24) {
            addToInventory("Fish");
            return true;
        } else {
            return false;
        }
    }

    /**
     * Removes items from inventory
     *
     * @param itemName item's name to remove
     * @param numberToRemove number of item to remove
     */
    public void removeFromInventory(String itemName, int numberToRemove) {
        if (inventory.containsKey(itemName)) {
            inventory.put(itemName, inventory.get(itemName) - numberToRemove);
        }
    }

    /**
     * Simulates drinking, increasing thirst value
     */
    public void drink() {
        thirst += 40;
        if (thirst >= 100) thirst = 100;
    }

    /**
     * Simulates eating, increasing hunger value
     */
    public void eat() {
        hunger += 60;
        if (hunger >= 100) hunger = 100;
    }

    public int getProtectedFor() {
        return protectedFor;
    }

    /**
     * Simulates shark attack, decreasing number of turns player is protected for
     */
    public void sharkAttack () {
        protectedFor -= 1;
    }

    public void setProtectedFor(int protectedFor) {
        this.protectedFor = protectedFor;
    }
}
