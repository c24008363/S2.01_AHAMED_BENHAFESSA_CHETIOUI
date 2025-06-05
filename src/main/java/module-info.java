module org.example.s201 {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.s201 to javafx.fxml;
    exports org.example.s201;
}