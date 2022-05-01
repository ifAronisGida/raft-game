package randomworld.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import randomworld.data.GameObject;
import randomworld.data.World;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Game UI and visual logic is happening here.
 * Gets input from user and shows data from the game world.
 * Reads game image files once.
 *
 *
 * @see World
 */
public class GameController implements Initializable {

    private World theWorld;
    private ImageView[][] imageViews;
    private ImageView playerImage;
    private ImageView sharkImage;
    private List<ImageView> gameObjectsImages;
    private List<ImageView> buildingImages;
    private boolean isBuilding;
    private final Image shark = new Image("file:img/arrow.png");
    private final Image raft = new Image("file:img/raft.png");
    private final Image water = new Image("file:img/water.png");
    private final Image human = new Image("file:img/stickman.png");
    private final Image leaf = new Image("file:img/leaf.png");
    private final Image junk = new Image("file:img/junk.png");
    private final Image board = new Image("file:img/board.png");
    private final Image barrel = new Image("file:img/barrel.png");
    private final Image stove = new Image("file:img/candle.png");
    private final Image waterCleaner = new Image("file:img/water-cleaner.png");
    private final Image net = new Image("file:img/net.png");

    @FXML
    AnchorPane acpGeneratedWorld;

    @FXML
    ScrollPane scpWorldWindow;

    @FXML
    private Label lbGameMessages;

    @FXML
    private ScrollPane scpGameMessages;

    @FXML
    private Label lbHunger;

    @FXML
    private Label lbThirst;

    @FXML
    private Label lbActionCounter;

    @FXML
    protected Slider sldSpawnRate;

    @FXML
    protected Slider sldWorldSize;

    @FXML
    protected Slider sldWinCondition;

    @FXML
    protected CheckBox chbShark;

    @FXML
    protected Slider sldMaterials;

    @FXML
    protected Label lbJunk;

    @FXML
    protected Label lbBoard;

    @FXML
    protected Label lbLeaf;

    @FXML
    protected Label lbPotato;

    @FXML
    protected Label lbFish;

    @FXML
    protected ComboBox cbBuildOptions;

    @FXML
    protected Label lbSpear;

    @FXML
    protected void onExitButtonClick() {
        System.exit(0);
    }

    @FXML
    protected void onRestartButtonClick() {
        initGame();
    }

    /**
     * Processes build command from UI.
     *
     */
    @FXML
    protected void onBuildButtonClick() {
        if (cbBuildOptions.getValue() == null) {
            lbGameMessages.setText(lbGameMessages.getText() + "Please select a build option first!\n");
        } else if (cbBuildOptions.getValue().toString().split(",")[0].equals("Spear")) {
            theWorld.getPlayer().removeFromInventory("Leaf", 4);
            theWorld.getPlayer().removeFromInventory("Junk", 4);
            theWorld.getPlayer().removeFromInventory("Board", 4);
            theWorld.getPlayer().addToInventory("Spear");
            lbGameMessages.setText(lbGameMessages.getText() + "Successfully built a spear!\n");
            progressGame();
        } else {
            isBuilding = true;
            lbGameMessages.setText(lbGameMessages.getText() + "Please select where to build!\n");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //autoscroll game messages to keep it updated
        scpGameMessages.vvalueProperty().bind(lbGameMessages.heightProperty());
        initGame();

    }

    /**
     * Sets shark on the screen.
     *
     */
    private void setShark() {
        //set shark
        sharkImage = new ImageView(shark);
        sharkImage.setFitHeight(50);
        sharkImage.setFitWidth(50);
        sharkImage.setY(theWorld.getSharkPosY() * 50);
        sharkImage.setX(theWorld.getSharkPosX() * 50);
        acpGeneratedWorld.getChildren().add(sharkImage);
    }

    /**
     * Sets shark on the screen.
     * Calls update labels to show updated player stats.
     */
    private void setPlayer() {
        //set player
        playerImage = new ImageView(human);
        playerImage.setFitWidth(25);
        playerImage.setFitHeight(50);
        playerImage.setY(theWorld.getPlayerPosY() * 50);
        playerImage.setX(theWorld.getPlayerPosX() * 50 + 12);
        acpGeneratedWorld.getChildren().add(playerImage);
        updateLabels();

    }

    /**
     * Updates material objects on the screen based on the game world object.
     * Sets event handlers for object usage.
     */
    private void setGameObjects() {
        //clear outdated images
        for (ImageView objectImage : gameObjectsImages) {
            acpGeneratedWorld.getChildren().remove(objectImage);
        }
        gameObjectsImages.clear();

        //add updated images
        for (GameObject object : theWorld.getMaterialList()) {

            int id = object.getObjectId();
            ImageView image;
            switch (object.getClass().getSimpleName()) {
                case "Barrel":
                    image = new ImageView(barrel);
                    image.setFitWidth(50);
                    image.setFitHeight(50);
                    image.setY(object.getPosY() * 50);
                    image.setX(object.getPosX() * 50);
                    break;
                case "Junk":
                    image = new ImageView(junk);
                    image.setFitWidth(20);
                    image.setFitHeight(50);
                    image.setY(object.getPosY() * 50);
                    image.setX(object.getPosX() * 50 + 15);
                    break;
                case "Leaf":
                    image = new ImageView(leaf);
                    image.setFitWidth(50);
                    image.setFitHeight(50);
                    image.setY(object.getPosY() * 50);
                    image.setX(object.getPosX() * 50);
                    break;
                case "Board":
                    image = new ImageView(board);
                    image.setFitWidth(20);
                    image.setFitHeight(50);
                    image.setY(object.getPosY() * 50);
                    image.setX(object.getPosX() * 50 + 15);
                    break;
                default:
                    System.out.println("Unknown object type case..");
                    image = new ImageView(junk);
                    image.setFitWidth(20);
                    image.setFitHeight(50);
                    image.setY(object.getPosY() * 50);
                    image.setX(object.getPosX() * 50 + 10);
                    break;
            }
            image.setOnMouseClicked((MouseEvent e) -> {
                objectClicked(id, image);
            });
            acpGeneratedWorld.getChildren().add(image);
            gameObjectsImages.add(image);
        }
    }

    /**
     * Updates map with new rafts.
     * Called when user builds new raft.
     */
    private void buildRaft() {
        for (int i = 0; i < theWorld.getHeight(); i++) {
            for (int j = 0; j < theWorld.getWidth(); j++) {
                if (theWorld.getFields()[i][j].isBuildRaftHere()) {
                    theWorld.getFields()[i][j].setBuildRaftHere(false);
                    ImageView image = imageViews[i][j];
                    image.setImage(raft);
                }
            }
        }
    }

    /**
     * Sets starting map on the screen.
     * Sets event handlers for movement/fishing.
     */
    private void setMap() {
        //set water and starting raft
        for (int i = 0; i < theWorld.getHeight(); i++) {
            for (int j = 0; j < theWorld.getWidth(); j++) {

                final int row = i;
                final int column = j;
                ImageView image;

                if (theWorld.getFields()[i][j].isRaft()) {
                    image = new ImageView(raft);
                } else {
                    image = new ImageView(water);
                }
                image.setFitHeight(50);
                image.setFitWidth(50);
                image.setY(i * 50);
                image.setX(j * 50);
                imageViews[i][j] = image;
                image.setOnMouseClicked((MouseEvent e) -> {
                    if (e.getButton() == MouseButton.PRIMARY) {
                        tileClicked(row, column);
                    } else if (e.getButton() == MouseButton.SECONDARY) {
                        fish(row, column);
                    }
                });
                acpGeneratedWorld.getChildren().add(image);
            }
        }
    }

    /**
     * Processes player input.
     * Handles movement and building.
     */
    private void tileClicked(int posY, int posX) {
        //lbGameMessages.setText(lbGameMessages.getText() + "Tile clicked: "+ posX + "." + posY + "\n" );
        if (!isBuilding) movePlayer(posX, posY);
        else build(posY, posX);
    }

    /**
     * Visualises buildings from the world object.
     * Adds event listeners for building usage.
     * Calls buildRaft to redraw part of the map.
     */
    private void showBuildings() {
        buildRaft();
        for (GameObject object : theWorld.getBuildings()) {
            int id = object.getObjectId();
            ImageView image;
            switch (object.getClass().getSimpleName()) {
                case "Net":
                    image = new ImageView(net);
                    image.setFitWidth(50);
                    image.setFitHeight(50);
                    image.setY(object.getPosY() * 50);
                    image.setX(object.getPosX() * 50);
                    break;
                case "Stove":
                    image = new ImageView(stove);
                    image.setFitWidth(50);
                    image.setFitHeight(50);
                    image.setY(object.getPosY() * 50);
                    image.setX(object.getPosX() * 50);
                    break;
                case "WaterCleaner":
                    image = new ImageView(waterCleaner);
                    image.setFitWidth(50);
                    image.setFitHeight(50);
                    image.setY(object.getPosY() * 50);
                    image.setX(object.getPosX() * 50);
                    break;
                default:
                    System.out.println("Unknown object type case..");
                    image = new ImageView(water);
                    break;
            }
            image.setOnMouseClicked((MouseEvent e) -> {
                objectClicked(id, image);
            });
            acpGeneratedWorld.getChildren().add(image);
            buildingImages.add(image);
        }

    }

    /**
     * Processes building command from the UI.
     * Calls the world object's build method and processes the building output.
     *
     * @param posX building action target X position
     * @param posY building action target Y position
     */
    private void build(int posY, int posX) {
        String buildingOptionMenuName = cbBuildOptions.getValue().toString();
        String buildingName = buildingOptionMenuName.split(",")[0];
        String buildingMessage = theWorld.build(posX, posY, buildingName);
        if (buildingMessage.contains("Successfully")) {
            lbGameMessages.setText(lbGameMessages.getText() + buildingMessage);
            isBuilding = false;
            showBuildings();
            progressGame();
        } else {
            lbGameMessages.setText(lbGameMessages.getText() + buildingMessage);
            isBuilding = false;
        }
    }

    /**
     * Processes fishing command from the UI.
     * Calls the world object's fish method and processes the fishing output.
     *
     * @param posX fishing action target X position
     * @param posY fishing action target Y position
     */
    private void fish(int posY, int posX) {
        lbGameMessages.setText(lbGameMessages.getText() + "Tile right-clicked: " + posX + "." + posY + "\n");
        String fishingMessage = theWorld.fish(posX, posY);
        if (fishingMessage == "You can only fish in neighboring waters!\n") {
            lbGameMessages.setText(lbGameMessages.getText() + fishingMessage);
        } else {
            lbGameMessages.setText(lbGameMessages.getText() + fishingMessage);
            progressGame();
        }
    }

    /**
     * Processes object click command from the UI.
     * Calls the world object's clickObject method and processes the action's output.
     *
     * @param id id of the clicked object
     * @param image image of the clicked object
     */
    public void objectClicked(int id, ImageView image) {
        String objectClickMessage = theWorld.clickObject(id);
        if (objectClickMessage.contains("Successfully picked up")) {
            gameObjectsImages.remove(image);
            acpGeneratedWorld.getChildren().remove(image);
            lbGameMessages.setText(lbGameMessages.getText() + objectClickMessage);
            progressGame();
        } else if (objectClickMessage.contains("water") || objectClickMessage.toUpperCase().contains("FISH") ||
                objectClickMessage.toUpperCase().contains("POTATO")) {
            lbGameMessages.setText(lbGameMessages.getText() + objectClickMessage);
            progressGame();
        } else {
            lbGameMessages.setText(lbGameMessages.getText() + objectClickMessage);
        }
    }
    /**
     * Processes player movement command from the UI.
     * Calls the world object's movePlayer method and processes the action's output.
     *
     * @param posX movement action target X position
     * @param posY movement action target Y position
     */
    private void movePlayer(int posX, int posY) {
        if (theWorld.movePlayer(posX, posY)) {
            playerImage.setX(theWorld.getPlayerPosX() * 50 + 12);
            playerImage.setY(theWorld.getPlayerPosY() * 50);
            lbGameMessages.setText(lbGameMessages.getText() + "Player moves to: " + posX + "." + posY + "\n");
            progressGame();
        }
    }

    /**
     * Processes shark movement command from the game loop.
     * Calls the world object's moveShark method and processes the action's output.
     */
    private void moveShark() {
        String sharkMessage = theWorld.moveShark();
        lbGameMessages.setText(lbGameMessages.getText() + sharkMessage);
        sharkImage.setX(theWorld.getSharkPosX() * 50);
        sharkImage.setY(theWorld.getSharkPosY() * 50);
    }

    /**
     * Processes object movement command from the game loop.
     * Calls the world object's moveObjects method and processes the action's output.
     */
    private void moveObjects() {
        lbGameMessages.setText(lbGameMessages.getText() + theWorld.moveObjects());
        setGameObjects();
    }

    /**
     * Progresses the game state by one action.
     * Calls the world's progress methods and updates the UI based on the methods' output.
     * Handles game over and win condition as well.
     */
    private void progressGame() {
        theWorld.setActionCounter(theWorld.getActionCounter() + 1);
        theWorld.getPlayer().age();
        theWorld.spawnObjects((int) sldSpawnRate.getValue());
        moveObjects();
        updateLabels();
        lbGameMessages.setText(lbGameMessages.getText() + theWorld.progressBuildings());
        cbBuildOptions.getItems().setAll(theWorld.getBuildOptions());
        theWorld.setGameOverIfDead();
        if (chbShark.isSelected()) moveShark();
        if (theWorld.isGameOver()) lbGameMessages.setText(lbGameMessages.getText() + "Game is over, please restart!\n");
        if (theWorld.getActionCounter() >= (int) sldWinCondition.getValue()) {
            lbGameMessages.setText(lbGameMessages.getText() + "CONGRATULATIONS, YOU SURVIVED!!\n");
            theWorld.setGameOver(true);
        }
    }

    /**
     * Updates player data like inventory on the UI.
     * Gets data from the world object.
     *
     */
    private void updateLabels() {
        lbHunger.setText("Hunger: " + theWorld.getPlayer().getHunger());
        lbThirst.setText("Thirst: " + theWorld.getPlayer().getThirst());
        lbBoard.setText("Board: " + theWorld.getPlayer().getItemCount("Board"));
        lbJunk.setText("Junk: " + theWorld.getPlayer().getItemCount("Junk"));
        lbLeaf.setText("Leaf: " + theWorld.getPlayer().getItemCount("Leaf"));
        lbPotato.setText("Potato: " + theWorld.getPlayer().getItemCount("Potato"));
        lbFish.setText("Fish: " + theWorld.getPlayer().getItemCount("Fish"));
        lbActionCounter.setText("Actions: " + theWorld.getActionCounter());
        lbSpear.setText("Spear: " + theWorld.getPlayer().getItemCount("Spear"));
    }

    /**
     * Creates new game world with input from UI.
     * Shows new game world with the data
     */
    private void initGame() {
        theWorld = new World(10 * (int) sldWorldSize.getValue(), 12 * (int) sldWorldSize.getValue(),
                (int) sldSpawnRate.getValue(), chbShark.isSelected(), (int) sldMaterials.getValue());
        imageViews = new ImageView[theWorld.getHeight()][theWorld.getWidth()];
        gameObjectsImages = new ArrayList<>();
        buildingImages = new ArrayList<>();
        cbBuildOptions.getItems().setAll();
        setMap();
        setPlayer();
        if (chbShark.isSelected()) setShark();
        setGameObjects();
        lbGameMessages.setText("Game started!\n");
        lbGameMessages.setText(lbGameMessages.getText() + "Game size: " + sldWorldSize.getValue() + "\n");
        lbGameMessages.setText(lbGameMessages.getText() + "Left click to move, right click to fish!\n");
        lbGameMessages.setText(lbGameMessages.getText() + "Get materials, build, and survive!\n");
    }
}