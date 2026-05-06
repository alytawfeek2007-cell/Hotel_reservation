
module hotel {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    opens hotel to javafx.fxml;
    exports hotel;
}