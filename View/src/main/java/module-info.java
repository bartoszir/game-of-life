module org.example.view {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens org.example.view to javafx.fxml;
    exports org.example.view;
}