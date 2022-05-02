module randomworld {
    requires javafx.controls;
    requires javafx.fxml;


    opens raftgame to javafx.fxml;
    exports raftgame;
    exports raftgame.ui;
    opens raftgame.ui to javafx.fxml;
}