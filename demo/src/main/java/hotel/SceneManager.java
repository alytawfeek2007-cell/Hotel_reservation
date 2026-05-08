package hotel;

import javafx.scene.Scene;
import javafx.stage.Stage;

public class SceneManager {

    private static Stage stage;
    private static Guest currentGuest;
    private static Staff currentStaff;

    // called once at startup
    public static void init(Stage primaryStage) {
        stage = primaryStage;
    }

    // ─── Session getters/setters ─────────────────────────────────
    public static Guest getCurrentGuest()        { return currentGuest; }
    public static void  setCurrentGuest(Guest g) { currentGuest = g; }
    public static Staff getCurrentStaff()        { return currentStaff; }
    public static void  setCurrentStaff(Staff s) { currentStaff = s; }

    // ─── Navigation methods ──────────────────────────────────────
    public static void showLogin() {
        setScene(new LoginScreen().getScene());
    }

    public static void showRegister() {
        setScene(new RegisterScreen().getScene());
    }

    public static void showGuestDashboard() {
        setScene(new GuestDashboardScreen().getScene());
    }

    public static void showRoomBrowsing() {
        setScene(new RoomBrowsingScreen().getScene());
    }

    public static void showReservationManagement() {
        setScene(new ReservationScreen().getScene());
    }

    public static void showCheckout(Reservation reservation) {
        setScene(new CheckoutScreen(reservation).getScene());
    }

    public static void showAdminDashboard() {
        setScene(new AdminDashboardScreen().getScene());
    }

    public static void logout() {
        currentGuest = null;
        currentStaff = null;
        showLogin();
    }

    private static void setScene(Scene scene) {
        try {
            scene.getStylesheets().add(
                SceneManager.class
                    .getResource("/css/styles.css")
                    .toExternalForm());
        } catch (Exception e) {
            System.out.println("CSS not found, skipping.");
        }
        stage.setScene(scene);
        stage.centerOnScreen();
    }
}
