module UI{
    requires javafx.controls;
    requires javafx.fxml;
    requires jdk.compiler;


    opens UI to javafx.fxml;
    opens UI.controller to javafx.fxml;
    exports UI;
}