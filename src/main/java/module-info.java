module randomworld {
    requires javafx.controls;
    requires javafx.fxml;


    opens randomworld to javafx.fxml;
    exports randomworld;
    exports randomworld.ui;
    opens randomworld.ui to javafx.fxml;
}