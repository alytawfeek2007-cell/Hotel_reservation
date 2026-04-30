package hotel;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class GuestDashboardScreen {

    private final Scene scene;

    public GuestDashboardScreen() {
        scene = buildScene();
    }

    public Scene getScene() { return scene; }

    private Scene buildScene() {
        Guest guest = SceneManager.getCurrentGuest();

        // ── Sidebar ──────────────────────────────────────────────
        VBox sidebar = buildSidebar();

        // ── Topbar ───────────────────────────────────────────────
        HBox topbar = buildTopbar(guest.getUsername());

        // ── Content ──────────────────────────────────────────────
        VBox content = new VBox(20);
        content.setPadding(new Insets(24));
        content.setStyle("-fx-background-color:#f4f6fb;");

        Label welcome = new Label(
            "Welcome back, " + guest.getUsername() + "!");
        welcome.setStyle(
            "-fx-font-size:22px;-fx-font-weight:bold;" +
            "-fx-text-fill:#1a2744;");

        // Stat cards row
        long activeRes = HotelDatabase.getReservations()
            .stream()
            .filter(r -> r.getGuest().getUsername()
                .equals(guest.getUsername()))
            .filter(r -> r.getStatus() == ReservationStatus.CONFIRMED
                || r.getStatus() == ReservationStatus.PENDING)
            .count();

        long availableRooms = HotelDatabase.getRooms()
            .stream()
            .filter(Room::isAvailable)
            .count();

        HBox statsRow = new HBox(16,
            statCard("💰",
                String.format("$%.2f", guest.getBalance()),
                "Balance", "#c8a04a"),
            statCard("📋",
                String.valueOf(activeRes),
                "Active Reservations", "#1a2744"),
            statCard("🛏",
                String.valueOf(availableRooms),
                "Available Rooms", "#27ae60")
        );

        // Profile card
        VBox profileCard = buildCard("My Profile",
            profileRow("Username",   guest.getUsername()),
            profileRow("Address",
                guest.getAddress() != null
                ? guest.getAddress() : "N/A"),
            profileRow("Gender",
                guest.getGender() != null
                ? guest.getGender().toString() : "N/A"),
            profileRow("Date of Birth",
                guest.getDateOfBirth() != null
                ? guest.getDateOfBirth().toString() : "N/A")
        );

        // Quick actions
        Button browseBtn = goldButton("Browse Rooms");
        browseBtn.setOnAction(
            e -> SceneManager.showRoomBrowsing());
        Button myResBtn = outlineButton("My Reservations");
        myResBtn.setOnAction(
            e -> SceneManager.showReservationManagement());
        HBox actionsRow = new HBox(12, browseBtn, myResBtn);
        VBox actionsCard = buildCard("Quick Actions", actionsRow);

        content.getChildren().addAll(
            welcome, statsRow, actionsCard, profileCard);

        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle(
            "-fx-background:transparent;" +
            "-fx-background-color:#f4f6fb;");

        VBox mainArea = new VBox(topbar, scroll);
        VBox.setVgrow(scroll, Priority.ALWAYS);

        BorderPane root = new BorderPane();
        root.setLeft(sidebar);
        root.setCenter(mainArea);

        return new Scene(root, 1100, 720);
    }

    // ─── Profile row helper ──────────────────────────────────────
    private HBox profileRow(String key, String value) {
        Label k = new Label(key + ":");
        k.setStyle("-fx-font-weight:bold;" +
            "-fx-text-fill:#1a2744;-fx-min-width:140;" +
            "-fx-font-size:13px;");
        Label v = new Label(value);
        v.setStyle("-fx-text-fill:#7a8599;-fx-font-size:13px;");
        HBox row = new HBox(12, k, v);
        row.setPadding(new Insets(4, 0, 4, 0));
        return row;
    }

    // ─── Stat card helper ────────────────────────────────────────
    private VBox statCard(String emoji, String value,
                          String label, String color) {
        Label e = new Label(emoji);
        e.setStyle("-fx-font-size:26px;");
        Label v = new Label(value);
        v.setStyle("-fx-font-size:20px;-fx-font-weight:bold;" +
            "-fx-text-fill:" + color + ";");
        Label l = new Label(label);
        l.setStyle("-fx-font-size:12px;-fx-text-fill:#7a8599;");
        VBox card = new VBox(4, e, v, l);
        card.setPadding(new Insets(18, 22, 18, 22));
        card.setStyle(
            "-fx-background-color:white;" +
            "-fx-background-radius:12;" +
            "-fx-effect:dropshadow(gaussian," +
            "rgba(0,0,0,0.07),8,0,0,2);");
        HBox.setHgrow(card, Priority.ALWAYS);
        return card;
    }

    // ─── Card builder ────────────────────────────────────────────
    private VBox buildCard(String title,
                           javafx.scene.Node... children) {
        VBox card = new VBox(12);
        card.setPadding(new Insets(18));
        card.setStyle(
            "-fx-background-color:white;" +
            "-fx-background-radius:12;" +
            "-fx-effect:dropshadow(gaussian," +
            "rgba(0,0,0,0.07),8,0,0,2);");
        Label heading = new Label(title);
        heading.setStyle("-fx-font-size:15px;" +
            "-fx-font-weight:bold;-fx-text-fill:#1a2744;");
        card.getChildren().add(heading);
        card.getChildren().add(new Separator());
        card.getChildren().addAll(children);
        return card;
    }

    // ─── Sidebar ─────────────────────────────────────────────────
    private VBox buildSidebar() {
        VBox sidebar = new VBox(4);
        sidebar.setPrefWidth(210);
        sidebar.setStyle("-fx-background-color:#1a2744;");
        sidebar.setPadding(new Insets(0, 0, 20, 0));

        Label brand = new Label("🏨  LUXE STAY");
        brand.setStyle("-fx-text-fill:white;" +
            "-fx-font-size:16px;-fx-font-weight:bold;" +
            "-fx-padding:24 16 24 16;");

        sidebar.getChildren().add(brand);
        sidebar.getChildren().add(new Separator());

        String[][] items = {
            {"🏠", "Dashboard"},
            {"🛏", "Browse Rooms"},
            {"📋", "My Reservations"},
            {"💳", "Checkout"}
        };

        for (String[] item : items) {
            HBox btn = sidebarButton(
                item[0], item[1],
                item[1].equals("Dashboard"));
            btn.setOnMouseClicked(e -> {
                switch (item[1]) {
                    case "Browse Rooms" ->
                        SceneManager.showRoomBrowsing();
                    case "My Reservations" ->
                        SceneManager.showReservationManagement();
                    case "Checkout" -> {
                        var res = HotelDatabase.getReservations()
                            .stream()
                            .filter(r -> r.getGuest().getUsername()
                                .equals(SceneManager
                                    .getCurrentGuest()
                                    .getUsername()))
                            .filter(r -> r.getStatus()
                                == ReservationStatus.CONFIRMED)
                            .findFirst();
                        res.ifPresentOrElse(
                            SceneManager::showCheckout,
                            () -> showError(
                                "No confirmed reservation found.")
                        );
                    }
                }
            });
            sidebar.getChildren().add(btn);
        }
        return sidebar;
    }

    private HBox sidebarButton(String emoji,
                               String label, boolean active) {
        Label icon = new Label(emoji);
        icon.setStyle("-fx-font-size:15px;");
        Label lbl = new Label(label);
        lbl.setStyle(active
            ? "-fx-text-fill:#1a2744;-fx-font-weight:bold;" +
              "-fx-font-size:13px;"
            : "-fx-text-fill:rgba(255,255,255,0.85);" +
              "-fx-font-size:13px;");
        HBox btn = new HBox(10, icon, lbl);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setPadding(new Insets(11, 16, 11, 20));
        btn.setStyle(active
            ? "-fx-background-color:#c8a04a;" +
              "-fx-background-radius:0 24 24 0;-fx-cursor:hand;"
            : "-fx-background-color:transparent;" +
              "-fx-cursor:hand;");
        if (!active) {
            btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-color:" +
                "rgba(255,255,255,0.1);" +
                "-fx-background-radius:0 24 24 0;" +
                "-fx-cursor:hand;"));
            btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color:transparent;" +
                "-fx-cursor:hand;"));
        }
        return btn;
    }

    // ─── Topbar ──────────────────────────────────────────────────
    private HBox buildTopbar(String username) {
        Label title = new Label("Dashboard");
        title.setStyle("-fx-font-size:18px;" +
            "-fx-font-weight:bold;-fx-text-fill:#1a2744;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Label user = new Label("👤  " + username);
        user.setStyle(
            "-fx-background-color:#1a2744;" +
            "-fx-text-fill:white;-fx-padding:6 14;" +
            "-fx-background-radius:20;-fx-font-size:12px;");
        Button logout = new Button("Logout");
        logout.setStyle(
            "-fx-background-color:#e74c3c;" +
            "-fx-text-fill:white;-fx-padding:6 14;" +
            "-fx-background-radius:8;-fx-cursor:hand;");
        logout.setOnAction(e -> SceneManager.logout());
        HBox topbar = new HBox(12, title, spacer, user, logout);
        topbar.setAlignment(Pos.CENTER_LEFT);
        topbar.setPadding(new Insets(16, 24, 16, 24));
        topbar.setStyle(
            "-fx-background-color:white;" +
            "-fx-effect:dropshadow(gaussian," +
            "rgba(0,0,0,0.07),6,0,0,2);");
        return topbar;
    }

    // ─── Button helpers ──────────────────────────────────────────
    private Button goldButton(String text) {
        Button btn = new Button(text);
        btn.setStyle(
            "-fx-background-color:#c8a04a;" +
            "-fx-text-fill:#1a2744;-fx-font-weight:bold;" +
            "-fx-padding:10 22;-fx-background-radius:8;" +
            "-fx-cursor:hand;");
        return btn;
    }

    private Button outlineButton(String text) {
        Button btn = new Button(text);
        btn.setStyle(
            "-fx-background-color:transparent;" +
            "-fx-text-fill:#7a8599;" +
            "-fx-padding:10 22;-fx-background-radius:8;" +
            "-fx-border-color:#dde3f0;-fx-border-radius:8;" +
            "-fx-cursor:hand;");
        return btn;
    }

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
