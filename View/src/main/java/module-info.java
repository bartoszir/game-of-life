module org.example.view {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires org.example;
    requires jdk.compiler;
    requires org.apache.logging.log4j;

    opens org.example.view to javafx.fxml;
    exports org.example.view;
}