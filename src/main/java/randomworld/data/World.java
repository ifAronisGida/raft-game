package randomworld.data;

import java.util.*;

import randomworld.Util;

/**
 * Represents the game world.
 * Used to store and process the main game logic.
 */
public class World {
    private int height;
    private int width;
    private Tile[][] fields;
    private List<GameObject> materialList;
    private Human player;
    private Shark enemy;
    private boolean gameOver;
    private int actionCounter;
    private List<GameObject> buildings;

    /**
     * Initializes game world with input from UI.
     *
     * @param height height of the world
     * @param width width of the world
     * @param spawnLimit max number of materials to spawn
     * @param spawnShark boolean whether to spawn enemy
     * @param startingMaterials number of materials to start the game with
     *
     */
    public World(int height, int width, int spawnLimit, boolean spawnShark, int startingMaterials) {
        this.height = height;
        this.width = width;
        this.gameOver = false;
        this.actionCounter = 0;
        materialList = new ArrayList<>();
        buildings = new ArrayList<>();
        generate(spawnLimit, spawnShark, startingMaterials);

    }

    /**
     * Generates game map.
     *
     * @param spawnLimit max number of materials to spawn
     * @param spawnShark boolean whether to spawn enemy
     * @param startingMaterials number of materials to start the game with
     *
     */
    public void generate(int spawnLimit, boolean spawnShark, int startingMaterials) {
        fields = new Tile[height][width];
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                fields[i][j] = new Tile();
                if ((i == 14 || i == 15) && (j == 20 || j == 21)) {
                    fields[i][j].setRaft(true);
                }
            }
        }

        spawnPlayer(20,15, startingMaterials);
        if (spawnShark) spawnShark(10,10);
        spawnObjects(spawnLimit);
    }

    /**
     * Spawns a random number of material objects in the world.
     *
     * @param spawnLimit max number of materials to spawn
     *
     */
    public void spawnObjects(int spawnLimit) {
        Random random = new Random();
        int numberOfObjects = random.nextInt(spawnLimit + 1);
        for (int i = 0; i < numberOfObjects; i++) {
            GameObject randomObject = createRandomObject();
            materialList.add(randomObject);
        }
    }

    /**
     * Creates a random type material object with a high chance of normal material
     * and a low chance for a big container.
     *
     * @return generated material object
     */
    private GameObject createRandomObject() {
        Random random = new Random();
        int randomInt = random.nextInt(100);
        int randomCoordinate = random.nextInt(5,32);
        if (Util.isBetween(randomInt, 0, 31)) return new Board(randomCoordinate, 0);
        else if (Util.isBetween(randomInt,32, 63)) return new Leaf(randomCoordinate, 0);
        else if (Util.isBetween(randomInt,64, 95)) return new Junk(randomCoordinate, 0);
        else return new Barrel(randomCoordinate, 0);
    }

    /**
     * Creates player character in the game world.
     *
     * @param posX starting X position
     * @param posY starting Y position
     * @param startingMaterials number of materials to start the game with
     */
    private void spawnPlayer(int posX, int posY, int startingMaterials) {
        Human human = new Human(posX, posY);
        for (int i = 0; i < startingMaterials; i++) {
            human.addToInventory(createRandomObject().getClass().getSimpleName());
        }
        player = human;
    }

    /**
     * Creates enemy character in the game world.
     *
     * @param posX starting X position
     * @param posY starting Y position
     */
    private void spawnShark(int posX, int posY) {
        Shark shark = new Shark(posX, posY);
        //objectList.add(shark);
        enemy = shark;
    }

    /**
     * Moves player in the game world if the inputs are valid.
     *
     * @param posX movement target X position
     * @param posY movement target Y position
     * @return boolean if the movement was successful
     */
    public boolean movePlayer(int posX, int posY) {
        if (!gameOver) {
            int playerPosX = player.getPosX();
            int playerPosY = player.getPosY();

            //check for invalid moves
            if (posX == playerPosX && posY == playerPosY) return false;
            if (Math.abs(playerPosX - posX) > 1 || Math.abs(playerPosY - posY) > 1) return false;

            //set player coordinates
            player.setPosX(posX);
            player.setPosY(posY);

            //reset spear protection if moved to raft
            if (fields[playerPosY][playerPosX].isRaft() && player.getItemCount("Spear") > 0)
                player.setProtectedFor(5);
            return true;
        }
        return false;
    }

    /**
     * Moves enemy in the game world if the inputs are valid.
     * Attacks player character if possible.
     *
     * @return string containing the outcome of the move
     */
    public String moveShark() {


        StringBuilder sharkMessage = new StringBuilder();
        int playerPosX = player.getPosX();
        int playerPosY = player.getPosY();

        //player is on raft
        //TODO smarter neutral shark
        if (fields[playerPosY][playerPosX].isRaft()) {
            enemy.setAttacking(false);
            if (enemy.getPosX() < 15) {
                enemy.setPosX(enemy.getPosX() + 1);
            } else {
                enemy.setPosX(10);
            }
            sharkMessage.append("The shark is swimming peacefully..\n");

        //player is in water
        } else {
            enemy.setAttacking(true);
            if (enemy.getPosX() > playerPosX) {
                enemy.setPosX(enemy.getPosX() - 1);
            } else if (enemy.getPosX() < playerPosX) {
                enemy.setPosX(enemy.getPosX() + 1);
            }
            if (enemy.getPosY() > playerPosY) {
                enemy.setPosY(enemy.getPosY() - 1);
            } else if (enemy.getPosY() < playerPosY){
                enemy.setPosY(enemy.getPosY() + 1);
            }
            sharkMessage.append("The shark is moving towards you!!\n");
        }

        //check if shark reached you
        if (playerPosY == enemy.getPosY() && playerPosX == enemy.getPosX()) {
            if (player.getProtectedFor() > 0) {
                player.sharkAttack();
                sharkMessage.append("The shark will overwhelm you in " + player.getProtectedFor() + " turns!!\n");
            }
            else {
                gameOver = true;
                sharkMessage.append("The shark killed you..\n");
            }
        }
        return sharkMessage.toString();
    }

    /**
     * Moves material objects in the game world.
     * Remove objects if they reach end of map.
     * Pick up objects if the touch a net.
     *
     * @return string containing the pickup message
     */
    public String moveObjects() {
        StringBuilder netPickupMessage = new StringBuilder();
        List<GameObject> objectsToPickup = new ArrayList<>();

        //processes each material object in the world
        for (GameObject object : materialList) {
            object.setPosY(object.getPosY() + 1);

            //get net object if it is at material's position
            GameObject netAtPosition = buildings.stream()
                    .filter(b -> b.getPosY() == object.getPosY() && b.getPosX() == object.getPosX() &&
                            b.getClass().getSimpleName().equals("Net"))
                    .findFirst()
                    .orElse(null);

            if (netAtPosition != null) {
                objectsToPickup.add(object);
            }
        }

        for (GameObject object : objectsToPickup) {
            netPickupMessage.append(pickupMaterial(object));
        }

        //remove objects at the end of the map
        materialList.removeIf(o -> o.getPosY() >= height);
        return netPickupMessage.toString();
    }

    public int getHeight() {
        return height;
    }

    /**
     * Processes game object logic when they are clicked.
     * Materials can be picked up.
     * Buildings can be used.
     *
     * @param id clicked object's id to identify it in the game world

     * @return string containing the action's message
     */
    public String clickObject(int id) {
        if (!gameOver) {

            int playerPosX = player.getPosX();
            int playerPosY = player.getPosY();

            //gets clicked material based on object id
            GameObject material = materialList.stream()
                    .filter(o -> o.getObjectId() == id)
                    .findFirst()
                    .orElse(null);

            //gets clicked building based on object id
            GameObject building = buildings.stream()
                    .filter(b -> b.getObjectId() == id)
                    .findFirst()
                    .orElse(null);

            //pickup material
            if (material != null) {
                int objectPosX = material.getPosX();
                int objectPosY = material.getPosY();
                if (Math.abs(playerPosX - objectPosX) > 1 || Math.abs(playerPosY - objectPosY) > 1) {
                    return "You can only pickup/use items next to you!\n";
                }
                return pickupMaterial(material);
            }

            //use buildings
            else if (building != null) {
                int objectPosX = building.getPosX();
                int objectPosY = building.getPosY();
                if (Math.abs(playerPosX - objectPosX) > 1 || Math.abs(playerPosY - objectPosY) > 1) {
                    return "You can only pickup/use items next to you!\n";
                }
                if (building.getClass().getSimpleName().equals("WaterCleaner")) {
                    WaterCleaner waterCleaner = (WaterCleaner) building;
                    if (waterCleaner.isWaterReady()) {
                        player.drink();
                        waterCleaner.reset();
                        return "Drank water!\n";
                    }
                }

                //stove logic
                if (building.getClass().getSimpleName().equals("Stove")) {
                    Stove stove = (Stove) building;
                    if (stove.IsCookingReady()){
                        player.eat();
                        stove.reset();
                        return "Ate some " + stove.getFood() + "\n";
                    } else if (player.getItemCount("Fish") > 0) {
                        stove.startCooking("Fish");
                        player.removeFromInventory("Fish", 1);
                        return "Started cooking a fish!\n";
                    } else if (player.getItemCount("Potato") > 0) {
                        stove.startCooking("Potato");
                        player.removeFromInventory("Potato", 1);
                        return "Started cooking a potato!\n";
                    }
                }
            }
        }
        return "Game is Over!";
    }

    /**
     * Picks up material, adds it to player inventory, removes it from game world.
     *
     * @param material object which is picked up
     * @return string containing the action's message
     */
    private String pickupMaterial(GameObject material) {
        materialList.remove(material);
        if (material.getClass().getSimpleName().equals("Barrel")) {
            Barrel barrel = (Barrel) material;
            for (String objectName : barrel.getContent()) {
                player.addToInventory(objectName);
            }
        } else {
            player.addToInventory(material.getClass().getSimpleName());
        }
        return "Successfully picked up " + material.getClass().getSimpleName() + "!\n";
    }

    /**
     * Progresses buildings' water and food generation.
     *
     * @return string containing the action's message
     */
    public String progressBuildings() {
            StringBuilder sb = new StringBuilder();
            for (GameObject building : buildings) {
                if (building.getClass().getSimpleName().equals("WaterCleaner")) {
                    WaterCleaner waterCleaner = (WaterCleaner) building;
                    waterCleaner.progress();
                    if (waterCleaner.isWaterReady()) {
                        sb.append("Water is ready, click on the water cleaner to drink!\n");
                    } else {
                        sb.append("Water cleaner will be ready in " + waterCleaner.getActionsToMakeWater() + " actions!\n");
                    }
                }
                if (building.getClass().getSimpleName().equals("Stove")) {
                    Stove stove = (Stove) building;
                    stove.progress();
                    if (stove.IsCookingReady()) {
                        sb.append("Cooking is ready, click on the stove to eat!\n");
                    } else if (stove.isCooking()) {
                        sb.append("Cooking will be ready in " + stove.getActionsToCook() + " actions!\n");
                    } else {
                        sb.append("Place food on stove to start cooking!\n");
                    }
                }
            }
        return sb.toString();
    }

    /**
     * Gets possible build options based on player's inventory.
     *
     * @return List of strings containing the possible build options
     */
    public List<String> getBuildOptions() {
        List<String> buildOptions = new ArrayList<>();
        if (player.getItemCount("Board") >= 2 && player.getItemCount("Leaf") >= 2)
            buildOptions.add("Raft, cost: 2 boards, 2 leafs");
        if (player.getItemCount("Junk") >= 4 && player.getItemCount("Leaf") >= 2)
            buildOptions.add("Water-cleaner, cost: 4 junks, 2 leafs");
        if (player.getItemCount("Board") >= 2 && player.getItemCount("Leaf") >= 6)
            buildOptions.add("Net, cost: 2 boards, 6 leafs");
        if (player.getItemCount("Board") >= 2 && player.getItemCount("Leaf") >= 4
                && player.getItemCount("Junk") >= 3)
            buildOptions.add("Stove, cost: 2 boards, 4 leafs, 3 junks");
        if (player.getItemCount("Board") >= 4 && player.getItemCount("Leaf") >= 4
                && player.getItemCount("Junk") >= 4)
            buildOptions.add("Spear, cost: 4 boards, 4 leafs, 4 junks");
        return buildOptions;
    }

    /**
     * Creates a building object in the game world
     *
     * @param posX X position to create building at
     * @param posY Y position to create building at
     * @param buildingName name of the building type to create
     */
    public void spawnBuilding(int posX, int posY, String buildingName) {
        switch (buildingName) {
            case "Raft":
                player.removeFromInventory("Board", 2);
                player.removeFromInventory("Leaf", 2);
                fields[posY][posX].setRaft(true);
                fields[posY][posX].setBuildRaftHere(true);
                break;
            case "Net":
                player.removeFromInventory("Leaf", 6);
                player.removeFromInventory("Board", 2);
                buildings.add(new Net(posX, posY));
                break;
            case "Stove":
                player.removeFromInventory("Leaf", 4);
                player.removeFromInventory("Junk", 3);
                player.removeFromInventory("Board", 2);
                buildings.add(new Stove(posX, posY));
                break;
            case "Water-cleaner":
                player.removeFromInventory("Leaf", 2);
                player.removeFromInventory("Junk", 4);
                buildings.add(new WaterCleaner(posX, posY));
                break;
            default:
                System.out.println("Unknown building, can't spawn!");
                break;
        }
    }

    /**
     * Validates build command arriving from the UI.
     * Spawns building if command is valid.
     *
     * @param posX X position to create building at
     * @param posY Y position to create building at
     * @param buildingName name of the building type to create
     *
     * @return  string containing the build command's outcome
     */
    public String build(int posX, int posY, String buildingName) {
        if (!isGameOver()) {
            int playerPosX = player.getPosX();
            int playerPosY = player.getPosY();

            if (Math.abs(playerPosX - posX) > 1 || Math.abs(playerPosY - posY) > 1) return "You can only build next to you!\n";

            if (buildingName.equals("Raft") || buildingName.equals("Net")) {
                if (fields[posY][posX].isRaft()) return "You can only build raft/net in water!\n";
                else if (!fields[playerPosY][playerPosX].isRaft()) {
                    return "You can only build raft/net if you are on a raft!\n";
                } else {
                    spawnBuilding(posX, posY, buildingName);
                    return "Successfully built " + buildingName + "!\n";
                }
            }
            else if (!fields[posY][posX].isRaft()) {
                return "You can only build on rafts!\n";
            } else {
                spawnBuilding(posX, posY, buildingName);
                return "Successfully built " + buildingName + "!\n";
            }
        }
        return "Game is Over!";
    }

    /**
     * Validates fish command arriving from the UI.
     * Simulates fishing if command is valid.
     *
     * @param posX X position to fish at
     * @param posY Y position to fish at
     *
     * @return  string containing the fish command's outcome
     */
    public String fish(int posX, int posY) {
        if (!gameOver) {
            int playerPosX = player.getPosX();
            int playerPosY = player.getPosY();

            if (Math.abs(playerPosX - posX) > 1 || Math.abs(playerPosY - posY) > 1 || fields[posY][posX].isRaft()){
                return "You can only fish in neighboring waters!\n";
            }
            if (player.tryToFish()) {
                return "Successfully caught a fish!\n";
            } else {
                return "Better luck next time!\n";
            }
        }
        return "Game is Over!\n";
    }

    /**
     * Set's the world to be game over if player reached starvation.
     *
     */
    public void setGameOverIfDead() {
        if (!player.isAlive()) gameOver = true;
    }

    public int getWidth() {
        return width;
    }

    public Tile[][] getFields() {
        return fields;
    }

    public List<GameObject> getMaterialList() {
        return materialList;
    }

    public int getPlayerPosX() {
        return player.getPosX();
    }

    public int getPlayerPosY() {
        return player.getPosY();
    }


    public int getSharkPosX() {
        return enemy.getPosX();
    }

    public int getSharkPosY() {
        return enemy.getPosY();
    }


    public Human getPlayer() {
        return player;
    }


    public boolean isGameOver() {
        return gameOver;
    }


    public int getActionCounter() {
        return actionCounter;
    }

    public void setActionCounter(int actionCounter) {
        this.actionCounter = actionCounter;
    }

    public List<GameObject> getBuildings() {
        return buildings;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
}
