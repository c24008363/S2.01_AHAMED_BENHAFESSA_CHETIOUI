module UI{
    requires javafx.controls;
    requires javafx.fxml;


    opens UI to javafx.fxml;
    opens UI.controller to javafx.fxml;
    exports UI;
}