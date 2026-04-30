
module hotel {
    requires javafx.controls;
    requires javafx.fxml;
    opens hotel to javafx.fxml;
    exports hotel;
}