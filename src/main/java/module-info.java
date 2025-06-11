module UI{
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens UI to javafx.fxml;
    exports UI;
}