package hotel;

import javafx.application.Application;
import javafx.stage.Stage;

public class HotelApp extends Application {

    public static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        primaryStage = stage;
        stage.setTitle("Hotel Reservation System");
        stage.setMinWidth(900);
        stage.setMinHeight(650);
        SceneManager.init(stage);
        SceneManager.showLogin();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
